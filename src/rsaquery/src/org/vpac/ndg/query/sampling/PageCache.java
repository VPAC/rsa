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
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.Diagnostics;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.QueryDefinition.CacheDefinition;
import org.vpac.ndg.query.VariableAdapter;
import org.vpac.ndg.query.math.VectorInt;


/**
 * Caches chunks of image data. This is similar to virtual memory paging. Note
 * that this doesn't implement PixelSource on purpose: a pixel source is
 * expected to be used frequently, while this should be used infrequently:
 * fetching objects from the cache is still relatively expensive.
 * @author Alex Fraser
 */
public class PageCache implements Diagnostics {

	final Logger log = LoggerFactory.getLogger(PageCache.class);

	static final int TILE_SIZE = 256;

	private static final int DEFAULT_MARGIN = 0;
	private static final int DEFAULT_CAPACITY = 10;
	private static final long DEFAULT_VOLUME = 200000;

	private VariableAdapter band;
	private VectorInt pageShape;
	private VectorInt index;
	private VectorInt margin;

	/**
	 * This is a least-recently-used, limited size map. The key is a page (tile)
	 * index, and pages are positioned on a grid; therefore, this is a so-called
	 * spatial hash.
	 */
	private CacheLru<VectorInt, Page> pages;

	long hardFaults;
	long softFaults;



	/**
	 * Create a new page cache.
	 * @param band The variable to manage pages for.
	 */
	public PageCache(VariableAdapter band) {

		this.band = band;
		index = VectorInt.createEmpty(band.getRank());
	}

	public synchronized void configure(CacheDefinition cd)
			throws QueryConfigurationException {

		if (cd == null) {
			configure();
			return;
		}

		log.debug("Configuring page cache for {}", band);

		// Configure cache
		int npages;
		if (cd.pages != null) {
			if (cd.pages > 0) {
				npages = cd.pages;
			} else {
				log.warn("Invalid cache capacity {} pages. Defaulting to 1.",
						cd.pages);
				npages = 1;
			}
		} else {
			npages = DEFAULT_CAPACITY;
		}
		pages = new CacheLru<VectorInt, Page>(npages);

		// Find minimum sampling window; unspecified axes default to 1. Set the
		// page margin to half the sampling window.
		VectorInt window = findWindow(cd);
		margin = window.divNew(2);

		int[] dimensionPrecedence = resolveDimensionPrecedence(cd);

		// Configure tiling strategy
		long volume;
		if (cd.volume != null)
			volume = cd.volume;
		else
			volume = DEFAULT_VOLUME;
		TilingStrategyCustom ts = new TilingStrategyCustom(volume);
		ts.setPrecedence(dimensionPrecedence);
		ts.setWindow(window);

		pageShape = ts.getTileShape(band.getShape());

		log.debug("Pages: {}", pages.getCapacity());
		log.debug("Page shape: {}", pageShape);
		log.debug("Margin: {}", margin);
	}

	private synchronized void configure() {
		log.debug("Configuring page cache for {}", band);

		pages = new CacheLru<VectorInt, Page>(DEFAULT_CAPACITY);
		TilingStrategy ts = new TilingStrategyStride(DEFAULT_VOLUME);
		pageShape = ts.getTileShape(band.getShape());
		margin = VectorInt.createEmpty(band.getRank(), DEFAULT_MARGIN);

		log.debug("Pages: {}", pages.getCapacity());
		log.debug("Page shape: {}", pageShape);
		log.debug("Margin: {}", margin);
	}

	/**
	 * @return Dimension indices in the preferred order.
	 */
	private int[] resolveDimensionPrecedence(CacheDefinition cd) {
		int[] dimensionPrecedence;
		if (cd.precedence != null) {
			String[] dims = cd.precedence.split(" ");
			dimensionPrecedence = new int[dims.length];
			int i = 0;
			for (String dim : dims) {
				int dimIndex = band.findDimensionIndex(dim);
				if (dimIndex < 0) {
					log.debug("Skipping unknown dimension {}", dim);
					continue;
				}
				dimensionPrecedence[i] = dimIndex;
				i++;
			}
			// Some items may have been excluded above; copy only valid items to
			// a new array.
			dimensionPrecedence = Arrays.copyOf(dimensionPrecedence, i);
		} else {
			dimensionPrecedence = new int[0];
		}
		return dimensionPrecedence;
	}

	/**
	 * @param cd The cache definition.
	 * @return The typical sampling window. Unspecified axes will be set to 1.
	 */
	private VectorInt findWindow(CacheDefinition cd)
			throws QueryConfigurationException {
		VectorInt window = VectorInt.createEmpty(band.getRank(), 1);
		if (cd.window == null)
			return window;

		// Try to match available or specified axes with specified lengths.
		String[] lengths = cd.window.split(" ");
		String[] axes;
		if (cd.windowAxes != null)
			axes = cd.windowAxes.split(" ");
		else
			axes = band.getDimensionsString().split(" ");

		if (axes.length > lengths.length) {
			int start = axes.length - lengths.length;
			int end = axes.length;
			axes = Arrays.copyOfRange(axes, start, end);
		} else if (axes.length < lengths.length) {
			int start = lengths.length - axes.length;
			int end = lengths.length;
			lengths = Arrays.copyOfRange(lengths, start, end);
		}
		log.debug("Using window axes {} with lengths {}", axes, lengths);

		// Adjust window shape based on specified lengths.
		for (int i = 0; i < lengths.length; i++) {
			int j = band.findDimensionIndex(axes[i]);
			try {
				int length = Integer.parseInt(lengths[i]);
				window.set(j, length);
			} catch (IndexOutOfBoundsException e) {
				log.debug("Skipping missing dimension {}", axes[i]);
			} catch (NumberFormatException e) {
				throw new QueryConfigurationException(String.format("Cache " +
						"specified invalid window shape %s", lengths[i]), e);
			}
		}

		return window;
	}

	/**
	 * Gets a page (tile) of data. This method is thread-safe.
	 * 
	 * @param co The coordinates that the page should contain.
	 * @return The page. If an existing page exists that contains the requested
	 *         coordinates, it will be returned. Otherwise a new page will be
	 *         created.
	 * @throws IOException If the coordinates are out of bounds, or if the file
	 *         can not be read.
	 */
	public synchronized Page getPage(VectorInt co) throws IOException {
		if (pageShape == null)
			configure();

		softFaults++;

		index.divOf(co, pageShape);

		Page res = pages.get(index);
		if (res == null) {
			res = newPage(index);
			// The index must be copied here, or all pages will have the same
			// key.
			pages.put(index.copy(), res);
		}
		return res;
	}

	protected Page newPage(VectorInt index) throws IOException {
		hardFaults++;

		log.trace("Hard fault: loading page {} for band {}", index, band);

		VectorInt origin = index.mulNew(pageShape);
		VectorInt end = origin.addNew(pageShape);

		origin.sub(margin);
		origin.max(0);

		end.add(margin);
		end.min(band.getShape());

		Page page = new Page(origin, end, band);
		page.read();
		return page;
	}

	@Override
	public String toString() {
		return String.format("PageCache(%s, %d/%d)", band, hardFaults, softFaults);
	}

	@Override
	public void diagnostics() {
		String diagnostics = String.format(
				"PageCache(%s): %d hard fault(s), %d soft fault(s).", band,
				hardFaults, softFaults);
		log.debug(diagnostics);
	}
}
