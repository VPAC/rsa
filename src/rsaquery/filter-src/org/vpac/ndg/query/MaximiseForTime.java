package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.iteration.Reduction;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;
import org.vpac.ndg.query.sampling.PixelSourceScalar;

/**
 * Find and keep the greatest value.
 *
 * @author Alex Fraser
 */
@Description(name = "Maximise Time", description = "Get the latest time")
@InheritDimensions(from = "toMaximise", reduceBy = 1)
public class MaximiseForTime implements Filter {

	/**
	 * When the value becomes greater than or equal to this value, the filter
	 * will return early. Otherwise, the filter will search over all time
	 * values. Defaults to Integer.MAX_VALUE.
	 */
	public int threshold = Integer.MAX_VALUE;

	@Constraint(dimensions=1)
	public PixelSource intime;

	/**
	 * The field to find the maximum value of.
	 */
	public PixelSourceScalar toMaximise;
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

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		reduction = new Reduction(toMaximise.getBounds());
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		double besttime = 0;
		ScalarElement max = null;

		// Search over all times.
		for (VectorReal varcoords : reduction.getIterator(coords)) {
			ScalarElement val = toMaximise.getScalarPixel(varcoords);
			if (!val.isValid())
				continue;
			if (max == null || val.compareTo(max) > 0) {
				besttime = varcoords.getT();
				max = val;
			}
			if (val.compareTo(threshold) >= 0) {
				// Threshold reached; no need to continue.
				break;
			}
		}

		// Store result.
		if (max == null) {
			output.unset();
			outtime.unset();
		} else {
			VectorReal co = reduction.getSingle(coords, besttime);
			output.set(toKeep.getPixel(co));
			tco.setT(besttime);
			outtime.set(intime.getPixel(tco));
		}
	}
}
