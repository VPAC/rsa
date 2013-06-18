package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.coordinates.HasBounds;
import org.vpac.ndg.query.coordinates.HasRank;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorReal;

/**
 * A pixel source is a field of {@link Element Elements}; essentially a
 * read-only image. It can be queried for pixel values.
 *
 * <p><img src="doc-files/PixelSource_and_Cell_class.png" /></p>
 *
 * @see Cell
 * @author Alex Fraser
 */
public interface PixelSource extends HasBounds, HasRank, HasPrototype {

	/**
	 * @param co The coordinates to retrieve the value of, in the global
	 *        coordinate system.
	 * @return The value of the image at the specified coordinates.
	 * @throws IOException If the pixel value could not be read.
	 */
	Element<?> getPixel(VectorReal co) throws IOException;

	/**
	 * @return The effective bounds of this input in the global coordinate
	 *         system.
	 */
	@Override
	BoxReal getBounds();

}
