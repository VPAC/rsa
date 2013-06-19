package org.vpac.ndg.exceptions;

import org.vpac.ndg.application.Constant;

/**
 * Exception class when a failure is detected when performing task.
 * @author hsumanto
 *
 */
public class TaskException extends Exception {
	private static final long serialVersionUID = 1L;

	public TaskException() {
		super();
	}

	public TaskException(String message, String cause) {
		super(String.format(Constant.ERR_TASK_EXCEPTION, message, cause));
	}	
	
	public TaskException(String message, Throwable cause) {
		super(String.format(Constant.ERR_TASK_EXCEPTION, message, cause));
	}

	public TaskException(String message) {
		super(message);
	}

	public TaskException(Throwable cause) {
		super(cause);
	}	
}
