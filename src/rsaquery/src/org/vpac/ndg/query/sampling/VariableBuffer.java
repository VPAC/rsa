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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.VariableAdapter;
import org.vpac.ndg.query.coordinates.HasRank;
import org.vpac.ndg.query.math.BoxInt;
import org.vpac.ndg.query.math.VectorInt;

import ucar.ma2.Index;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;

public abstract class VariableBuffer implements HasRank, Binding {

	final Logger log = LoggerFactory.getLogger(VariableBuffer.class);

	protected ArrayList<Index> indices;
	protected List<ArrayAdapter> buffers;
	private List<VariableAdapter> variables;
	private BoxInt bounds;
	private int dimensions;

	public VariableBuffer(List<VariableAdapter> variables)
			throws QueryConfigurationException {

		indices = new ArrayList<Index>();
		this.variables = variables;
		dimensions = -1;

		// Note: internal buffers are null until setBounds is called.
		buffers = new ArrayList<ArrayAdapter>();
		for (VariableAdapter variable : variables) {
			ArrayAdapter buffer = ArrayAdapterImpl.create(variable.getDataType(),
					variable.getNodataStrategy());
			buffers.add(buffer);

			if (dimensions == -1) {
				dimensions = variable.getRank();
			} else if (dimensions != variable.getRank()) {
				throw new QueryConfigurationException(String.format("Can not " +
						"create variable binding: variables have different " +
						"dimensions. e.g. variable %s", variable));
			}
		}
	}

	@Override
	public void commit(NetcdfFileWriter output) throws IOException {
		for (int i = 0; i < variables.size(); i++) {
			int[] origin = bounds.getMin().asIntArray();
			Variable var = variables.get(i).getVariable();
			ArrayAdapter buf = buffers.get(i);
			try {
				output.write(var, origin, buf.getArray());
			} catch (InvalidRangeException e) {
				// Raise runtime exception to indicate bug! This shouldn't
				// happen, because we control the Index.
				log.error("Could not write to output {} \"{}\"", i, var);
				log.error("Variable shape: {}", var.getShape());
				log.error("Buffer shape: {}", buf.getShape());
				log.error("Buffer bounds: {}", bounds);
				log.error("Current origin: {}", origin);
				throw new IndexOutOfBoundsException(String.format(
						"Unexpected error writing to variable: %s", e));
			}
		}
	}

	@Override
	public int getRank() {
		return dimensions;
	}

	abstract List<? extends PixelSource> getSources();

	public void setBounds(BoxInt bounds) {
		if (bounds.equals(this.bounds))
			return;
		VectorInt shape = bounds.getSize();
		for (ArrayAdapter buffer : buffers)
			buffer.resize(shape);
		indices.clear();
		for (int i = 0; i < getSources().size(); i++)
			indices.add(Index.factory(shape.asIntArray()));
		this.bounds = bounds.copy();
	}

	@Override
	public String toString() {
		if (getSources().size() > 0 && variables.size() > 0)
			return String.format("VariableBuffer(%s -> %s)", getSources().get(0), variables.get(0));
		else if (getSources().size() > 0)
			return String.format("VariableBuffer(%s -> null)", getSources().get(0));
		else if (variables.size() > 0)
			return String.format("VariableBuffer(null -> %s)", variables.get(0));
		else
			return String.format("VariableBuffer(null -> null)");
	}
}
