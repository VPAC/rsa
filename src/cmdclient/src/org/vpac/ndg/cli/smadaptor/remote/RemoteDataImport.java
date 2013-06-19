package org.vpac.ndg.cli.smadaptor.remote;

import java.util.List;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.vpac.ndg.cli.smadaptor.DataImport;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.web.model.response.ImportResponse;

public class RemoteDataImport implements DataImport {
	public static String GET_IMPORT_URL = "/SpatialCubeService/Data/Import.xml?taskId={taskId}&bandId={bandId}&srcnodata={srcnodata}&useBilinearInterpolation={useBilinearInterpolation}";

	private String baseUri;
	@Autowired
	protected RestTemplate restTemplate;

	protected String uploadId;
	protected String bandId;
	protected String srcnodata;
	private Boolean useBilinearInterpolation;
	private List<String> remainingArgs;

	public RemoteDataImport(String baseUri) {
		this.srcnodata = null;
		this.baseUri = baseUri;
	}

	@Override
	public String start() throws TaskInitialisationException, TaskException {
		ImportResponse response = restTemplate.postForObject(baseUri + GET_IMPORT_URL, null, ImportResponse.class, this.uploadId, this.bandId, this.srcnodata, this.useBilinearInterpolation);
		return response.getTaskId();
	}

	@Override
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	@Override
	public void setBand(String bandId) {
		this.bandId = bandId;
	}

	@Override
	public void setSrcnodata(String srcnodata) {
		this.srcnodata = srcnodata;
	}

	@Override
	public void setUseBilinearInterpolation(Boolean value) {
		useBilinearInterpolation = value;
	}
	
	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	/**
	 * Get the command description of this import.
	 */
	@Override
	public String getDescription() {
		if(remainingArgs == null) {
			return "";
		}
		String strRemainingArgs = StringUtil.join(remainingArgs, " ");
		return String.format("rsa data import %s", strRemainingArgs); 
	}

	/**
	 * Set the remaining import arguments.
	 */
	@Override
	public void setRemainingArgs(List<String> remainingArgs) {
		this.remainingArgs = remainingArgs;
	}
}
