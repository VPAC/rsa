package org.vpac.ndg.cli.smadaptor;

import java.io.IOException;
import java.nio.file.Path;

public interface DataUpload {
	public String upload() throws IOException, IllegalArgumentException;

	public void addInput(Path sourceFile);

	public void setTimeSlice(String timeSliceId);

	public void setUploadId(String uploadId);
}
