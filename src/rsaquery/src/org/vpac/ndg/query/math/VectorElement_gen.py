#!/usr/bin/env python

#
# This file is part of the Raster Storage Archive (RSA).
#
# The RSA is free software: you can redistribute it and/or modify it under the
# terms of the GNU General Public License as published by the Free Software
# Foundation, either version 3 of the License, or (at your option) any later
# version.
#
# The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
# WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
# A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along with
# the RSA.  If not, see <http://www.gnu.org/licenses/>.
#
# Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
# http://www.crcsi.com.au/
#

#
# This program generates the scalar Element classes.
#

from string import Template

import Element_types

CLASS_HEADER_TEMPLATE = Template("""/*
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

// THIS IS GENERATED CODE. Do not modify this file. See VectorElement_gen.py.

package org.vpac.ndg.query.math;

import java.util.Arrays;

/**
 * A pixel that stores multiple values.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
public class VectorElement implements Element<VectorElement> {

	private ScalarElement[] components;

	/**
	 * Create a new vector as the concatenation of a collection of scalar
	 * elements.
	 * @param other The elements to concatenate. These will be copied.
	 */
	public VectorElement(ScalarElement... other) {
		this.components = new ScalarElement[other.length];
		for (int i = 0; i < other.length; i++)
			this.components[i] = (ScalarElement)other[i].copy();
	}

	private VectorElement(int capacity) {
		this.components = new ScalarElement[capacity];
	}

	/**
	 * Create a new vector of {@link ElementLong ElementLongs}.
	 * @param capacity The number of components.
	 * @param fillValue The initial value to assign to the components.
	 * @return The new vector.
	 */
	public static VectorElement createInt(int capacity, long fillValue) {
		ScalarElement[] components = new ScalarElement[capacity];
		for (int i = 0; i < capacity; i++) {
			ScalarElement elem = new ElementLong(fillValue);
			components[i] = elem;
		}
		return new VectorElement(components);
	}

	/**
	 * Create a new vector of {@link ElementDouble ElementDoubles}.
	 * @param capacity The number of components.
	 * @param fillValue The initial value to assign to the components.
	 * @return The new vector.
	 */
	public static VectorElement createReal(int capacity, double fillValue) {
		ScalarElement[] components = new ScalarElement[capacity];
		for (int i = 0; i < capacity; i++) {
			ScalarElement elem = new ElementDouble(fillValue);
			components[i] = elem;
		}
		return new VectorElement(components);
	}

	public static VectorElement create(int capacity, Type type) {
		VectorElement elem = new VectorElement(capacity);
		for (int i = 0; i < capacity; i++) {
			elem.components[i] = type.getElement().copy();
			elem.components[i].set(0);
		}
		return elem;
	}

	public static VectorElement create(Type[] types) {
		VectorElement elem = new VectorElement(types.length);
		for (int i = 0; i < elem.components.length; i++) {
			elem.components[i] = types[i].getElement().copy();
			elem.components[i].set(0);
		}
		return elem;
	}

	@Override
	public VectorElement copy() {
		return new VectorElement(components);
	}

	@Override
	public boolean isValid() {
		for (ScalarElement c : components) {
			if (!c.isValid())
				return false;
		}
		return true;
	}
	@Override
	public void setValid(boolean valid) {
		for (ScalarElement c : components)
			c.setValid(valid);
	}
	@Override
	public void setValid(Element<?> mask) {
		if (mask.getClass() == VectorElement.class)
			setValid((VectorElement) mask);
		else
			setValid((ScalarElement) mask);
	}
	public void setValid(ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].setValid(mask.isValid());
	}
	public void setValid(VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].setValid(mask.components[i].isValid());
	}
	@Override
	public void setValidIfValid(Element<?> mask) {
		if (mask.getClass() == VectorElement.class)
			setValidIfValid((VectorElement) mask);
		else
			setValidIfValid((ScalarElement) mask);
	}
	public void setValidIfValid(ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].setValidIfValid(mask);
	}
	public void setValidIfValid(VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].setValidIfValid(mask.components[i]);
	}

	@Override
	public ScalarElement[] getComponents() {
		return components;
	}
""")

CLASS_FOOTER_TEMPLATE = Template("""
}
""")

SPECIAL_CAST_TEMPLATE = Template("""
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		boolean first = true;
		for (ScalarElement e : components){
			if (!first)
				sb.append(", ");
			else
				first = false;
			sb.append(e);
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * @param i The index of the component to fetch.
	 * @return The component at the specified index.
	 */
	public ScalarElement get(int i) {
		return components[i];
	}

	/**
	 * @param i The index of the component to set.
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 * @return The component at the specified index.
	 */
	public ScalarElement set(int i, ScalarElement value) {
		return components[i].set(value);
	}

	/**
	 * @return The number of components in this vector.
	 */
	public int size() {
		return components.length;
	}

	@Override
	public VectorElement set(Element<?> value) {
		if (value.getClass() == VectorElement.class) {
			set((VectorElement) value);
		} else {
			set((ScalarElement) value);
		}
		return this;
	}

	public VectorElement set(ScalarElement value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}

	public VectorElement set(VectorElement value) {
		for (int i = 0; i < components.length; i++)
			components[i].set(value.components[i]);
		return this;
	}

	@Override
	public VectorElement set(Number value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}

	/**
	 * @return The component that represents time. This is always the first
	 * element.
	 */
	public ScalarElement getT() {
		return components[0];
	}
	/**
	 * Set the time component (first).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void setT(ScalarElement value) {
		components[0].set(value);
	}
	/**
	 * Set the time component (first).
	 * @param value The value to assign to the component.
	 */
	public void setT(Number value) {
		components[0].set(value);
	}
""")

ELEMENT_ACCESSOR_TEMPLATE = Template("""
	private final static int OFFSET_${characterUpper} = $index + 1;
	/**
	 * @return The ${characterUpper} component (${placement}${synonym}).
	 */
	public ScalarElement get${characterUpper}() {
		return components[components.length - OFFSET_${characterUpper}];
	}
	/**
	 * Set the ${characterUpper} component (${placement}${synonym}).
	 * @param value The value to assign to the component. Note that this does
	 *        not retain a reference to the value; it will be assigned as with
	 *        the {@link ScalarElement#set(Element)} method.
	 */
	public void set${characterUpper}(ScalarElement value) {
		components[components.length - OFFSET_${characterUpper}].set(value);
	}
	/**
	 * Set the ${characterUpper} component (${placement}${synonym}).
	 * @param value The value to assign to the component.
	 */
	public void set${characterUpper}(Number value) {
		components[components.length - OFFSET_${characterUpper}].set(value);
	}
""")

CAST_TEMPLATE = Template("""
	@Override
	public VectorElement set(${ptype} value) {
		for (ScalarElement c : components)
			c.set(value);
		return this;
	}
	@Override
	public VectorElement as${ftype}() {
		VectorElement res = new VectorElement();
		res.components = new ScalarElement[components.length];
		for (int i = 0; i < components.length; i++)
			res.components[i] = (ScalarElement) components[i].as${ftype}();
		return res;
	}
""")

ARITHMETIC_TEMPLATE = Template("""
	// $longname

	@Override
	public VectorElement ${opname}(long other) {
		for (ScalarElement c : components)
			c.${opname}(other);
		return this;
	}
	@Override
	public VectorElement ${opname}(double other) {
		for (ScalarElement c : components)
			c.${opname}(other);
		return this;
	}
	@Override
	public VectorElement ${opname}(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			${opname}((VectorElement) other);
		} else {
			${opname}((ScalarElement) other);
		}
		return this;
	}
	public VectorElement ${opname}(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}(other);
		return this;
	}
	public VectorElement ${opname}(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}(other.components[i]);
		return this;
	}

	@Override
	public VectorElement ${opname}IfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			${opname}IfValid(other, (VectorElement) mask);
		} else {
			${opname}IfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement ${opname}IfValid(long other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}IfValid(other, mask);
		return this;
	}
	public VectorElement ${opname}IfValid(long other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}IfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}IfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			${opname}IfValid(other, (VectorElement) mask);
		} else {
			${opname}IfValid(other, (ScalarElement) mask);
		}
		return this;
	}
	public VectorElement ${opname}IfValid(double other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}IfValid(other, mask);
		return this;
	}
	public VectorElement ${opname}IfValid(double other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}IfValid(other, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}IfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			${opname}IfValid((VectorElement) other);
		} else {
			${opname}IfValid((ScalarElement) other);
		}
		return this;
	}
	public VectorElement ${opname}IfValid(ScalarElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}IfValid(other);
		return this;
	}
	public VectorElement ${opname}IfValid(VectorElement other) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}IfValid(other.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}IfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return ${opname}IfValid((VectorElement) other, (VectorElement) mask);
			else
				return ${opname}IfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return ${opname}IfValid((ScalarElement) other, (VectorElement) mask);
			else
				return ${opname}IfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}IfValid(ScalarElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}IfValid(other, mask);
		return this;
	}
	public VectorElement ${opname}IfValid(ScalarElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}IfValid(other, mask.components[i]);
		return this;
	}
	public VectorElement ${opname}IfValid(VectorElement other, ScalarElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}IfValid(other.components[i], mask);
		return this;
	}
	public VectorElement ${opname}IfValid(VectorElement other, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}IfValid(other.components[i], mask.components[i]);
		return this;
	}

	@Override
	public VectorElement ${opname}New(long other) {
		VectorElement res = copy();
		return res.${opname}(other);
	}
	@Override
	public VectorElement ${opname}New(double other) {
		VectorElement res = copy();
		return res.${opname}(other);
	}
	@Override
	public VectorElement ${opname}New(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return ${opname}New((VectorElement) other);
		} else {
			return ${opname}New((ScalarElement) other);
		}
	}
	public VectorElement ${opname}New(ScalarElement other) {
		VectorElement res = copy();
		return res.${opname}(other);
	}
	public VectorElement ${opname}New(VectorElement other) {
		VectorElement res = copy();
		return res.${opname}(other);
	}

	@Override
	public VectorElement ${opname}NewIfValid(long other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return ${opname}NewIfValid(other, (VectorElement) mask);
		} else {
			return ${opname}NewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}NewIfValid(long other, ScalarElement mask) {
		VectorElement res = copy();
		return res.${opname}IfValid(other, mask);
	}
	public VectorElement ${opname}NewIfValid(long other, VectorElement mask) {
		VectorElement res = copy();
		return res.${opname}IfValid(other, mask);
	}
	@Override
	public VectorElement ${opname}NewIfValid(double other, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return ${opname}NewIfValid(other, (VectorElement) mask);
		} else {
			return ${opname}NewIfValid(other, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}NewIfValid(double other, ScalarElement mask) {
		VectorElement res = copy();
		return res.${opname}IfValid(other, mask);
	}
	public VectorElement ${opname}NewIfValid(double other, VectorElement mask) {
		VectorElement res = copy();
		return res.${opname}IfValid(other, mask);
	}
	@Override
	public VectorElement ${opname}NewIfValid(Element<?> other) {
		if (other.getClass() == VectorElement.class) {
			return ${opname}NewIfValid((VectorElement) other);
		} else {
			return ${opname}NewIfValid((ScalarElement) other);
		}
	}
	public VectorElement ${opname}NewIfValid(ScalarElement other) {
		VectorElement res = copy();
		return res.${opname}IfValid(other);
	}
	public VectorElement ${opname}NewIfValid(VectorElement other) {
		VectorElement res = copy();
		return res.${opname}IfValid(other);
	}
	@Override
	public VectorElement ${opname}NewIfValid(Element<?> other, Element<?> mask) {
		if (other.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return ${opname}NewIfValid((VectorElement) other, (VectorElement) mask);
			else
				return ${opname}NewIfValid((VectorElement) other, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return ${opname}NewIfValid((ScalarElement) other, (VectorElement) mask);
			else
				return ${opname}NewIfValid((ScalarElement) other, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}NewIfValid(ScalarElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.${opname}IfValid(other, mask);
	}
	public VectorElement ${opname}NewIfValid(ScalarElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.${opname}IfValid(other, mask);
	}
	public VectorElement ${opname}NewIfValid(VectorElement other, ScalarElement mask) {
		VectorElement res = copy();
		return res.${opname}IfValid(other, mask);
	}
	public VectorElement ${opname}NewIfValid(VectorElement other, VectorElement mask) {
		VectorElement res = copy();
		return res.${opname}IfValid(other, mask);
	}

	@Override
	public VectorElement ${opname}Of(long a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}Of(a, b);
		return this;
	}
	@Override
	public VectorElement ${opname}Of(double a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}Of(a, b);
		return this;
	}
	@Override
	public VectorElement ${opname}Of(long a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}Of(a, b);
		return this;
	}
	@Override
	public VectorElement ${opname}Of(double a, double b) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}Of(a, b);
		return this;
	}
	@Override
	public VectorElement ${opname}Of(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(a, b);
		}
		return this;
	}
	@Override
	public VectorElement ${opname}Of(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(a, b);
		}
		return this;
	}
	@Override
	public VectorElement ${opname}Of(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(av.components[i], b);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(a, b);
		}
		return this;
	}
	@Override
	public VectorElement ${opname}Of(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(a, b);
		}
		return this;
	}
	@Override
	public VectorElement ${opname}Of(Element<?> a, Element<?> b) {
		if (a.getClass() == VectorElement.class && b.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(av.components[i], bv.components[i]);
		} else if (a.getClass() == VectorElement.class) {
			VectorElement av = (VectorElement)a;
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(av.components[i], b);
		} else if (b.getClass() == VectorElement.class) {
			VectorElement bv = (VectorElement)b;
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(a, bv.components[i]);
		} else {
			for (int i = 0; i < components.length; i++)
				components[i].${opname}Of(a, b);
		}
		return this;
	}

	@Override
	public VectorElement ${opname}OfIfValid(long a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return ${opname}OfIfValid(a, b, (VectorElement) mask);
		} else {
			return ${opname}OfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}OfIfValid(long a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(long a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(double a, long b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return ${opname}OfIfValid(a, b, (VectorElement) mask);
		} else {
			return ${opname}OfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}OfIfValid(double a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(double a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(long a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return ${opname}OfIfValid(a, b, (VectorElement) mask);
		} else {
			return ${opname}OfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}OfIfValid(long a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(long a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(double a, double b, Element<?> mask) {
		if (mask.getClass() == VectorElement.class) {
			return ${opname}OfIfValid(a, b, (VectorElement) mask);
		} else {
			return ${opname}OfIfValid(a, b, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}OfIfValid(double a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(double a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(Element<?> a, long b) {
		if (a.getClass() == VectorElement.class) {
			return ${opname}OfIfValid((VectorElement) a, b);
		} else {
			return ${opname}OfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, long b) {
		if (!a.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, long b) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a.components[i], b);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(Element<?> a, long b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return ${opname}OfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return ${opname}OfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return ${opname}OfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return ${opname}OfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, long b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}Of(a, b);
		return this;
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, long b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b, mask);
		return this;
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, long b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b, mask);
		return this;
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, long b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(long a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return ${opname}OfIfValid(a, (VectorElement) b);
		} else {
			return ${opname}OfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement ${opname}OfIfValid(long a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(long a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(long a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return ${opname}OfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return ${opname}OfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return ${opname}OfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return ${opname}OfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}OfIfValid(long a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(long a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement ${opname}OfIfValid(long a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement ${opname}OfIfValid(long a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(Element<?> a, double b) {
		if (a.getClass() == VectorElement.class) {
			return ${opname}OfIfValid((VectorElement) a, b);
		} else {
			return ${opname}OfIfValid((ScalarElement) a, b);
		}
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, double b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, double b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, double b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a.components[i], b, mask);
		return this;
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, double b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(Element<?> a, double b, Element<?> mask) {
		if (a.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return ${opname}OfIfValid((VectorElement) a, b, (VectorElement) mask);
			else
				return ${opname}OfIfValid((VectorElement) a, b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return ${opname}OfIfValid((ScalarElement) a, b, (VectorElement) mask);
			else
				return ${opname}OfIfValid((ScalarElement) a, b, (ScalarElement) mask);
		}
	}
	@Override
	public VectorElement ${opname}OfIfValid(double a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			return ${opname}OfIfValid(a, (VectorElement) b);
		} else {
			return ${opname}OfIfValid(a, (ScalarElement) b);
		}
	}
	public VectorElement ${opname}OfIfValid(double a, ScalarElement b) {
		if (!b.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(double a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(double a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class)
				return ${opname}OfIfValid(a, (VectorElement) b, (VectorElement) mask);
			else
				return ${opname}OfIfValid(a, (VectorElement) b, (ScalarElement) mask);
		} else {
			if (mask.getClass() == VectorElement.class)
				return ${opname}OfIfValid(a, (ScalarElement) b, (VectorElement) mask);
			else
				return ${opname}OfIfValid(a, (ScalarElement) b, (ScalarElement) mask);
		}
	}
	public VectorElement ${opname}OfIfValid(double a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(double a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement ${opname}OfIfValid(double a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b.components[i], mask);
		return this;
	}
	public VectorElement ${opname}OfIfValid(double a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(Element<?> a, Element<?> b) {
		if (b.getClass() == VectorElement.class) {
			if (a.getClass() == VectorElement.class)
				return ${opname}OfIfValid((VectorElement) a, (VectorElement) b);
			else
				return ${opname}OfIfValid((ScalarElement) a, (VectorElement) b);
		} else {
			if (a.getClass() == VectorElement.class)
				return ${opname}OfIfValid((VectorElement) a, (ScalarElement) b);
			else
				return ${opname}OfIfValid((ScalarElement) a, (ScalarElement) b);
		}
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, ScalarElement b) {
		if (!a.isValid() || !b.isValid())
			return this;
		return ${opname}Of(a, b);
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, VectorElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, ScalarElement b) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, VectorElement b) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a.components[i], b.components[i]);
		return this;
	}
	@Override
	public VectorElement ${opname}OfIfValid(Element<?> a, Element<?> b, Element<?> mask) {
		if (b.getClass() == VectorElement.class) {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return ${opname}OfIfValid((VectorElement) a, (VectorElement) b, (VectorElement) mask);
				else
					return ${opname}OfIfValid((ScalarElement) a, (VectorElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return ${opname}OfIfValid((VectorElement) a, (VectorElement) b, (ScalarElement) mask);
				else
					return ${opname}OfIfValid((ScalarElement) a, (VectorElement) b, (ScalarElement) mask);
			}
		} else {
			if (mask.getClass() == VectorElement.class) {
				if (a.getClass() == VectorElement.class)
					return ${opname}OfIfValid((VectorElement) a, (ScalarElement) b, (VectorElement) mask);
				else
					return ${opname}OfIfValid((ScalarElement) a, (ScalarElement) b, (VectorElement) mask);
			} else {
				if (a.getClass() == VectorElement.class)
					return ${opname}OfIfValid((VectorElement) a, (ScalarElement) b, (ScalarElement) mask);
				else
					return ${opname}OfIfValid((ScalarElement) a, (ScalarElement) b, (ScalarElement) mask);
			}
		}
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, ScalarElement b, ScalarElement mask) {
		if (!a.isValid() || !b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}Of(a, b);
		return this;
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, ScalarElement b, VectorElement mask) {
		if (!a.isValid() || !b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b, mask.components[i]);
		return this;
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, VectorElement b, ScalarElement mask) {
		if (!a.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b.components[i]);
		return this;
	}
	public VectorElement ${opname}OfIfValid(ScalarElement a, VectorElement b, VectorElement mask) {
		if (!a.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a, b.components[i], mask.components[i]);
		return this;
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, ScalarElement b, ScalarElement mask) {
		if (!b.isValid() || !mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a.components[i], b);
		return this;
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, ScalarElement b, VectorElement mask) {
		if (!b.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a.components[i], b, mask.components[i]);
		return this;
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, VectorElement b, ScalarElement mask) {
		if (!mask.isValid())
			return this;
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a.components[i], b.components[i]);
		return this;
	}
	public VectorElement ${opname}OfIfValid(VectorElement a, VectorElement b, VectorElement mask) {
		for (int i = 0; i < components.length; i++)
			components[i].${opname}OfIfValid(a.components[i], b.components[i], mask.components[i]);
		return this;
	}
""")

SPECIAL_BOUNDING_TEMPLATE = Template("""
	@Override
	public VectorElement clamp(long min, long max) {
		for (int i = 0; i < components.length; i++) {
			components[i].clamp(min, max);
		}
		return this;
	}
	@Override
	public VectorElement clamp(double min, double max) {
		for (int i = 0; i < components.length; i++) {
			components[i].clamp(min, max);
		}
		return this;
	}
	@Override
	public VectorElement clamp(Element<?> min, Element<?> max) {
		for (int i = 0; i < components.length; i++) {
			components[i].clamp(min, max);
		}
		return this;
	}
	public VectorElement clamp(VectorElement min, VectorElement max) {
		for (int i = 0; i < components.length; i++) {
			components[i].clamp(min.components[i], max.components[i]);
		}
		return this;
	}

	@Override
	public VectorElement clampNew(long min, long max) {
		VectorElement res = copy();
		return res.clamp(min, max);
	}
	@Override
	public VectorElement clampNew(double min, double max) {
		VectorElement res = copy();
		return res.clamp(min, max);
	}
	@Override
	public VectorElement clampNew(Element<?> min, Element<?> max) {
		VectorElement res = copy();
		return res.clamp(min, max);
	}
	public VectorElement clampNew(VectorElement min, VectorElement max) {
		VectorElement res = copy();
		return res.clamp(min, max);
	}
""")

COMPARISON_TEMPLATE = Template("""
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VectorElement other = (VectorElement) obj;
		if (!Arrays.equals(components, other.components))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(components);
		return result;
	}
""")

def write_class(output):

	mapping = {}

	output.write(CLASS_HEADER_TEMPLATE.substitute(mapping))
	output.write("\n	// CASTING\n")
	output.write(SPECIAL_CAST_TEMPLATE.substitute(mapping))
	for t in Element_types.TYPES:
		local_mapping = {
				"ptype": t.primitive_name,
				"ftype": t.primitive_name.capitalize(),
				}
		output.write(CAST_TEMPLATE.substitute(local_mapping))

	for character, index, placement, syn in Element_types.ELEMENT_NAMES:
		local_mapping = {
				"characterUpper": character.upper(),
				"index": index,
				"placement": placement,
				"synonym": "; synonym for %s" % syn,
				}
		output.write(ELEMENT_ACCESSOR_TEMPLATE.substitute(local_mapping))

	output.write("\n	// ARITHMETIC\n")
	for opname, opchar, longname, _ in Element_types.ARITHMETIC_OPS:
		output.write(ARITHMETIC_TEMPLATE.substitute(mapping, opname=opname,
				opchar=opchar, longname=longname))

	# For vectors, the 
	output.write("\n	// BOUNDING\n")
	for opname, opchar, longname in Element_types.BOUNDING_OPS:
		output.write(ARITHMETIC_TEMPLATE.substitute(mapping, opname=opname,
				opchar=opchar, longname=longname))
	output.write(SPECIAL_BOUNDING_TEMPLATE.substitute(mapping))

	output.write(COMPARISON_TEMPLATE.substitute(mapping))
	output.write(CLASS_FOOTER_TEMPLATE.substitute(mapping))



if __name__ == "__main__":
	print "Writing VectorElement.java"
	with open("VectorElement.java", 'w') as f:
		write_class(f)

