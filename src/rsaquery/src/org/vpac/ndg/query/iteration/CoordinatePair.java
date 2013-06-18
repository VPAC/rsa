package org.vpac.ndg.query.iteration;

import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Combines an image index with more abstract coordinates.
 * @author Alex Fraser
 */
public class CoordinatePair {
	public VectorInt imageIndex;
	public VectorReal coordinates;

	public CoordinatePair(int ndimensions) {
		imageIndex = VectorInt.createEmpty(ndimensions);
		coordinates = VectorReal.createEmpty(ndimensions);
	}

	public CoordinatePair copy() {
		CoordinatePair other = new CoordinatePair(imageIndex.size());
		other.coordinates.set(coordinates);
		other.imageIndex.set(imageIndex);
		return other;
	}
}
