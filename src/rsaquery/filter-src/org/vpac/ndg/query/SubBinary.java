package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * Finds the difference between two bands.
 *
 * @author Alex Fraser
 */
@Description(name = "Subtract", description = "Subtract two pixels together")
@InheritDimensions(from = "inputA")
public class SubBinary implements Filter {

	public PixelSource inputA;

	public PixelSource inputB;

	@CellType("inputA")
	public Cell output;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		output.set(inputA.getPixel(coords).sub(inputB.getPixel(coords)));
	}

}
