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

package org.vpac.web.model.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is intended for a collection of QueryDataset response.
 * 
 * @author hsumanto
 * @author adfries
 * 
 */
@XmlRootElement(name = "nodes")
public class QueryNodeCollectionResponse {

	private List<? extends QueryNodeResponse> items;
	private String name;

	@XmlElement(name = "node")
	public List<? extends QueryNodeResponse> getItems() {
		return items;
	}

	public void setItems(List<? extends QueryNodeResponse> items) {
		this.items = items;
	}

	public QueryNodeCollectionResponse() {
		name = "";
		items = new ArrayList<>();
	}

	public QueryNodeCollectionResponse(String name, List<? extends QueryNodeResponse> items) {
		this.name = name;
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
