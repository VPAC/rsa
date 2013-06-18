package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.math.VectorElement;
import org.vpac.ndg.query.math.VectorReal;

public interface PixelSourceVector extends PixelSource {

	/**
	 * @param co
	 *            The location in the array to retrieve data from.
	 * @return The value at the specified coordinates.
	 * @throws IOException
	 *             If the file could not be read.
	 */
	VectorElement getVectorPixel(VectorReal co) throws IOException;

}
