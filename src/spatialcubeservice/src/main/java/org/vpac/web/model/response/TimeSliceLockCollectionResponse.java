package org.vpac.web.model.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.storage.model.TimeSliceLock;

@XmlRootElement(name = "TimeSliceLockCollection")
public class TimeSliceLockCollectionResponse {
	private List<TimeSliceLockResponse> items;

	public List<TimeSliceLockResponse> getItems() {
		return items;
	}

	@XmlElement(name = "TimeSlice")
	public void setItems(List<TimeSliceLockResponse> items) {
		this.items = items;
	}
	
	public TimeSliceLockCollectionResponse(Collection<TimeSliceLock> locks) {
		if(locks != null) {
			items = new ArrayList<TimeSliceLockResponse>();
			for(TimeSliceLock lock: locks)
				this.items.add(new TimeSliceLockResponse(lock));
		}
	}
	
	public TimeSliceLockCollectionResponse() {
		items = new ArrayList<TimeSliceLockResponse>();
	}

	public List<TimeSliceLock> toTimeSliceList() {
		List<TimeSliceLock> tsList = new ArrayList<TimeSliceLock>();
		for(TimeSliceLockResponse tsr : this.items) {
			tsList.add(tsr.toTimeSliceLock());
		}
		return tsList;
	}
}
