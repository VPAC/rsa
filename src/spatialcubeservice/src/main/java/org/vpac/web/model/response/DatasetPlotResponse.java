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
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.storage.model.TimeSlice;

import ucar.ma2.Array;

 
@XmlRootElement(name = "DatasetPlot")
public class DatasetPlotResponse {
	private TimeSliceCollectionResponse timeslices;
	private List<Double> values = new ArrayList<Double>();

	public List<Double> getValues() {
		return values;
	}

	@XmlElement
	public void setValues(List<Double> values) {
		this.values = values;
	}

	public DatasetPlotResponse(List<TimeSlice> slices, Array d) {
		this.timeslices = new TimeSliceCollectionResponse(slices);
		
		for(int i = 0; i < d.getIndex().getSize(); i++) {
			this.values.add(d.getDouble(i));
		}
	}
	
	public TimeSliceCollectionResponse getTimeslices() {
		return timeslices;
	}

	@XmlElement(name = "TimeSlice")
	public void setTimeslices(TimeSliceCollectionResponse timeslices) {
		this.timeslices = timeslices;
	}

	public DatasetPlotResponse() {
	}
}
