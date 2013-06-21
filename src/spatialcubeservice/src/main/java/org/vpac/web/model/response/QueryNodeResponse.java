/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.web.model.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is intended for a query root element response. This is the base
 * class for QueryDatasetResponse and QueryFilterResponse.
 * 
 * @author hsumanto
 * 
 */
@XmlRootElement(name = "QueryRootElement")
public class QueryNodeResponse extends QueryElementResponse {

	private String qualname;
	private String description;
	private List<QueryInputResponse> inputs;
	private List<QueryOutputResponse> outputs;

	public QueryNodeResponse() {
	}

	public String getQualname() {
		return qualname;
	}

	public void setQualname(String qualname) {
		this.qualname = qualname;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement(name = "input")
	public List<QueryInputResponse> getInputs() {
		return inputs;
	}

	public void setInputs(List<QueryInputResponse> inputs) {
		this.inputs = inputs;
	}

	@XmlElement(name = "output")
	public List<QueryOutputResponse> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<QueryOutputResponse> outputs) {
		this.outputs = outputs;
	}
}
