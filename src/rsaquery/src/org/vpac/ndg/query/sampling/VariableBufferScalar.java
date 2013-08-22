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

package org.vpac.ndg.query.sampling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.VariableAdapter;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

import ucar.ma2.Index;

public class VariableBufferScalar extends VariableBuffer {

	private ArrayList<PixelSourceScalar> sources;

	public VariableBufferScalar(List<VariableAdapter> variables)
			throws QueryConfigurationException {

		super(variables);
		sources = new ArrayList<PixelSourceScalar>();
	}

	public void addSource(PixelSourceScalar source) {
		sources.add(source);
	}

	@Override
	public void transfer(VectorReal coFrom, VectorInt coTo, int sourceIndex) throws IOException {
		ScalarElement value = sources.get(sourceIndex).getScalarPixel(coFrom);
		Index ima = indices.get(sourceIndex);
		coTo.fillIndex(ima);
		for (int i = 0; i < buffers.size(); i++)
			buffers.get(i).set(ima, value);
	}

	@Override
	List<? extends PixelSource> getSources() {
		return sources;
	}

}
