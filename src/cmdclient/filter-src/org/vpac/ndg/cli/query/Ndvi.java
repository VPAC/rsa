package org.vpac.ndg.cli.query;

import java.io.IOException;

import org.vpac.ndg.query.Filter;
import org.vpac.ndg.query.InheritDimensions;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * NDVI = Normalised Difference Vegetation Index described by the following
 * (band4 - band3)/(band4 + band3) - produces values between -1 and 1.
 * 
 * <p>
 * Formula provided by Geoscience Australia.
 * </p>
 * 
 * @author Alex Faser
 */
@InheritDimensions(from = "band3")
public class Ndvi implements Filter {

	// Input fields.
	public PixelSource band3;
	public PixelSource band4;

	// Use the same number of components as band3 for the output, but convert it
	// to a float type. While it is unlikely that the input will be vector, it
	// is good practice to keep things general, and shouldn't slow it down.
	@CellType(value="band3", as="float")
	public Cell output;

	Element<?> ndvi;
	Element<?> temp;

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
		ndvi = output.getPrototype().getElement().copy();
		temp = ndvi.copy();
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		Element<?> b3 = band3.getPixel(coords);
		Element<?> b4 = band4.getPixel(coords);
		temp.addOf(b4, b3);
		ndvi.subOf(b4, b3);
		ndvi.div(temp);
		output.set(ndvi);
	}
}
