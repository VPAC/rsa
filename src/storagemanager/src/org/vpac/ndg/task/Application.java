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

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;

/**
 * An abstract storage manager tool. This encapsulates the execution of a task
 * pipeline.
 * @author adfries
 */
public abstract class Application implements Callable<Void> {
	final private Logger log = LoggerFactory.getLogger(Application.class);

	private TaskPipeline taskPipeline;
	private Path temporaryDirectory;

	public Application() {
		taskPipeline = new TaskPipeline(getJobName(), getTaskType());
	}

	/**
	 * Configure the job. After calling this, it is safe to use
	 * {@link #getTaskId()}.
	 */
	public void configure() throws TaskInitialisationException {
		// Perform task pipeline initialisation
		try {
			// Initialise run
			initialise();
			// Set progress description with this task description
			getTaskPipeline().setProgressDescription(getJobDescription());
			// Set up the tasks
			createTasks();
			// Run each specific initialisation on each task in pipeline
			getTaskPipeline().initialise();
		} catch (TaskInitialisationException | RuntimeException e) {
			// Set error state on progress
			getTaskPipeline().taskpipelineErrorState(TaskState.INITIALISATION_ERROR, e.getMessage());
			// Report error
			log.error(Constant.ERR_TASK_INITIALISATION_FAILED);
			log.error(e.getMessage());
			if (e instanceof TaskInitialisationException)
				throw e;
			else
				throw new TaskInitialisationException(e);
		} finally {
		}
	}

	/**
	 * Run the job in a background thread.
	 *
	 * <p>
	 * <b>NOTE:</b> This class is not thread-safe. After calling this method, it
	 * is <em>not OK</em> to continue using this object on the calling thread.
	 * </p>
	 */
	// TODO: The Executor should be separated out into a different class. For
	// now, the task is run as though it was a simple Runnable.
	public void runInBackground() {
//		ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
//        try {
//			threadExecutor.submit(this).get();
//		} catch (InterruptedException | ExecutionException e) {
//			e.printStackTrace();
//		}

		final Callable<Void> appInstance = this;

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					appInstance.call();
				} catch (Exception e) {
					log.error("Task exited abnormally: {}", e.getMessage());
				}
			}
		});

		thread.start();
	}

	/**
	 * Run the job.
	 */
	@Override
	public Void call() throws TaskException {
		executeTaskPipeline();
		return null;
	}

	/**
	 * Executes the task pipeline. 
	 * @throws TaskException When there is error when executing task pipeline.
	 */
	protected void executeTaskPipeline() throws TaskException {
		// Perform task pipeline execution
		try {
			preExecute();
			try {
				// Run tasks as a transaction
				log.info("Task starting: {}", getTaskPipeline().getProgress().getId());
				getTaskPipeline().run();
				log.info("Task finished: {}", getTaskPipeline().getProgress().getId());
			} catch (Exception e) {
				log.error("Exception during execution of task pipeline:", e);
				throw e;
			} finally {
				try {
					postExecute();
				} catch (Exception e) {
					log.error("Exception during finally block:", e);
					throw e;
				}
			}
		} catch (TaskException | RuntimeException e) {
			// Set error state on progress
			log.info("Task failed: {}", getTaskPipeline().getProgress().getId());
			getTaskPipeline().taskpipelineErrorState(TaskState.EXECUTION_ERROR, e.getMessage());
			// Report error
			log.error("TaskException:", e);
			// Roll back tasks as a transaction
			getTaskPipeline().rollback();
			if (e instanceof TaskException)
				throw e;
			else {
				e.printStackTrace();
				throw new TaskException(e);
			}
		} finally {
			try {
				finalise();
			} catch (Exception e) {
				log.error("Exception during finally block:", e);
				throw e;
			}
		}
	}

	protected void preExecute() throws TaskException {
		
	}

	protected void postExecute() {
		
	}

	/**
	 * @return The ID of the running task. Note that this will only be valid
	 * after the task has started!
	 */
	public String getTaskId() {
		String result = null;
		if(getTaskPipeline().getProgress() != null) {
			result = getTaskPipeline().getProgress().getId();
		}

		return result;
	}

	/**
	 * Get the task pipeline for the current import.
	 * @return Returns the task pipeline for the current import.
	 */
	protected TaskPipeline getTaskPipeline() {
		return taskPipeline;
	}

	protected Path getWorkingDirectory() {
		return temporaryDirectory;
	}

	protected void initialise() throws TaskInitialisationException {
		try {
			temporaryDirectory = FileUtils.createTmpLocation();
		} catch (IOException e) {
			log.error("Could not create temporary directory: {}", e);
			throw new TaskInitialisationException(String.format("Error encountered when creating temporary directory: %s", temporaryDirectory));
		}
		log.info("Temporary directory: {}", temporaryDirectory);
	}

	protected void finalise() {
		// Perform cleaning up required by each task
		getTaskPipeline().finalise();

		try {
			FileUtils.removeDirectory(temporaryDirectory);
		} catch (IOException e) {
			log.error("Failed to delete temporary directory: {}", e);
		}
	}

	protected abstract void createTasks() throws TaskInitialisationException;
	protected abstract String getJobName();
	protected abstract String getJobDescription();
	protected abstract TaskType getTaskType();

}
