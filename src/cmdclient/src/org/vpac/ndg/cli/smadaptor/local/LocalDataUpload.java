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
