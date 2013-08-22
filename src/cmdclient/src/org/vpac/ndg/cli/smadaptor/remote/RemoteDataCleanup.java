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
