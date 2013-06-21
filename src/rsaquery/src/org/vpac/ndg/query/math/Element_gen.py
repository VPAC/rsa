#!/usr/bin/env python

#
# This program generates the Element interface.
#

from string import Template

import Element_types


INTERFACE_HEADER_TEMPLATE = Template("""
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
	void setValid(Element<?> mask);
	void setValidIfValid(Element<?> mask);

	ScalarElement[] getComponents();
""")

INTERFACE_FOOTER_TEMPLATE = Template("""
}
""")

CAST_DECLARATION_TEMPLATE = Template("""
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
""")

ARITHMETIC_DECLARATION_TEMPLATE = Template("""
	/**
	 * $longname_upper a value $direction this one.
	 *
	 * @param other The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T $opname(long other);
	/**
	 * $longname_upper a value $direction this one.
	 *
	 * @param other The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T $opname(double other);
	/**
	 * $longname_upper a value $direction this one.
	 *
	 * @param other The value to $longname. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T $opname(Element<?> other);

	/**
	 * $longname_upper a value $direction this one, unless the mask is invalid.
	 *
	 * @param other The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}IfValid(long other, Element<?> mask);
	/**
	 * $longname_upper a value $direction this one, unless the mask is invalid.
	 *
	 * @param other The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}IfValid(double other, Element<?> mask);
	/**
	 * $longname_upper a value $direction this one, unless it is invalid.
	 *
	 * @param other The value to $longname. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}IfValid(Element<?> other);
	/**
	 * $longname_upper a value $direction this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to $longname. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}IfValid(Element<?> other, Element<?> mask);

	/**
	 * $longname_upper a value $direction this one.
	 *
	 * @param other The value to $longname.
	 * @return The result as a new object.
	 */
	T ${opname}New(long other);
	/**
	 * $longname_upper a value $direction this one.
	 *
	 * @param other The value to $longname. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T ${opname}New(double other);
	/**
	 * $longname_upper a value $direction this one.
	 *
	 * @param other The value to $longname. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T ${opname}New(Element<?> other);

	/**
	 * $longname_upper a value $direction this one, unless the mask is invalid.
	 *
	 * @param other The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T ${opname}NewIfValid(long other, Element<?> mask);
	/**
	 * $longname_upper a value $direction this one, unless the mask is invalid.
	 *
	 * @param other The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T ${opname}NewIfValid(double other, Element<?> mask);
	/**
	 * $longname_upper a value $direction this one, unless it is invalid.
	 *
	 * @param other The value to $longname. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @return The result as a new object.
	 */
	T ${opname}NewIfValid(Element<?> other);
	/**
	 * $longname_upper a value $direction this one, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to $longname. If this value is invalid, it will be
	 *        ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T ${opname}NewIfValid(Element<?> other, Element<?> mask);

	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(long a, long b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(double a, long b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(long a, double b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(double a, double b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(Element<?> a, long b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(long a, Element<?> b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(Element<?> a, double b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(double a, Element<?> b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(Element<?> a, Element<?> b);

	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(long a, long b, Element<?> mask);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(double a, long b, Element<?> mask);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(long a, double b, Element<?> mask);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless the mask is invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(double a, double b, Element<?> mask);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, long b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, long b, Element<?> mask);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(long a, Element<?> b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(long a, Element<?> b, Element<?> mask);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, double b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, double b, Element<?> mask);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(double a, Element<?> b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(double a, Element<?> b, Element<?> mask);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless any of the values are invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, Element<?> b);
	/**
	 * $longname_upper two values, storing the result in this (third) instance,
	 * unless any of the values or the mask are invalid.
	 *
	 * @param a The value to $longname $direction.
	 * @param b The value to $longname.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, Element<?> b, Element<?> mask);
""")

BOUNDING_DECLARATION_TEMPLATE = Template("""
	/**
	 * Find the $longname of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T $opname(long other);
	/**
	 * Find the $longname of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T $opname(double other);
	/**
	 * Find the $longname of this and another value.
	 *
	 * @param other The value to $longname. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T $opname(Element<?> other);

	/**
	 * Find the $longname of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}IfValid(long other, Element<?> mask);
	/**
	 * Find the $longname of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}IfValid(double other, Element<?> mask);
	/**
	 * Find the $longname of this and another value, unless it is invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}IfValid(Element<?> other);
	/**
	 * Find the $longname of this and another value, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}IfValid(Element<?> other, Element<?> mask);

	/**
	 * Find the $longname of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return The result as a new object.
	 */
	T ${opname}New(long other);
	/**
	 * Find the $longname of this and another value.
	 *
	 * @param other The value to compare to.
	 * @return The result as a new object.
	 */
	T ${opname}New(double other);
	/**
	 * Find the $longname of this and another value.
	 *
	 * @param other The value to $longname. If this value is invalid, this
	 *        element will also be marked as invalid (for vectors, this is done
	 *        on a component-by-component basis).
	 * @return The result as a new object.
	 */
	T ${opname}New(Element<?> other);

	/**
	 * Find the $longname of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T ${opname}NewIfValid(long other, Element<?> mask);
	/**
	 * Find the $longname of this and another value, unless the mask is invalid.
	 *
	 * @param other The value to compare to.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T ${opname}NewIfValid(double other, Element<?> mask);
	/**
	 * Find the $longname of this and another value, unless it is invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @return The result as a new object.
	 */
	T ${opname}NewIfValid(Element<?> other);
	/**
	 * Find the $longname of this and another value, unless it or the mask is
	 * invalid.
	 *
	 * @param other The value to compare to. If this value is invalid, it will
	 *        be ignored (the operation will not take place).
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return The result as a new object.
	 */
	T ${opname}NewIfValid(Element<?> other, Element<?> mask);

	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(long a, long b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(double a, long b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(long a, double b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(double a, double b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(Element<?> a, long b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(long a, Element<?> b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(Element<?> a, double b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(double a, Element<?> b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}Of(Element<?> a, Element<?> b);

	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(long a, long b, Element<?> mask);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(double a, long b, Element<?> mask);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(long a, double b, Element<?> mask);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(double a, double b, Element<?> mask);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, long b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, long b, Element<?> mask);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(long a, Element<?> b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(long a, Element<?> b, Element<?> mask);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, double b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, double b, Element<?> mask);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(double a, Element<?> b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(double a, Element<?> b, Element<?> mask);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, Element<?> b);
	/**
	 * Find the $longname of two values, storing the result in this (third)
	 * instance).
	 *
	 * @param a The first operand.
	 * @param b The second operand.
	 * @param mask A mask to use for the operation. If this is invalid, the
	 *        operation will not take place.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	T ${opname}OfIfValid(Element<?> a, Element<?> b, Element<?> mask);
""")

SPECIAL_DECLARATION_TEMPLATE = Template("""
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
""")

def write_interface(output):
	output.write(INTERFACE_HEADER_TEMPLATE.substitute())

	output.write("\n	// CASTING\n")
	output.write(CAST_DECLARATION_TEMPLATE.substitute())

	output.write("\n	// ARITHMETIC\n")
	for opname, _, longname, direction in Element_types.ARITHMETIC_OPS:
		output.write(ARITHMETIC_DECLARATION_TEMPLATE.substitute(
				opname=opname, longname=longname,
				longname_upper=longname.capitalize(), direction=direction))

	output.write("\n	// BOUNDING\n")
	for opname, _, longname in Element_types.BOUNDING_OPS:
		output.write(BOUNDING_DECLARATION_TEMPLATE.substitute(
				opname=opname, longname=longname,
				longname_upper=longname.capitalize()))

	output.write(SPECIAL_DECLARATION_TEMPLATE.substitute())

	output.write(INTERFACE_FOOTER_TEMPLATE.substitute())




SCALAR_INTERFACE_HEADER_TEMPLATE = Template("""
package org.vpac.ndg.query.math;

/**
 * A scalar numeric value. Scalar elements have the special property that they
 * are comparable.
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See elementgen.py.
public interface ScalarElement extends Element<ScalarElement>, Comparable<ScalarElement> {
""")

SCALAR_INTERFACE_FOOTER_TEMPLATE = Template("""
}
""")

SCALAR_CAST_DECLARATION_TEMPLATE = Template("""
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
""")

COMPARISON_DECLARATION_TEMPLATE = Template("""
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
""")

def write_scalar_interface(output):
	output.write(SCALAR_INTERFACE_HEADER_TEMPLATE.substitute())
	output.write(COMPARISON_DECLARATION_TEMPLATE.substitute())

	output.write("\n	// CASTING\n")
	output.write(SCALAR_CAST_DECLARATION_TEMPLATE.substitute())

	output.write(SCALAR_INTERFACE_FOOTER_TEMPLATE.substitute())



if __name__ == "__main__":
	with open("Element.java", 'w') as f:
		print "Writing", "Element.java"
		write_interface(f)

	with open("ScalarElement.java", 'w') as f:
		print "Writing", "ScalarElement.java"
		write_scalar_interface(f)

