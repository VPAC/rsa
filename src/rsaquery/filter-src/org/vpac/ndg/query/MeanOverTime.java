package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.iteration.Reduction;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * Finds the mean value over time.
 *
 * @author Alex Fraser
 */
@Description(name = "Mean", description = "Find the mean over time")
@InheritDimensions(from = "input", reduceBy = 1)
public class MeanOverTime implements Filter {

	// Input fields.
	public PixelSource input;

	// Output fields.
	@CellType("input")
	public Cell output;

	// Internal variables.
	private Element<?> val;
	private Element<?> delta;
	private Element<?> temp;
	private Element<?> mean;
	private Element<?> n;

	Reduction reduction;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		// Don't just create an ElementFloat here. Using asFloat() allows the
		// element to be a vector type.
		val = input.getPrototype().getElement().asFloat();
		delta = val.copy();
		temp = val.copy();
		mean = val.copy();
		n = input.getPrototype().getElement().asInt();

		reduction = new Reduction(input.getBounds());
	}

	@Override
	public final void kernel(VectorReal coords) throws IOException {
		// Use Knuth's single-pass algorithm: assume memory access is slower
		// than division.
		mean.set(0);
		delta.set(0);
		n.set(0);

		// Start off invalid, and become valid later if a valid pixel is found.
		mean.setValid(false);

		for (VectorReal co : reduction.getIterator(coords)) {
			// Coerce the pixel into a float type with the same number of bands.
			val.set(input.getPixel(co));
			mean.setValidIfValid(val);

			n.addIfValid(1, val);

			// delta = val - mean
			delta.subOfIfValid(val, mean);

			// mean += delta / n
			temp.divOfIfValid(delta, n, val);
			mean.addIfValid(temp, val);

			// M2 += delta * (val - mean)
			temp.subOfIfValid(val, mean).mulIfValid(delta, val);
		}

		output.set(mean);
	}

}
