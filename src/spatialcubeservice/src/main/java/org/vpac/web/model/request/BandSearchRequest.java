package org.vpac.web.model.request;

import javax.validation.constraints.NotNull;

public class BandSearchRequest {

	@NotNull
	private String datasetId;

	public String getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}
}
