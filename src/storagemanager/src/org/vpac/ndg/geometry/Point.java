package org.vpac.ndg.geometry;

import java.util.Objects;

public class Point<T extends Number> {
	protected T x;
	protected T y;

	public Point() {
	}

	/**
	 * Copy constructor.
	 * @param other The point to duplicate.
	 */
	public Point(Point<T> other) {
		setX(other.getX());
		setY(other.getY());
	}

	/**
	 * Create a new point with the specified values.
	 * @param x The x component.
	 * @param y The y component.
	 */
	public Point(T x, T y) {
		setX(x);
		setY(y);
	}

	/**
	 * Create a new point from the specified values.
	 * @param arr An array with two values: {x, y}.
	 */
	public Point(T[] arr) {
		setX(arr[0]);
		setY(arr[1]);
	}

	/**
	 * Set all components to match those of another point.
	 * @param other The point to copy.
	 */
	public void set(Point<T> other) {
		setX(other.getX());
		setY(other.getY());
	}

	public void setX(T x) {
		this.x = x;
	}

	public T getX() {
		return x;
	}

	public void setY(T y) {
		this.y = y;
	}

	public T getY() {
		return y;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", 
				getX().toString(), 
				getY().toString());
	}

	@Override
	public boolean equals(Object x) {
		if (!(x instanceof Point))
			return false;
		else {
			Point<?> that = (Point<?>) x;
			return Objects.equals(this.x, that.x)
					&& Objects.equals(this.y, that.y);
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}


	//
	// ARITHMETIC
	//

	/**
	 * Performs component-wise addition of two points.
	 */
	public static Point<Integer> addi(Point<Integer> a, Point<Integer> b) {
		return new Point<Integer>(a.x + b.x, a.y + b.y);
	}
	public static Point<Integer> addi(Point<Integer> a, int b) {
		return new Point<Integer>(a.x + b, a.y + b);
	}

	/**
	 * Performs component-wise subtraction of two points.
	 */
	public static Point<Integer> subi(Point<Integer> a, Point<Integer> b) {
		return new Point<Integer>(a.x - b.x, a.y - b.y);
	}
	public static Point<Integer> subi(Point<Integer> a, int b) {
		return new Point<Integer>(a.x - b, a.y - b);
	}

	/**
	 * Performs component-wise multiplication of two points.
	 */
	public static Point<Integer> muli(Point<Integer> a, Point<Integer> b) {
		return new Point<Integer>(a.x * b.x, a.y * b.y);
	}

	/**
	 * Multiplies both components of a point by a scalar.
	 */
	public static Point<Integer> muli(Point<Integer> a, Integer b) {
		return new Point<Integer>(a.x * b, a.y * b);
	}

	/**
	 * Performs component-wise division of two points.
	 */
	public static Point<Integer> divi(Point<Integer> a, Point<Integer> b) {
		return new Point<Integer>(a.x / b.x, a.y / b.y);
	}

	/**
	 * Divides both components of a point by a scalar.
	 */
	public static Point<Integer> divi(Point<Integer> a, Integer b) {
		return new Point<Integer>(a.x / b, a.y / b);
	}

	/**
	 * Performs component-wise addition of two points.
	 */
	public static Point<Double> add(Point<Double> a, Point<Double> b) {
		return new Point<Double>(a.x + b.x, a.y + b.y);
	}

	/**
	 * Sums both components with a scalar.
	 */
	public static Point<Double> add(Point<Double> a, double b) {
		return new Point<Double>(a.x + b, a.y + b);
	}

	/**
	 * Performs component-wise subtraction of two points.
	 */
	public static Point<Double> sub(Point<Double> a, Point<Double> b) {
		return new Point<Double>(a.x - b.x, a.y - b.y);
	}

	/**
	 * Subtracts a scalar from both components.
	 */
	public static Point<Double> sub(Point<Double> a, double b) {
		return new Point<Double>(a.x - b, a.y - b);
	}

	/**
	 * Performs component-wise multiplication of two points.
	 */
	public static Point<Double> mul(Point<Double> a, Point<Double> b) {
		return new Point<Double>(a.x * b.x, a.y * b.y);
	}

	/**
	 * Multiplies both components of a point by a scalar.
	 */
	public static Point<Double> mul(Point<Double> a, Double b) {
		return new Point<Double>(a.x * b, a.y * b);
	}

	/**
	 * Performs component-wise division of two points.
	 */
	public static Point<Double> div(Point<Double> a, Point<Double> b) {
		return new Point<Double>(a.x / b.x, a.y / b.y);
	}

	/**
	 * Divides both components of a point by a scalar.
	 */
	public static Point<Double> div(Point<Double> a, Double b) {
		return new Point<Double>(a.x / b, a.y / b);
	}

	public static double magnitude(Point<Double> a) {
		return Math.sqrt(Math.pow(a.x, 2.0) + Math.pow(a.y, 2.0));
	}

}
