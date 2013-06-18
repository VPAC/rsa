package org.vpac.web.model.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class is intended for a collection of QueryDataset response.
 * 
 * @author hsumanto
 * @author adfries
 * 
 */
@XmlRootElement(name = "nodes")
public class QueryNodeCollectionResponse {

	private List<? extends QueryNodeResponse> items;
	private String name;

	@XmlElement(name = "node")
	public List<? extends QueryNodeResponse> getItems() {
		return items;
	}

	public void setItems(List<? extends QueryNodeResponse> items) {
		this.items = items;
	}

	public QueryNodeCollectionResponse() {
		name = "";
		items = new ArrayList<>();
	}

	public QueryNodeCollectionResponse(String name, List<? extends QueryNodeResponse> items) {
		this.name = name;
		this.items = items;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
