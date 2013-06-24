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

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.vpac.ndg.common.datamodel.CellSize;

import com.sun.istack.Nullable;

/**
 * Used for dataset related request.
 * Validate that the dataset name is not null/empty/trailing whitespaces.
 * Validate that the dataset resolution is the accepted resolution.
 */
public class DatasetRequest {

	@Nullable
	private String id;
	@NotBlank
	private String name;
	@NotNull
	private CellSize resolution;
	@NotNull
	private String precision;
	@NotNull
	private String dataAbstract;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CellSize getResolution() {
		return resolution;
	}
	public void setResolution(CellSize resolution) {
		this.resolution = resolution;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getDataAbstract() {
		return dataAbstract;
	}
	public void setDataAbstract(String dataAbstract) {
		this.dataAbstract = dataAbstract;
	}
}
