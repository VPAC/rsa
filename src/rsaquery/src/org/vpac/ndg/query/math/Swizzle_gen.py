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
# This program generates Swizzle classes.
#

from string import Template

import Element_types

# Code

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

// THIS IS GENERATED CODE. Do not modify this file. See Swizzle_gen.py.

package org.vpac.ndg.query.math;

import java.util.List;

/**
 * Transfers values from one vector to another, rearranging the components along
 * the way.
 *
 * <pre>
 *     VectorLong source = VectorLong.create(1, 2);
 *     VectorLong target = VectorLong.createEmpty(3); // target == (0, 0, 0)
 *     Swizzle swiz = SwizzleFactory.compile("xxy");
 *     swiz.swizzle(source, target); // target == (2, 2, 1)
 * </pre>
 * @author Alex Fraser
 */
public abstract class Swizzle {

    /**
	 * Reorganise the components of a vec	
	 *
	 * @param in The vector to use as the source.
	 * @param out The vector to write to. It must have the same number of
	 *        components as was specified when creating this swizzle instance.
	 */
	public abstract void swizzle(VectorInt in, VectorInt out);

	/**
	 * Reorganise the components of a vector.
	 *
	 * @param in The vector to use as the source.
	 * @param out The vector to write to. It must have the same number of
	 *        components as was specified when creating this swizzle instance.
	 */
	public abstract void swizzle(VectorReal in, VectorReal out);

	/**
	 * Reorganise the components of a vector. The type of each component will
	 * not be changed; the source values (in) will be converted into to the
	 * types of the target (out).
	 *
	 * @param in The vector to use as the source.
	 * @param out The vector to write to. It must have the same number of
	 *        components as was specified when creating this swizzle instance.
	 */
	public abstract void swizzle(VectorElement in, VectorElement out);

	/**
	 * Reorganise the components of a box.
	 *
	 * @param in The box to use as the source.
	 * @param out The box to write to. It must have the same number of
	 *        dimensions as was specified when creating this swizzle instance.
	 */
	public void swizzle(BoxInt in, BoxInt out) {
		swizzle(in.getMin(), out.getMin());
		swizzle(in.getMax(), out.getMax());
	}

	/**
	 * Reorganise the components of a box.
	 *
	 * @param in The box to use as the source.
	 * @param out The box to write to. It must have the same number of
	 *        dimensions as was specified when creating this swizzle instance.
	 */
	public void swizzle(BoxReal in, BoxReal out) {
		swizzle(in.getMin(), out.getMin());
		swizzle(in.getMax(), out.getMax());
	}


	/**
	 * Abstracts component access from a vector.
	 * @author Alex Fraser
	 */
	interface SwizzleOp {
		public long get(VectorInt from);
		public double get(VectorReal from);
		public ScalarElement get(VectorElement from);

		public void set(VectorInt to, long value);
		public void set(VectorReal to, double value);
		public void set(VectorElement to, ScalarElement value);
	}

	static class SwizzleOp0 implements SwizzleOp {

		ScalarElement prototype = new ElementInt(0);

		@Override
		public long get(VectorInt from) {
			return 0;
		}

		@Override
		public double get(VectorReal from) {
			return 0.0;
		}

		@Override
		public ScalarElement get(VectorElement from) {
			return prototype;
		}

		@Override
		public String toString() {
			return "0";
		}

		@Override
		public void set(VectorInt to, long value) {
			// Nothing to do for this virtual component.
		}

		@Override
		public void set(VectorReal to, double value) {
			// Nothing to do for this virtual component.
		}

		@Override
		public void set(VectorElement to, ScalarElement value) {
			// Nothing to do for this virtual component.
		}
	}

	static class SwizzleOp1 implements SwizzleOp {

		ScalarElement prototype = new ElementInt(1);

		@Override
		public long get(VectorInt from) {
			return 1;
		}

		@Override
		public double get(VectorReal from) {
			return 1.0;
		}

		@Override
		public ScalarElement get(VectorElement from) {
			return prototype;
		}

		@Override
		public String toString() {
			return "1";
		}

		@Override
		public void set(VectorInt to, long value) {
			// Nothing to do for this virtual component.
		}

		@Override
		public void set(VectorReal to, double value) {
			// Nothing to do for this virtual component.
		}

		@Override
		public void set(VectorElement to, ScalarElement value) {
			// Nothing to do for this virtual component.
		}
	}

""")

CLASS_FOOTER_TEMPLATE = Template("""
}
""")


GET_OPERATION_CHAR_TEMPLATE = Template("""
	static SwizzleOp getOperation(char axis) {
		switch (axis) {
		case '0':
			return new SwizzleOp0();
		case '1':
			return new SwizzleOp1();
${operations}
		default:
			throw new IllegalArgumentException(String.format(
					"Invalid swizzle axis specified: \\"%c\\".", axis));
		}
	}
""")
GET_OPERATION_CHAR_SINGLE_TEMPLATE = Template("""
		case '${opchar}':
			return new SwizzleOp${opcharupper}();
""")


GET_OPERATION_STR_TEMPLATE = Template("""
	static SwizzleOp getOperation(String dimension) {
		if (dimension.equals("0") || dimension.equals("zero"))
			return new SwizzleOp0();
		else if (dimension.equals("1") || dimension.equals("one"))
			return new SwizzleOp1();
		else if (dimension.equals("t") || dimension.equals("time"))
			return new SwizzleOpT();
${operations}
		else {
			throw new IllegalArgumentException(String.format(
					"Invalid swizzle dimension specified: \\"%s\\".", dimension));
		}
	}
""")
GET_OPERATION_STR_SINGLE_TEMPLATE = Template("""
		else if (dimension.equals("${opchar}"))
			return new SwizzleOp${opcharupper}();
""")


SWIZZLE_TEMPLATE = Template("""
	static class Swizzle${rank} extends Swizzle {
${declarations}

		public Swizzle${rank}(List<SwizzleOp> from, List<SwizzleOp> to) {
${constructor}
		}

		@Override
		public void swizzle(VectorInt in, VectorInt out) {
${apply}
		}

		@Override
		public void swizzle(VectorReal in, VectorReal out) {
${apply}
		}

		@Override
		public void swizzle(VectorElement in, VectorElement out) {
${apply}
		}

		@Override
		public String toString() {
			String fromstr = "";
			String tostr = "";
${formatbuilder}
			return String.format("Swizzle(%s -> %s)", fromstr, tostr);
		}
	}
""")
SWIZZLE_DECL_TEMPLATE = Template("""
		SwizzleOp from${opnum};
		SwizzleOp to${opnum};""")
SWIZZLE_DECL_GENERIC = """
		SwizzleOp[] from;
		SwizzleOp[] to;"""
SWIZZLE_CTOR_TEMPLATE = Template("""
			from${opnum} = from.get(${opnum});
			to${opnum} = to.get(${opnum});""")
SWIZZLE_CTOR_GENERIC = """
			this.from = new SwizzleOp[from.size()];
			this.to = new SwizzleOp[to.size()];
			for (int i = 0; i < from.size(); i++) {
				this.from[i] = from.get(i);
				this.to[i] = to.get(i);
			}"""
SWIZZLE_APPLY_TEMPLATE = Template("""
			to${opnum}.set(out, from${opnum}.get(in));""")
SWIZZLE_APPLY_GENERIC = """
			for (int i = 0; i < from.length; i++) {
				to[i].set(out, from[i].get(in));
			}"""
SWIZZLE_STR_TEMPLATE = Template("""
			fromstr += from${opnum};
			tostr += to${opnum};""")
SWIZZLE_STR_GENERIC = """
			for (int i = 0; i < from.length; i++) {
				fromstr += from[i];
				tostr += to[i];
			}"""



OPERATION_TEMPLATE = Template("""
	static class SwizzleOp${opcharupper} implements SwizzleOp {

		@Override
		public long get(VectorInt from) {
			return from.get${opcharupper}();
		}

		@Override
		public double get(VectorReal from) {
			return from.get${opcharupper}();
		}

		@Override
		public ScalarElement get(VectorElement from) {
			return from.get${opcharupper}();
		}

		@Override
		public String toString() {
			return "${opchar}";
		}

		@Override
		public void set(VectorInt to, long value) {
			to.set${opcharupper}(value);
		}

		@Override
		public void set(VectorReal to, double value) {
			to.set${opcharupper}(value);
		}

		@Override
		public void set(VectorElement from, ScalarElement value) {
			from.set${opcharupper}(value);
		}
	}
""")

def write_op_factory_method(output, components):
    operations = ""
    for component in components:
        mapping = {
                   "opchar": component,
                   "opcharupper": component.upper()
                   }
        operations += GET_OPERATION_CHAR_SINGLE_TEMPLATE.substitute(mapping)
    mapping = {"operations" : operations}
    output.write(GET_OPERATION_CHAR_TEMPLATE.substitute(mapping))

    operations = ""
    for component in components:
        if component == "t":
            # Skip time; it needs special (hard-coded) formatting.
            continue
        mapping = {
                   "opchar": component,
                   "opcharupper": component.upper()
                   }
        operations += GET_OPERATION_STR_SINGLE_TEMPLATE.substitute(mapping)
    mapping = {"operations" : operations}
    output.write(GET_OPERATION_STR_TEMPLATE.substitute(mapping))

def write_operations(output, components):
    for component in components:
        mapping = {
                   "opchar": component,
                   "opcharupper": component.upper()
                   }
        output.write(OPERATION_TEMPLATE.substitute(mapping))

def write_swizzles(output, ranks):
    for i in ranks:
        declaration = ""
        construction = ""
        application = ""
        formatbuilder = ""
        for j in xrange(i):
            mapping = {
                       "opnum": str(j)
                       }
            declaration += SWIZZLE_DECL_TEMPLATE.substitute(mapping)
            construction += SWIZZLE_CTOR_TEMPLATE.substitute(mapping)
            application += SWIZZLE_APPLY_TEMPLATE.substitute(mapping)
            formatbuilder += SWIZZLE_STR_TEMPLATE.substitute(mapping)
        mapping = {
                   "rank" : str(i),
                   "declarations" : declaration,
                   "constructor" : construction,
                   "apply" : application,
                   "formatbuilder" : formatbuilder
                   }
        output.write(SWIZZLE_TEMPLATE.substitute(mapping))

    mapping = {
               "rank" : "N",
               "declarations" : SWIZZLE_DECL_GENERIC,
               "constructor" : SWIZZLE_CTOR_GENERIC,
               "apply" : SWIZZLE_APPLY_GENERIC,
               "formatbuilder" : SWIZZLE_STR_GENERIC
               }
    output.write(SWIZZLE_TEMPLATE.substitute(mapping))


def write_class(output):

    output.write(CLASS_HEADER_TEMPLATE.substitute({}))

    components = []
    for name, _, _, _ in Element_types.ELEMENT_NAMES:
        components.append(name)
    components.append("t")

    write_op_factory_method(output, components)
    write_operations(output, components)
    write_swizzles(output, [0, 1, 2, 3, 4])

    output.write(CLASS_FOOTER_TEMPLATE.substitute())


if __name__ == "__main__":
    print "Writing", "Swizzle.java"
    with open("Swizzle.java", 'w') as f:
        write_class(f)

