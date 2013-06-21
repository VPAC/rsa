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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.storage.model.Dataset;
 
@XmlRootElement(name = "DatasetCollection")
public class DatasetCollectionResponse {
	private List<DatasetResponse> items;

	public List<DatasetResponse> getItems() {
		return items;
	}

	@XmlElement(name = "Dataset")
	public void setItems(List<DatasetResponse> items) {
		this.items = items;
	}

	public DatasetCollectionResponse(List<Dataset> list) {
		if(list != null) {
			items = new ArrayList<DatasetResponse>();
			for(Dataset ds: list)
				this.items.add(new DatasetResponse(ds));
		}
	}
	
	public DatasetCollectionResponse() {
		items = new ArrayList<DatasetResponse>();
	}
	
	public List<Dataset> toDatasetList() {
		List<Dataset> list = new ArrayList<Dataset>();
		for(DatasetResponse dsr: items) {
			Dataset ds = new Dataset();
			ds.setId(dsr.getId());
			ds.setAbst(dsr.getDataAbstract());
			ds.setCreated(dsr.getCreated());
			ds.setName(dsr.getName());
			ds.setPrecision(dsr.getPrecision());
			ds.setResolution(dsr.getResolution());
			list.add(ds);
		}
		return list;
	}
}
