package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Defines a regular grid of data. A grid may have more than 2 dimensions, but
 * irregular axes (e.g. time) should not be included.
 * @author Alex Fraser
 */
public class Grid implements HasRank, HasShape {

	private VectorReal resolution;
	private BoxReal bounds;
	private VectorInt shape;

	public Grid(BoxReal bounds, VectorReal resolution) {
		this.bounds = bounds;
		this.resolution = resolution;
		shape = bounds.getSize().divNew(resolution).toInt().add(1);
	}

	/**
	 * @return The resolution of the grid in the same coordinate system
	 *         as getSrs().
	 */
	public VectorReal getResolution() {
		return resolution;
	}

	public void setResolution(VectorReal resolution) {
		this.resolution = resolution;
	}

	/**
	 * @return The extents of this dataset's grid in the same coordinate system
	 *         as getSrs().
	 */
	public BoxReal getBounds() {
		return bounds;
	}

	public void setBounds(BoxReal bounds) {
		this.bounds = bounds;
	}

	@Override
	public VectorInt getShape() {
		return shape;
	}

	@Override
	public int getRank() {
		return bounds.getRank();
	}

	@Override
	public String toString() {
		return String.format("Grid(%s / %s)", bounds, resolution);
	}

}
