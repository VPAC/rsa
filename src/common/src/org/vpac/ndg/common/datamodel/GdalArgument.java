package org.vpac.ndg.common.datamodel;

public interface GdalArgument {
	/**
	 * @return A string representation of this sampling method that can be
	 * used in an argument to gdal.
	 */
	public String toGdalString();
}
