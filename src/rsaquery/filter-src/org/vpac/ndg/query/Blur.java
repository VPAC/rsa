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

import org.vpac.ndg.query.iteration.Kernel;
import org.vpac.ndg.query.iteration.KernelPair;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * Blurs each time slice in a data cube, using a 2D Gaussian kernel.
 *
 * @author Alex Burns
 * @author Alex Fraser
 *
 */
@Description(name = "Blur", description = "Blur pixel using 2D Gaussian kernel")
@InheritDimensions(from = "input")
public class Blur implements Filter {

	public PixelSource input;

	@CellType("input")
	public Cell output;

	Kernel<Float> kernelGen;
	Element<?> result;
	Element<?> val;
	Element<?> sum;

	protected static Float[] convolution_kernel = {
		0.00000067f, 0.00002292f, 0.00019117f, 0.00038771f, 0.00019117f, 0.00002292f, 0.00000067f,
		0.00002292f, 0.00078633f, 0.00655965f, 0.01330373f, 0.00655965f, 0.00078633f, 0.00002292f,
		0.00019117f, 0.00655965f, 0.05472157f, 0.11098164f, 0.05472157f, 0.00655965f, 0.00019117f,
		0.00038771f, 0.01330373f, 0.11098164f, 0.22508352f, 0.11098164f, 0.01330373f, 0.00038771f,
		0.00019117f, 0.00655965f, 0.05472157f, 0.11098164f, 0.05472157f, 0.00655965f, 0.00019117f,
		0.00002292f, 0.00078633f, 0.00655965f, 0.01330373f, 0.00655965f, 0.00078633f, 0.00002292f,
		0.00000067f, 0.00002292f, 0.00019117f, 0.00038771f, 0.00019117f, 0.00002292f, 0.00000067f};

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		VectorInt shape = VectorInt.createEmpty(input.getRank(), 1);
		shape.setX(7);
		shape.setY(7);
		kernelGen = new Kernel<Float>(shape, convolution_kernel);
		result = input.getPrototype().getElement().asFloat();
		val = result.copy();
		sum = result.copy();
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		// Even when it is necessary to do arithmetic with a certain data type,
		// it's better to use Elements because they preserve NODATA - and in
		// this case, automatic handling of vector types.
		sum.set(0.0);
		result.set(0.0);
		// Start by inheriting the validity of the central (current) pixel. This
		// prevents dilation.
		result.setValid(input.getPixel(coords));
		for (KernelPair<Float> pair : kernelGen.setCentre(coords)) {
			val.set(input.getPixel(pair.coordinates));
			// Only add the pixel if it is valid, for each component. This
			// prevents erosion.
			result.addIfValid(val.mul(pair.value));
			// Add the current kernel value, but mask it by the pixel that has
			// been read - to ensure the sum matches the result for the division
			// later.
			sum.addIfValid(pair.value, val);
		}
		result.div(sum);
		output.set(result);
	}

}
