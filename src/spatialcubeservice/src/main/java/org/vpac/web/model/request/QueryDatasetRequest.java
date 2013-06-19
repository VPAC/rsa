package org.vpac.web.model.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.vpac.web.model.response.QueryInputResponse;
import org.vpac.web.model.response.QueryOutputResponse;

/**
 * Used for query dataset related request validation.
 * 
 * @author hsumanto
 * 
 */
public class QueryDatasetRequest {

	@NotBlank
	private String name;
	@NotBlank
	private String type;
	@NotNull
	private String qualname;
	@NotNull
	private String description;
	@NotNull
	private List<QueryInputResponse> inputs;
	@NotNull
	private List<QueryOutputResponse> outputs;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public List<QueryInputResponse> getInputs() {
		return inputs;
	}

	public void setInputs(List<QueryInputResponse> inputs) {
		this.inputs = inputs;
	}

	public List<QueryOutputResponse> getOutputs() {
		return outputs;
	}

	public void setOutputs(List<QueryOutputResponse> outputs) {
		this.outputs = outputs;
	}
}
