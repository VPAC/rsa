
package org.vpac.ndg.query.math;

/**
 * A scalar numeric value. Scalar elements have the special property that they
 * are comparable.
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See elementgen.py.
public interface ScalarElement extends Element<ScalarElement>, Comparable<ScalarElement> {

	/**
	 * The same as {@link Comparable#compareTo(Object)}, but specialised for
	 * integer data.
	 */
	int compareTo(long other);
	/**
	 * The same as {@link Comparable#compareTo(Object)}, but specialised for
	 * real data.
	 */
	int compareTo(double other);

	// CASTING

	/**
	 * @return The value of this element, boxed in its native Number subclass.
	 */
	Number getValue();
	/**
	 * @return The value of this element, cast to byte.
	 */
	byte byteValue();
	/**
	 * @return The value of this element, cast to short.
	 */
	short shortValue();
	/**
	 * @return The value of this element, cast to int.
	 */
	int intValue();
	/**
	 * @return The value of this element, cast to long.
	 */
	long longValue();
	/**
	 * @return The value of this element, cast to float.
	 */
	float floatValue();
	/**
	 * @return The value of this element, cast to double.
	 */
	double doubleValue();

}
