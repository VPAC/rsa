package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

 
@XmlRootElement(name = "CleanUp")
public class CleanUpResponse {
	private int count;
	
	public CleanUpResponse() {
	}

	public CleanUpResponse(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}

	@XmlElement
	public void setCount(int count) {
		this.count = count;
	}
}
