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
# This program generates the numeric Element classes.
#

from collections import namedtuple

ARITHMETIC_OPS = [
		("add", "+", "add", "to"),
		("sub", "-", "subtract", "from"),
		("mul", "*", "multiply", "with"),
		("div", "/", "divide", "by"),
		("mod", "%", "modulo", "by"),
		]

BOUNDING_OPS = [
		("min", "<", "minimum"),
		("max", ">", "maximum"),
		]

T = namedtuple("T", "formal_name primitive_name boxed_name format_spec special_functions")
TYPES = [
		T("ElementByte", "byte", "Byte", "%d", """
	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + value;
	}
"""),

		T("ElementShort", "short", "Short", "%d", """
	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + value;
	}
"""),

		T("ElementInt", "int", "Integer", "%d", """
	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + value;
	}
"""),

		T("ElementLong", "long", "Long", "%d", """
	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + (int) (value ^ (value >>> 32));
	}
"""),

		T("ElementFloat", "float", "Float", "%g", """
	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + Float.floatToIntBits(value);
	}
"""),

		T("ElementDouble", "double", "Double", "%g", """
	@Override
	public int hashCode() {
		final int prime = 31;
		long temp;
		temp = Double.doubleToLongBits(value);
		return prime + (int) (temp ^ (temp >>> 32));
	}
"""),
		]


ELEMENT_NAMES = [
		("x", 0, "last", "a"),
		("y", 1, "second last", "b"),
		("z", 2, "third last", "c"),
		("w", 3, "fourth last", "d"),

		("a", 0, "last", "x"),
		("b", 1, "second last", "y"),
		("c", 2, "third last", "z"),
		("d", 3, "fourth last", "w"),
		("e", 4, "fifth last", None),
		("f", 5, "sixth last", None),
		("g", 6, "seventh last", None),
		("h", 7, "eighth last", None),
		("i", 8, "ninth last", None),
		("j", 9, "tenth last", None),

		# t is special (always first). Don't specify it here.

		]
