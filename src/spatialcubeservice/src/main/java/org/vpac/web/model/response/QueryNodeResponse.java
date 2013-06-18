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
