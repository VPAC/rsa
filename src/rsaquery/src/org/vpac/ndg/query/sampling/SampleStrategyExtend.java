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

package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Returns the nearest pixel when accessing a location that would otherwise be
 * out of bounds. This causes the edge pixels to "smear" beyond the natural
 * extents.
 * 
 * @author Alex Fraser
 */
public class SampleStrategyExtend implements SampleStrategy {

	private VectorInt minbounds;
	private VectorInt maxbounds;

	public SampleStrategyExtend(VectorInt shape) {
		minbounds = VectorInt.createEmpty(shape.size());
		maxbounds = shape.copy().subNew(1);
	}

	@Override
	public ScalarElement get(SamplerScalar samp, VectorReal co) throws IOException {
		co.min(maxbounds);
		co.max(minbounds);
		return samp.reallyGet(co);
	}

}
