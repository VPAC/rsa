package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Transforms coordinates.
 * @author Alex Fraser
 */
public interface Warp {

	/**
	 * Transform a point to the target coordinate system.
	 * 
	 * @param co The point to transform. It will be transformed in place, i.e.
	 *        it will be modified.
	 */
	void warp(VectorReal co);

	/**
	 * Transform a bounding box into the target coordinate system.
	 */
	void warp(BoxReal box);

}
