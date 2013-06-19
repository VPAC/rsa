package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.VectorInt;

/**
 * Determines how an image should be split up into tiles.
 * @author Alex Fraser
 */
public interface TilingStrategy {

	VectorInt getTileShape(VectorInt baseShape);
	VectorInt getGridShape(VectorInt baseShape);

}
