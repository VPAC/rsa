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

package org.vpac.ndg.query.iteration;

import java.util.Iterator;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Swizzle;
import org.vpac.ndg.query.math.SwizzleFactory;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Generates a series of coordinates along the outer dimension.
 * @author Alex Fraser
 */
public class Reduction implements Iterable<VectorReal> {

	VectorReal inputCoordinates;
	Swizzle vcs;
	int t;
	int length;
	ReductionIterator iter;

	public Reduction(BoxReal bounds) {
		this(bounds, true);
	}

	public Reduction(BoxReal bounds, boolean forward) {
		int dims = bounds.getRank();
		inputCoordinates = VectorReal.createEmpty(bounds.getRank());
		vcs = SwizzleFactory.resize(dims - 1, dims);
		length = (int) bounds.getSize().getT();

		if (forward)
			iter = new ForwardReductionIterator(bounds.getMin().getT() + 0.5);
		else
			iter = new ReverseReductionIterator(bounds.getMax().getT() - 0.5);
	}

	public VectorReal getSingle(VectorReal outputCoordinates, double time) {
		vcs.swizzle(outputCoordinates, inputCoordinates);
		inputCoordinates.setT(time);
		return inputCoordinates;
	}

	public Iterable<VectorReal> getIterator(VectorReal outputCoordinates) {
		vcs.swizzle(outputCoordinates, inputCoordinates);
		t = 0;
		return this;
	}

	@Override
	public Iterator<VectorReal> iterator() {
		return iter;
	}

	abstract class ReductionIterator implements Iterator<VectorReal> {

		@Override
		public boolean hasNext() {
			return t < length;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("Can't remove value from a " +
					"coordinate axis.");
		}
	}

	class ForwardReductionIterator extends ReductionIterator {
		double start;

		public ForwardReductionIterator(double start) {
			this.start = start;
		}

		@Override
		public VectorReal next() {
			inputCoordinates.setT(start + t);
			t++;
			return inputCoordinates;
		}

	}

	class ReverseReductionIterator extends ReductionIterator {
		double start;

		public ReverseReductionIterator(double start) {
			this.start = start;
		}

		@Override
		public VectorReal next() {
			inputCoordinates.setT(start - t);
			t++;
			return inputCoordinates;
		}

	}

}
