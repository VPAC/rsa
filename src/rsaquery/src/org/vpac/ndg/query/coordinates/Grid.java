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
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Defines a regular grid of data. A grid may have more than 2 dimensions, but
 * irregular axes (e.g. time) should not be included.
 * @author Alex Fraser
 */
public class Grid implements HasRank, HasShape {

	private VectorReal resolution;
	private BoxReal bounds;
	private VectorInt shape;

	public Grid(BoxReal bounds, VectorReal resolution) {
		this.bounds = bounds;
		this.resolution = resolution;
		shape = bounds.getSize().divNew(resolution).toInt().add(1);
	}

	/**
	 * @return The resolution of the grid in the same coordinate system
	 *         as getSrs().
	 */
	public VectorReal getResolution() {
		return resolution;
	}

	public void setResolution(VectorReal resolution) {
		this.resolution = resolution;
	}

	/**
	 * @return The extents of this dataset's grid in the same coordinate system
	 *         as getSrs().
	 */
	public BoxReal getBounds() {
		return bounds;
	}

	public void setBounds(BoxReal bounds) {
		this.bounds = bounds;
	}

	@Override
	public VectorInt getShape() {
		return shape;
	}

	@Override
	public int getRank() {
		return bounds.getRank();
	}

	@Override
	public String toString() {
		return String.format("Grid(%s / %s)", bounds, resolution);
	}

}
