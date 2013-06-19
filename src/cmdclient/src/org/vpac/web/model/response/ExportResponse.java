package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlRootElement;

 
@XmlRootElement(name = "Export")
public class ExportResponse {
	private String taskId;
	
	public ExportResponse() {
		
	}
	
	public ExportResponse(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
