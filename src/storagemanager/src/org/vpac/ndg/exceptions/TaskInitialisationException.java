package org.vpac.ndg.exceptions;

import org.vpac.ndg.application.Constant;

/**
 * Exception class when a failure is detected when performing task initialisation.
 * @author hsumanto
 *
 */
public class TaskInitialisationException extends Exception {
	private static final long serialVersionUID = 1L;

	public TaskInitialisationException() {
		super();
	}

	public TaskInitialisationException(String message, String cause) {
		super(String.format(Constant.ERR_TASK_INITIALISATION_EXCEPTION, message, cause));
	}	
	
	public TaskInitialisationException(String message, Throwable cause) {
		super(String.format(Constant.ERR_TASK_INITIALISATION_EXCEPTION, message, cause));
	}

	public TaskInitialisationException(String message) {
		super(message);
	}

	public TaskInitialisationException(Throwable cause) {
		super(cause);
	}
}
