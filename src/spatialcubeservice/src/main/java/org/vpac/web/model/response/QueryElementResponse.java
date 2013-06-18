package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is intended for query element response.
 * This is the base class for QueryInputResponse and QueryOutputResponse.
 * 
 * @author hsumanto
 * 
 */
@XmlRootElement(name = "QueryElement")
public class QueryElementResponse {

	private String name;
	private String type;

	public QueryElementResponse() {
	}

	public QueryElementResponse(String name, String type) {
		setName(name);
		setType(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
