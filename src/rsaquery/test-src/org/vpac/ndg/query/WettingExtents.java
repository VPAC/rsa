package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.iteration.Reduction;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.ElementInt;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.Swizzle;
import org.vpac.ndg.query.math.SwizzleFactory;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;
import org.vpac.ndg.query.sampling.PixelSourceScalar;

/**
 * WATER ( Query returns 0 or 1 ) - For a period between date A and date B,
 * display pixels with a value of NBAR Landsat band 5 value of less than 1000
 * (reflectance scaled by 10000) - true = 1 false = 0. The pixel level metadata
 * for positive results should provide capture date information.
 * 
 * @author Alex Fraser
 * 
 */
@InheritDimensions(from = "input", reduceBy = 1)
public class WettingExtents implements Filter {

	// Parameters.
	public int dryThreshold;

	// Input fields.
	@Constraint(dimensions=1)
	public PixelSource intime;

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
		outtime.unset();
		for (VectorReal varcoords : reduction.getIterator(coords)) {
			ScalarElement val = input.getScalarPixel(varcoords);
			if (val.compareTo(dryThreshold) < 0) {
				val.set(1);
				output.set(val);
				tcs.swizzle(varcoords, tco);
				outtime.set(intime.getPixel(tco));
				return;
			}
		}

		output.set(new ElementInt(0));
	}

}
