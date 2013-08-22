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

package org.vpac.ndg.common;

import java.util.Arrays;

/**
 * This is a string utility class containing various methods for string
 * manipulation.
 * 
 * @author hsumanto
 * 
 */
public class StringUtils {
	/**
	 * A list of allowable datetime patterns which are used when parsing string
	 * into datetime object.
	 * 
	 * <p>
	 * NOTE: The order of these patterns is crucial and needs to be tested if
	 * you change it.
	 * </p>
	 * 
	 * <p>
	 * NOTE: The quotes '' in these strings are ignored. Do not put quotes in an
	 * actual time stamp.
	 * </p>
	 * 
	 * */
	public static final String[] ALLOWABLE_DATETIME_PATTERNS = {
			Default.MILLISECOND_PATTERN,  // 1999-04-28T13-45-20.001
			Default.MILLISECOND_PATTERN2, // 1999-04-28T13:45:20.001
			Default.SECOND_PATTERN,       // 1999-04-28T13-45-20
			Default.SECOND_PATTERN2,      // 1999-04-28T13:45:20
//			Default.MINUTE_PATTERN,       // 1999-04-28T13-45-00
//			Default.HOUR_PATTERN,         // 1999-04-28T13-00-00
			// Other allowable patterns
			"EEE, dd MMM yyyy HH:mm:ss z", // Sun, 22 Jan 2012 00:00:00 UTC
			"EEE MMM dd HH:mm:ss z yyyy", // Sun Jan 22 00:00:00 UTC 2012
			"yyyy-MM-dd HH:mm:ss z",
			"yyyy-MM-dd HH:mm:ss", // Variant of SECOND_PATTERN
			"yyyy-MM-dd HH:mm",
			"dd M yyyy HH:mm:ss", // 22 1 2012 00:00:00
			"dd/MM/yyyy HH:mm:ss", // 22/01/2012 00:00:00
			Default.DAY_PATTERN, // 1999-04-28 <--- midnight
			"yyyyMMdd HH:mm:ss.SSS", // Variant of MILLISECOND_PATTERN
			"yyyyMMdd HH:mm:ss", "yyyyMMdd HH:mm", "yyyyMMddHHmmss",
			"yyyyMMddHHmm", "yyyyMMdd", "dd/MM/yyyy HH:mm:ss z",
			"dd/MM/yyyy HH:mm:ss a", "dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy HH:mm",
			"dd/MM/yyyy", "dd/M/yyyy", // 22/1/2012
			"dd MMM yyyy HH:mm:ss", // 22 jan 2012 00:00:00
			"dd MMM yyyy", // 22 january 2012, 22 jan 2012 (midnight)
	};

	/**
	 * Concatenate a list of values together as a string.
	 * 
	 * @param arr The array to convert to a string.
	 * @param delimeter A string that will separate each element in `arr'.
	 * @return All elements of `arr' delimited by `delimiter'.
	 */
	public static String join(Iterable<? extends Object> arr, String delimeter) {
		StringBuilder str = new StringBuilder();

		boolean first = true;
		for (Object ob : arr) {
			if (first) {
				first = false;
			} else {
				str.append(delimeter);
			}
			str.append(ob);
		}

		return str.toString();
	}

	/**
	 * Concatenate a list of values together as a string using the specified
	 * delimiter.
	 * 
	 * @param arr The array to convert to a string.
	 * @param delimeter A string that will separate each element in `arr'.
	 * @return All elements of `arr' delimited by `delimiter'.
	 */
	public static String join(Object[] arr, String delimeter) {
		return join(Arrays.asList(arr), delimeter);
	}

	/**
	 * Concatenate a list of values together as a string.
	 * 
	 * @param arr The array to convert to a string.
	 * @return All elements of `arr' delimited by a comma and space (", ").
	 */
	public static String join(Iterable<? extends Object> arr) {
		return join(arr, ", ");
	}

	/**
	 * Concatenate a list of values together as a string using the comma
	 * delimiter.
	 * 
	 * @param arr The array to convert to a string.
	 * @return All elements of `arr' delimited by a comma and space (", ").
	 */
	public static String join(Object[] arr) {
		return join(Arrays.asList(arr), ", ");
	}
}
