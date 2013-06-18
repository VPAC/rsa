package org.vpac.ndg.query.iteration;

import org.vpac.ndg.query.math.VectorReal;

/**
 * Combines coordinates with a value.
 * @author Alex Fraser
 *
 * @param <T> The type of the value.
 */
public class KernelPair<T> {
	public T value;
	public VectorReal coordinates;

	public KernelPair(int ndimensions) {
		coordinates = VectorReal.createEmpty(ndimensions);
	}
}
