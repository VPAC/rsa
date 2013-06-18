#!/usr/bin/env python

#
# This program generates the specialised vector classes.
#

from collections import namedtuple
from string import Template

import Element_types


CLASS_HEADER_TEMPLATE = Template("""
package org.vpac.ndg.query.math;

import java.util.Arrays;

import ucar.ma2.Index;

/**
 * A short array of numbers that can be used in arithmetic.
 * 
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See VectorX_gen.py.
public class $cname {

	$ptype[] components;

	protected $cname() {
	}
	protected $cname(int size) {
		components = new $ptype[size];
	}

	public static $cname createEmpty(int capacity) {
		return new $cname(capacity);
	}

	public static $cname createEmpty(int capacity, $ptype fill) {
		$cname res = new $cname(capacity);
		for (int i = 0; i < res.components.length; i++) {
			res.components[i] = fill;
		}
		return res;
	}

	public static $cname create($ptype... components) {
		if (components.length == 0 || components.length > 4) {
			throw new IllegalArgumentException(
					"Vectors must have from 1-4 components.");
		}
		$cname res = new $cname();
		res.components = components.clone();
		return res;
	}

	public static $cname fromInt(int... components) {
		if (components.length == 0 || components.length > 4) {
			throw new IllegalArgumentException(
					"Vectors must have from 1-4 components.");
		}
		$cname res = createEmpty(components.length);
		for (int i = 0; i < components.length; i++)
			res.components[i] = components[i];
		return res;
	}

	public $cname copy() {
		$cname res = new $cname();
		res.components = this.components.clone();
		return res;
	}
""")

CLASS_FOOTER_TEMPLATE = Template("""
}
""")

SPECIAL_CAST_TEMPLATE = Template("""
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("(");
		boolean first = true;
		for ($ptype c : components) {
			if (first)
				first = false;
			else
				buf.append(", ");
			buf.append(c);
		}
		buf.append(")");
		return buf.toString();
	}

	public void set($ptype other) {
		for (int i = 0; i < components.length; i++)
			components[i] = other;
	}
	public void set(VectorReal other) {
		for (int i = 0; i < components.length; i++)
			components[i] = ${fromdouble}other.components[i];
	}
	public void set(VectorInt other) {
		for (int i = 0; i < components.length; i++)
			components[i] = ${fromlong}other.components[i];
	}

	public ${ptype}[] asArray() {
		return components;
	}

	public $ptype volume() {
		$ptype vol = 1;
		for (int i = 0; i < components.length; i++) {
			vol *= components[i];
		}
		return vol;
	}

	public int size() {
		return components.length;
	}
	public $ptype get(int i) {
		return components[i];
	}
	public void set(int i, $ptype other) {
		components[i] = other;
	}

	/**
	 * @return The component that represents time. This is always the first
	 * element.
	 */
	public $ptype getT() {
		return components[0];
	}
	/**
	 * Set the time component (first).
	 * @param value The value to assign to the component.
	 */
	public void setT($ptype value) {
		components[0] = value;
	}

	/**
	 * Finds the index pointed to by this vector in an image.
	 * 
	 * @param shape The shape of the image.
	 * @return The index of the pixel (assuming the image is stored as a 1D
	 *         array).
	 */
	public long toPixelIndex(VectorInt shape) {
		// Iterate in reverse: x, y, z, w. Later dimensions depend on the result
		// of previous ones.
		long idx = 0;
		long vol = 1;
		for (int i = components.length - 1; i >= 0; i--) {
			idx += components[i] * vol;
			vol *= shape.components[i];
		}
		return idx;
	}
""")

SPECIAL_INT_TEMPLATE = Template("""
	public VectorReal toReal() {
		VectorReal res = VectorReal.createEmpty(size());
		res.set(this);
		return res;
	}

	private int[] intComponents;
	public void fromIndex(Index ima) {
		int[] counter = ima.getCurrentCounter();
		for (int i = 0; i < components.length; i++)
			components[i] = counter[i];
	}
	public void fillIndex(Index ima) {
		ima.set(asIntArray());
	}
	public int[] asIntArray() {
		if (intComponents == null)
			intComponents = new int[components.length];
		for (int i = 0; i < components.length; i++)
			intComponents[i] = (int) components[i];
		return intComponents;
	}

    /**
     * Reinterpret the current value so it remains within the bounds of the
     * given shape. The value will be changed such that lower dimensions will
     * overflow into higher dimensions; thus this is ideal for traversing
     * scanline images.
     *
     * @param shape The shape to bound the value to
     */
	public void wrapInPlace(VectorInt shape) {
		long quotient = 0;
		// Iterate in reverse: x, y, z, w. Later dimensions depend on the result
		// of previous ones.
		for (int i = components.length - 1; i >= 0; i--) {
			long current = components[i] + quotient;
			components[i] = current % shape.components[i];
			quotient = current / shape.components[i];
		}
	}

	/**
	 * Add one to the fastest varying dimension, and then wrap according to the
	 * shape. This allows iteration over the pixels of an image with the given
	 * shape. Equivalent to adding one to the X axis and calling
	 * {@link #wrapInPlace(VectorInt) wrapInPlace}, but faster.
	 * 
	 * @param shape The dimensions of the image to iterate over.
	 */
	public void incr(VectorInt shape) {
		// Iterate in reverse: x, y, z, w. Later dimensions depend on the result
		// of previous ones.
		for (int i = components.length - 1; i >= 0; i--) {
			components[i] += 1;
			if (components[i] < shape.components[i])
				return;
			components[i] = 0;
		}
	}
""")

SPECIAL_REAL_TEMPLATE = Template("""
	public VectorInt toInt() {
		VectorInt res = VectorInt.createEmpty(size());
		res.set(this);
		return res;
	}

	/**
	 * Set this vector to the same location as an Index.
	 * 
	 * <p>
	 * Note that the Index is essentially an array of integers, which natually
	 * address a discrete set of pixels. But a real vector is ambiguous about
	 * the coordinates of the "centre" of a pixel. The convention used in
	 * rsaquery is (0.0, 0.0) - <em>not</em> (0.5, 0.5) as in some other imaging
	 * toolkits.
	 * </p>
	 * 
	 * @param ima
	 */
	public void fromIndex(Index ima) {
		int[] counter = ima.getCurrentCounter();
		for (int i = 0; i < components.length; i++)
			components[i] = counter[i];
	}
""")

ELEMENT_INDEX_OF_HEADER = Template("""
	/**
	 * @return The index of an axis, or -1 if the axis is not present.
	 */
	public int indexOf(String axis) {
		int i = -1;

		if (axis.equals("time"))
			i = 0;
""")
ELEMENT_INDEX_OF_TEMPLATE = Template("""
		else if (axis.equals("${character}"))
			i = components.length - OFFSET_${characterUpper};
""")
ELEMENT_INDEX_OF_FOOTER = Template("""

		if (i >= components.length)
			return -1;
		else
			return i;
	}
""")

ELEMENT_ACCESSOR_TEMPLATE = Template("""
	private final static int OFFSET_${characterUpper} = $index + 1;
	/**
	 * @return The ${characterUpper} component (${placement}${synonym}).
	 */
	public $ptype get${characterUpper}() {
		return components[components.length - OFFSET_${characterUpper}];
	}
	/**
	 * Set the ${characterUpper} component (${placement}${synonym}).
	 * @param value The value to assign to the component.
	 */
	public void set${characterUpper}($ptype value) {
		components[components.length - OFFSET_${characterUpper}] = value;
	}
""")

ARITHMETIC_TEMPLATE = Template("""
	/**
	 * $longname_upper this object $direction a value.
	 *
	 * @param other The value to $longname.
	 * @return The result as a new object.
	 */
	public $cname ${opname}New(long other) {
		$cname res = copy();
		return res.${opname}(other);
	}
	/**
	 * $longname_upper this object $direction a value.
	 *
	 * @param other The value to $longname.
	 * @return The result as a new object.
	 */
	public $cname ${opname}New(double other) {
		$cname res = copy();
		return res.${opname}(other);
	}
	/**
	 * $longname_upper this object $direction a value.
	 *
	 * @param other The value to $longname.
	 * @return The result as a new object.
	 */
	public $cname ${opname}New(VectorInt other) {
		$cname res = copy();
		return res.${opname}(other);
	}
	/**
	 * $longname_upper this object $direction a value.
	 *
	 * @param other The value to $longname.
	 * @return The result as a new object.
	 */
	public $cname ${opname}New(VectorReal other) {
		$cname res = copy();
		return res.${opname}(other);
	}

	/**
	 * $longname_upper this object $direction a value.
	 *
	 * @param other The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	public $cname ${opname}(long other) {
		for (int i = 0; i < components.length; i++)
			components[i] = components[i] $opchar ${fromlong}other;
		return this;
	}
	/**
	 * $longname_upper this object $direction a value.
	 *
	 * @param other The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	public $cname ${opname}(double other) {
		for (int i = 0; i < components.length; i++)
			components[i] = components[i] $opchar ${fromdouble}other;
		return this;
	}
	/**
	 * $longname_upper this object $direction a value.
	 *
	 * @param other The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	public $cname ${opname}(VectorInt other) {
		for (int i = 0; i < components.length; i++) {
			components[i] = components[i] $opchar ${fromlong}other.components[i];
		}
		return this;
	}
	/**
	 * $longname_upper this object $direction a value.
	 *
	 * @param other The value to $longname.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	public $cname ${opname}(VectorReal other) {
		for (int i = 0; i < components.length; i++) {
			components[i] = components[i] $opchar ${fromdouble}other.components[i];
		}
		return this;
	}

	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname.
	 * @param b The value to $longname $direction.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	public $cname ${opname}Of(VectorReal a, VectorReal b) {
		for (int i = 0; i < components.length; i++) {
			components[i] = ${fromdouble}(a.components[i] $opchar b.components[i]);
		}
		return this;
	}
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname.
	 * @param b The value to $longname $direction.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	public $cname ${opname}Of(VectorReal a, VectorInt b) {
		for (int i = 0; i < components.length; i++) {
			components[i] = ${fromdouble}a.components[i] $opchar ${fromlong}b.components[i];
		}
		return this;
	}
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname.
	 * @param b The value to $longname $direction.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	public $cname ${opname}Of(VectorInt a, VectorReal b) {
		for (int i = 0; i < components.length; i++) {
			components[i] = ${fromlong}a.components[i] $opchar ${fromdouble}b.components[i];
		}
		return this;
	}
	/**
	 * $longname_upper two values, storing the result in this (third) instance.
	 *
	 * @param a The value to $longname.
	 * @param b The value to $longname $direction.
	 * @return A reference to this object. Note that this method does not create
	 *         a new instance.
	 */
	public $cname ${opname}Of(VectorInt a, VectorInt b) {
		for (int i = 0; i < components.length; i++) {
			components[i] = ${fromlong}a.components[i] $opchar ${fromlong}b.components[i];
		}
		return this;
	}
""")

BOUNDING_TEMPLATE = Template("""
	public $cname ${opname}(long other) {
		for (int i = 0; i < components.length; i++) {
			if (other $opchar components[i])
				components[i] = ${fromlong}other;
		}
		return this;
	}
	public $cname ${opname}(double other) {
		for (int i = 0; i < components.length; i++) {
			if (other $opchar components[i])
				components[i] = ${fromdouble}other;
		}
		return this;
	}
	public $cname ${opname}(VectorInt other) {
		for (int i = 0; i < components.length; i++) {
			if (other.components[i] $opchar components[i])
				components[i] = ${fromlong}other.components[i];
		}
		return this;
	}
	public $cname ${opname}(VectorReal other) {
		for (int i = 0; i < components.length; i++) {
			if (other.components[i] $opchar components[i])
				components[i] = ${fromdouble}other.components[i];
		}
		return this;
	}

	public $cname ${opname}New(long other) {
		$cname res = copy();
		return res.${opname}(other);
	}
	public $cname ${opname}New(double other) {
		$cname res = copy();
		return res.${opname}(other);
	}
	public $cname ${opname}New(VectorInt other) {
		$cname res = copy();
		return res.${opname}(other);
	}
	public $cname ${opname}New(VectorReal other) {
		$cname res = copy();
		return res.${opname}(other);
	}

	public $cname ${opname}Of(long a, long b) {
		for (int i = 0; i < components.length; i++) {
			if (a $opchar b)
				components[i] = ${fromlong}a;
			else
				components[i] = ${fromlong}b;
		}
		return this;
	}
	public $cname ${opname}Of(long a, double b) {
		for (int i = 0; i < components.length; i++) {
			if (a $opchar b)
				components[i] = ${fromlong}a;
			else
				components[i] = ${fromdouble}b;
		}
		return this;
	}
	public $cname ${opname}Of(double a, long b) {
		for (int i = 0; i < components.length; i++) {
			if (a $opchar b)
				components[i] = ${fromdouble}a;
			else
				components[i] = ${fromlong}b;
		}
		return this;
	}
	public $cname ${opname}Of(double a, double b) {
		for (int i = 0; i < components.length; i++) {
			if (a $opchar b)
				components[i] = ${fromdouble}a;
			else
				components[i] = ${fromdouble}b;
		}
		return this;
	}
	public $cname ${opname}Of(long a, VectorInt b) {
		for (int i = 0; i < components.length; i++) {
			if (a $opchar b.components[i])
				components[i] = ${fromlong}a;
			else
				components[i] = ${fromlong}b.components[i];
		}
		return this;
	}
	public $cname ${opname}Of(double a, VectorInt b) {
		for (int i = 0; i < components.length; i++) {
			if (a $opchar b.components[i])
				components[i] = ${fromdouble}a;
			else
				components[i] = ${fromlong}b.components[i];
		}
		return this;
	}
	public $cname ${opname}Of(long a, VectorReal b) {
		for (int i = 0; i < components.length; i++) {
			if (a $opchar b.components[i])
				components[i] = ${fromlong}a;
			else
				components[i] = ${fromdouble}b.components[i];
		}
		return this;
	}
	public $cname ${opname}Of(double a, VectorReal b) {
		for (int i = 0; i < components.length; i++) {
			if (a $opchar b.components[i])
				components[i] = ${fromdouble}a;
			else
				components[i] = ${fromdouble}b.components[i];
		}
		return this;
	}
	public $cname ${opname}Of(VectorInt a, long b) {
		for (int i = 0; i < components.length; i++) {
			if (a.components[i] $opchar b)
				components[i] = ${fromlong}a.components[i];
			else
				components[i] = ${fromlong}b;
		}
		return this;
	}
	public $cname ${opname}Of(VectorInt a, double b) {
		for (int i = 0; i < components.length; i++) {
			if (a.components[i] $opchar b)
				components[i] = ${fromlong}a.components[i];
			else
				components[i] = ${fromdouble}b;
		}
		return this;
	}
	public $cname ${opname}Of(VectorReal a, long b) {
		for (int i = 0; i < components.length; i++) {
			if (a.components[i] $opchar b)
				components[i] = ${fromdouble}a.components[i];
			else
				components[i] = ${fromlong}b;
		}
		return this;
	}
	public $cname ${opname}Of(VectorReal a, double b) {
		for (int i = 0; i < components.length; i++) {
			if (a.components[i] $opchar b)
				components[i] = ${fromdouble}a.components[i];
			else
				components[i] = ${fromdouble}b;
		}
		return this;
	}
	public $cname ${opname}Of(VectorInt a, VectorInt b) {
		for (int i = 0; i < components.length; i++) {
			if (a.components[i] $opchar b.components[i])
				components[i] = ${fromlong}a.components[i];
			else
				components[i] = ${fromlong}b.components[i];
		}
		return this;
	}
	public $cname ${opname}Of(VectorInt a, VectorReal b) {
		for (int i = 0; i < components.length; i++) {
			if (a.components[i] $opchar b.components[i])
				components[i] = ${fromlong}a.components[i];
			else
				components[i] = ${fromdouble}b.components[i];
		}
		return this;
	}
	public $cname ${opname}Of(VectorReal a, VectorInt b) {
		for (int i = 0; i < components.length; i++) {
			if (a.components[i] $opchar b.components[i])
				components[i] = ${fromdouble}a.components[i];
			else
				components[i] = ${fromlong}b.components[i];
		}
		return this;
	}
	public $cname ${opname}Of(VectorReal a, VectorReal b) {
		for (int i = 0; i < components.length; i++) {
			if (a.components[i] $opchar b.components[i])
				components[i] = ${fromdouble}a.components[i];
			else
				components[i] = ${fromdouble}b.components[i];
		}
		return this;
	}
""")

SPECIAL_BOUNDING_TEMPLATE = Template("""
	public $cname clamp(long min, long max) {
		for (int i = 0; i < components.length; i++) {
			if (components[i] < min)
				components[i] = ${fromlong}min;
			else if (components[i] > max)
				components[i] = ${fromlong}max;
		}
		return this;
	}
	public $cname clamp(double min, double max) {
		for (int i = 0; i < components.length; i++) {
			if (components[i] < min)
				components[i] = ${fromdouble}min;
			else if (components[i] > max)
				components[i] = ${fromdouble}max;
		}
		return this;
	}
	public $cname clamp(VectorInt min, VectorInt max) {
		for (int i = 0; i < components.length; i++) {
			if (components[i] < min.components[i])
				components[i] = ${fromlong}min.components[i];
			else if (components[i] > max.components[i])
				components[i] = ${fromlong}max.components[i];
		}
		return this;
	}
	public $cname clamp(VectorReal min, VectorReal max) {
		for (int i = 0; i < components.length; i++) {
			if (components[i] < min.components[i])
				components[i] = ${fromdouble}min.components[i];
			else if (components[i] > max.components[i])
				components[i] = ${fromdouble}max.components[i];
		}
		return this;
	}

	public $cname clampNew(long min, long max) {
		$cname res = copy();
		return res.clamp(min, max);
	}
	public $cname clampNew(double min, double max) {
		$cname res = copy();
		return res.clamp(min, max);
	}
	public $cname clampNew(VectorInt min, VectorInt max) {
		$cname res = copy();
		return res.clamp(min, max);
	}
	public $cname clampNew(VectorReal min, VectorReal max) {
		$cname res = copy();
		return res.clamp(min, max);
	}
""")

COMPARISON_TEMPLATE = Template("""
	public boolean equals($cname other) {
		try {
			return Arrays.equals(components, other.components);
		} catch (NullPointerException e) {
			return false;
		}
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if ($cname.class != obj.getClass())
			return false;
		$cname other = ($cname) obj;
		return Arrays.equals(components, other.components);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(components);
	}
""")

def write_class(output, t):

	mapping = {
			"cname": t.formal_name,
			"ptype": t.primitive_name,
			}

	for u in TYPES:
		cast_to_key = "to%s" % u.primitive_name
		cast_from_key = "from%s" % u.primitive_name
		if t.primitive_name != u.primitive_name:
			mapping[cast_to_key] = "(%s)" % u.primitive_name
			mapping[cast_from_key] = "(%s)" % t.primitive_name
		else:
			mapping[cast_to_key] = ""
			mapping[cast_from_key] = ""

	output.write(CLASS_HEADER_TEMPLATE.substitute(mapping))
	output.write("\n	// CASTING\n")
	output.write(SPECIAL_CAST_TEMPLATE.substitute(mapping))

	for character, index, placement, syn in Element_types.ELEMENT_NAMES:
		local_mapping = {
				"character": character,
				"characterUpper": character.upper(),
				"index": index,
				"placement": placement,
				"synonym": "; synonym for %s" % syn,
				}
		local_mapping.update(mapping)
		output.write(ELEMENT_ACCESSOR_TEMPLATE.substitute(local_mapping))

	output.write(ELEMENT_INDEX_OF_HEADER.substitute(mapping))
	for character, index, placement, syn in Element_types.ELEMENT_NAMES:
		local_mapping = {
				"character": character,
				"characterUpper": character.upper(),
				"index": index,
				"placement": placement,
				"synonym": "; synonym for %s" % syn,
				}
		local_mapping.update(mapping)
		output.write(ELEMENT_INDEX_OF_TEMPLATE.substitute(local_mapping))
	output.write(ELEMENT_INDEX_OF_FOOTER.substitute(mapping))

	output.write(t.specials.substitute(mapping))

	output.write("\n	// ARITHMETIC\n")
	for opname, opchar, longname, direction in Element_types.ARITHMETIC_OPS:
		output.write(ARITHMETIC_TEMPLATE.substitute(mapping, opname=opname,
				opchar=opchar, longname=longname,
				longname_upper=longname.capitalize(), direction=direction))

	output.write("\n	// BOUNDING\n")
	for opname, opchar, longname in Element_types.BOUNDING_OPS:
		output.write(BOUNDING_TEMPLATE.substitute(mapping, opname=opname, opchar=opchar, longname=longname))
	output.write(SPECIAL_BOUNDING_TEMPLATE.substitute(mapping))

	output.write(COMPARISON_TEMPLATE.substitute(mapping))
	output.write(CLASS_FOOTER_TEMPLATE.substitute(mapping))


T = namedtuple("T", "formal_name primitive_name specials")
TYPES = [
		T("VectorInt", "long", SPECIAL_INT_TEMPLATE),
		T("VectorReal", "double", SPECIAL_REAL_TEMPLATE),
		]

if __name__ == "__main__":
	for t in TYPES:
		file_name = t.formal_name + ".java"
		print "Writing", file_name
		with open(file_name, 'w') as f:
			write_class(f, t)

