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

	Reduction reduction;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		// Don't just create an ElementFloat here. Using asFloat() allows the
		// element to be a vector type.
		val = input.getPrototype().getElement().asFloat();
		delta = val.copy();
		temp = val.copy();
		mean = val.copy();

		reduction = new Reduction(input.getBounds());
	}

	@Override
	public final void kernel(VectorReal coords) throws IOException {
		// Use Knuth's single-pass algorithm: assume memory access is slower
		// than division.
		int n = 0;
		mean.set(0);
		delta.set(0);

		for (VectorReal co : reduction.getIterator(coords)) {
			// Coerce the pixel into a float type with the same number of bands.
			val.set(input.getPixel(co));
			if (!val.isValid())
				continue;

			n++;

			// delta = val - mean
			delta.subOf(val, mean);

			// mean += delta / n
			temp.divOf(delta, n);
			mean.add(temp);

			// M2 += delta * (val - mean)
			temp.subOf(val, mean).mul(delta);
		}

		if (n > 0)
			output.set(mean);
		else
			output.unset();
	}

}
