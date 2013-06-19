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
