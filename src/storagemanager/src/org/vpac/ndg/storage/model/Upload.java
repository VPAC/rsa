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

/**
 * 
 */
package org.vpac.ndg.storage.model;
import java.util.Date;

public class Upload implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getTimeSliceId() {
		return timeSliceId;
	}
	public void setTimeSliceId(String timeSliceId) {
		this.timeSliceId = timeSliceId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	private String fileId;
	private String timeSliceId;
	private Date created;
	
	public Upload(String timeSliceId) {
		this.timeSliceId = timeSliceId;
		this.created = new Date();
	}
	public Upload() {
	}
}