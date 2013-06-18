package org.vpac.ndg.cli.smadaptor.remote;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.vpac.ndg.cli.smadaptor.DataQuery;
import org.vpac.ndg.geometry.Box;
import org.vpac.web.model.response.QueryResponse;

public class RemoteDataQuery implements DataQuery {
	public static String DATA_QUERY_URL = "/SpatialCubeService/Data/Query.xml?minX={minX}&minY={minY}&maxX={maxX}&maxY={maxY}&startDate={startDate}&endDate={endDate}";

	static final Logger log = LoggerFactory.getLogger(RemoteDataQuery.class);

	Path queryPath;
	Integer threads;
	private String baseUri;
	private Box extent;
	private String startDate;
	private String endDate;
	private String netcdfVersion;

	@Autowired
	protected RestTemplate restTemplate;
	
	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	@Override
	public void setQueryPath(Path definition) {
		this.queryPath = definition;
	}

	@Override
	public void setThreads(String threads) {
		this.threads = Integer.parseInt(threads);
	}

	@Override
	public void setExtents(String x1, String y1, String x2, String y2) {
		this.extent = new Box(Double.parseDouble(x1), Double.parseDouble(y1), Double.parseDouble(x2), Double.parseDouble(y2));
	}

	@Override
	public void setTimeRange(String start, String end) {
		this.startDate = start;
		this.endDate = end;
	}

	@Override
	public String start() {
		log.debug("Running query {}", queryPath);
		log.debug("Threads {}", threads);
		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
	    mvm.add("threads", "1");
	    Double minX = null, minY = null, maxX = null, maxY = null;
	    if(extent != null) {
	    	minX = extent.getXMin();
	    	minY = extent.getYMin();
	    	maxX = extent.getXMax();
	    	maxY = extent.getYMax();
	    }

	    if(startDate != null) {
		    mvm.add("startDate", startDate);
		    mvm.add("endDate", endDate);
	    }
	    Resource file = new FileSystemResource(queryPath.toFile());
	    mvm.add("file", file);

		QueryResponse response = restTemplate.postForObject(baseUri
				+ DATA_QUERY_URL, mvm, QueryResponse.class, minX, minY, maxX,
				maxY, startDate, endDate, getNetcdfVersion());
		return response.getTaskId();
	}

	@Override
	public void setOutputPath(Path outputFile) {
		// do nothing because this property only used for local copy
		// remote query output is located in pickup directory. so command line
		// client should use "data download" command
	}

	public String getNetcdfVersion() {
		return netcdfVersion;
	}

	public void setNetcdfVersion(String netcdfVersion) {
		this.netcdfVersion = netcdfVersion;
	}


}
