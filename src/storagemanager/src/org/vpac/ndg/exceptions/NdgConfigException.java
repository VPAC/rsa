package org.vpac.ndg.exceptions;

import org.vpac.ndg.application.Constant;

/**
 * Exception class when a failure is detected when parsing configuration file.
 * @author hsumanto
 *
 */
public class NdgConfigException extends Exception {
	private static final long serialVersionUID = 1L;

	public NdgConfigException() {
		super();
	}

	public NdgConfigException(String message, String cause) {
		super(String.format(Constant.ERR_READ_CONFIGURATION, message, cause));
	}	
	
	public NdgConfigException(String message, Throwable cause) {
		super(String.format(Constant.ERR_READ_CONFIGURATION, message, cause));
	}

	public NdgConfigException(String message) {
		super(message);
	}

	public NdgConfigException(Throwable cause) {
		super(cause);
	}
}
