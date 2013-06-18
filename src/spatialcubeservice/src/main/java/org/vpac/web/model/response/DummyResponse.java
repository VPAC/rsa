package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlRootElement;

 
@XmlRootElement(name = "Dataset")
public class DummyResponse {
	private String id;
	private String dataAbstract;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public DummyResponse() {
		
	}

	public String getDataAbstract() {
		return dataAbstract;
	}

	public void setDataAbstract(String dataAbstract) {
		this.dataAbstract = dataAbstract;
	}
}
