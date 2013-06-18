package org.vpac.web.model.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.storage.model.Band;
 
@XmlRootElement(name = "BandCollection")
public class BandCollectionResponse {
	private List<BandResponse> items;

	public List<BandResponse> getItems() {
		return items;
	}

	@XmlElement(name = "Band")
	public void setItems(List<BandResponse> items) {
		this.items = items;
	}
	
	public BandCollectionResponse(Collection<Band> bands) {
		if(bands != null) {
			items = new ArrayList<BandResponse>();
			for(Band band: bands)
				this.items.add(new BandResponse(band));
		}
	}
	
	public BandCollectionResponse() {
		items = new ArrayList<BandResponse>();
	}

	public List<Band> toBand() {
		List<Band> list = new ArrayList<Band>();
		for(BandResponse br : this.items) {
			list.add(br.toBand());
		}
		return list;
	}
}
