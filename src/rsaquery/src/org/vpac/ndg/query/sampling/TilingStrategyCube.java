package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.VectorInt;

/**
 * Splits an image into cubes. If any dimension of the cube is larger than the
 * corresponding dimension in the image, it will be truncated (i.e. volume is
 * not guaranteed).
 * @author Alex Fraser
 */
public class TilingStrategyCube extends TilingStrategyBase {

	private int edgeLen;

	/**
	 * Create a new tiling strategy that tries to make cubic tiles.
	 * @param edgeLen The maximum length of any tile dimension.
	 */
	public TilingStrategyCube(int edgeLen) {
		this.edgeLen = edgeLen;
	}

	@Override
	public VectorInt getTileShape(VectorInt baseShape) {
		VectorInt tileShape = VectorInt.createEmpty(baseShape.size());
		for (int i = 0; i < tileShape.size(); i++) {
			if (baseShape.get(i) < edgeLen)
				tileShape.set(i, baseShape.get(i));
			else
				tileShape.set(i, edgeLen);
		}
		return tileShape;
	}

}
