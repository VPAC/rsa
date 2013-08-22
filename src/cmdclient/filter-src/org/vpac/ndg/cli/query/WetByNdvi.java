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

package org.vpac.ndg.cli.query;

import java.io.IOException;

import org.vpac.ndg.query.Filter;
import org.vpac.ndg.query.InheritDimensions;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.ElementByte;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSourceScalar;

/**
 * Run a double threshold of Band 5 < 1000 and NDVI < 0.08. Anything with Band 5
 * < 1000 and NDVI > 0.08 should be set to another class called "mixed".
 *
 * <p>
 * Formula provided by Geoscience Australia.
 * </p>
 *
 * @author Alex Fraser
 */
@InheritDimensions(from = "band5")
public class WetByNdvi implements Filter {

	// Parameters.
	public int dryThreshold = 1000;
	public double ndviThreshold = 0.08;

	// Input fields. Must be scalar to allow comparison.
	public PixelSourceScalar ndvi;
	public PixelSourceScalar band5;

	// Output fields.
	@CellType("byte")
	public Cell output;

	ScalarElement wet = new ElementByte((byte) 2);
	ScalarElement mixed = new ElementByte((byte) 1);
	ScalarElement dry = new ElementByte((byte) 0);

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		ScalarElement b5 = band5.getScalarPixel(coords);
		if (!b5.isValid()) {
			output.unset();
			return;
		}
		if (band5.getScalarPixel(coords).compareTo(dryThreshold) > 0) {
			output.set(dry);
			return;
		}

		ScalarElement ndviVal = ndvi.getScalarPixel(coords);

		if (!ndviVal.isValid()) {
			output.unset();
			return;
		}

		if (ndviVal.compareTo(ndviThreshold) < 0)
			output.set(wet);
		else
			output.set(mixed);
	}
}
