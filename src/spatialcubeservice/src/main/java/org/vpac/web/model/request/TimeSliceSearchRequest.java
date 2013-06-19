package org.vpac.web.model.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.sun.istack.Nullable;

public class TimeSliceSearchRequest {

	@NotNull
	private String datasetId;
	@Nullable
	private Date creationDate;
	@Nullable
	private Date searchBeginDate;
	@Nullable
	private Date searchEndDate;
	
	public String getDatasetId() {
		return datasetId;
	}
	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getSearchEndDate() {
		return searchEndDate;
	}
	public void setSearchEndDate(Date searchEndDate) {
		this.searchEndDate = searchEndDate;
	}
	public Date getSearchBeginDate() {
		return searchBeginDate;
	}
	public void setSearchBeginDate(Date searchBeginDate) {
		this.searchBeginDate = searchBeginDate;
	}
}
