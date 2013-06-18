package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlRootElement;

 
@XmlRootElement(name = "Query")
public class QueryResponse {
	private String taskId;
	
	public QueryResponse() {
		
	}
	
	public QueryResponse(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
}
