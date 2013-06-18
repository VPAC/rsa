package org.vpac.ndg.query.io;

import java.util.List;

import org.vpac.ndg.query.coordinates.QueryCoordinateSystem;

public class DatasetMetadata {

	private QueryCoordinateSystem csys;
	private List<String> variables;

	public QueryCoordinateSystem getCsys() {
		return csys;
	}
	public void setCsys(QueryCoordinateSystem csys) {
		this.csys = csys;
	}

	public List<String> getVariables() {
		return variables;
	}
	public void setVariables(List<String> variables) {
		this.variables = variables;
	}

}
