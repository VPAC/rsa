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

package org.vpac.ndg.common;

import org.vpac.ndg.common.datamodel.CellSize;

/**
 * Default class for storing constants.
 * 
 * @author hsumanto
 *
 */
public class Default {
	public static final CellSize RESOLUTION = CellSize.m100; // Default resolution if resolution not specified
	public static final String BAND1 = "Band1"; // Default band if band information not found

	// NOTE: The quotes '' in these strings are ignored. Do not put quotes in
	// an actual time stamp.
	public static final String MILLISECOND_PATTERN = "yyyy-MM-dd'T'HH-mm-ss.SSS"; // 1999-04-28T13-45-20.001
	public static final String MILLISECOND_PATTERN2 = "yyyy-MM-dd'T'HH:mm:ss.SSS"; // 1999-04-28T13:45:20.001
	public static final String SECOND_PATTERN = "yyyy-MM-dd'T'HH-mm-ss"; // 1999-04-28T13-45-20
	public static final String SECOND_PATTERN2 = "yyyy-MM-dd'T'HH:mm:ss"; // 1999-04-28T13:45:20
	public static final String MINUTE_PATTERN = SECOND_PATTERN; // 1999-04-28T13-45-00
	public static final String MINUTE_PATTERN2 = SECOND_PATTERN2; // 1999-04-28T13:45:00
	public static final String HOUR_PATTERN = MINUTE_PATTERN; // 1999-04-28T13-00-00
	public static final String HOUR_PATTERN2 = MINUTE_PATTERN2; // 1999-04-28T13:00:00
	public static final String DAY_PATTERN = "yyyy-MM-dd"; // 1999-04-28 (midnight)
}
