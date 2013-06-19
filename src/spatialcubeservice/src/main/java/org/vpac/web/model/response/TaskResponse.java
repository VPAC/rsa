package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.application.Constant;
import org.vpac.ndg.storage.model.JobProgress;
import org.vpac.ndg.common.datamodel.TaskState;

@XmlRootElement(name = "Task")
public class TaskResponse {
	private String id;
	private int numberOfSteps = 0;
	private int currentStep = 0;
	private double currentStepProgress = 0.0;
	private String stepDescription = Constant.EMPTY;
	private String jobDescription = Constant.UNKNOWN;
	private String processingSource = Constant.UNKNOWN;
	private String name = Constant.EMPTY;
	private TaskState state = null;
	private String errorMessage = Constant.EMPTY;
	
	public int getNumberOfSteps() {
		return numberOfSteps;
	}
	@XmlAttribute
	public void setNumberOfSteps(int numberOfSteps) {
		this.numberOfSteps = numberOfSteps;
	}
	public int getCurrentStep() {
		return currentStep;
	}
	@XmlAttribute
	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
	}
	public double getCurrentStepProgress() {
		return currentStepProgress;
	}
	public void setCurrentStepProgress(double currentStepProgress) {
		this.currentStepProgress = currentStepProgress;
	}
	public String getStepDescription() {
		return stepDescription;
	}
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
	public TaskState getState() {
		return state;
	}
	@XmlAttribute
	public void setState(TaskState state) {
		this.state = state;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getId() {
		return id;
	}
	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TaskResponse() {
	}
	
	public TaskResponse(JobProgress jp) {
		this.setId(jp.getId());
		this.setName(jp.getName());
		this.setNumberOfSteps(jp.getNumberOfSteps());
		this.setCurrentStep(jp.getCurrentStep());
		this.setCurrentStepProgress(jp.getCurrentStepProgress());
		this.setErrorMessage(jp.getErrorMessage());
		this.setJobDescription(jp.getJobDescription());
		this.setProcessingSource(jp.getProcessingSource());
		this.setState(jp.getState());
		this.setStepDescription(jp.getStepDescription());		
	}
	
	public JobProgress toJobProgress() {
		JobProgress jp = new JobProgress();
		jp.setId(this.id);
		jp.setName(this.name);
		jp.setNumberOfSteps(this.numberOfSteps);
		jp.setCurrentStep(this.currentStep);
		jp.setCurrentStepProgress(this.currentStepProgress);
		jp.setErrorMessage(this.errorMessage);
		jp.setJobDescription(this.jobDescription);
		jp.setProcessingSource(this.processingSource);
		jp.setState(this.state);
		jp.setStepDescription(this.stepDescription);		
		return jp;
	}
}
