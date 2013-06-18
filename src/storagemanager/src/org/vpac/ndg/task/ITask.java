package org.vpac.ndg.task;

import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;

public interface ITask {
	/**
	 * Perform initialisation on the task.
	 * @throws TaskInitialisationException An error encountered during initialisation of the task.
	 */	
	public void initialise() throws TaskInitialisationException;
	/**
	 * Perform the execution of the task.
	 * @throws TaskException An error ecountered during execution of the task.
	 */
	public void execute() throws TaskException;
	/**
	 * Perform the required rollback action when task execution has failed.
	 */
	public void rollback();
	/**
	 * Perform cleaning up action required at the end of the task.
	 */
	public void finalise();
	/**
	 * Get description of the task.
	 * @return Returns the description of the task.
	 */
	public String getDescription();
}
