package org.vpac.web.model.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class TimeSliceFileRequest {

	private String timeSliceId;
	private List<MultipartFile> files;
	private String fileId;
	
	public String getTimeSliceId() {
		return timeSliceId;
	}
	public void setTimeSliceId(String timeSliceId) {
		this.timeSliceId = timeSliceId;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public List<MultipartFile> getFiles() {
		return files;
	}
	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
}
