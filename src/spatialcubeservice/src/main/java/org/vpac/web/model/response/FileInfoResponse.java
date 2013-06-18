package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FileInfo")
public class FileInfoResponse {
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	public FileInfoResponse() {
		
	}
	
	public FileInfoResponse(String id) {
		this.setId(id);
	}

}
