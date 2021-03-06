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
	private String resolution;
	@Nullable
	private String format;
	@Nullable
	private Point<Double> topLeft;
	@Nullable
	private Point<Double> bottomRight;
	@Nullable
	private Boolean useBilinearInterpolation;
	
	public Boolean getUseBilinearInterpolation() {
		return useBilinearInterpolation;
	}

	public void setUseBilinearInterpolation(Boolean useBilinearInterpolation) {
		this.useBilinearInterpolation = useBilinearInterpolation;
	}

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

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}
	
	
	
}
