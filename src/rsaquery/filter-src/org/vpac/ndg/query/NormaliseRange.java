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

package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * Re-maps numbers from a given input range to 0-1.
 * 
 * @author Alex Fraser
 */
@Description(name = "Normalise", description = "Normalise pixel to 0-1 range")
@InheritDimensions(from = "input")
public class NormaliseRange implements Filter {

	public long upper = 255;
	public long lower = 0;

	public PixelSource input;

	@CellType(value = "input", as = "float")
	public Cell output;

	Element<?> offset;
	Element<?> fraction;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		offset = input.getPrototype().getElement().asFloat();
		fraction = offset.copy();
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		offset.subOf(input.getPixel(coords), lower);
		fraction.divOf(offset, upper);
		output.set(fraction);
	}

}
