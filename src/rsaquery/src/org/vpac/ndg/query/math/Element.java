
package org.vpac.ndg.query.math;

/**
 * A numeric value.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See Element_gen.py
public interface Element<T extends Element<?>> {
	/**
	 * @return A new copy of this element.
	 */
	T copy();

	/**
	 * @return true if this element contains valid data. Data may be marked as
	 * invalid if it is read from a <em>nodata</em> pixel, or as the result of
	 * failed arithmetic e.g. divide by zero.
	 */
	boolean isValid();
	void setValid(boolean valid);

	ScalarElement[] getComponents();

	// CASTING

	/**
	 * Set this element to the given value. This also marks the element as
	 * valid.
	 * @param value The value to assign to this element. It will be cast to the
	 * same type as this element.
	 * @return A reference to this element (does not create a new instance).
	 */
	T set(byte value);
	/**
	 * Set this element to the given value. This also marks the element as
	 * valid.
	 * @param value The value to assign to this element. It will be cast to the
	 * same type as this element.
	 * @return A reference to this element (does not create a new instance).
	 */
	T set(short value);
	/**
	 * Set this element to the given value. This also marks the element as
	 * valid.
	 * @param value The value to assign to this element. It will be cast to the
	 * same type as this element.
	 * @return A reference to this element (does not create a new instance).
	 */
	T set(int value);
	/**
	 * Set this element to the given value. This also marks the element as
	 * valid.
	 * @param value The value to assign to this element. It will be cast to the
	 * same type as this element.
	 * @return A reference to this element (does not create a new instance).
	 */
	T set(long value);
	/**
	 * Set this element to the given value. This also marks the element as
	 * valid.
	 * @param value The value to assign to this element. It will be cast to the
	 * same type as this element.
	 * @return A reference to this element (does not create a new instance).
	 */
	T set(float value);
	/**
	 * Set this element to the given value. This also marks the element as
	 * valid.
	 * @param value The value to assign to this element. It will be cast to the
	 * same type as this element.
	 * @return A reference to this element (does not create a new instance).
	 */
	T set(double value);
	/**
	 * Set this element to the given value. Validity will be inherited from the
	 * supplied element.
	 * @param value The value to assign to this element. It will be cast to the
	 * same type as this element.
	 * @return A reference to this element (does not create a new instance).
	 */
	T set(Element<?> value);
	/**
	 * Set this element to the given value. This also marks the element as
	 * valid.
	 * @param value The value to assign to this element. It will be cast to the
	 * same type as this element.
	 * @return A reference to this element (does not create a new instance).
	 */
	T set(Number value);

	/**
	 * Reinterpret this element as byte data. Note: this creates a new
	 * instance. It is faster to assign a value to an existing instance; see
	 * {@link #set(Element)}.
	 * @return A new element with the same rank as this one.
	 */
	Element<?> asByte();
	/**
	 * Reinterpret this element as short data. Note: this creates a new
	 * instance. It is faster to assign a value to an existing instance; see
	 * {@link #set(Element)}.
	 * @return A new element with the same rank as this one.
	 */
	Element<?> asShort();
	/**
	 * Reinterpret this element as int data. Note: this creates a new
	 * instance. It is faster to assign a value to an existing instance; see
	 * {@link #set(Element)}.
	 * @return A new element with the same rank as this one.
	 */
	Element<?> asInt();
	/**
	 * Reinterpret this element as long data. Note: this creates a new
	 * instance. It is faster to assign a value to an existing instance; see
	 * {@link #set(Element)}.
	 * @return A new element with the same rank as this one.
	 */
	Element<?> asLong();
	/**
	 * Reinterpret this element as float data. Note: this creates a new
	 * instance. It is faster to assign a value to an existing instance; see
	 * {@link #set(Element)}.
	 * @return A new element with the same rank as this one.
	 */
	Element<?> asFloat();
	/**
	 * Reinterpret this element as double data. Note: this creates a new
	 * instance. It is faster to assign a value to an existing instance; see
	 * {@link #set(Element)}.
	 * @return A new element with the same rank as this one.
	 */
	Element<?> asDouble();

	// ARITHMETIC

	/**
	 * Add this object to a value.
	 *
	 * @param other The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T add(long other);
	/**
	 * Add this object to a value.
	 *
	 * @param other The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T add(double other);
	/**
	 * Add this object to a value.
	 *
	 * @param other The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T add(Element<?> other);

	/**
	 * Add this object to a value.
	 *
	 * @param other The value to add.
	 * @return The result as a new object.
	 */
	T addNew(long other);
	/**
	 * Add this object to a value.
	 *
	 * @param other The value to add.
	 * @return The result as a new object.
	 */
	T addNew(double other);
	/**
	 * Add this object to a value.
	 *
	 * @param other The value to add.
	 * @return The result as a new object.
	 */
	T addNew(Element<?> other);

	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add.
	 * @param b The value to add to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(long a, long b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add.
	 * @param b The value to add to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(double a, long b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add.
	 * @param b The value to add to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(long a, double b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add.
	 * @param b The value to add to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(double a, double b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add.
	 * @param b The value to add to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(Element<?> a, long b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add.
	 * @param b The value to add to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(long a, Element<?> b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add.
	 * @param b The value to add to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(Element<?> a, double b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add.
	 * @param b The value to add to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(double a, Element<?> b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add.
	 * @param b The value to add to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(Element<?> a, Element<?> b);

	/**
	 * Subtract this object from a value.
	 *
	 * @param other The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T sub(long other);
	/**
	 * Subtract this object from a value.
	 *
	 * @param other The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T sub(double other);
	/**
	 * Subtract this object from a value.
	 *
	 * @param other The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T sub(Element<?> other);

	/**
	 * Subtract this object from a value.
	 *
	 * @param other The value to subtract.
	 * @return The result as a new object.
	 */
	T subNew(long other);
	/**
	 * Subtract this object from a value.
	 *
	 * @param other The value to subtract.
	 * @return The result as a new object.
	 */
	T subNew(double other);
	/**
	 * Subtract this object from a value.
	 *
	 * @param other The value to subtract.
	 * @return The result as a new object.
	 */
	T subNew(Element<?> other);

	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract.
	 * @param b The value to subtract from.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(long a, long b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract.
	 * @param b The value to subtract from.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(double a, long b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract.
	 * @param b The value to subtract from.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(long a, double b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract.
	 * @param b The value to subtract from.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(double a, double b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract.
	 * @param b The value to subtract from.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(Element<?> a, long b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract.
	 * @param b The value to subtract from.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(long a, Element<?> b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract.
	 * @param b The value to subtract from.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(Element<?> a, double b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract.
	 * @param b The value to subtract from.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(double a, Element<?> b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract.
	 * @param b The value to subtract from.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(Element<?> a, Element<?> b);

	/**
	 * Multiply this object with a value.
	 *
	 * @param other The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mul(long other);
	/**
	 * Multiply this object with a value.
	 *
	 * @param other The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mul(double other);
	/**
	 * Multiply this object with a value.
	 *
	 * @param other The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mul(Element<?> other);

	/**
	 * Multiply this object with a value.
	 *
	 * @param other The value to multiply.
	 * @return The result as a new object.
	 */
	T mulNew(long other);
	/**
	 * Multiply this object with a value.
	 *
	 * @param other The value to multiply.
	 * @return The result as a new object.
	 */
	T mulNew(double other);
	/**
	 * Multiply this object with a value.
	 *
	 * @param other The value to multiply.
	 * @return The result as a new object.
	 */
	T mulNew(Element<?> other);

	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply.
	 * @param b The value to multiply with.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(long a, long b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply.
	 * @param b The value to multiply with.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(double a, long b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply.
	 * @param b The value to multiply with.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(long a, double b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply.
	 * @param b The value to multiply with.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(double a, double b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply.
	 * @param b The value to multiply with.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(Element<?> a, long b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply.
	 * @param b The value to multiply with.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(long a, Element<?> b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply.
	 * @param b The value to multiply with.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(Element<?> a, double b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply.
	 * @param b The value to multiply with.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(double a, Element<?> b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply.
	 * @param b The value to multiply with.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(Element<?> a, Element<?> b);

	/**
	 * Divide this object by a value.
	 *
	 * @param other The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T div(long other);
	/**
	 * Divide this object by a value.
	 *
	 * @param other The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T div(double other);
	/**
	 * Divide this object by a value.
	 *
	 * @param other The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T div(Element<?> other);

	/**
	 * Divide this object by a value.
	 *
	 * @param other The value to divide.
	 * @return The result as a new object.
	 */
	T divNew(long other);
	/**
	 * Divide this object by a value.
	 *
	 * @param other The value to divide.
	 * @return The result as a new object.
	 */
	T divNew(double other);
	/**
	 * Divide this object by a value.
	 *
	 * @param other The value to divide.
	 * @return The result as a new object.
	 */
	T divNew(Element<?> other);

	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide.
	 * @param b The value to divide by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(long a, long b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide.
	 * @param b The value to divide by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(double a, long b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide.
	 * @param b The value to divide by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(long a, double b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide.
	 * @param b The value to divide by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(double a, double b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide.
	 * @param b The value to divide by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(Element<?> a, long b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide.
	 * @param b The value to divide by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(long a, Element<?> b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide.
	 * @param b The value to divide by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(Element<?> a, double b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide.
	 * @param b The value to divide by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(double a, Element<?> b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide.
	 * @param b The value to divide by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(Element<?> a, Element<?> b);

	/**
	 * Modulo this object by a value.
	 *
	 * @param other The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mod(long other);
	/**
	 * Modulo this object by a value.
	 *
	 * @param other The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mod(double other);
	/**
	 * Modulo this object by a value.
	 *
	 * @param other The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mod(Element<?> other);

	/**
	 * Modulo this object by a value.
	 *
	 * @param other The value to modulo.
	 * @return The result as a new object.
	 */
	T modNew(long other);
	/**
	 * Modulo this object by a value.
	 *
	 * @param other The value to modulo.
	 * @return The result as a new object.
	 */
	T modNew(double other);
	/**
	 * Modulo this object by a value.
	 *
	 * @param other The value to modulo.
	 * @return The result as a new object.
	 */
	T modNew(Element<?> other);

	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo.
	 * @param b The value to modulo by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(long a, long b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo.
	 * @param b The value to modulo by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(double a, long b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo.
	 * @param b The value to modulo by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(long a, double b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo.
	 * @param b The value to modulo by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(double a, double b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo.
	 * @param b The value to modulo by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(Element<?> a, long b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo.
	 * @param b The value to modulo by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(long a, Element<?> b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo.
	 * @param b The value to modulo by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(Element<?> a, double b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo.
	 * @param b The value to modulo by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(double a, Element<?> b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo.
	 * @param b The value to modulo by.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(Element<?> a, Element<?> b);

	// BOUNDING

	/**
	 * Find the minimum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T min(long other);
	/**
	 * Find the minimum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T min(double other);
	/**
	 * Find the minimum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T min(Element<?> other);

	/**
	 * Find the minimum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return The result as a new object.
	 */
	T minNew(long other);
	/**
	 * Find the minimum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return The result as a new object.
	 */
	T minNew(double other);
	/**
	 * Find the minimum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return The result as a new object.
	 */
	T minNew(Element<?> other);

	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOf(long a, long b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOf(double a, long b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOf(long a, double b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOf(double a, double b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOf(Element<?> a, long b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOf(long a, Element<?> b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOf(Element<?> a, double b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOf(double a, Element<?> b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOf(Element<?> a, Element<?> b);

	/**
	 * Find the maximum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T max(long other);
	/**
	 * Find the maximum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T max(double other);
	/**
	 * Find the maximum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T max(Element<?> other);

	/**
	 * Find the maximum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return The result as a new object.
	 */
	T maxNew(long other);
	/**
	 * Find the maximum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return The result as a new object.
	 */
	T maxNew(double other);
	/**
	 * Find the maximum of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return The result as a new object.
	 */
	T maxNew(Element<?> other);

	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOf(long a, long b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOf(double a, long b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOf(long a, double b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOf(double a, double b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOf(Element<?> a, long b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOf(long a, Element<?> b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOf(Element<?> a, double b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOf(double a, Element<?> b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOf(Element<?> a, Element<?> b);

	/**
	 * Constrain this value in a pair of bounds.
	 *
	 * @param min The lower bound.
	 * @param max The upper bound. This should be greater than or equal to the
	 *        lower bound.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T clamp(long min, long max);
	/**
	 * Constrain this value in a pair of bounds.
	 *
	 * @param min The lower bound.
	 * @param max The upper bound. This should be greater than or equal to the
	 *        lower bound.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T clamp(double min, double max);
	/**
	 * Constrain this value in a pair of bounds.
	 *
	 * @param min The lower bound.
	 * @param max The upper bound. This should be greater than or equal to the
	 *        lower bound.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T clamp(Element<?> min, Element<?> max);

	/**
	 * Constrain this value in a pair of bounds.
	 *
	 * @param min The lower bound.
	 * @param max The upper bound. This should be greater than or equal to the
	 *        lower bound.
	 * @return The result as a new object.
	 */
	T clampNew(long min, long max);
	/**
	 * Constrain this value in a pair of bounds.
	 *
	 * @param min The lower bound.
	 * @param max The upper bound. This should be greater than or equal to the
	 *        lower bound.
	 * @return The result as a new object.
	 */
	T clampNew(double min, double max);
	/**
	 * Constrain this value in a pair of bounds.
	 *
	 * @param min The lower bound.
	 * @param max The upper bound. This should be greater than or equal to the
	 *        lower bound.
	 * @return The result as a new object.
	 */
	T clampNew(Element<?> min, Element<?> max);

}
