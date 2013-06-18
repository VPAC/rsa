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
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		// Only blur neighbouring pixels if this pixel is not nodata - prevents
		// dilation.
		if (!input.getPixel(coords).isValid()) {
			output.unset();
			return;
		}

		// Even when it is necessary to do arithmetic with a certain data type,
		// it's better to use Elements because they preserve NODATA - and in
		// this case, automatic handling of vector types.
		float sum = 0;
		result.set(0);
		for (KernelPair<Float> pair : kernelGen.setCentre(coords)) {
			val.set(input.getPixel(pair.coordinates));
			// Only include this pixel if not nodata - prevents erosion.
			if (val.isValid()) {
				result.add(val.mul(pair.value));
				sum += pair.value;
			}
		}
		// kernel has no zeros; this should be safe.
		result.div(sum);
		output.set(result);
	}

}
