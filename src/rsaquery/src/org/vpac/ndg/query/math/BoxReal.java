package org.vpac.ndg.query.math;

import org.vpac.ndg.query.coordinates.HasRank;


/**
 * A simple axis-aligned box.
 * @author Alex Fraser
 *
 */
public class BoxReal implements HasRank {

	protected VectorReal min;
	protected VectorReal max;

	protected BoxReal() {
	}

	public BoxReal(int dimensions) {
		this.min = new VectorReal(dimensions);
		this.max = new VectorReal(dimensions);
	}

	public BoxReal(VectorReal point) {
		this(point.size());
		this.min.set(point);
		this.max.set(point);
	}

	public BoxReal(VectorReal min, VectorReal max) {
		this(min.size());
		this.min.set(min);
		this.max.set(max);
	}

	/**
	 * @param bounds The extents specified as a string, separated by spaces:
	 *        "xmin ymin xmax ymax" or "xmin ymin zmin xmax ymax zmax"
	 */
	public BoxReal(String bounds) {
		String[] components = bounds.split(" ");
		int dimensions = components.length / 2;
		min = VectorReal.createEmpty(dimensions);
		max = VectorReal.createEmpty(dimensions);
		int i;

		for (i = 0; i < dimensions; i++) {
			// Target axes are in reverse order
			int axis = dimensions - 1 - i;
			min.set(axis, Double.parseDouble(components[i]));
			max.set(axis, Double.parseDouble(components[dimensions + i]));
		}
	}

	/**
	 * Grow this box so that it includes <em>other</em>.
	 * @param other The box to engulf.
	 */
	public void union(BoxReal other) {
		this.min.min(other.getMin());
		this.max.max(other.getMax());
	}

	/**
	 * Grow this box so that it includes the point <em>other</em>.
	 * @param other The point to engulf.
	 */
	public void union(VectorReal other) {
		this.min.min(other);
		this.max.max(other);
	}

	/**
	 * Shrink this box so it does not extend beyond <em>other</em>.
	 * @param other The box to be constrained by.
	 */
	public void intersect(BoxReal other) {
		this.min.max(other.getMin());
		this.max.min(other.getMax());
	}

	public VectorReal getMin() {
		return min;
	}

	public void setMin(VectorReal min) {
		this.min.set(min);
	}

	public VectorReal getMax() {
		return max;
	}

	public void setMax(VectorReal max) {
		this.max.set(max);
	}

	public VectorReal getSize() {
		return getMax().subNew(getMin());
	}

	@Override
	public String toString() {
		return String.format("Box(%s - %s)", min, max);
	}

	public BoxReal copy() {
		BoxReal res = new BoxReal();
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
		BoxReal other = (BoxReal) obj;
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

	@Override
	public int getRank() {
		return min.size();
	}
}
