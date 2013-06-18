package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * Adds two bands together.
 * 
 * @author Alex Fraser
 */
@Description(name = "Add", description = "Add two pixels together")
@InheritDimensions(from = "inputA")
public class AddBinary implements Filter {

	public PixelSource inputA;

	public PixelSource inputB;

	@CellType("inputA")
	public Cell output;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		output.set(inputA.getPixel(coords).add(inputB.getPixel(coords)));
	}
}
