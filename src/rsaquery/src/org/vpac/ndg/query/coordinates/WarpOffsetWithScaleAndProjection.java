package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

/**
 * A translation and scale. Use this to align datasets that share a coordinate
 * system, but which may not share the same grid.
 * 
 * <p>
 * <em>f(x) = (s<sub>a</sub>x + o<sub>a</sub> - o<sub>b</sub>) / s<sub>b</sub></em>
 * </p>
 * 
 * <p>
 * Where <em>s<sub>a</sub></em> and <em>s<sub>b</sub></em> are the cell sizes
 * (scale) of the input and output datasets, and <em>o<sub>a</sub></em> and
 * <em>o<sub>b</sub></em> are the offsets of the two grids from the origin of
 * the coordinate system.
 * </p>
 * 
 * @author Alex Fraser
 */
public class WarpOffsetWithScaleAndProjection implements Warp {

	VectorReal offsetIn;
	VectorReal offsetOut;
	VectorReal scaleIn;
	VectorReal scaleOutInv;
	Warp projectionWarp;

	public WarpOffsetWithScaleAndProjection(
			VectorReal offsetIn, VectorReal offsetOut,
			VectorReal scaleIn, VectorReal scaleOut,
			Warp projectionWarp) {

		int size = offsetIn.size();
		if (offsetOut.size() != size || scaleIn.size() != size
				|| scaleOut.size() != size) {
			throw new IllegalArgumentException(
					"Warp specified with incompatible vector dimensionality.");
		}

		this.offsetIn = offsetIn.copy();
		this.offsetOut = offsetOut.copy();
		this.scaleIn = scaleIn.copy();
		// Invert output scale so multiply can be used during actual warp.
		this.scaleOutInv = VectorReal.createEmpty(scaleOut.size(), 1.0);
		this.scaleOutInv.div(scaleOut);
		this.projectionWarp = projectionWarp;
	}

	@Override
	public void warp(VectorReal co) {
		try {
			// Transform into input projection coordinates.
			co.mul(scaleIn);
			co.add(offsetIn);

			// Transform into output projection coordinates.
			projectionWarp.warp(co);

			// Transform into output cell index.
			co.sub(offsetOut);
			co.mul(scaleOutInv);

		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(
					"Warp with incompatible vector dimensionality.");
		}
	}

	@Override
	public void warp(BoxReal box) {
		// No need to warp all four corners: offset remains axis-aligned.
		warp(box.getMin());
		warp(box.getMax());
	}

	@Override
	public String toString() {
		return String.format("Offset((Proj(%sx + %s) - %s) * %s)", scaleIn,
				offsetIn, offsetOut, scaleOutInv);
	}
}
