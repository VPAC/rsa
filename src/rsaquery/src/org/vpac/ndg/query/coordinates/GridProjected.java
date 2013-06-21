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

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

import ucar.nc2.dataset.CoordinateSystem;

/**
 * Defines a geospatial raster grid.
 * @author Alex Fraser
 */
public class GridProjected extends Grid {

	private CoordinateSystem srs;

	public GridProjected(BoxReal bounds, VectorReal resolution,
			CoordinateSystem srs) {

		super(bounds, resolution);
		this.srs = srs;
	}

	/**
	 * @return The coordinate system used by the grid.
	 */
	public CoordinateSystem getSrs() {
		return srs;
	}

	public void setSrs(CoordinateSystem srs) {
		this.srs = srs;
	}

}
