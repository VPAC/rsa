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
