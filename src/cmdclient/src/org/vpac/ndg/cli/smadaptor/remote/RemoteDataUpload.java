/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

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
