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
