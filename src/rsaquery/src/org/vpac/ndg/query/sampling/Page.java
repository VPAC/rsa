/*
 * This file is part of SpatialCube.
 *
 * SpatialCube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SpatialCube is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SpatialCube.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.VariableAdapter;
import org.vpac.ndg.query.coordinates.HasShape;
import org.vpac.ndg.query.math.BoxInt;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorInt;

import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Section;

/**
 * Gives access to a chunk of data; a <em>page</em> in a similar sense to pages
 * in virtual memory.
 *
 * @author Alex Fraser
 */
public class Page implements HasShape {
	final Logger log = LoggerFactory.getLogger(Page.class);

	private BoxInt bounds;
	private ArrayAdapter data;
	private VariableAdapter band;

	/**
	 * A thread-safe adapter for {@link Page Pages}.
	 * @author Alex Fraser
	 */
	public class LocalPage {
		Page parent;
		Index ima;
		VectorInt cellCoords;

		public LocalPage(Page parent) {
			this.parent = parent;
			VectorInt shape = parent.getShape();
			ima = Index.factory(shape.asIntArray());
			cellCoords = VectorInt.createEmpty(shape.size());
		}

		/**
		 * @param co The coordinates of the pixel in global cell-space. It will
		 *        not be modified.
		 * @return The value of the requested pixel.
		 */
		public ScalarElement getPixel(final VectorInt co) {
			return parent.getPixel(co, cellCoords, ima);
		}
	}

	/**
	 * Create a new page.
	 *
	 * @param bounds The bounds to restrict this page to, in cell (index) space
	 *        of <em>band</em>.
	 * @param band The base variable to read from.
	 */
	public Page(BoxInt bounds, VariableAdapter band) {
		this.bounds = bounds;
		this.band = band;
		data = null;
	}

	public Page(VectorInt min, VectorInt max, VariableAdapter band) {
		this(new BoxInt(min, max), band);
	}

	/**
	 * @return A thread-safe interface to this page.
	 */
	public LocalPage getLocalPage() {
		return new LocalPage(this);
	}

	/**
	 * Get a value out of the page. This method is thread-safe as long as the
	 * arguments are not shared between threads.
	 *
	 * @param co The coordinates of the pixel in global cell-space. It will not
	 *        be modified.
	 * @param cellCoords Storage for co to be transformed into local
	 *        coordinates.
	 * @param ima Storage for cellCoords to be used to read from the array.
	 * @return The value of the requested pixel.
	 */
	protected ScalarElement getPixel(VectorInt co, VectorInt cellCoords, Index ima) {
		cellCoords.subOf(co, bounds.getMin());
		cellCoords.fillIndex(ima);
		return data.get(ima);
	}

	@Override
	public VectorInt getShape() {
		return bounds.getSize();
	}

	@Override
	public int getRank() {
		return band.getRank();
	}

	/**
	 * Read from the underlying image into memory. This method is not
	 * thread-safe.
	 *
	 * @throws IOException If the requested bounds are not within the underlying
	 *         image, or if the resource can't be read.
	 */
	public void read() throws IOException {
		try {
			Section section;
			section = new Section(
					bounds.getMin().asIntArray(),
					bounds.getSize().asIntArray());
			Array array = band.read(section);
			log.trace("Reading data from {}: {}.", band, section);
			data = ArrayAdapterImpl.createAndPromote(array, band.getDataType(),
					band.getNodataStrategy());

		} catch (InvalidRangeException e) {
			throw new IOException("Could not read array: Invalid section.", e);
		} catch (QueryConfigurationException e) {
			throw new IOException("Could not read array: congfiguration error.", e);
		}
	}

	@Override
	public String toString() {
		return String.format("Page(%s, %s)", band, bounds);
	}

}