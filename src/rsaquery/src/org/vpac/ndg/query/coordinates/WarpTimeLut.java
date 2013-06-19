package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

public class WarpTimeLut implements Warp {

	double pre;
	double post;
	double[] lut;

	public WarpTimeLut(double[] lut) {
		this.lut = lut;
		pre = -1;
		post = lut[lut.length - 1] + 1.0;
	}

	@Override
	public void warp(VectorReal co) {
		int i = (int)co.getT();
		if (i < 0) {
			co.setT(pre);
			return;
		} else if (i >= lut.length) {
			co.setT(post);
			return;
		}
		co.setT(lut[i]);
	}

	@Override
	public void warp(BoxReal box) {
		// No need to warp all four corners: assume time is uniform across space
		warp(box.getMin());
		warp(box.getMax());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TimeLUT(");
		boolean first = true;
		for (double value : lut) {
			if (!first)
				sb.append(", ");
			else
				first = false;
			sb.append(value);
		}
		sb.append(")");
		return sb.toString();
	}
}
