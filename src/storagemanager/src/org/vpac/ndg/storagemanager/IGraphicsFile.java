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

package org.vpac.ndg.storagemanager;

import java.nio.file.Path;

import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.common.datamodel.GdalFormat;

/**
 * Interface required for getting details of graphics file.
 * @author hsumanto
 *
 */
public interface IGraphicsFile {
	/**
	 * @return Returns the location of the graphics file.
	 */
	public Path getFileLocation();
	/**
	 * @return Returns the bounding box of the graphics file.
	 */
	public Box getBounds();
	/**
	 * @return Returns the project of the graphics file.
	 */
	public String getSrs();
	/**
	 * @return Returns the format of the graphics file.
	 */
	public GdalFormat getFormat();
	/**
	 * @return Returns the resolution of the graphics file.
	 */
	public CellSize getResolution();
	/**
	 * @return Returns whether the graphics file is vector dataset.
	 */
	public boolean isVector();
	/**
	 * @return Returns whether the graphics file is raster dataset.
	 */
	public boolean isRaster();
}
