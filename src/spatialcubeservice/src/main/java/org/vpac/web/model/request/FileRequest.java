package org.vpac.web.model.request;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.sun.istack.Nullable;

import org.springframework.web.multipart.MultipartFile;

public class FileRequest {

	@NotNull
	private String timeSliceId;
	
	private List<MultipartFile> files;
	@Nullable
	private String taskId;
	
	public String getTimeSliceId() {
		return timeSliceId;
	}
	public void setTimeSliceId(String timeSliceId) {
		this.timeSliceId = timeSliceId;
	}
	public List<MultipartFile> getFiles() {
		return files;
	}
	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
