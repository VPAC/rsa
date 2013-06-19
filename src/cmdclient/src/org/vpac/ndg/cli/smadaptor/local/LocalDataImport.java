package org.vpac.ndg.cli.smadaptor.local;

import java.util.List;

import org.jsoup.helper.StringUtil;
import org.vpac.ndg.cli.smadaptor.DataImport;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.task.Importer;

public class LocalDataImport implements DataImport {

	protected String uploadId;
	protected String bandId;
	protected String srcnodata;
	private Boolean useBilinearInterpolation;
	private List<String> remainingArgs;

	public LocalDataImport() {
		srcnodata = null;
	}

	@Override
	public String start() throws TaskInitialisationException, TaskException {
		Importer importer = new Importer();
		importer.setUploadId(this.uploadId);
		importer.setBand(this.bandId);
		importer.setSrcnodata(this.srcnodata);
		if (useBilinearInterpolation != null)
			importer.setUseBilinearInterpolation(useBilinearInterpolation);

		importer.configure();
		String taskId = importer.getTaskId();
		//		importer.call();
		// changed this code to asynchronous because need to display progress
		importer.runInBackground();
		return taskId;
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
		this.useBilinearInterpolation = value;
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
