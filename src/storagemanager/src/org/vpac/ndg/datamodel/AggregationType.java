package org.vpac.ndg.datamodel;

public enum AggregationType {
	UNION("union"),
	TILED("tiled"),
	JOIN_NEW("joinNew");

	protected String value;

	private AggregationType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
