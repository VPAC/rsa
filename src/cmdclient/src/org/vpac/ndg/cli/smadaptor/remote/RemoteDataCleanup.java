package org.vpac.ndg.cli.smadaptor.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.vpac.ndg.cli.smadaptor.DataCleanup;
import org.vpac.web.model.response.CleanUpResponse;

public class RemoteDataCleanup implements DataCleanup {
	public static String CLEAN_UP_LOCKS_URL = "/SpatialCubeService/Data/CleanUp.xml?force={force}";
	private String baseUri;
	
	@Autowired
	protected RestTemplate restTemplate;

	@Override
	public int cleanLocks(boolean force) {
	    CleanUpResponse response = restTemplate.postForObject(baseUri + CLEAN_UP_LOCKS_URL, null, CleanUpResponse.class, force);
		return response.getCount();
/*
		Process process = ProcessUpdateTimer.INSTANCE.getProcess();
		return timeSliceUtil.cleanOthers(process.getId(), force);
*/	}

	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
}
