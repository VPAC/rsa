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

package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.iteration.Reduction;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.Swizzle;
import org.vpac.ndg.query.math.SwizzleFactory;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;
import org.vpac.ndg.query.sampling.PixelSourceScalar;

/**
 * Find and keep the smallest value.
 *
 * @author Alex Fraser
 */
@Description(name = "Minimise Time", description = "Get the earliest time")
@InheritDimensions(from = "toMinimise", reduceBy = 1)
public class MinimiseForTime implements Filter {

	/**
	 * When the value becomes greater than or equal to this value, the filter
	 * will return early. Otherwise, the filter will search over all time
	 * values. Defaults to Integer.MIN_VALUE.
	 */
	public int threshold = Integer.MIN_VALUE;

	@Constraint(dimensions=1)
	public PixelSource intime;

	/**
	 * The field to find the minimum value of.
	 */
	public PixelSourceScalar toMinimise;
	/**
	 * The field to write to the output.
	 */
	public PixelSource toKeep;

	// Output fields.
	@CellType("intime")
	public Cell outtime;
	@CellType("toKeep")
	public Cell output;

	Reduction reduction;
	VectorReal tco = VectorReal.createEmpty(1);
	Swizzle tcs = SwizzleFactory.compile("t");

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		reduction = new Reduction(toMinimise.getBounds());
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		double besttime = 0;
		ScalarElement min = null;

		// Search over all times.
		for (VectorReal varcoords : reduction.getIterator(coords)) {
			ScalarElement val = toMinimise.getScalarPixel(varcoords);
			if (!val.isValid())
				continue;
			if (min == null || val.compareTo(min) < 0) {
				besttime = varcoords.getT();
				min = val;
			}
			if (val.compareTo(threshold) <= 0) {
				// Threshold reached; no need to continue.
				break;
			}
		}

		// Store result.
		if (min == null) {
			output.unset();
			outtime.unset();
		} else {
			VectorReal co = reduction.getSingle(coords, besttime);
			output.set(toKeep.getPixel(co));
			tcs.swizzle(co, tco);
			outtime.set(intime.getPixel(tco));
		}
	}
}
