package org.vpac.web.model.request;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.vpac.ndg.geometry.Point;

import com.sun.istack.Nullable;

public class DataExportRequest {

	@NotNull
	private String datasetId;
	@Nullable
	private List<String> bandId;
	@Nullable
	private Date searchStartDate;
	@Nullable
	private Date searchEndDate;
	@Nullable
	private String projection;
	@Nullable
	private Point<Double> topLeft;
	@Nullable
	private Point<Double> bottomRight;
	
	public String getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public Date getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate(Date searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public Date getSearchEndDate() {
		return searchEndDate;
	}

	public void setSearchEndDate(Date searchEndDate) {
		this.searchEndDate = searchEndDate;
	}

	public String getProjection() {
		return projection;
	}

	public void setProjection(String projection) {
		this.projection = projection;
	}

	public Point<Double> getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(Point<Double> topLeft) {
		this.topLeft = topLeft;
	}

	public Point<Double> getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(Point<Double> bottomRight) {
		this.bottomRight = bottomRight;
	}
	public DataExportRequest() {
		
	}

	public List<String> getBandId() {
		return bandId;
	}

	public void setBandId(List<String> bandId) {
		this.bandId = bandId;
	}
}
