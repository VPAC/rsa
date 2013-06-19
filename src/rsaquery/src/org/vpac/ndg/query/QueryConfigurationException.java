package org.vpac.ndg.query;

public class QueryConfigurationException extends Exception {

	private static final long serialVersionUID = -8844585640720514816L;

	public QueryConfigurationException() {
		super();
	}

	public QueryConfigurationException(String s) {
		super(s);
	}

	public QueryConfigurationException(Throwable e) {
		super(e);
	}

	public QueryConfigurationException(String s, Throwable e) {
		super(s, e);
	}
}
