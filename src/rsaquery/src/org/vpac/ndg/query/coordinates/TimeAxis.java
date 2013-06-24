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
