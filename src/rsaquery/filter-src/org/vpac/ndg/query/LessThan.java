package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.ElementByte;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSourceScalar;

@Description(name = "Less Than", description = "Generates a mask\nmask = input < value")
@InheritDimensions(from = "input")
public class LessThan implements Filter {

	// Parameters.
	public int value = 1;

	// Input fields.
	public PixelSourceScalar input;

	// Output fields.
	@CellType("byte")
	public Cell mask;

	ScalarElement val;
	ScalarElement match = new ElementByte((byte) 1);
	ScalarElement fail = new ElementByte((byte) 0);

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		val = (ScalarElement) input.getPrototype().getElement().copy();
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		val = input.getScalarPixel(coords);
		if (!val.isValid()) {
			mask.unset();
			return;
		}
		if (val.compareTo(value) >= 0)
			mask.set(match);
		else
			mask.set(fail);
	}
}
