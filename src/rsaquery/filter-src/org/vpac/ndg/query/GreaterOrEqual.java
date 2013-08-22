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
import org.vpac.ndg.query.math.ElementByte;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSourceScalar;

@Description(name = "Greater or Equal", description = "Generates a mask\nmask = input >= value")
@InheritDimensions(from = "input")
public class GreaterOrEqual implements Filter {

	// Parameters.
	public float value = 1.0f;

	// Input fields.
	public PixelSourceScalar input;

	// Output fields.
	@CellType("byte")
	public Cell mask;

	ScalarElement val;
	ScalarElement match = new ElementByte((byte) 1);
	ScalarElement fail = new ElementByte((byte) 0);

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		val = (ScalarElement) input.getPrototype().getElement().copy();
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		val = input.getScalarPixel(coords);
		if (!val.isValid()) {
			mask.unset();
			return;
		}
		if (val.compareTo(value) >= 0)
			mask.set(match);
		else
			mask.set(fail);
	}
}
