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

package org.vpac.ndg.rasterdetails;

import org.gdal.gdalconst.gdalconstConstants;


/**
 * Constants used when storing data.
 * @author glennf
 */
public enum RasterDetails {

	BYTE(gdalconstConstants.GDT_Byte,  "-byte", "-128"),  // min = -(2^7)
	INT16(gdalconstConstants.GDT_Int16, "-int16", "-9999"), // min = -(2^15)
	INT32(gdalconstConstants.GDT_Int32, "-int32", "-9999"), // min = -(2^31)
	FLOAT32(gdalconstConstants.GDT_Float32, "-float32", "nan"),
	FLOAT64(gdalconstConstants.GDT_Float64, "-float64", "nan");
	
	private String gdalId;
	private String nullValue;
	private int gdalConstant;
	
	private RasterDetails(int gdalConst, String gdalId, String nullValue) {
		this.gdalConstant = gdalConst;
		this.nullValue = nullValue;
		this.gdalId = gdalId;
	}
	
	// the identifierMethod
	public int toInt() {
		return gdalConstant;
	}
	
	// the valueOfMethod
	public static RasterDetails valueOf(int gdalConst) {
		for (RasterDetails v : RasterDetails.values()) {
			if (v.gdalConstant == gdalConst)
				return v;
		}
		throw new IllegalArgumentException("No matching value for gdalconst \"" +
				gdalConst + "\".");
	}

	/**
	 * @return the value to use as the default null (NODATA) value. This is a
	 * string represenation of a number, suitable to be passed to GDAL (e.g.
	 * "-9999").
	 */
	public String getDefaultNoDataValue() {
		return nullValue;
	}
	
	/**
	 * @return A string representation of this data type that can be used with
	 * GDAL commands (e.g. "-int32").
	 */
	public String getGdalId() {
		return gdalId;
	}

	/**
	 * @return A string representation of this data type that can be used with
	 * GDAL utilities (e.g. "int32").
	 */
	public String getGdalDataType() {
		return gdalId.substring(1);
	}

}
