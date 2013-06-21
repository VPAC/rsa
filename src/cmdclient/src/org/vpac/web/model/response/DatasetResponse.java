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

import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.storage.model.Dataset;

 
@XmlRootElement(name = "Dataset")
public class DatasetResponse {
	private String id;
	private String name;
	private CellSize resolution;
	private long precision;
	private String dataAbstract;
	private Date created;

	public String getId() {
		return id;
	}

	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public CellSize getResolution() {
		return resolution;
	}

	@XmlAttribute
	public void setResolution(CellSize resolution) {
		this.resolution = resolution;
	}
	
	public long getPrecision() {
		return precision;
	}

	@XmlAttribute
	public void setPrecision(long precision) {
		this.precision = precision;
	}

	public String getDataAbstract() {
		return dataAbstract;
	}

	@XmlElement
	public void setDataAbstract(String dataAbstract) {
		this.dataAbstract = dataAbstract;
	}

	public DatasetResponse(Dataset d) {
		if(d != null){
			this.setId(d.getId());
			this.setName(d.getName());
			this.setDataAbstract(d.getAbst());
			this.setResolution(d.getResolution());
			this.setPrecision(d.getPrecision());
			this.setCreated(d.getCreated());
		}
	}
	
	public DatasetResponse() {
	}

	public Date getCreated() {
		return created;
	}

	@XmlAttribute
	public void setCreated(Date created) {
		this.created = created;
	}

	public Dataset toDataset() {
		Dataset ds = new Dataset();
		ds.setId(this.id);
		ds.setAbst(this.dataAbstract);
		ds.setName(this.name);
		ds.setCreated(this.created);
		ds.setPrecision(this.precision);
		ds.setResolution(this.resolution);
		return ds;
	}
}
