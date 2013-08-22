/*
 * This file is part of SpatialCube.
 *
 * SpatialCube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SpatialCube is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SpatialCube.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

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
