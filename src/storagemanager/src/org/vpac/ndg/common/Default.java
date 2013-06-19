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
