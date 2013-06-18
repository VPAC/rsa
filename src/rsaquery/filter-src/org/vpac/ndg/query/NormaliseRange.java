package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * Re-maps numbers from a given input range to 0-1.
 * 
 * @author Alex Fraser
 */
@Description(name = "Normalise", description = "Normalise pixel to 0-1 range")
@InheritDimensions(from = "input")
public class NormaliseRange implements Filter {

	public long upper = 255;
	public long lower = 0;

	public PixelSource input;

	@CellType(value = "input", as = "float")
	public Cell output;

	Element<?> offset;
	Element<?> fraction;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		offset = input.getPrototype().getElement().asFloat();
		fraction = offset.copy();
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		offset.subOf(input.getPixel(coords), lower);
		fraction.divOf(offset, upper);
		output.set(fraction);
	}

}
