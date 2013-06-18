package org.vpac.ndg.cli.query;

import java.io.IOException;

import org.vpac.ndg.query.Filter;
import org.vpac.ndg.query.InheritDimensions;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.ElementByte;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.PixelSourceScalar;

/**
 * Run a double threshold of Band 5 < 1000 and NDVI < 0.08. Anything with Band 5
 * < 1000 and NDVI > 0.08 should be set to another class called "mixed".
 *
 * <p>
 * Formula provided by Geoscience Australia.
 * </p>
 *
 * @author Alex Fraser
 */
@InheritDimensions(from = "band5")
public class WetByNdvi implements Filter {

	// Parameters.
	public int dryThreshold = 1000;
	public double ndviThreshold = 0.08;

	// Input fields. Must be scalar to allow comparison.
	public PixelSourceScalar ndvi;
	public PixelSourceScalar band5;

	// Output fields.
	@CellType("byte")
	public Cell output;

	ScalarElement wet = new ElementByte((byte) 2);
	ScalarElement mixed = new ElementByte((byte) 1);
	ScalarElement dry = new ElementByte((byte) 0);

	@Override
	public void initialise(BoxReal bounds) throws QueryConfigurationException {
	}

	@Override
	public void kernel(VectorReal coords) throws IOException {
		ScalarElement b5 = band5.getScalarPixel(coords);
		if (!b5.isValid()) {
			output.unset();
			return;
		}
		if (band5.getScalarPixel(coords).compareTo(dryThreshold) > 0) {
			output.set(dry);
			return;
		}

		ScalarElement ndviVal = ndvi.getScalarPixel(coords);

		if (!ndviVal.isValid()) {
			output.unset();
			return;
		}

		if (ndviVal.compareTo(ndviThreshold) < 0)
			output.set(wet);
		else
			output.set(mixed);
	}
}
