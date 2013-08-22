/*
 * This file is part of SpatialCube.
 *
 * SpatialCube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SpatialCube is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SpatialCube.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.query.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vpac.ndg.query.math.Swizzle.Swizzle0;
import org.vpac.ndg.query.math.Swizzle.Swizzle1;
import org.vpac.ndg.query.math.Swizzle.Swizzle2;
import org.vpac.ndg.query.math.Swizzle.Swizzle3;
import org.vpac.ndg.query.math.Swizzle.Swizzle4;
import org.vpac.ndg.query.math.Swizzle.SwizzleN;
import org.vpac.ndg.query.math.Swizzle.SwizzleOp;

public class SwizzleFactory {

	/**
	 * @return A new Swizzle instance that can transform vectors.
	 *
	 * @param format The arrangement of components to use. Must be 1-4
	 *        characters long and use only '0', 'x', 'y', 'z' and 't'.
	 *        Characters may be repeated, e.g. "xyx", which returns a
	 *        3-component vector. Note that the natural order of a vector is
	 *        wzyx. '0' is a special component that is always set to zero; '1'
	 *        is always set to one.
	 */
	public static Swizzle compile(String format) {
		// By default, write to all axes.
		final String defaultTarget = "wzyx";
		int offset = defaultTarget.length() - format.length();
		String target = defaultTarget.substring(offset);
		return compile(format, target);
	}

	public static Swizzle compile(String formatSrc, String formatTgt) {
		List<String> fsa = new ArrayList<String>();
		for (int i = 0; i < formatSrc.length(); i++)
			fsa.add(formatSrc.substring(i, i + 1));

		List<String> fta = new ArrayList<String>();
		for (int i = 0; i < formatTgt.length(); i++)
			fta.add(formatTgt.substring(i, i + 1));

		try {
			return compile(fsa, fta);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(String.format(
					"Swizzle \"%s\" -> \"%s\": %s", formatSrc, formatTgt,
					e.getMessage()));
		}
	}

	public static Swizzle compileLong(String format) {
		// By default, write to all axes.
		final String defaultTarget = "wzyx";
		int offset = defaultTarget.length() - format.length();
		String target = defaultTarget.substring(offset);

		String[] dimSrc = format.split(" ");
		String[] dimTgt = target.split(" ");
		try {
			return compile(Arrays.asList(dimSrc), Arrays.asList(dimTgt));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(String.format(
					"Swizzle \"%s\": %s", format, e.getMessage()));
		}
	}

	/**
	 * Compile a swizzle from dimensions string.
	 * @param formatSrc The source axes as a space-delimited list of dimensions, e.g. "time x y".
	 * @param formatTgt The target axes.
	 * @return The new swizzle object.
	 */
	public static Swizzle compileLong(String formatSrc, String formatTgt) {
		String[] dimSrc = formatSrc.split(" ");
		String[] dimTgt = formatTgt.split(" ");
		try {
			return compile(Arrays.asList(dimSrc), Arrays.asList(dimTgt));
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(String.format(
					"Swizzle \"%s\" -> \"%s\": %s", formatSrc, formatTgt,
					e.getMessage()));
		}
	}

	public static Swizzle compile(List<String> sources, List<String> targets) {
		if (sources.size() != targets.size()) {
			throw new IllegalArgumentException(
					"Source has a different length to the target.");
		}

		// Get a list of operations.
		List<SwizzleOp> ops = new ArrayList<SwizzleOp>(4);
		List<SwizzleOp> targetOps = new ArrayList<SwizzleOp>(4);

		for (int i = 0; i < sources.size(); i++) {
			targetOps.add(Swizzle.getOperation(targets.get(i)));
			ops.add(Swizzle.getOperation(sources.get(i)));
		}

		return collate(ops, targetOps);
	}

	protected static Swizzle collate(List<SwizzleOp> sourceOps,
			List<SwizzleOp> targetOps) {

		switch (sourceOps.size()) {
		case 0:
			return new Swizzle0(sourceOps, targetOps);
		case 1:
			return new Swizzle1(sourceOps, targetOps);
		case 2:
			return new Swizzle2(sourceOps, targetOps);
		case 3:
			return new Swizzle3(sourceOps, targetOps);
		case 4:
			return new Swizzle4(sourceOps, targetOps);
		default:
			return new SwizzleN(sourceOps, targetOps);
		}
	}

	/**
	 * Create a swizzle object performs resize operations. If the new size is
	 * less than the original, the higher dimensions are simply discarded. X is
	 * the lowest dimension. If the new size is greater than the original, the
	 * components that were missing in the original are set to
	 * <em>defaultAxis</em>.
	 *
	 * @param originalSize The size of the source object.
	 * @param newSize The size of the target object.
	 * @param defaultAxis The axis to use for axes in the target that are out of
	 *        range in the source (i.e. when the target is larger than the
	 *        source).
	 * @return The new swizzle object.
	 * @see #resize(int, int)
	 */
	public static Swizzle resize(int originalSize, int newSize, char defaultAxis) {
		// Note: this string is reversed before compilation.
		final String inputDims = "xyzw";
		StringBuilder def = new StringBuilder(5);
		for (int i = 0; i < newSize; i++) {
			if (i < originalSize)
				def.append(inputDims.charAt(i));
			else
				def.append(defaultAxis);
		}
		def.reverse();
		return compile(def.toString(), def.toString());
	}

	/**
	 * As {@link #resize(int, int, char)}, using '0' for missing axes.
	 * @param originalSize The size of the source object.
	 * @param newSize The size of the target object.
	 * @return The new swizzle object.
	 * @see #resize(int, int, char)
	 */
	public static Swizzle resize(int originalSize, int newSize) {
		return resize(originalSize, newSize, '0');
	}

}
