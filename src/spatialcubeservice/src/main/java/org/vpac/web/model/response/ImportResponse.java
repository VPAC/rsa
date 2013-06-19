package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlRootElement;
 
@XmlRootElement(name = "Import")
public class ImportResponse {
	private String taskId;

	public ImportResponse(String taskId) {
		this.setTaskId(taskId);
	}
	
	public ImportResponse() {
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
