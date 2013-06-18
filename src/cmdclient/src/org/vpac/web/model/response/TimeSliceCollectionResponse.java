package org.vpac.web.model.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.storage.model.TimeSlice;

@XmlRootElement(name = "TimeSliceCollection")
public class TimeSliceCollectionResponse {
	private List<TimeSliceResponse> items;

	public List<TimeSliceResponse> getItems() {
		return items;
	}

	@XmlElement(name = "TimeSlice")
	public void setItems(List<TimeSliceResponse> items) {
		this.items = items;
	}
	
	public TimeSliceCollectionResponse(Collection<TimeSlice> slices) {
		if(slices != null) {
			items = new ArrayList<TimeSliceResponse>();
			for(TimeSlice ts: slices)
				this.items.add(new TimeSliceResponse(ts));
		}
	}
	
	public TimeSliceCollectionResponse() {
		items = new ArrayList<TimeSliceResponse>();
	}

	public List<TimeSlice> toTimeSliceList() {
		List<TimeSlice> tsList = new ArrayList<TimeSlice>();
		for(TimeSliceResponse tsr : this.items) {
			tsList.add(tsr.toTimeSlice());
		}
		return tsList;
	}
}
