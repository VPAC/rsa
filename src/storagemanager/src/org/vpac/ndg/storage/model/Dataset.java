/*
 * This file is part of SpatialCube.
 *
 * SpatialCube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SpatialCube is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SpatialCube.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.storage.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;
import org.vpac.ndg.common.datamodel.CellSize;

public class Dataset implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private List<Band> bands;
	private List<TimeSlice> slices;
	private CellSize resolution = CellSize.km10;
	private String name;
	private Date created;
	private Date modified;
	private String abst;
	private long precision = DateUtils.MILLIS_PER_DAY;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Band> getBands() {
		return bands;
	}
	public void setBands(List<Band> bands) {
		this.bands = bands;
	}
	public List<TimeSlice> getSlices() {
		return slices;
	}
	public void setSlices(List<TimeSlice> slices) {
		this.slices = slices;
	}
	public String getName() {
		return name;
	}
	public String getUri() {
		return String.format("rsa:%s/%s", getName(), getResolution().toHumanString());
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public String getAbst() {
		return abst;
	}
	public void setAbst(String abst) {
		this.abst = abst;
	}
	public CellSize getResolution() {
		return resolution;
	}
	public void setResolution(CellSize resolution) {
		this.resolution = resolution;
	}
	public long getPrecision() {
		return precision;
	}
	public void setPrecision(long precision) {
		this.precision = precision;
	}

	public Dataset() {
		setDefaultCreationTime();
	}

	public Dataset(String name, CellSize resolution, long precision) {
		this(name, "", resolution, precision);
	}

	public Dataset(String name, String abstraction, CellSize resolution, long precision) {
		this.name = name;
		this.resolution = resolution;
		this.abst = abstraction;
		this.precision = precision;
		setDefaultCreationTime();
	}

	private void setDefaultCreationTime() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));;
		this.created = cal.getTime();
		this.modified = cal.getTime();
	}

	@Override
	public String toString() {
		return String.format("Dataset(%s/%s)", getName(),
				getResolution().toHumanString());
	}

	public boolean hasBand(Band band) {
		boolean result = false;
		for(Band b: bands) {
			if(b.getName().equalsIgnoreCase(band.getName())) {
				result = true;
				break;
			}
		}
		return result;
	}
}