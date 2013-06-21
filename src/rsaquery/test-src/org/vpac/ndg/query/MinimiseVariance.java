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

import org.vpac.ndg.query.iteration.CoordinatePair;
import org.vpac.ndg.query.iteration.Rectangle;
import org.vpac.ndg.query.iteration.Reduction;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.Swizzle;
import org.vpac.ndg.query.math.SwizzleFactory;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;
import org.vpac.ndg.query.sampling.PixelSourceScalar;

/**
 * WITHIN A WINDOW OF PIXELS, VARIANCE IS MINIMISED (Query returns an RGB colour
 * image for a selection of bands) - for a 3x3 kernel which iterates through the
 * combination of pixels for that kernel in the stack within the time period -
 * output pixels for a date range which minimise the variance within the search
 * window for a given band i.e. reduce noise.
 * 
 * <p>
 * This is a single-pass filter that performs two operations on each pixel:
 * </p>
 * <ol>
 * 	<li>Find the variance of neighbouring pixels for all times.</li>
 * 	<li>Set the output to the value at the time with the lowest variance.</li>
 * </ol>
 * 
 * @author Alex Fraser
 * @see Variance
 * @see MinimiseForTime
 */
@InheritDimensions(from = "input", reduceBy = 1)
public class MinimiseVariance implements Filter {

	public int windowSize = 3;

	// Input fields.
	public PixelSource intime;
	public PixelSourceScalar input;

	// Output fields.
	@CellType("intime")
	public Cell outtime;
	@CellType("input")
	public Cell output;

	Reduction reduction;
	Rectangle rect;
	VectorReal tco = VectorReal.createEmpty(1);
	Swizzle tcs = SwizzleFactory.compile("t");

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		reduction = new Reduction(input.getBounds(), false);

		VectorInt rectShape = VectorInt.createEmpty(input.getRank(), windowSize);
		rectShape.setT(1);
		rect = new Rectangle(rectShape);
	}

	public float getVariance(VectorReal coords) throws IOException {
		// Use Knuth's single-pass algorithm: assume memory access is slower
		// than division.
		int n = 0;
		float mean = 0.0f;
		float M2 = 0.0f;
		float delta;
		//float variance_n;
		float variance;
		ScalarElement val;

		rect.setCentre(coords);
		for (CoordinatePair co : rect) {
			n = n + 1;
			val = input.getScalarPixel(co.coordinates);
			delta = val.floatValue() - mean;
			mean = mean + delta / n;
			M2 = M2 + delta * (val.floatValue() - mean);
		}

		variance = M2 / (n - 1);
		return variance;
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		// Search over all times at the specified coordinates.
		float lowestVariance = Float.MAX_VALUE;
		double besttime = 0;
		for (VectorReal varcoords : reduction.getIterator(coords)) {
			float variance = getVariance(varcoords);
			if (variance < lowestVariance) {
				besttime = varcoords.getT();
				lowestVariance = variance;
			}
		}

		VectorReal varcoords = reduction.getSingle(coords, besttime);
		output.set(input.getScalarPixel(varcoords));
		tcs.swizzle(varcoords, tco);
		outtime.set(intime.getPixel(tco));
	}
}
