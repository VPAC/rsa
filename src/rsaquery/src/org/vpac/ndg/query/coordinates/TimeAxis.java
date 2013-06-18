package org.vpac.ndg.query.coordinates;

import java.util.List;

import ucar.nc2.time.CalendarDate;
import ucar.nc2.time.CalendarDateUnit;

public class TimeAxis {
	private CalendarDateUnit units;
	private List<CalendarDate> values;

	public CalendarDateUnit getUnits() {
		return units;
	}

	public void setUnits(CalendarDateUnit units) {
		this.units = units;
	}

	public List<CalendarDate> getValues() {
		return values;
	}

	public void setValues(List<CalendarDate> coordinates) {
		this.values = coordinates;
	}

	@Override
	public String toString() {
		return String.format("TimeAxis(%s)", units);
	}
}
