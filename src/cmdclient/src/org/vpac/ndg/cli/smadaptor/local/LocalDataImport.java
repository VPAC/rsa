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
