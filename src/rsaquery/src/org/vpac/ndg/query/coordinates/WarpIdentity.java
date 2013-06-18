package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

/**
 * The identity transform. Output == input. Use this when datasets are already
 * aligned and in the same coordinate system.
 *
 * @author Alex Fraser
 */
public class WarpIdentity implements Warp {

	@Override
	public void warp(VectorReal co) {
		// Do nothing.
	}

	@Override
	public void warp(BoxReal box) {
		// Do nothing.
	}

	@Override
	public String toString() {
		return "Identity";
	}
}
