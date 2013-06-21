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

// THIS IS GENERATED CODE. Do not modify this file. See Element_gen.py

package org.vpac.ndg.query.math;

/**
 * A numeric value.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
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
	void setValid(Element<?> mask);
	void setValidIfValid(Element<?> mask);

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
	 * Add a value to this one.
	 *
	 * @param other The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T add(long other);
	/**
	 * Add a value to this one.
	 *
	 * @param other The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T add(double other);
	/**
	 * Add a value to this one.
	 *
	 * @param other The value to add. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T add(Element<?> other);

	/**
	 * Add a value to this one, unless the mask is invalid.
	 *
	 * @param other The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addIfValid(long other, Element<?> mask);
	/**
	 * Add a value to this one, unless the mask is invalid.
	 *
	 * @param other The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addIfValid(double other, Element<?> mask);
	/**
	 * Add a value to this one, unless it is invalid.
	 *
	 * @param other The value to add. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addIfValid(Element<?> other);
	/**
	 * Add a value to this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to add. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addIfValid(Element<?> other, Element<?> mask);

	/**
	 * Add a value to this one.
	 *
	 * @param other The value to add.
	 * @return The result as a new object.
	 */
	T addNew(long other);
	/**
	 * Add a value to this one.
	 *
	 * @param other The value to add. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T addNew(double other);
	/**
	 * Add a value to this one.
	 *
	 * @param other The value to add. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T addNew(Element<?> other);

	/**
	 * Add a value to this one, unless the mask is invalid.
	 *
	 * @param other The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T addNewIfValid(long other, Element<?> mask);
	/**
	 * Add a value to this one, unless the mask is invalid.
	 *
	 * @param other The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T addNewIfValid(double other, Element<?> mask);
	/**
	 * Add a value to this one, unless it is invalid.
	 *
	 * @param other The value to add. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return The result as a new object.
	 */
	T addNewIfValid(Element<?> other);
	/**
	 * Add a value to this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to add. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T addNewIfValid(Element<?> other, Element<?> mask);

	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(long a, long b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(double a, long b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(long a, double b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(double a, double b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(Element<?> a, long b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(long a, Element<?> b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(Element<?> a, double b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(double a, Element<?> b);
	/**
	 * Add two values, storing the result in this (third) instance.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOf(Element<?> a, Element<?> b);

	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(long a, long b, Element<?> mask);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(double a, long b, Element<?> mask);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(long a, double b, Element<?> mask);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(double a, double b, Element<?> mask);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(Element<?> a, long b);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(Element<?> a, long b, Element<?> mask);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(long a, Element<?> b);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(long a, Element<?> b, Element<?> mask);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(Element<?> a, double b);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(Element<?> a, double b, Element<?> mask);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(double a, Element<?> b);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(double a, Element<?> b, Element<?> mask);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(Element<?> a, Element<?> b);
	/**
	 * Add two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to add to.
	 * @param b The value to add.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T addOfIfValid(Element<?> a, Element<?> b, Element<?> mask);

	/**
	 * Subtract a value from this one.
	 *
	 * @param other The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T sub(long other);
	/**
	 * Subtract a value from this one.
	 *
	 * @param other The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T sub(double other);
	/**
	 * Subtract a value from this one.
	 *
	 * @param other The value to subtract. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T sub(Element<?> other);

	/**
	 * Subtract a value from this one, unless the mask is invalid.
	 *
	 * @param other The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subIfValid(long other, Element<?> mask);
	/**
	 * Subtract a value from this one, unless the mask is invalid.
	 *
	 * @param other The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subIfValid(double other, Element<?> mask);
	/**
	 * Subtract a value from this one, unless it is invalid.
	 *
	 * @param other The value to subtract. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subIfValid(Element<?> other);
	/**
	 * Subtract a value from this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to subtract. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subIfValid(Element<?> other, Element<?> mask);

	/**
	 * Subtract a value from this one.
	 *
	 * @param other The value to subtract.
	 * @return The result as a new object.
	 */
	T subNew(long other);
	/**
	 * Subtract a value from this one.
	 *
	 * @param other The value to subtract. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T subNew(double other);
	/**
	 * Subtract a value from this one.
	 *
	 * @param other The value to subtract. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T subNew(Element<?> other);

	/**
	 * Subtract a value from this one, unless the mask is invalid.
	 *
	 * @param other The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T subNewIfValid(long other, Element<?> mask);
	/**
	 * Subtract a value from this one, unless the mask is invalid.
	 *
	 * @param other The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T subNewIfValid(double other, Element<?> mask);
	/**
	 * Subtract a value from this one, unless it is invalid.
	 *
	 * @param other The value to subtract. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return The result as a new object.
	 */
	T subNewIfValid(Element<?> other);
	/**
	 * Subtract a value from this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to subtract. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T subNewIfValid(Element<?> other, Element<?> mask);

	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(long a, long b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(double a, long b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(long a, double b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(double a, double b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(Element<?> a, long b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(long a, Element<?> b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(Element<?> a, double b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(double a, Element<?> b);
	/**
	 * Subtract two values, storing the result in this (third) instance.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOf(Element<?> a, Element<?> b);

	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(long a, long b, Element<?> mask);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(double a, long b, Element<?> mask);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(long a, double b, Element<?> mask);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(double a, double b, Element<?> mask);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(Element<?> a, long b);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(Element<?> a, long b, Element<?> mask);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(long a, Element<?> b);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(long a, Element<?> b, Element<?> mask);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(Element<?> a, double b);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(Element<?> a, double b, Element<?> mask);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(double a, Element<?> b);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(double a, Element<?> b, Element<?> mask);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(Element<?> a, Element<?> b);
	/**
	 * Subtract two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to subtract from.
	 * @param b The value to subtract.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T subOfIfValid(Element<?> a, Element<?> b, Element<?> mask);

	/**
	 * Multiply a value with this one.
	 *
	 * @param other The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mul(long other);
	/**
	 * Multiply a value with this one.
	 *
	 * @param other The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mul(double other);
	/**
	 * Multiply a value with this one.
	 *
	 * @param other The value to multiply. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mul(Element<?> other);

	/**
	 * Multiply a value with this one, unless the mask is invalid.
	 *
	 * @param other The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulIfValid(long other, Element<?> mask);
	/**
	 * Multiply a value with this one, unless the mask is invalid.
	 *
	 * @param other The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulIfValid(double other, Element<?> mask);
	/**
	 * Multiply a value with this one, unless it is invalid.
	 *
	 * @param other The value to multiply. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulIfValid(Element<?> other);
	/**
	 * Multiply a value with this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to multiply. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulIfValid(Element<?> other, Element<?> mask);

	/**
	 * Multiply a value with this one.
	 *
	 * @param other The value to multiply.
	 * @return The result as a new object.
	 */
	T mulNew(long other);
	/**
	 * Multiply a value with this one.
	 *
	 * @param other The value to multiply. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T mulNew(double other);
	/**
	 * Multiply a value with this one.
	 *
	 * @param other The value to multiply. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T mulNew(Element<?> other);

	/**
	 * Multiply a value with this one, unless the mask is invalid.
	 *
	 * @param other The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T mulNewIfValid(long other, Element<?> mask);
	/**
	 * Multiply a value with this one, unless the mask is invalid.
	 *
	 * @param other The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T mulNewIfValid(double other, Element<?> mask);
	/**
	 * Multiply a value with this one, unless it is invalid.
	 *
	 * @param other The value to multiply. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return The result as a new object.
	 */
	T mulNewIfValid(Element<?> other);
	/**
	 * Multiply a value with this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to multiply. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T mulNewIfValid(Element<?> other, Element<?> mask);

	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(long a, long b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(double a, long b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(long a, double b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(double a, double b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(Element<?> a, long b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(long a, Element<?> b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(Element<?> a, double b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(double a, Element<?> b);
	/**
	 * Multiply two values, storing the result in this (third) instance.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOf(Element<?> a, Element<?> b);

	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(long a, long b, Element<?> mask);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(double a, long b, Element<?> mask);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(long a, double b, Element<?> mask);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(double a, double b, Element<?> mask);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(Element<?> a, long b);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(Element<?> a, long b, Element<?> mask);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(long a, Element<?> b);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(long a, Element<?> b, Element<?> mask);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(Element<?> a, double b);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(Element<?> a, double b, Element<?> mask);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(double a, Element<?> b);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(double a, Element<?> b, Element<?> mask);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(Element<?> a, Element<?> b);
	/**
	 * Multiply two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to multiply with.
	 * @param b The value to multiply.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mulOfIfValid(Element<?> a, Element<?> b, Element<?> mask);

	/**
	 * Divide a value by this one.
	 *
	 * @param other The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T div(long other);
	/**
	 * Divide a value by this one.
	 *
	 * @param other The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T div(double other);
	/**
	 * Divide a value by this one.
	 *
	 * @param other The value to divide. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T div(Element<?> other);

	/**
	 * Divide a value by this one, unless the mask is invalid.
	 *
	 * @param other The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divIfValid(long other, Element<?> mask);
	/**
	 * Divide a value by this one, unless the mask is invalid.
	 *
	 * @param other The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divIfValid(double other, Element<?> mask);
	/**
	 * Divide a value by this one, unless it is invalid.
	 *
	 * @param other The value to divide. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divIfValid(Element<?> other);
	/**
	 * Divide a value by this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to divide. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divIfValid(Element<?> other, Element<?> mask);

	/**
	 * Divide a value by this one.
	 *
	 * @param other The value to divide.
	 * @return The result as a new object.
	 */
	T divNew(long other);
	/**
	 * Divide a value by this one.
	 *
	 * @param other The value to divide. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T divNew(double other);
	/**
	 * Divide a value by this one.
	 *
	 * @param other The value to divide. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T divNew(Element<?> other);

	/**
	 * Divide a value by this one, unless the mask is invalid.
	 *
	 * @param other The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T divNewIfValid(long other, Element<?> mask);
	/**
	 * Divide a value by this one, unless the mask is invalid.
	 *
	 * @param other The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T divNewIfValid(double other, Element<?> mask);
	/**
	 * Divide a value by this one, unless it is invalid.
	 *
	 * @param other The value to divide. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return The result as a new object.
	 */
	T divNewIfValid(Element<?> other);
	/**
	 * Divide a value by this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to divide. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T divNewIfValid(Element<?> other, Element<?> mask);

	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(long a, long b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(double a, long b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(long a, double b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(double a, double b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(Element<?> a, long b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(long a, Element<?> b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(Element<?> a, double b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(double a, Element<?> b);
	/**
	 * Divide two values, storing the result in this (third) instance.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOf(Element<?> a, Element<?> b);

	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(long a, long b, Element<?> mask);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(double a, long b, Element<?> mask);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(long a, double b, Element<?> mask);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(double a, double b, Element<?> mask);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(Element<?> a, long b);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(Element<?> a, long b, Element<?> mask);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(long a, Element<?> b);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(long a, Element<?> b, Element<?> mask);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(Element<?> a, double b);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(Element<?> a, double b, Element<?> mask);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(double a, Element<?> b);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(double a, Element<?> b, Element<?> mask);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(Element<?> a, Element<?> b);
	/**
	 * Divide two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to divide by.
	 * @param b The value to divide.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T divOfIfValid(Element<?> a, Element<?> b, Element<?> mask);

	/**
	 * Modulo a value by this one.
	 *
	 * @param other The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mod(long other);
	/**
	 * Modulo a value by this one.
	 *
	 * @param other The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mod(double other);
	/**
	 * Modulo a value by this one.
	 *
	 * @param other The value to modulo. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T mod(Element<?> other);

	/**
	 * Modulo a value by this one, unless the mask is invalid.
	 *
	 * @param other The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modIfValid(long other, Element<?> mask);
	/**
	 * Modulo a value by this one, unless the mask is invalid.
	 *
	 * @param other The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modIfValid(double other, Element<?> mask);
	/**
	 * Modulo a value by this one, unless it is invalid.
	 *
	 * @param other The value to modulo. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modIfValid(Element<?> other);
	/**
	 * Modulo a value by this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to modulo. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modIfValid(Element<?> other, Element<?> mask);

	/**
	 * Modulo a value by this one.
	 *
	 * @param other The value to modulo.
	 * @return The result as a new object.
	 */
	T modNew(long other);
	/**
	 * Modulo a value by this one.
	 *
	 * @param other The value to modulo. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T modNew(double other);
	/**
	 * Modulo a value by this one.
	 *
	 * @param other The value to modulo. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T modNew(Element<?> other);

	/**
	 * Modulo a value by this one, unless the mask is invalid.
	 *
	 * @param other The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T modNewIfValid(long other, Element<?> mask);
	/**
	 * Modulo a value by this one, unless the mask is invalid.
	 *
	 * @param other The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T modNewIfValid(double other, Element<?> mask);
	/**
	 * Modulo a value by this one, unless it is invalid.
	 *
	 * @param other The value to modulo. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return The result as a new object.
	 */
	T modNewIfValid(Element<?> other);
	/**
	 * Modulo a value by this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to modulo. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T modNewIfValid(Element<?> other, Element<?> mask);

	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(long a, long b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(double a, long b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(long a, double b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(double a, double b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(Element<?> a, long b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(long a, Element<?> b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(Element<?> a, double b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(double a, Element<?> b);
	/**
	 * Modulo two values, storing the result in this (third) instance.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOf(Element<?> a, Element<?> b);

	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(long a, long b, Element<?> mask);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(double a, long b, Element<?> mask);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(long a, double b, Element<?> mask);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(double a, double b, Element<?> mask);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(Element<?> a, long b);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(Element<?> a, long b, Element<?> mask);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(long a, Element<?> b);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(long a, Element<?> b, Element<?> mask);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(Element<?> a, double b);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(Element<?> a, double b, Element<?> mask);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(double a, Element<?> b);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(double a, Element<?> b, Element<?> mask);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(Element<?> a, Element<?> b);
	/**
	 * Modulo two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to modulo by.
	 * @param b The value to modulo.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T modOfIfValid(Element<?> a, Element<?> b, Element<?> mask);

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
	 * @param other The value to minimum. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T min(Element<?> other);

	/**
	 * Find the minimum of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minIfValid(long other, Element<?> mask);
	/**
	 * Find the minimum of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minIfValid(double other, Element<?> mask);
	/**
	 * Find the minimum of this and another value, unless it is invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minIfValid(Element<?> other);
	/**
	 * Find the minimum of this and another value, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minIfValid(Element<?> other, Element<?> mask);

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
	 * @param other The value to minimum. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T minNew(Element<?> other);

	/**
	 * Find the minimum of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T minNewIfValid(long other, Element<?> mask);
	/**
	 * Find the minimum of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T minNewIfValid(double other, Element<?> mask);
	/**
	 * Find the minimum of this and another value, unless it is invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @return The result as a new object.
	 */
	T minNewIfValid(Element<?> other);
	/**
	 * Find the minimum of this and another value, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T minNewIfValid(Element<?> other, Element<?> mask);

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
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(long a, long b, Element<?> mask);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(double a, long b, Element<?> mask);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(long a, double b, Element<?> mask);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(double a, double b, Element<?> mask);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(Element<?> a, long b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(Element<?> a, long b, Element<?> mask);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(long a, Element<?> b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(long a, Element<?> b, Element<?> mask);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(Element<?> a, double b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(Element<?> a, double b, Element<?> mask);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(double a, Element<?> b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(double a, Element<?> b, Element<?> mask);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(Element<?> a, Element<?> b);
	/**
	 * Find the minimum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T minOfIfValid(Element<?> a, Element<?> b, Element<?> mask);

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
	 * @param other The value to maximum. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T max(Element<?> other);

	/**
	 * Find the maximum of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxIfValid(long other, Element<?> mask);
	/**
	 * Find the maximum of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxIfValid(double other, Element<?> mask);
	/**
	 * Find the maximum of this and another value, unless it is invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxIfValid(Element<?> other);
	/**
	 * Find the maximum of this and another value, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxIfValid(Element<?> other, Element<?> mask);

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
	 * @param other The value to maximum. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T maxNew(Element<?> other);

	/**
	 * Find the maximum of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T maxNewIfValid(long other, Element<?> mask);
	/**
	 * Find the maximum of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T maxNewIfValid(double other, Element<?> mask);
	/**
	 * Find the maximum of this and another value, unless it is invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @return The result as a new object.
	 */
	T maxNewIfValid(Element<?> other);
	/**
	 * Find the maximum of this and another value, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T maxNewIfValid(Element<?> other, Element<?> mask);

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
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(long a, long b, Element<?> mask);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(double a, long b, Element<?> mask);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(long a, double b, Element<?> mask);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(double a, double b, Element<?> mask);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(Element<?> a, long b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(Element<?> a, long b, Element<?> mask);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(long a, Element<?> b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(long a, Element<?> b, Element<?> mask);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(Element<?> a, double b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(Element<?> a, double b, Element<?> mask);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(double a, Element<?> b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(double a, Element<?> b, Element<?> mask);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(Element<?> a, Element<?> b);
	/**
	 * Find the maximum of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T maxOfIfValid(Element<?> a, Element<?> b, Element<?> mask);

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
