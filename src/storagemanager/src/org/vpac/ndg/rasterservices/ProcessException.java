package org.vpac.ndg.rasterservices;

public class ProcessException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private int exitCode;

	public ProcessException(int exitCode) {
		super("Command exited with error code " + exitCode);
		this.exitCode = exitCode;
	}

	public int getExitCode() {
		return exitCode;
	}
}
