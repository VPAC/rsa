package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.iteration.CoordinatePair;
import org.vpac.ndg.query.iteration.Rectangle;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * Finds the variance of an NxN selection of cells around the current
 * coordinates.
 *
 * @author Alex Fraser
 */
@Description(name = "Variance", description = "Find the variance of NxN selection of pixels")
@InheritDimensions(from = "input")
public class Variance implements Filter {

	// Input fields.
	public int windowSize = 3;
	public PixelSource input;

	// Output fields.
	@CellType(value = "input", as = "float")
	public Cell output;

	// Internal variables.
	Rectangle rect;
	Element<?> val;
	Element<?> delta;
	Element<?> temp;
	Element<?> mean;
	Element<?> M2;
	Element<?> n;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		// Create a window to iterate over. Ignore the highest dimension so that
		// this filter can be used in conjunction with a reduction. It would be
		// possible to make this configurable, e.g. by ignoring the first n
		// dimensions.
		VectorInt rectShape = VectorInt.createEmpty(input.getRank(), windowSize);
		rectShape.setT(1);
		rect = new Rectangle(rectShape);

		// Don't just create an ElementFloat here. Using asFloat() allows the
		// element to be a vector type.
		val = input.getPrototype().getElement().asFloat();
		delta = val.copy();
		temp = val.copy();
		mean = val.copy();
		M2 = val.copy();
		n = input.getPrototype().getElement().asInt();
	}

	@Override
	public final void kernel(VectorReal coords) throws IOException {
		// Use Knuth's single-pass algorithm: assume memory access is slower
		// than division.
		mean.set(0);
		M2.set(0);
		delta.set(0);
		n.set(0);

		// Prevent dilation
		M2.setValid(input.getPixel(coords));

		for (CoordinatePair co : rect.setCentre(coords)) {
			// Coerce the pixel into a float type with the same number of bands.
			val.set(input.getPixel(co.coordinates));

			n.addIfValid(1, val);

			// delta = val - mean
			delta.subOfIfValid(val, mean);

			// mean += delta / n
			temp.divOfIfValid(delta, n, val);
			mean.addIfValid(temp, val);

			// M2 += delta * (val - mean)
			temp.subOfIfValid(val, mean).mulIfValid(delta, val);
			M2.addIfValid(temp, val);
		}

		M2.div(n.sub(1));
		output.set(M2);
	}

}
