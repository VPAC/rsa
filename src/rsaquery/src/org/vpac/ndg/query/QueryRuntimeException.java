package org.vpac.ndg.query;

public class QueryRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -218461885702808857L;

	public QueryRuntimeException() {
		super();
	}

	public QueryRuntimeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public QueryRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryRuntimeException(String message) {
		super(message);
	}

	public QueryRuntimeException(Throwable cause) {
		super(cause);
	}

}
