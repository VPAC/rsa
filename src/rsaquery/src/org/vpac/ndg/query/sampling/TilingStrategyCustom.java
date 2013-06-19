package org.vpac.ndg.query.sampling;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.math.VectorInt;

/**
 * Splits an image into chunks, with configurable parameters such as which
 * dimensions to give priority to.
 *
 * @author Alex Fraser
 */
public class TilingStrategyCustom  extends TilingStrategyBase {

	Logger log = LoggerFactory.getLogger(TilingStrategyCustom.class);

	// Number of pixels to use
	private long volume;
	// e.g. "time y x" for canonical, or "y x time" for time-reduction
	private int[] dimensionPrecedence;
	// The minimum window for sampling.
	private VectorInt window;

	/**
	 * Create a new tiling strategy that splits the image into long chunks.
	 *
	 * @param minVolume The minimum number of pixels to have in a chunk. The
	 *        actual volume may differ, because the tile must be rectangular
	 *        (not ragged).
	 */
	public TilingStrategyCustom(long minVolume) {
		this.volume = minVolume;
		this.dimensionPrecedence = new int[0];
	}

	@Override
	public VectorInt getTileShape(VectorInt baseShape) {

		List<Integer> dims = new ArrayList<Integer>();

		// Reverse the order, to preserve NetCDF canonical order.
		for (int i = dimensionPrecedence.length - 1; i >= 0; i--) {
			dims.add(dimensionPrecedence[i]);
		}

		// Append missing dimensions, also in reverse.
		for (int i = baseShape.size() - 1; i >= 0; i--) {
			if (!dims.contains(i))
				dims.add(i);
		}

		// Initialise shape to minimum window size.
		VectorInt tileShape;
		if (window == null)
			tileShape = VectorInt.createEmpty(baseShape.size(), 1);
		else
			tileShape = window.minNew(baseShape);
		log.trace("Initial shape: {}", tileShape);

		// Grow shape to fill volume in the order determined above.
		for (Integer i : dims) {
			// Find remainder, excluding current dimension.
			log.trace("Growing dimension {}", i);
			long currentVolume = tileShape.volume();
			long currentAxis = tileShape.get(i);
			currentVolume /= currentAxis;
			long remainder = (volume / currentVolume) + 1;
			log.trace("Volume, ignoring this dimension: {}, remainder: {}",
					currentVolume, remainder);

			if (baseShape.get(i) < remainder) {
				log.trace("Filling dimension");
				currentAxis = baseShape.get(i);
			} else if (remainder > currentAxis) {
				log.trace("Expanding dimension");
				currentAxis = remainder;
			} else {
				log.trace("Dimension is already the right size.");
			}
			tileShape.set(i, currentAxis);
		}

		log.debug("Custom tile shape is {}; actual volume is {}", tileShape,
				tileShape.volume());
		if (tileShape.volume() <= 0) {
			throw new IllegalStateException(String.format("Error creating " +
					"tile shape: resulting shape %s has no volume.", tileShape));
		}
		return tileShape;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	public int[] getPrecedence() {
		return dimensionPrecedence;
	}

	public void setPrecedence(int[] dimensionPrecedence) {
		this.dimensionPrecedence = dimensionPrecedence;
	}

	public VectorInt getWindow() {
		return window;
	}

	public void setWindow(VectorInt window) {
		this.window = window;
	}

}
