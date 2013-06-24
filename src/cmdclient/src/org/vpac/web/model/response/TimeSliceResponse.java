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

package org.vpac.web.model.response;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.storage.model.TimeSlice;

 
@XmlRootElement(name = "TimeSlice")
public class TimeSliceResponse {
	
	private String id;
	private String relativeLocation;
	private String vrtFile;
	private Date created;
	private int lockCount;
	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;
	
	public int getLockCount() {
		return lockCount;
	}

	@XmlElement
	public void setLockCount(int lockCount) {
		this.lockCount = lockCount;
	}

	private String dataAbstract;
	
	public String getId() {
		return id;
	}

	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}

	public String getRelativeLocation() {
		return relativeLocation;
	}

	@XmlElement
	public void setRelativeLocation(String relativeLocation) {
		this.relativeLocation = relativeLocation;
	}

	public String getVrtFile() {
		return vrtFile;
	}

	@XmlElement
	public void setVrtFile(String vrtFile) {
		this.vrtFile = vrtFile;
	}

	public Date getCreated() {
		return created;
	}

	@XmlElement
	public void setCreated(Date created) {
		this.created = created;
	}

	public String getDataAbstract() {
		return dataAbstract;
	}
	
	@XmlElement
	public void setDataAbstract(String dataAbstract) {
		this.dataAbstract = dataAbstract;
	}

	public double getXmin() {
		return xmin;
	}
	
	@XmlElement
	public void setXmin(double xmin) {
		this.xmin = xmin;
	}
	
	public double getXmax() {
		return xmax;
	}
	
	@XmlElement
	public void setXmax(double xmax) {
		this.xmax = xmax;
	}
	
	public double getYmin() {
		return ymin;
	}
	
	@XmlElement
	public void setYmin(double ymin) {
		this.ymin = ymin;
	}
	
	public double getYmax() {
		return ymax;
	}
	
	@XmlElement
	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	
	public TimeSliceResponse(TimeSlice ts) {
		if(ts != null) {
			this.id = ts.getId();
			this.relativeLocation = ts.getRelativeLocation();
			this.vrtFile = ts.getVrtFile();
			this.created = ts.getCreated();
			this.dataAbstract = ts.getDataAbstract();
			this.xmin = ts.getXmin();
			this.xmax = ts.getXmax();
			this.ymin = ts.getYmin();
			this.ymax = ts.getYmax();
		}
	}
	
	public TimeSliceResponse() {
	}

	public TimeSlice toTimeSlice() {
		TimeSlice ts = new TimeSlice();
		ts.setId(this.id);
		ts.setCreated(this.created);
		ts.setLockCount(this.lockCount);
		ts.setXmin(this.xmin);
		ts.setXmax(this.xmax);
		ts.setYmin(this.ymin);
		ts.setYmax(this.ymax);
		return ts;
	}
}
