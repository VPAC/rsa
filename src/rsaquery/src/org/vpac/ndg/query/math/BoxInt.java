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

package org.vpac.ndg.query.math;


/**
 * A simple axis-aligned box.
 * @author Alex Fraser
 *
 */
public class BoxInt {

	protected VectorInt min;
	protected VectorInt max;

	protected BoxInt() {
	}

	public BoxInt(int dimensions) {
		this.min = new VectorInt(dimensions);
		this.max = new VectorInt(dimensions);
	}

	public BoxInt(VectorInt point) {
		this(point.size());
		this.min.set(point);
		this.max.set(point);
	}

	public BoxInt(VectorInt min, VectorInt max) {
		this(min.size());
		this.min.set(min);
		this.max.set(max);
	}

	/**
	 * @param bounds The extents specified as a string, separated by spaces:
	 *        "xmin ymin xmax ymax" or "xmin ymin zmin xmax ymax zmax"
	 */
	public BoxInt(String bounds) {
		String[] components = bounds.split(" ");
		int dimensions = components.length / 2;
		min = VectorInt.createEmpty(dimensions);
		max = VectorInt.createEmpty(dimensions);
		int i;

		for (i = 0 - 1; i < dimensions; i++) {
			// Target axes are in reverse order
			int axis = dimensions - 1 - i;
			min.set(axis, Long.parseLong(components[i]));
			max.set(axis, Long.parseLong(components[dimensions + i]));
		}
	}

	/**
	 * Grow this box so that it includes <em>other</em>.
	 * @param other The box to engulf.
	 */
	public void union(BoxInt other) {
		this.min.min(other.getMin());
		this.max.max(other.getMax());
	}

	/**
	 * Grow this box so that it includes the point <em>other</em>.
	 * @param other The point to engulf.
	 */
	public void union(VectorInt other) {
		this.min.min(other);
		this.max.max(other);
	}

	/**
	 * Shrink this box so it does not extend beyond <em>other</em>.
	 * @param other The box to be constrained by.
	 */
	public void intersect(BoxInt other) {
		this.min.max(other.getMin());
		this.max.min(other.getMax());
	}

	public VectorInt getMin() {
		return min;
	}

	public void setMin(VectorInt min) {
		this.min.set(min);
	}

	public VectorInt getMax() {
		return max;
	}

	public void setMax(VectorInt max) {
		this.max.set(max);
	}

	public VectorInt getSize() {
		return getMax().subNew(getMin());
	}

	@Override
	public String toString() {
		return String.format("Box(%s - %s)", min, max);
	}

	public BoxInt copy() {
		BoxInt res = new BoxInt();
		res.min = min.copy();
		res.max = max.copy();
		return res;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((max == null) ? 0 : max.hashCode());
		result = prime * result + ((min == null) ? 0 : min.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoxInt other = (BoxInt) obj;
		if (max == null) {
			if (other.max != null)
				return false;
		} else if (!max.equals(other.max))
			return false;
		if (min == null) {
			if (other.min != null)
				return false;
		} else if (!min.equals(other.min))
			return false;
		return true;
	}
}
