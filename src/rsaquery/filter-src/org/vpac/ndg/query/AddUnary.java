package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * Adds a constant value to each pixel.
 *
 * @author Alex Fraser
 */
@Description(name = "Add Unary", description = "Add a constant value to each pixel")
@InheritDimensions(from = "input")
public class AddUnary implements Filter {

	public int value;

	public PixelSource input;

	@CellType("input")
	public Cell output;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		Element<?> currentCell = input.getPixel(coords);
		currentCell.add(value);
		output.set(currentCell);
	}

}
