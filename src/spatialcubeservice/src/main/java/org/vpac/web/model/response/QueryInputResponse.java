package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is intended for a query input response.
 * 
 * @author hsumanto
 * 
 */
@XmlRootElement(name = "QueryInput")
public class QueryInputResponse extends QueryElementResponse {

	public QueryInputResponse() {
	}

	public QueryInputResponse(String name, String type) {
		super(name, type);
	}
}
