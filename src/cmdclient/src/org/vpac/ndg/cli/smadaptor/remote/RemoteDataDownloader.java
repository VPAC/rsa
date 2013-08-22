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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.vpac.ndg.cli.smadaptor.DataDownloader;

public class RemoteDataDownloader implements DataDownloader {
	public static String DATA_DOWNLOAD_URL = "/SpatialCubeService/Data/Download/{taskId}.xml";
	private String baseUri;
	
	@Autowired
	protected RestTemplate restTemplate;

	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	@Override
	public void Download(String taskId, Path output) {
		try {
			URL httpUrl = new URL(baseUri + "/SpatialCubeService/Data/Download/" + taskId + ".xml");
			FileUtils.copyURLToFile(httpUrl, output.toFile());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
