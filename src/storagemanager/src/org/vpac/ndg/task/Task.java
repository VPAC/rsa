package org.vpac.ndg.task;

import org.vpac.ndg.storage.model.JobProgress;

/**
 * This abstract class represents a command to be executed.
 * @author hsumanto
 *
 */
public abstract class Task implements ITask {
	/** Task description */
	private String description;
	/** Job progress on the task */
	private JobProgress progress;
	/** Perform cleaning up on source during finalise */
	private boolean cleanupSource;
	/** Perform cleaning up on target during finalise */
	private boolean cleanupTarget;
	/** Perform checking on source during execution */
	private boolean checkSource;
	/**
	 * Construct a task using the specified description.
	 * @param description The given task description.
	 */
	public Task(String description) {
		setDescription(description);
		// By default clean up source and target
		setCleanupSource(true);
		setCleanupTarget(true);
		// By default check source during execution
		setCheckSource(true);
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setProgress(JobProgress progress) {
		this.progress = progress;
	}

	public JobProgress getProgress() {
		return progress;
	}

	public void setCleanupSource(boolean cleanupSource) {
		this.cleanupSource = cleanupSource;
	}

	public boolean isCleanupSource() {
		return cleanupSource;
	}

	public void setCleanupTarget(boolean cleanupTarget) {
		this.cleanupTarget = cleanupTarget;
	}

	public boolean isCleanupTarget() {
		return cleanupTarget;
	}

	public boolean isCheckSource() {
		return checkSource;
	}

	public void setCheckSource(boolean checkSource) {
		this.checkSource = checkSource;
	}
}
