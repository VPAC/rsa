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
