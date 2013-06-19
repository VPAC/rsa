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
 * Sum values over time
 *
 * @author Alex Fraser
 */
@Description(name = "Sum", description = "Sum values over time")
@InheritDimensions(from = "input", reduceBy = 1)
public class SumOverTime implements Filter {

	public PixelSource input;

	@CellType("input")
	public Cell output;

	Reduction reduction;
	VectorReal tco = VectorReal.createEmpty(1);
	Element<?> val;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		reduction = new Reduction(input.getBounds());
		val = input.getPrototype().getElement().copy();
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		// Search over all times.
		boolean valid = false;
		val.set(0);
		for (VectorReal varcoords : reduction.getIterator(coords)) {
			Element<?> ival = input.getPixel(varcoords);
			if (!ival.isValid())
				continue;
			val.add(ival);
			valid = true;
		}

		if (valid)
			output.set(val);
		else
			output.unset();
	}
}
