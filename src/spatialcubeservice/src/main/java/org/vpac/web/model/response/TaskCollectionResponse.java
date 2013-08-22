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

import org.vpac.ndg.storage.model.JobProgress;
 
@XmlRootElement(name = "TaskCollection")
public class TaskCollectionResponse {
	private List<TaskResponse> items;

	public List<TaskResponse> getItems() {
		return items;
	}

	@XmlElement(name = "Task")
	public void setItems(List<TaskResponse> items) {
		this.items = items;
	}

	public TaskCollectionResponse(List<JobProgress> list) {
		if(list != null) {
			items = new ArrayList<TaskResponse>();
			for(JobProgress jp: list)
				this.items.add(new TaskResponse(jp));
		}
	}
	
	public TaskCollectionResponse() {
		items = new ArrayList<TaskResponse>();
	}

	public List<JobProgress> toJobProgress() {
		List<JobProgress> list = new ArrayList<JobProgress>();
		for(TaskResponse tr : this.getItems()) {
			list.add(tr.toJobProgress());
		}
		return list;
	}
}
