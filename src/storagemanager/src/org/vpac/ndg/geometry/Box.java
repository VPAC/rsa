package org.vpac.ndg.geometry;


/**
 * A simple bounding box defined by two points. This is only suitable when the
 * box will be aligned to the coordinate system.
 * @author adfries
 * @author hsumanto
 */
public class Box {
	private double xMin;
	private double xMax;
	private double yMax;
	private double yMin;

	/**
	 * Create an infinitely small bounding box around a point.
	 * @param p The point that this bounding box should contain.
	 */
	public Box(Point<Double> p) {
		setXMin(p.getX());
		setXMax(p.getX());
		setYMin(p.getY());
		setYMax(p.getY());
	}

	/**
	 * Create the smallest bounding box that encompasses two points.
	 */
	public Box(double xmin, double ymin, double xmax, double ymax) {
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
	public Box(Point<Double> p1, Point<Double> p2) {
		setXMin(Math.min(p1.getX(), p2.getX()));
		setXMax(Math.max(p1.getX(), p2.getX()));
		setYMin(Math.min(p1.getY(), p2.getY()));
		setYMax(Math.max(p1.getY(), p2.getY()));
	}
	
	/**
	 * Copy constructor.
	 * @param other The box to copy.
	 */
	public Box(Box other) {
		setXMin(other.getXMin());
		setXMax(other.getXMax());
		setYMin(other.getYMin());
		setYMax(other.getYMax());
	}

	/**
	 * Minimally expands this bounding box to enclose a point.
	 * @param p The point that this bounding box should contain.
	 */
	public void union(Point<Double> p) {
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
	public void union(Box other) {
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
	public boolean intersects(Box other) {
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

	/**
	 * Make this bounding box expand uniformly by the given amount.
	 */
	public Box expand(double amount) {
		Point<Double> min = Point.sub(getMin(), amount);
		Point<Double> max = Point.add(getMax(), amount);
		Box res = new Box(min);
		res.union(max);
		return res;
	}

	public double getArea() {
		return getWidth() * getHeight();
	}

	public double getWidth() {
		return getXMax() - getXMin();
	}

	public double getHeight() {
		return getYMax() - getYMin();
	}

	public void setXMin(double xMin) {
		this.xMin = xMin;
	}

	public double getXMin() {
		return xMin;
	}

	public void setXMax(double xMax) {
		this.xMax = xMax;
	}

	public double getXMax() {
		return xMax;
	}

	public void setYMax(double yMax) {
		this.yMax = yMax;
	}

	public double getYMax() {
		return yMax;
	}

	public void setYMin(double yMin) {
		this.yMin = yMin;
	}

	public double getYMin() {
		return yMin;
	}

	public Point<Double> getMin() {
		return new Point<Double>(getXMin(), getYMin());
	}

	public Point<Double> getMax() {
		return new Point<Double>(getXMax(), getYMax());
	}

	/**
	 * Get the upper left corner of the bounding box.
	 * @return Returns the upper left corner of the bounding box.
	 */
	public Point<Double> getUlCorner() {
		Point<Double> ulCorner = new Point<Double>(getXMin(), getYMax());
		return ulCorner;
	}

	/**
	 * Get the lower left corner of the bounding box.
	 * @return Returns the lower left corner of the bounding box.
	 */
	public Point<Double> getLlCorner() {
		Point<Double> llCorner = new Point<Double>(getXMin(), getYMin());
		return llCorner;
	}

	/**
	 * Get the lower left corner of the bounding box.
	 * @return Returns the lower left corner of the bounding box.
	 */
	public Point<Double> getLrCorner() {
		Point<Double> lrCorner = new Point<Double>(getXMax(), getYMin());
		return lrCorner;
	}

	/**
	 * Get the upper right corner of the bounding box.
	 * @return Returns the upper right corner of the bounding box.
	 */
	public Point<Double> getUrCorner() {
		Point<Double> ulCorner = new Point<Double>(getXMax(), getYMax());
		return ulCorner;
	}

	@Override
	public String toString() {
		return String.format("Box(%f %f %f %f)",
				getXMin(), getYMin(), getXMax(), getYMax());
	}
}
