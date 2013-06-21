/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.storage.dao.JobProgressDao;
import org.vpac.ndg.storage.model.JobProgress;

/**
 * Manages a collection of tasks as a transaction.
 * @author hsumanto
 */
public class TaskPipeline {

	final private Logger log = LoggerFactory.getLogger(TaskPipeline.class);

	JobProgressDao jobProgressDao;

	/** task pipeline name */
	private String name;
	/** task pipeline queue */
	private List<ITask> queue;
	/** for communicating a raster task's progress to the outside world */
	private JobProgress progress;
	/** for differentiating main pipeline and child pipeline */
	private boolean isMain;

	public TaskPipeline() {
		this(true);
	}

	public TaskPipeline(boolean isMain) {
		setMain(isMain);
		queue = new ArrayList<ITask>();
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		jobProgressDao = (JobProgressDao) appContext.getBean("jobProgressDao");
		if(isMain()) {
			progress = new JobProgress("Task pipeline execution progress");
			jobProgressDao.save(progress);
		} else {
			progress = null;
		}
	}

	public TaskPipeline(String name, TaskType taskType) {
		this();
		setProgressName(name, taskType);
	}

	/**
	 * Add a task into task pipeline.
	 * @param task The task to add into pipeline.
	 */
	public void addTask(ITask task) {
		queue.add(task);
	}

	/**
	 * Perform initialisation on each task in task pipeline.
	 * @throws TaskInitialisationException
	 */
	public void initialise() throws TaskInitialisationException {
		// This must be done first to ensure the task ID is initialised.
		taskpipelineCurrentState(TaskState.RUNNING); 

		String tab = Constant.EMPTY;
		if (!isMain) {
			tab = Constant.DEBUG_INNER_PIPELINE;
		}
		log.info("{}Start of task initialisation.", tab);

		int currTaskStep = 1;
		// Perform any necessary initialisation before execution
		for (ITask task : queue) {
			log.debug(String.format("%sTASK_INIT [%s] = %s", tab,
					currTaskStep, task.getDescription()));
			task.initialise();
			currTaskStep++;
		}

	}

	/**
	 * Execute each task in task pipeline.
	 * @throws TaskException
	 * @throws TaskInitialisationException
	 */
	public void run() throws TaskException {
		// Set the number of tasks in the taskpipeline
		taskpipelineNumberOfSteps();

		String tab = Constant.EMPTY;
		if (!isMain) {
			tab = Constant.DEBUG_INNER_PIPELINE;
		}

		log.info("{}START OF TASK EXECUTION", tab);

		int currTaskStep = 1;
		// Perform execution of each task in pipeline
		for (ITask task : queue) {
			log.debug(String.format("%sTASK_EXEC [%s] = %s", tab,
					currTaskStep, task.getDescription()));
			task.execute();
			taskpipelineCurrentStep(currTaskStep, task.getDescription());
			currTaskStep++;
		}

		// Set taskpipeline progress to FINISHED (after finishing execution)
		taskpipelineCurrentState(TaskState.FINISHED);
	}

	/**
	 * Roll back each task in task pipeline.
	 * @throws TaskException
	 */
	public void rollback() {
		String tab = Constant.EMPTY;
		if (!isMain) {
			tab = Constant.DEBUG_INNER_PIPELINE;
		}

		log.info("{}ROLLBACK", tab);

		for (ITask task : queue) {
			try {
				task.rollback();
			} catch (RuntimeException e) {
				log.error(e.getMessage());
			}
		}
	}

	/**
	 * Perform any cleaning up required for each task in task pipeline.
	 * @throws TaskException
	 */
	public void finalise() {
		String tab = Constant.EMPTY;
		if (!isMain) {
			tab = Constant.DEBUG_INNER_PIPELINE;
		}

		log.info("{}START OF TASK FINALISE", tab);

		// Reverse the order of task in the queue so that 
		// cleaning up is performed from the last task to the beginning task
		Collections.reverse(queue);

		int currTaskStep = 1;
		// Perform finalise of each task in pipeline
		for (ITask task : queue) {
			try {
				log.debug(String.format("%sTASK_CLEANUP [%s] = %s", tab,
						currTaskStep, task.getDescription()));
				task.finalise();
				currTaskStep++;
			} catch (RuntimeException e) {
				log.error(e.getMessage());
			}
		}

		log.info("{}END OF TASK", tab);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<ITask> getQueue() {
		return queue;
	}

	public JobProgress getProgress() {
		if (progress == null) {
			progress = new JobProgress("Task pipeline execution progress");
		}
		return progress;
	}

	public void setMain(boolean isMain) {
		this.isMain = isMain;
	}

	public boolean isMain() {
		return isMain;
	}

	/**
	 * Set the name for the current taskpipeline.
	 * Also set the progress name and its task type.
	 * @param name The name for this taskpipeline.
	 * @param description The description for this taskpipeline. 
	 * @param taskType The type of taskpipeline.
	 */
	private void setProgressName(String name, TaskType taskType) {
		// Set taskpipeline name
		setName(name);

		// Ignore if no job progress e.g. inner taskpipeline
		if (!isMain()) {
			return;
		}

		getProgress().setName(name); // Set progress name
		getProgress().setTaskType(taskType);
	}

	/**
	 * Set the total number of steps required for this taskpipeline.
	 */
	private void taskpipelineNumberOfSteps() {
		// Ignore if inner taskpipeline job progress
		if (!isMain()) {
			return;
		}

		getProgress().setNumberOfSteps(queue.size());
		jobProgressDao.save(getProgress());
	}

	/**
	 * Set the current task step in the taskpipeline.
	 * @param currTaskStep The current task step.
	 * @param currTaskDesc The current task description.
	 */
	private void taskpipelineCurrentStep(int currTaskStep, String currTaskDesc) {
		// Ignore if inner taskpipeline job progress
		if (!isMain()) {
			return;
		}

		getProgress().setCurrentStep(currTaskStep, currTaskDesc);
		getProgress().updateProgressBasedOnCurrentStep();
		jobProgressDao.save(getProgress());
	}

	/**
	 * Set the current state in the taskpipeline.
	 * @param taskState The current task state.
	 */
	private void taskpipelineCurrentState(TaskState taskState) {
	// Ignore if inner taskpipeline job progress
		if (!isMain()) {
			return;
		}

		getProgress().setState(taskState);
		jobProgressDao.save(getProgress());
	}

	/**
	 * Set the current state in the taskpipeline
	 * and also set error message into current progress.
	 * @param taskState The current task state.
	 * @param taskError The current task error message.
	 */
	public void taskpipelineErrorState(TaskState taskState, String taskError) {
		// Ignore if inner taskpipeline job progress
		if (!isMain()) {
			return;
		}

		getProgress().setState(taskState);
		getProgress().setErrorMessage(taskError);
		jobProgressDao.save(getProgress());
	}

	/**
	 * Set the progress description for the current taskpipeline.
	 * @param description The specified description.
	 */
	public void setProgressDescription(String description) {
		// Ignore if no job progress e.g. inner taskpipeline
		if (!isMain()) {
			return;
		}

		getProgress().setJobDescription(description);
	}

}
