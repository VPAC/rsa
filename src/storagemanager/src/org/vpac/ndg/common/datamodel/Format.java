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

/**
 * 
 */
package org.vpac.ndg.common.datamodel;

import java.util.EnumSet;

import org.vpac.ndg.common.StringUtils;

/**
 * Note: simply including additional formats in this enum will not result in full
 * support in the classes that utilise gdal applications.  To add gdal support please
 * update the appropriate RasterTask (ie; MakeFormatTranslation).
 * @author lachlan
 *
 */
public enum Format implements UiOption<Format> {
	GEOTIFF("GeoTIFF", ".tif", "image/tiff", true),
	NC("NetCDF", ".nc", "application/x-netcdf", true),
	NCML("NetCDF Markup Language", ".ncml", "application/xml", true),
	ASCII("ASCII Grid", ".asc", "text/plain", true),
	GIF("Graphics Interchange Format", ".gif", "image/gif", false),
	JPEG("JPEG JFIF", ".jpg", "image/jpeg", false),
	PNG("Portable Network Graphics", ".png","image/png", false),
	SHAPEFILE_POINTSET("Shapefile Pointset", ".shp.zip", "application/zip", true),
	MATLAB("MATLAB", ".mat", "application/matlab", true);
	
	protected String title;
	protected String extension;
	protected String mimeType;
	protected boolean highFidelity;
	private static String argList = null;
	
	private Format(String title, String extension, String mimeType, boolean hifi) {
		this.title = title;
		this.extension = extension;
		this.mimeType = mimeType;
		this.highFidelity = hifi;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getExtension() {
		return extension;
	}

	/**
	 * gets the mime type that roughly corresponds to this format.
	 * @return Returns the mime type that roughly corresponds to this format.
	 */
	public String getMimeType() {
		return mimeType;
	}
	
	@Override
	public String toHumanString() {
		return getTitle() + " (" + getExtension() + ")";
	}
	
	@Override
	public Format getValueOf(String identifier) {
		return Format.valueOf(identifier);
	}
	
	@Override
	public Format[] getValues() {
		return Format.values();
	}
	
	@Override
	public Format[] getEnumSetValues(EnumSet<Format> enumSet) {
		Format[] result = enumSet.toArray(new Format[0]);
		
		return result;
	}	
	
	/**
	 * @return true if this format supports high-fidelity data. If this method
	 * returns false, then saving to this format may cause data loss (e.g.
	 * values might be re-mapped or compressed in a lossy manner).
	 */
	public boolean isHighFidelity() {
		return highFidelity;
	}
	
	/**
	 * @return A string that lists each possible option (suitable for
	 * displaying to the user).
	 */
	public static String toArgList() {
		if (argList == null) {
			argList = StringUtils.join(Format.values());
		}
		return argList;
	}
}