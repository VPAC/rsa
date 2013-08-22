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

package org.vpac.ndg.common.datamodel;

/**
 * File formats that may be used when transforming data.
 * @author adfries
 */
public enum GdalFormat {
	ASCII("AAIGrid", ".asc", new String[] {}),
	GEOTIFF("GTiff", ".tif", new String[] {"COMPRESS=LZW", "BIGTIFF=IF_SAFER"}),
	JPEG("JPEG", ".jpg", new String[] {}),
	PNG("PNG", ".png", new String[] {}),
	GIF("GIF", ".gif", new String[] {}),
	/* NetCDF option require version 1.9dev upwards ... */
	NC("netCDF", ".nc", new String[] {"COMPRESS=DEFLATE", "ZLEVEL=6", "FORMAT=NC4C", "WRITE_GDAL_TAGS=yes"}),
	VRT("VRT", ".vrt", new String[] {});
	
	protected String gdalString;
	protected String extension;
	protected String[] creationOptions;
	
	/**
	 * Create a new GDAL format specifier. 
	 * @param gdalString The format to use; see the Code column in the
	 * <a href="http://www.gdal.org/formats_list.html">GDAL formats list</a>.
	 * @param creationOptions The "-co" options to pass in. See e.g.
	 * <a href="http://www.gdal.org/frmt_gtiff.html">the GeoTIFF format documentation</a>.
	 * @see <a href="http://www.gdal.org/gdal_translate.html">gdalwarp docs</a>
	 */
	private GdalFormat(String gdalString, String extension, String[] creationOptions) {
		this.gdalString = gdalString;
		this.extension = extension;
		this.creationOptions = creationOptions;
	}
	
	/**
	 * @return A string representation of this format that can be used in an
	 * argument to gdal.
	 */
	public String toGdalString() {
		return gdalString;
	}
	
	public String getExtension() {
		return extension;
	}
	
	public String[] getCreationOptions() {
		return creationOptions;
	}
	
	/**
	 * Find the GDAL-specific format of a generic format.
	 * @param fmt The generic format.
	 * @return The specific format.
	 * @throws IllegalArgumentException if the generic format has no
	 * corresponding GDAL format.
	 */
	public static GdalFormat valueOf(Format fmt)
			throws IllegalArgumentException {
		
		switch (fmt) {
		case ASCII: return GdalFormat.ASCII;
		case GEOTIFF: return GdalFormat.GEOTIFF;
		case NC: return GdalFormat.NC; 
		case GIF: return GdalFormat.GIF;
		case JPEG: return GdalFormat.JPEG;
		case PNG: return GdalFormat.PNG;
		case SHAPEFILE_POINTSET:
			// Not handled by GDAL (.shp is a vector format).
		default:
			throw new IllegalArgumentException(
					"No matching GDAL format for \"" +
					fmt.getTitle() +
					"\"");
		}
	}
}