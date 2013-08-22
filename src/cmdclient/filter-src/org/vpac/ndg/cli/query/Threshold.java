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

import org.vpac.ndg.query.Constraint;
import org.vpac.ndg.query.Filter;
import org.vpac.ndg.query.InheritDimensions;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.iteration.Reduction;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.ElementInt;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.Swizzle;
import org.vpac.ndg.query.math.SwizzleFactory;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;
import org.vpac.ndg.query.sampling.PixelSourceScalar;

@InheritDimensions(from = "input", reduceBy = 1)
public class Threshold implements Filter {

	// Parameters.
	public int threshold;

	@Constraint(dimensions=1)
	public PixelSource intime;

	// Input fields.
	public PixelSourceScalar input;

	// Output fields.
	@CellType("intime")
	public Cell outtime;
	@CellType("input")
	public Cell output;

	Reduction reduction;
	VectorReal tco = VectorReal.createEmpty(1);
	Swizzle tcs = SwizzleFactory.compile("t");

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		reduction = new Reduction(input.getBounds());
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		// Search over all times at the specified coordinates.
		ScalarElement maxval = null;
		Element<?> timeval = null;
		for (VectorReal varcoords : reduction.getIterator(coords)) {
			ScalarElement temp = input.getScalarPixel(varcoords);
			if ((maxval == null || temp.compareTo(maxval) > 0) &&
					temp.compareTo(threshold) > 0) {
				maxval = temp;
				tcs.swizzle(varcoords, tco);
				timeval = intime.getPixel(tco);
			}
		}

		if (maxval != null) {
			output.set(maxval);
			outtime.set(timeval);
		} else {
			output.set(new ElementInt(0));
			outtime.unset();
		}
	}
}
