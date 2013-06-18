package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

/**
 * A translation: output = input + constant. Use this to align datasets that
 * share a coordinate system.
 *
 * @author Alex Fraser
 */
public class WarpOffset implements Warp {

	VectorReal offset;

	public WarpOffset(VectorReal offsetIn, VectorReal offsetOut, VectorReal scale) {
		offset = offsetIn.subNew(offsetOut);
		offset.div(scale);
	}

	@Override
	public void warp(VectorReal co) {
		co.add(offset);
	}

	@Override
	public void warp(BoxReal box) {
		// No need to warp all four corners: offset remains axis-aligned.
		warp(box.getMin());
		warp(box.getMax());
	}

	@Override
	public String toString() {
		return String.format("Offset(%s)", offset);
	}
}
