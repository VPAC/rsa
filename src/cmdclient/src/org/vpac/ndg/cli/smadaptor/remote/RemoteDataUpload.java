package org.vpac.ndg.cli.smadaptor.remote;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.vpac.ndg.cli.smadaptor.DataUpload;
import org.vpac.web.model.response.FileInfoResponse;

public class RemoteDataUpload implements DataUpload {
	public static String UPLOAD_FILE_URL = "/SpatialCubeService/Data/Upload.xml";

	private String baseUri;
	@Autowired
	protected RestTemplate restTemplate;
	
	protected List<Path> sourceFiles;
	protected String timeSliceId;
	protected String uploadId;

	public RemoteDataUpload() {
		sourceFiles = new ArrayList<Path>();
	}

	@Override
	public String upload() throws IOException, IllegalArgumentException {
		if(sourceFiles.size() == 0) {
			throw new IllegalArgumentException("No source file to upload.");
		}
		
		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
	    mvm.add("timesliceId", "");
	    mvm.add("taskId", this.uploadId);
	    for(Path file : sourceFiles) {
		    Resource resource = new FileSystemResource(file.toFile());
		    mvm.add("files", resource);
	    }

	    FileInfoResponse response = restTemplate.postForObject(baseUri + UPLOAD_FILE_URL, mvm, FileInfoResponse.class);
		return response.getId();
	}

	@Override
	public void addInput(Path sourceFile) {
		sourceFiles.add(sourceFile);
	}

	@Override
	public void setTimeSlice(String timeSliceId) {
		this.timeSliceId = timeSliceId;
	}

	@Override
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	
	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
}
