package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Performs two warps, one after the other.
 * @author Alex Fraser
 */
public class WarpDual implements Warp {

	Warp warpA;
	Warp warpB;

	public WarpDual(Warp warpA, Warp warpB) {
		this.warpA = warpA;
		this.warpB = warpB;
	}

	@Override
	public void warp(VectorReal co) {
		warpA.warp(co);
		warpB.warp(co);
	}

	@Override
	public void warp(BoxReal box) {
		warpA.warp(box);
		warpB.warp(box);
	}

	@Override
	public String toString() {
		return String.format("WarpDual(%s, %s)", warpA, warpB);
	}
}
