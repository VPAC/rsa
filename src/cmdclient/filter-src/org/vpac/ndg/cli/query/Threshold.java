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
