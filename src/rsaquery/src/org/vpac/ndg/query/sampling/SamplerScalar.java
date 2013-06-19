package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.Diagnostics;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.VariableAdapter;
import org.vpac.ndg.query.coordinates.QueryCoordinateSystem;
import org.vpac.ndg.query.coordinates.Warp;
import org.vpac.ndg.query.coordinates.WarpIdentity;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Page.LocalPage;


/**
 * A pixel source that reads data from a variable.
 * @author Alex Fraser
 */
public class SamplerScalar implements PixelSourceScalar, Diagnostics {

	final Logger log = LoggerFactory.getLogger(SamplerScalar.class);

	// NOTE: the page cache may be shared with other samplers on different
	// threads. The page on the other hand is local to this thread - although it
	// may be backed by a shared page.
	protected PageCache pageCache;
	protected LocalPage page;

	protected String name;
	protected Warp warp;
	protected SampleStrategy sampleStrategy;
	protected BoxReal effectiveBounds;
	protected Prototype prototype;

	private VectorReal internalCoords;
	private VectorInt cellCoords;


	public SamplerScalar(VariableAdapter band, QueryCoordinateSystem context)
			throws QueryConfigurationException {
		name = band.getName();
		pageCache = band.getPageCache();
		warp = new WarpIdentity();
		sampleStrategy = new SampleStrategyExtend(band.getShape());
		effectiveBounds = band.calculateBounds(context);
		prototype = band.getPrototype().copy();

		// These are set in readData; they will be null until a page fault
		// occurs.
		page = null;
		internalCoords = VectorReal.createEmpty(band.getRank());
		cellCoords = VectorInt.createEmpty(band.getRank());
	}

	@Override
	public ScalarElement getScalarPixel(VectorReal co) throws IOException,
			IndexOutOfBoundsException {

		internalCoords.set(co);
		warp.warp(internalCoords);
		return sampleStrategy.get(this, internalCoords);
	}

	@Override
	public Element<?> getPixel(VectorReal co) throws IOException {
		return getScalarPixel(co);
	}

	/**
	 * @param coordinates
	 *            The location in the array to retrieve data from.
	 * @return The value at the specified coordinates.
	 * @throws IOException
	 *             If the file could not be read.
	 */
	public ScalarElement reallyGet(VectorReal coordinates) throws IOException {
		cellCoords.set(coordinates);
		try {
			return page.getPixel(cellCoords);
		} catch (IndexOutOfBoundsException e) {
			// page fault
		} catch (NullPointerException e) {
			// page fault
		}
		page = pageCache.getPage(cellCoords).getLocalPage();
		try {
			return page.getPixel(cellCoords);
		} catch (IndexOutOfBoundsException e) {
			log.error("Failed to read from page {}.", page);
			log.error("Cell coordinates: {}", cellCoords);
			throw e;
		}
	}

	/**
	 * @param warp The coordinate transform to use when sampling data.
	 */
	public void setWarp(Warp warp) {
		this.warp = warp;
	}

	@Override
	public BoxReal getBounds() {
		return effectiveBounds;
	}

	@Override
	public int getRank() {
		return effectiveBounds.getRank();
	}

	@Override
	public Prototype getPrototype() {
		return prototype;
	}

	public Warp getWarp() {
		return warp;
	}

	public PageCache getPageCache() {
		return pageCache;
	}

	@Override
	public String toString() {
		return String.format("Sampler(%s)", name);
	}

	@Override
	public void diagnostics() {
		pageCache.diagnostics();
	}
}
