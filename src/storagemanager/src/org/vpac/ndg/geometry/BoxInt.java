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

package org.vpac.ndg.geometry;


/**
 * A simple bounding box defined by two points. This is only suitable when the
 * box will be aligned to the coordinate system.
 * @author adfries
 * @author hsumanto
 */
public class BoxInt {
	private int xMin;
	private int xMax;
	private int yMax;
	private int yMin;

	/**
	 * Create an infinitely small bounding box around a point.
	 * @param p The point that this bounding box should contain.
	 */
	public BoxInt(Point<Integer> p) {
		setXMin(p.getX());
		setXMax(p.getX());
		setYMin(p.getY());
		setYMax(p.getY());
	}

	/**
	 * Create the smallest bounding box that encompasses two points.
	 */
	public BoxInt(int xmin, int ymin, int xmax, int ymax) {
		setXMin(xmin);
		setXMax(xmax);
		setYMin(ymin);
		setYMax(ymax);
	}
	
	/**
	 * Create the smallest bounding box that encompasses two points.
	 * @param p1 The first point.
	 * @param p2 The second point.
	 */
	public BoxInt(Point<Integer> p1, Point<Integer> p2) {
		setXMin(Math.min(p1.getX(), p2.getX()));
		setXMax(Math.max(p1.getX(), p2.getX()));
		setYMin(Math.min(p1.getY(), p2.getY()));
		setYMax(Math.max(p1.getY(), p2.getY()));
	}
	
	/**
	 * Copy constructor.
	 * @param other The box to copy.
	 */
	public BoxInt(BoxInt other) {
		setXMin(other.getXMin());
		setXMax(other.getXMax());
		setYMin(other.getYMin());
		setYMax(other.getYMax());
	}

	/**
	 * Minimally expands this bounding box to enclose a point.
	 * @param p The point that this bounding box should contain.
	 */
	public void union(Point<Integer> p) {
		if (p.getX() < getXMin())
			setXMin(p.getX());
		if (p.getX() > getXMax())
			setXMax(p.getX());
		if (p.getY() < getYMin())
			setYMin(p.getY());
		if (p.getY() > getYMax())
			setYMax(p.getY());
	}

	/**
	 * Minimally expands this bounding box to enclose another.
	 * @param other The other bounding box that should be enclosed by this one.
	 */
	public void union(BoxInt other) {
		if (other.getXMin() < getXMin())
			setXMin(other.getXMin());
		if (other.getXMax() > getXMax())
			setXMax(other.getXMax());
		if (other.getYMin() < getYMin())
			setYMin(other.getYMin());
		if (other.getYMax() > getYMax())
			setYMax(other.getYMax());
	}

	/**
	 * @param other The other bounding box that should clip this one.
	 */
	public void intersect(BoxInt other) {
		if (other.getXMin() > getXMin())
			setXMin(other.getXMin());
		if (other.getXMax() < getXMax())
			setXMax(other.getXMax());
		if (other.getYMin() > getYMin())
			setYMin(other.getYMin());
		if (other.getYMax() < getYMax())
			setYMax(other.getYMax());
	}

	/**
	 * Check if this box intersects the other box. To return true, the other box
	 * must either:
	 * <ul>
	 * <li>Protrude into this box.</li>
	 * <li>Be wholly contained in this box.</li>
	 * <li>Wholly contain this box.</li>
	 * </ul>
	 * If the two boxes merely touch, this method will return false.
	 * 
	 * @param other
	 *            The other box to check the intersection.
	 * @return true if this box intersects the other box, otherwise false.
	 */
	public boolean intersects(BoxInt other) {
		if (other.getXMin() >= getXMax())
			return false;
		else if (other.getXMax() <= getXMin())
			return false;
		else if (other.getYMin() >= getYMax())
			return false;
		else if (other.getYMax() <= getYMin())
			return false;
		else
			return true;
	}

	public boolean contains(Point<Integer> point) {
		if (point.getX() > getXMax())
			return false;
		else if (point.getX() < getXMin())
			return false;
		else if (point.getY() > getYMax())
			return false;
		else if (point.getY() < getYMin())
			return false;
		else
			return true;
	}

	/**
	 * Make this bounding box expand uniformly by the given amount.
	 */
	public BoxInt expand(int amount) {
		Point<Integer> min = Point.subi(getMin(), amount);
		Point<Integer> max = Point.addi(getMax(), amount);
		BoxInt res = new BoxInt(min);
		res.union(max);
		return res;
	}

	public int getArea() {
		return getWidth() * getHeight();
	}

	public int getWidth() {
		return getXMax() - getXMin();
	}

	public int getHeight() {
		return getYMax() - getYMin();
	}

	public void setXMin(int xMin) {
		this.xMin = xMin;
	}

	public int getXMin() {
		return xMin;
	}

	public void setXMax(int xMax) {
		this.xMax = xMax;
	}

	public int getXMax() {
		return xMax;
	}

	public void setYMax(int yMax) {
		this.yMax = yMax;
	}

	public int getYMax() {
		return yMax;
	}

	public void setYMin(int yMin) {
		this.yMin = yMin;
	}

	public int getYMin() {
		return yMin;
	}

	public Point<Integer> getMin() {
		return new Point<Integer>(getXMin(), getYMin());
	}

	public Point<Integer> getMax() {
		return new Point<Integer>(getXMax(), getYMax());
	}

	/**
	 * Get the upper left corner of the bounding box.
	 * @return Returns the upper left corner of the bounding box.
	 */
	public Point<Integer> getUlCorner() {
		Point<Integer> ulCorner = new Point<Integer>(getXMin(), getYMax());
		return ulCorner;
	}

	/**
	 * Get the lower left corner of the bounding box.
	 * @return Returns the lower left corner of the bounding box.
	 */
	public Point<Integer> getLlCorner() {
		Point<Integer> llCorner = new Point<Integer>(getXMin(), getYMin());
		return llCorner;
	}

	/**
	 * Get the lower left corner of the bounding box.
	 * @return Returns the lower left corner of the bounding box.
	 */
	public Point<Integer> getLrCorner() {
		Point<Integer> lrCorner = new Point<Integer>(getXMax(), getYMin());
		return lrCorner;
	}

	/**
	 * Get the upper right corner of the bounding box.
	 * @return Returns the upper right corner of the bounding box.
	 */
	public Point<Integer> getUrCorner() {
		Point<Integer> ulCorner = new Point<Integer>(getXMax(), getYMax());
		return ulCorner;
	}

	@Override
	public String toString() {
		return String.format("Box(%d %d %d %d)",
				getXMin(), getYMin(), getXMax(), getYMax());
	}
}
