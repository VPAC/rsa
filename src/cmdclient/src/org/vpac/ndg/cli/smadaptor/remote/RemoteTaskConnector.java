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

package org.vpac.ndg.cli.smadaptor.remote;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.vpac.ndg.cli.smadaptor.TaskConnector;
import org.vpac.ndg.storage.model.JobProgress;
import org.vpac.web.model.response.TaskCollectionResponse;
import org.vpac.web.model.response.TaskResponse;

public class RemoteTaskConnector implements TaskConnector {
	public static String GET_ALL_TASKS_URL = "/SpatialCubeService/Data/Task.xml";
	public static String GET_TASK_BY_ID_URL = "/SpatialCubeService/Data/Task/{id}.xml";

	private String baseUri;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Override
	public List<JobProgress> list(String typestr, String statusstr) {
		TaskCollectionResponse response = restTemplate.getForObject(baseUri + GET_ALL_TASKS_URL, TaskCollectionResponse.class);
		return response.toJobProgress();
	}

	@Override
	public JobProgress get(String id) {
		TaskResponse response = restTemplate.getForObject(baseUri + GET_TASK_BY_ID_URL, TaskResponse.class, id);
		return response.toJobProgress();
	}
	
	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
}
