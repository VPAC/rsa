package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * Encapsulates a function to run over pixels in an image.
 *
 * <p>The framework will create a pool of instances of the filter, which will be
 * used to iterate over the data in a stream processing fashion.</p>
 *
 * <p><img src="doc-files/Filter_class.png" /></p>
 *
 * @see <a href="http://en.wikipedia.org/wiki/Stream_processing">Stream processing</a>
 * @see PixelSource Input fields
 * @see Cell Output fields
 *
 * @author Alex Fraser
 */
public interface Filter {
	/**
	 * Called immediately before the kernel function runs for the first time.
	 * The values of fields will not be changed by the framework after this
	 * function runs; this is not true for the constructor. Should be used to
	 * configure the filter, e.g. set up custom views of the data and initialise
	 * local storage (such as {@link Element} instances).
	 *
	 * @param bounds The extents that this filter will operate over, in global
	 * cell space.
	 */
	void initialise(BoxReal bounds) throws QueryConfigurationException;

	/**
	 * Process a single pixel. This is the heart of a filter. This method
	 * should take the given coordinates and write values to the filter's
	 * {@link Cell output fields}. Optionally, data may be read from
	 * {@link PixelSource input fields}.
	 *
	 * @param outputCoords The coordinates to generate a value for, in pixel
	 * space. E.g. the first pixel ranges across coordinages 0.0 - 1.0 in all
	 * dimensions. If this filter is being used to populate an output variable
	 * (i.e. if the user has chosen to connect it to a file on disk), the query
	 * engine will ask for values centred on each pixel (i.e. with an offset
	 * 0.5). When called by another filter, some other offset may be given.
	 * @throws IOException If the inputs can not be read from.
	 */
	void kernel(VectorReal outputCoords) throws IOException;
}
