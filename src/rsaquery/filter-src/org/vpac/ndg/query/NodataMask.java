package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * Return 1 for data, and 0 for nodata
 *
 * @author Alex Fraser
 */
@Description(name = "Nodata Mask", description = "Return 1 for data, and 0 for nodata")
@InheritDimensions(from = "input")
public class NodataMask implements Filter {

	public PixelSource input;

	@CellType("input")
	public Cell output;

	Element<?> one;
	Element<?> zero;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		one = input.getPrototype().getElement().copy();
		one.set(1);
		zero = input.getPrototype().getElement().copy();
		zero.set(0);
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		Element<?> currentCell = input.getPixel(coords);
		if (currentCell.isValid())
			output.set(one);
		else
			output.set(zero);
	}

}
