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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.cli.smadaptor.DataUpload;
import org.vpac.ndg.storage.dao.UploadDao;
import org.vpac.ndg.storage.model.Upload;
import org.vpac.ndg.storage.util.UploadUtil;

public class LocalDataUpload implements DataUpload {
	@Autowired
	UploadDao uploadDao;
	@Autowired
	UploadUtil uploadUtil;

	protected List<Path> sourceFiles;
	protected String timeSliceId;
	protected String uploadId;

	public LocalDataUpload() {
		sourceFiles = new ArrayList<Path>();
	}

	@Override
	public String upload() throws IOException, IllegalArgumentException {
		if(sourceFiles.size() == 0) {
			throw new IllegalArgumentException("No source file to upload.");
		}

		Upload upload = new Upload(timeSliceId);
		uploadDao.create(upload);
		Path dir = uploadUtil.getDirectory(upload);
		uploadUtil.createDirectory(upload);
		for (Path file : sourceFiles) {
			Files.copy(file, dir.resolve(file.getFileName()));
		}
		return upload.getFileId();
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
}
