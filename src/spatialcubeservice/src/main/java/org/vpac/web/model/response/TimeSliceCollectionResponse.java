/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
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
}
