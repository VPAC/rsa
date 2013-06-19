package org.vpac.ndg.query.iteration;

import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.coordinates.HasShape;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Reinterprets a 1D array as an nD image.
 * @author Alex Fraser
 */
public class Image<T> implements HasShape {

	VectorInt shape;
	T[] image;

	public Image(T[] image, VectorInt shape) throws QueryConfigurationException {
		this.shape = shape;
		this.image = image;
	}

	public T getPixel(int i) {
		return image[i];
	}

	/**
	 * @param coordinates The coordinates to sample from.
	 * @return The value at the specified coordinates.
	 */
	public T getPixel(VectorInt coordinates) {
		int idx = (int) coordinates.toPixelIndex(shape);
		return image[idx];
	}

	/**
	 * Get the value at the specified coordinates. The coordinates will be
	 * floored to the nearest whole pixel; no interpolation is performed.
	 * 
	 * @param coordinates The coordinates to sample from.
	 * @return The value at the specified coordinates.
	 */
	public T getPixel(VectorReal coordinates) {
		int idx = (int) coordinates.toPixelIndex(shape);
		return image[idx];
	}

	@Override
	public VectorInt getShape() {
		return shape;
	}

	@Override
	public int getRank() {
		return shape.size();
	}

}
