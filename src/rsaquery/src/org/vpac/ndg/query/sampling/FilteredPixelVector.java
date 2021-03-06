/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.FilterAdapter;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorElement;
import org.vpac.ndg.query.math.VectorReal;

/**
 * A pixel source that retries data from a filter.
 * @author Alex Fraser
 */
public class FilteredPixelVector implements PixelSourceVector {

	FilterAdapter filter;
	CellVector source;
	BoxReal bounds;

	public FilteredPixelVector(FilterAdapter filter, CellVector source,
			BoxReal bounds) {

		this.filter = filter;
		this.source = source;
		this.bounds = bounds;
	}

	@Override
	public VectorElement getVectorPixel(VectorReal co) throws IOException {
		filter.invoke(co);
		return source.getVector();
	}

	@Override
	public Element<?> getPixel(VectorReal co) throws IOException {
		return getVectorPixel(co);
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
