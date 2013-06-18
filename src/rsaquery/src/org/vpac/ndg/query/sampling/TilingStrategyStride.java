package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.VectorInt;

/**
 * Splits an image into long chunks, based on the natural ordering of NetCDF.
 * @author Alex Fraser
 */
public class TilingStrategyStride  extends TilingStrategyBase {

	private long volume;

	/**
	 * Create a new tiling strategy that splits the image into long chunks.
	 *
	 * @param minVolume The minimum number of pixels to have in a chunk. The
	 *        actual volume may differ, because the tile must be rectangular
	 *        (not ragged).
	 */
	public TilingStrategyStride(long minVolume) {
		this.volume = minVolume;
	}

	@Override
	public VectorInt getTileShape(VectorInt baseShape) {
		VectorInt tileShape = VectorInt.createEmpty(baseShape.size());

		// NOTE that the ordering is reversed, so that the X axis is longest,
		// followed by Y, etc.
		long remainder = volume;
		for (int i = tileShape.size() - 1; i >= 0; i--) {
			long current;
			if (baseShape.get(i) < remainder)
				current = baseShape.get(i);
			else
				current = remainder;
			tileShape.set(i, current);
			remainder = (remainder / baseShape.get(i)) + 1;
		}

		return tileShape;
	}

}
