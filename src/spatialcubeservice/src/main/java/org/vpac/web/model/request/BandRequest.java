package org.vpac.web.model.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.vpac.ndg.rasterdetails.RasterDetails;

public class BandRequest {

	@NotBlank
	private String datasetId; // mandatory (not null/empty/trailing whitespaces)
	@NotBlank
	private String name; // mandatory (not null/empty/trailing whitespaces)
	private RasterDetails type;
	@NotNull
	private boolean metadata;
	@NotNull
	private boolean continuous;

	public String getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RasterDetails getType() {
		return type;
	}

	public void setType(RasterDetails type) {
		this.type = type;
	}

	public boolean isMetadata() {
		return metadata;
	}

	public void setMetadata(boolean metadata) {
		this.metadata = metadata;
	}

	public boolean isContinuous() {
		return continuous;
	}

	public void setContinuous(boolean continuous) {
		this.continuous = continuous;
	}
}
