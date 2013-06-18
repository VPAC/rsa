package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is intended for a query output response.
 * 
 * @author hsumanto
 * 
 */
@XmlRootElement(name = "QueryOutput")
public class QueryOutputResponse extends QueryElementResponse {

	public QueryOutputResponse() {
	}

	public QueryOutputResponse(String name, String type) {
		super(name, type);
	}
}
