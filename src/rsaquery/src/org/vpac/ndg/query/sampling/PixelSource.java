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

package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.coordinates.HasBounds;
import org.vpac.ndg.query.coordinates.HasRank;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorReal;

/**
 * A pixel source is a field of {@link Element Elements}; essentially a
 * read-only image. It can be queried for pixel values.
 *
 * <p><img src="doc-files/PixelSource_and_Cell_class.png" /></p>
 *
 * @see Cell
 * @author Alex Fraser
 */
public interface PixelSource extends HasBounds, HasRank, HasPrototype {

	/**
	 * @param co The coordinates to retrieve the value of, in the global
	 *        coordinate system.
	 * @return The value of the image at the specified coordinates.
	 * @throws IOException If the pixel value could not be read.
	 */
	Element<?> getPixel(VectorReal co) throws IOException;

	/**
	 * @return The effective bounds of this input in the global coordinate
	 *         system.
	 */
	@Override
	BoxReal getBounds();

}
