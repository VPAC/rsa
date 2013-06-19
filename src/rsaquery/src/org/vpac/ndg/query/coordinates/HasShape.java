package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.VectorInt;


public interface HasShape extends HasRank {
	/**
	 * @return The shape of the underlying variable (i.e. the length of each of
	 *         its dimensions).
	 */
    VectorInt getShape();
}
