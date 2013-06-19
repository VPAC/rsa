package org.vpac.web.model.request;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.sun.istack.Nullable;

public class TimeSliceRequest {

	@NotBlank
	private String datasetId;
	@NotNull
	private Date creationDate;
	@Nullable
	private String timesliceId;
	@Nullable
	private double xmin;
	public double getXmin() {
		return xmin;
	}
	public void setXmin(double xmin) {
		this.xmin = xmin;
	}
	public double getXmax() {
		return xmax;
	}
	public void setXmax(double xmax) {
		this.xmax = xmax;
	}
	public double getYmin() {
		return ymin;
	}
	public void setYmin(double ymin) {
		this.ymin = ymin;
	}
	public double getYmax() {
		return ymax;
	}
	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	@Nullable
	private double xmax;
	@Nullable
	private double ymin;
	@Nullable
	private double ymax;
	@Nullable
	private String abs;
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	public String getTimesliceId() {
		return timesliceId;
	}
	public void setTimesliceId(String timesliceId) {
		this.timesliceId = timesliceId;
	}
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
}
