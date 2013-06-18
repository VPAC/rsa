package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorReal;

public interface PixelSourceScalar extends PixelSource {

	/**
	 * @param co
	 *            The location in the array to retrieve data from.
	 * @return The value at the specified coordinates.
	 * @throws IOException
	 *             If the file could not be read.
	 */
	ScalarElement getScalarPixel(VectorReal co) throws IOException;

}
