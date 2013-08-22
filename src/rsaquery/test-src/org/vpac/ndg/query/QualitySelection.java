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
 * THE MOST RECENT CLOUD FREE PIXELS BEFORE A GIVEN DATE (Query returns a RGB
 * colour image for a selection of bands) - Display the latest quality assured
 * pixels.
 *
 * This fitler constructs a multi-band 2D image (y,x) from a multi-band 3D image
 * (t,y,x). The pixel with the highest value in the Quality band is kept from
 * each time slice.
 *
 * @author Alex Fraser
 *
 */
@InheritDimensions(from = "input", reduceBy = 1)
public class QualitySelection implements Filter {

	// Parameters
	public int qualityThreshold;

	// Input fields.
	public PixelSource intime;
	public PixelSource input;
	public PixelSourceScalar inquality;

	// Output fields.
	@CellType("intime")
	public Cell outtime;
	@CellType("input")
	public Cell output;
	@CellType("inquality")
	public Cell outquality;

	Reduction reduction;
	VectorReal tco = VectorReal.createEmpty(1);
	Swizzle tcs = SwizzleFactory.compile("t");

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		reduction = new Reduction(input.getBounds());
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		// Search over all times at the specified coordinates, looking for the
		// highest-quality pixel.
		double bestt = -1;
		ScalarElement bestq = null;
		for (VectorReal varcoords : reduction.getIterator(coords)) {
			ScalarElement quality = inquality.getScalarPixel(varcoords);
			if ((bestq == null || quality.compareTo(bestq) > 0) &&
					quality.compareTo(qualityThreshold) > 0) {

				bestq = quality;
				bestt = varcoords.getT();
			}
		}

		// Transfer data for good quality pixels.
		if (bestq != null) {
			VectorReal varcoords = reduction.getSingle(coords, bestt);
			output.set(input.getPixel(varcoords));
			outquality.set(bestq);
			tcs.swizzle(varcoords, tco);
			outtime.set(intime.getPixel(tco));
		} else {
			output.unset();
			outtime.unset();
			outquality.unset();
		}
	}
}
