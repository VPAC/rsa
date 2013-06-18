package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.FilterAdapter;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorReal;

/**
 * A pixel source that retries data from a filter.
 * @author Alex Fraser
 */
public class FilteredPixelScalar implements PixelSourceScalar {

	FilterAdapter filter;
	CellScalar source;
	BoxReal bounds;

	public FilteredPixelScalar(FilterAdapter filter, CellScalar source,
			BoxReal bounds) {

		this.filter = filter;
		this.source = source;
		this.bounds = bounds;
	}

	@Override
	public ScalarElement getScalarPixel(VectorReal co) throws IOException {
		filter.invoke(co);
		return source.getScalar();
	}

	@Override
	public Element<?> getPixel(VectorReal co) throws IOException {
		return getScalarPixel(co);
	}

	@Override
	public BoxReal getBounds() {
		return bounds;
	}

	@Override
	public int getRank() {
		return bounds.getRank();
	}

	@Override
	public Prototype getPrototype() {
		return source.getPrototype();
	}

	@Override
	public String toString() {
		return String.format("FilteredPixel(%s.%s)", filter.getName(), source);
	}
}
