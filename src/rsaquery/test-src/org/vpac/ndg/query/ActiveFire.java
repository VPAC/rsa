package org.vpac.ndg.query;

import java.io.IOException;

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

/**
 * ACTIVE FIRE ( Query returns temperature, metadata enables identification of
 * date of fire per pixel) - For a period between date A and date B, display the
 * greatest pixel temperature for Top of Atmosphere temperature with a value >
 * 360K. The pixel level metadata for the result should provide capture date
 * information.
 * 
 * @author Alex Fraser
 * 
 */
@InheritDimensions(from = "input", reduceBy = 1)
public class ActiveFire implements Filter {

	// Parameters.
	public int temperatureThreshold;

	// Input fields. It's better practice to not specify the dimensions, but we
	// do so here to test the API.
	@Constraint(dimensions=1)
	public PixelSource intime;
	@Constraint(dimensions=3)
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
		ScalarElement maxTemp = null;
		Element<?> timeval = null;
		for (VectorReal varcoords : reduction.getIterator(coords)) {
			ScalarElement temp = input.getScalarPixel(varcoords);
			if ((maxTemp == null || temp.compareTo(maxTemp) > 0) &&
					temp.compareTo(temperatureThreshold) > 0) {

				maxTemp = temp;
				tcs.swizzle(varcoords, tco);
				timeval = intime.getPixel(tco);
			}
		}

		if (maxTemp != null) {
			output.set(maxTemp);
			outtime.set(timeval);
		} else {
			output.set(new ElementInt(0));
			outtime.unset();
		}
	}
}
