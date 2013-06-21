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

package org.vpac.ndg.storage.model;

import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;

/**
 * Used to communicate the status of long-running jobs to the user. This is
 * actually composed of two progress items:
 * <ul>
 * <li>The current task (discrete).</li>
 * <li>The progress of the current task (continuous).</li>												
 * </ul>
 * @author adfries
 */
public class JobProgress {

	private String id;
	private int numberOfSteps = 0;
	private int currentStep = 0;
	private double currentStepProgress = 0.0;
	private String stepDescription = Constant.EMPTY;
	private String jobDescription = Constant.UNKNOWN;
	private String processingSource = Constant.UNKNOWN;
	private String name = Constant.EMPTY;
	private TaskState state = TaskState.RUNNING;
	private String errorMessage = Constant.EMPTY;
	private TaskType taskType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JobProgress() {
		setJobDescription(Constant.INFO_JOB_PROGRESS);
	}

	public JobProgress(String jobDesc) {
		setJobDescription(jobDesc);
	}
	
	public int getNumberOfSteps() {
		return numberOfSteps;
	}

	/**
	 * Update the progress object to reflect the current step of execution.
	 * <p>Note: This method does not cause the object to be saved to the database. To do
	 * that, call its relevant DAO method explicitely.</p>
	 * @param num The index of the step that is executing (0-based).
	 * @param desc The description of the current step.
	 */
	public void setCurrentStep(int num, String desc) {
		
		setStepDescription(desc);
		
		if( num < 0 ) {
			currentStep = 0;
		} else if( num > numberOfSteps ) {
			currentStep = numberOfSteps; 
		} else {
			currentStep = num;
		}
	}
	/**
	 * Set the progress of the current step.
	 * <p>Note: This method does not cause the object to be saved to the database. To do
	 * that, call its relevant DAO method explicitely.</p>
	 * @param progress The fraction of this step that is now complete (between
	 * zero and one hundred).
	 */
	public void setCurrentStepProgress(double progress) {
		currentStepProgress = progress;
	}

	/**
	 * Update progress based on current step.
	 * If numberOfSteps less or equal to zero then do nothing.
	 */
	public void updateProgressBasedOnCurrentStep() {
		if (numberOfSteps > 0) {
			double progress = currentStep * 100 / numberOfSteps; 
			setCurrentStepProgress(progress);
		}
	}

	/**
	 * Set the number of steps in the overall operation.
	 * <p>Note: This method does not cause the object to be saved to the database. To do
	 * that, call its relevant DAO method explicitely.</p>
	 * @param num The number of steps.
	 */
	public void setNumberOfSteps(int num) {
		numberOfSteps = num;
	}

	public void setCompleted() {
		setCurrentStep(getNumberOfSteps());
		setCurrentStepProgress(100.0);
		setState(TaskState.FINISHED);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCurrentStep() {
		return currentStep;
	}
	
	public void setCurrentStep(int curr) {
		currentStep = curr;
	}

	public double getCurrentStepProgress() {
		return currentStepProgress;
	}

	/**
	 * @return The name of the current step.
	 */
	public String getStepDescription() {
		return stepDescription;
	}

	/**
	 * @param stepDescription The name of the current step.
	 */
	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getProcessingSource() {
		return processingSource;
	}

	public void setProcessingSource(String processingSource) {
		this.processingSource = processingSource;
	}

	public void setState(TaskState state) {
		this.state = state;
	}

	public TaskState getState() {
		return state;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public TaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}

}
