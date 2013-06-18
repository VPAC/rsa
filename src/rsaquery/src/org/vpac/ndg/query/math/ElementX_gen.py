#!/usr/bin/env python

#
# This program generates the scalar Element classes.
#

from string import Template

import Element_types

# Code

CLASS_HEADER_TEMPLATE = Template("""
package org.vpac.ndg.query.math;

/**
 * A pixel that stores a $ptype value.
 *
 * <p><img src="doc-files/Element_class.png" /></p>
 *
 * @author Alex Fraser
 */
// THIS IS GENERATED CODE. Do not modify this file. See ElementX_gen.py.
public class $cname implements ScalarElement {
	private $ptype value;
	private boolean valid;

	/**
	 * Create a new $cname, initalised to zero.
	 */
	public $cname() {
		this.value = 0;
		this.valid = true;
	}
	/**
	 * Create a new $cname.
	 * @param value The initial value for the element.
	 */
	public $cname($ptype value) {
		this.value = value;
		this.valid = true;
	}
	@Override
	public $cname copy() {
		$cname res = new $cname(value);
		res.valid = valid;
		return res;
	}
	@Override
	public ScalarElement[] getComponents() {
		return new ScalarElement[] { this };
	}

	@Override
	public boolean isValid() {
		return valid;
	}
	@Override
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public $boxtype getValue() {
		return value;
	}
""")

CLASS_FOOTER_TEMPLATE = Template("""
${specials}

	@Override
	public String toString() {
		return String.format("$formatspec", value);
	}
}
""")

SPECIAL_CAST_TEMPLATE = Template("""
	@Override
	public $cname set(Element<?> value) {
		this.value = ((ScalarElement)value).${ptype}Value();
		this.valid = value.isValid();
		return this;
	}
	@Override
	public $cname set(Number value) {
		this.value = value.${ptype}Value();
		this.valid = true;
		return this;
	}
""")

CAST_TEMPLATE = Template("""
	@Override
	public ${local_ptype} ${local_ptype}Value() {
		return ${castto}value;
	}
	@Override
	public $cname set(${local_ptype} value) {
		this.value = ${castfrom}value;
		this.valid = true;
		return this;
	}
	@Override
	public $local_cname as${local_ftype}() {
		$local_cname res = new $local_cname(this.${local_ptype}Value());
		res.setValid(valid);
		return res;
	}
""")

ARITHMETIC_TEMPLATE = Template("""
	@Override
	public $cname ${opname}(long other) {
		try {
			value = ($ptype)(value $opchar ${fromlong}other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public $cname ${opname}(double other) {
		try {
			value = ($ptype)(value $opchar ${fromdouble}other);
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @throws ClassCastException if other is a vector.
	 */
	@Override
	public $cname ${opname}(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		try {
			value = ($ptype)(value $opchar ((ScalarElement)other).${ptype}Value());
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}

	@Override
	public $cname ${opname}New(long other) {
		$cname res = copy();
		return res.${opname}(other);
	}
	@Override
	public $cname ${opname}New(double other) {
		$cname res = copy();
		return res.${opname}(other);
	}
	@Override
	public $cname ${opname}New(Element<?> other) {
		$cname res = copy();
		return res.${opname}(other);
	}

	@Override
	public $cname ${opname}Of(long a, long b) {
		try {
			value = ($ptype)(a $opchar b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public $cname ${opname}Of(double a, long b) {
		try {
			value = ($ptype)(${fromdouble}a $opchar ${fromlong}b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public $cname ${opname}Of(long a, double b) {
		try {
			value = ($ptype)(${fromlong}a $opchar ${fromdouble}b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	@Override
	public $cname ${opname}Of(double a, double b) {
		try {
			value = ($ptype)(${fromlong}a $opchar ${fromdouble}b);
			valid = true;
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to $ptype before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public $cname ${opname}Of(Element<?> a, long b) {
		try {
			$ptype av = ((ScalarElement)a).${ptype}Value();
			value = ($ptype)(av $opchar b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to $ptype before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public $cname ${opname}Of(long a, Element<?> b) {
		try {
			$ptype bv = ((ScalarElement)b).${ptype}Value();
			value = ($ptype)(a $opchar bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to $ptype before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public $cname ${opname}Of(Element<?> a, double b) {
		try {
			$ptype av = ((ScalarElement)a).${ptype}Value();
			value = ($ptype)(av $opchar b);
			valid = a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param b This will be converted to $ptype before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public $cname ${opname}Of(double a, Element<?> b) {
		try {
			$ptype bv = ((ScalarElement)b).${ptype}Value();
			value = ($ptype)(a $opchar bv);
			valid = b.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
	/**
	 * @param a This will be converted to $ptype before the operation.
	 * @param b This will be converted to $ptype before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public $cname ${opname}Of(Element<?> a, Element<?> b) {
		try {
			$ptype av = ((ScalarElement)a).${ptype}Value();
			$ptype bv = ((ScalarElement)b).${ptype}Value();
			value = ($ptype)(av $opchar bv);
			valid = b.isValid() && a.isValid();
		} catch (ArithmeticException e) {
			this.setValid(false);
		}
		return this;
	}
""")

BOUNDING_TEMPLATE = Template("""
	@Override
	public $cname ${opname}(long other) {
		if (other $opchar value)
			value = ${fromlong}other;
		return this;
	}
	@Override
	public $cname ${opname}(double other) {
		if (other $opchar value)
			value = ${fromdouble}other;
		return this;
	}
	@Override
	public $cname ${opname}(Element<?> other) {
		if (!other.isValid()) {
			this.setValid(false);
			return this;
		}
		if (((ScalarElement)other).${ptype}Value() $opchar value)
			value = ((ScalarElement)other).${ptype}Value();
		return this;
	}

	@Override
	public $cname ${opname}New(long other) {
		$cname res = copy();
		return res.${opname}(other);
	}
	@Override
	public $cname ${opname}New(double other) {
		$cname res = copy();
		return res.${opname}(other);
	}
	@Override
	public $cname ${opname}New(Element<?> other) {
		$cname res = copy();
		return res.${opname}(other);
	}

	@Override
	public $cname ${opname}Of(long a, long b) {
		if (a $opchar b)
			value = ($ptype)a;
		else
			value = ($ptype)b;
		valid = true;
		return this;
	}
	@Override
	public $cname ${opname}Of(double a, long b) {
		if (a $opchar b)
			value = ($ptype)a;
		else
			value = ($ptype)b;
		valid = true;
		return this;
	}
	@Override
	public $cname ${opname}Of(long a, double b) {
		if (a $opchar b)
			value = ($ptype)a;
		else
			value = ($ptype)b;
		valid = true;
		return this;
	}
	@Override
	public $cname ${opname}Of(double a, double b) {
		if (a $opchar b)
			value = ($ptype)a;
		else
			value = ($ptype)b;
		valid = true;
		return this;
	}
	/**
	 * @param a This will be converted to $ptype before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public $cname ${opname}Of(Element<?> a, long b) {
		$ptype av = ((ScalarElement)a).${ptype}Value();
		if (av $opchar b)
			value = ($ptype)av;
		else
			value = ($ptype)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to $ptype before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public $cname ${opname}Of(long a, Element<?> b) {
		$ptype bv = ((ScalarElement)b).${ptype}Value();
		if (a $opchar bv)
			value = ($ptype)a;
		else
			value = ($ptype)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to $ptype before the operation.
	 * @throws ClassCastException if a is a vector.
	 */
	@Override
	public $cname ${opname}Of(Element<?> a, double b) {
		$ptype av = ((ScalarElement)a).${ptype}Value();
		if (av $opchar b)
			value = ($ptype)av;
		else
			value = ($ptype)b;
		valid = a.isValid();
		return this;
	}
	/**
	 * @param b This will be converted to $ptype before the operation.
	 * @throws ClassCastException if b is a vector.
	 */
	@Override
	public $cname ${opname}Of(double a, Element<?> b) {
		$ptype bv = ((ScalarElement)b).${ptype}Value();
		if (a $opchar bv)
			value = ($ptype)a;
		else
			value = ($ptype)bv;
		valid = b.isValid();
		return this;
	}
	/**
	 * @param a This will be converted to $ptype before the operation.
	 * @param b This will be converted to $ptype before the operation.
	 * @throws ClassCastException if a or b are vectors.
	 */
	@Override
	public $cname ${opname}Of(Element<?> a, Element<?> b) {
		$ptype av = ((ScalarElement)a).${ptype}Value();
		$ptype bv = ((ScalarElement)b).${ptype}Value();
		if (av $opchar bv)
			value = ($ptype)av;
		else
			value = ($ptype)bv;
		valid = a.isValid() && b.isValid();
		return this;
	}
""")

SPECIAL_BOUNDING_TEMPLATE = Template("""
	@Override
	public $cname clamp(long min, long max) {
		if (value < min)
			value = ${fromlong}min;
		else if (value > max)
			value = ${fromlong}max;
		return this;
	}
	@Override
	public $cname clamp(double min, double max) {
		if (value < min)
			value = ${fromdouble}min;
		else if (value > max)
			value = ${fromdouble}max;
		return this;
	}
	@Override
	public $cname clamp(Element<?> min, Element<?> max) {
		if (!min.isValid() || !max.isValid()) {
			this.setValid(false);
			return this;
		}
		// This may throw an exception - but that's OK, because VectorElements
		// are not comparable.
		ScalarElement minS = (ScalarElement)min;
		ScalarElement maxS = (ScalarElement)max;
		if (value < minS.${ptype}Value())
			value = minS.${ptype}Value();
		else if (value > maxS.${ptype}Value())
			value = maxS.${ptype}Value();
		return this;
	}

	@Override
	public $cname clampNew(long min, long max) {
		$cname res = copy();
		return res.clamp(min, max);
	}
	@Override
	public $cname clampNew(double min, double max) {
		$cname res = copy();
		return res.clamp(min, max);
	}
	@Override
	public $cname clampNew(Element<?> min, Element<?> max) {
		$cname res = copy();
		return res.clamp(min, max);
	}
""")

COMPARISON_TEMPLATE = Template("""
	@Override
	public int compareTo(long other) {
		if (value > other)
			return 1;
		else if (other == value)
			return 0;
		else
			return -1;
	}
	@Override
	public int compareTo(double other) {
		if (value > other)
			return 1;
		else if (other == value)
			return 0;
		else
			return -1;
	}
	@Override
	public int compareTo(ScalarElement other) {
		if (ElementByte.class == other.getClass()) {
			if (value > other.byteValue())
				return 1;
			else if (value == other.byteValue())
				return 0;
			else
				return -1;

		} else if (ElementShort.class == other.getClass()) {
			if (value > other.shortValue())
				return 1;
			else if (value == other.shortValue())
				return 0;
			else
				return -1;

		} else if (ElementInt.class == other.getClass()) {
			if (value > other.intValue())
				return 1;
			else if (value == other.intValue())
				return 0;
			else
				return -1;

		} else if (ElementLong.class == other.getClass()) {
			if (value > other.longValue())
				return 1;
			else if (value == other.longValue())
				return 0;
			else
				return -1;

		} else if (ElementFloat.class == other.getClass()) {
			if (value > other.floatValue())
				return 1;
			else if (value == other.floatValue())
				return 0;
			else
				return -1;

		} else if (ElementDouble.class == other.getClass()) {
			if (value > other.doubleValue())
				return 1;
			else if (value == other.doubleValue())
				return 0;
			else
				return -1;

		} else {
			throw new UnsupportedOperationException(String.format(
					"No known comparison for type %s", other.getClass()));
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;

		if (!ScalarElement.class.isAssignableFrom(obj.getClass()))
			return false;

		ScalarElement other = (ScalarElement) obj;

		if (ElementByte.class == obj.getClass()) {
			return value == other.byteValue();
		} else if (ElementShort.class == obj.getClass()) {
			return value == other.shortValue();
		} else if (ElementInt.class == obj.getClass()) {
			return value == other.intValue();
		} else if (ElementLong.class == obj.getClass()) {
			return value == other.longValue();
		} else if (ElementFloat.class == obj.getClass()) {
			return value == other.floatValue();
		} else if (ElementDouble.class == obj.getClass()) {
			return value == other.doubleValue();
		} else {
			throw new UnsupportedOperationException(String.format(
					"No known comparison for type %s", other.getClass()));
		}
	}
""")

def write_class(output, t):

	mapping = {
			"cname": t.formal_name,
			"ptype": t.primitive_name,
			"boxtype": t.boxed_name,
			"formatspec": t.format_spec,
			"specials": t.special_functions,
			}

	for u in Element_types.TYPES:
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
	for u in Element_types.TYPES:
		local_mapping = mapping.copy()
		local_mapping["local_cname"] = u.formal_name
		local_mapping["local_ptype"] = u.primitive_name
		local_mapping["local_ftype"] = u.primitive_name.capitalize()
		if t.primitive_name != u.primitive_name:
			local_mapping["castto"] = "(%s)" % u.primitive_name
			local_mapping["castfrom"] = "(%s)" % t.primitive_name
		else:
			local_mapping["castto"] = ""
			local_mapping["castfrom"] = ""
		output.write(CAST_TEMPLATE.substitute(local_mapping))

	output.write("\n	// ARITHMETIC\n")
	for opname, opchar, _, _ in Element_types.ARITHMETIC_OPS:
		output.write(ARITHMETIC_TEMPLATE.substitute(mapping, opname=opname, opchar=opchar))

	output.write("\n	// BOUNDING\n")
	for opname, opchar, _ in Element_types.BOUNDING_OPS:
		output.write(BOUNDING_TEMPLATE.substitute(mapping, opname=opname, opchar=opchar))
	output.write(SPECIAL_BOUNDING_TEMPLATE.substitute(mapping))

	output.write(COMPARISON_TEMPLATE.substitute(mapping))
	output.write(CLASS_FOOTER_TEMPLATE.substitute(mapping))



if __name__ == "__main__":
	for t in Element_types.TYPES:
		file_name = t.formal_name + ".java"
		print "Writing", file_name
		with open(file_name, 'w') as f:
			write_class(f, t)

