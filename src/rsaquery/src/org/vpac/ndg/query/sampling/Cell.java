package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.Element;

/**
 * Stores a single pixel value. May be scalar or vector.
 *
 * <p><img src="doc-files/PixelSource_and_Cell_class.png" /></p>
 *
 * @see PixelSource
 * @author Alex Fraser
 */
public interface Cell extends HasPrototype {
	/**
	 * @return The value of the cell in the dataset.
	 */
	Element<?> get();

	/**
	 * Write into the dataset.
	 *
	 * @param value The value to write.
	 * @throws ClassCastException If the value is of the wrong type (scalar vs.
	 *         vector).
	 */
	void set(Element<?> value) throws ClassCastException;

	/**
	 * Set the cell to be blank, i.e. <em>NODATA</em>.
	 */
	void unset();

}
