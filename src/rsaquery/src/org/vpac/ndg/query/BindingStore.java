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

package org.vpac.ndg.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.sampling.Binding;
import org.vpac.ndg.query.sampling.PixelSourceScalar;
import org.vpac.ndg.query.sampling.PixelSourceVector;
import org.vpac.ndg.query.sampling.VariableBufferScalar;
import org.vpac.ndg.query.sampling.VariableBufferVector;

/**
 * Groups bindings by shape. This allows all output variables of the same shape
 * to be processed together.
 * @author Alex Fraser
 */
public class BindingStore {

	private Map<VectorInt, Collection<Binding>> map;
	private Map<String, VariableBufferScalar> scalarMap;
	private Map<String, VariableBufferVector> vectorMap;
	private Set<VariableAdapter> uniqueVars;

	public BindingStore() {
		map = new HashMap<VectorInt, Collection<Binding>>();
		scalarMap = new HashMap<String, VariableBufferScalar>();
		vectorMap = new HashMap<String, VariableBufferVector>();
		uniqueVars = new HashSet<VariableAdapter>();
	}

	private void add(VectorInt key, Binding value) {
		Collection<Binding> values = map.get(key);
		if (values == null) {
			values = new ArrayList<Binding>();
			map.put(key, values);
		}
		values.add(value);
	}

	public Collection<Binding> get(VectorInt key) {
		return map.get(key);
	}

	/**
	 * Bind a pixel source to an output variable.
	 *
	 * @param source The input.
	 * @param variables The output(s). Note that even a scalar input can have
	 *        multiple outputs (the same value will be written to each one).
	 * @throws QueryConfigurationException If the shapes of the variables are
	 *         inconsistent.
	 */
	public void bind(String id, PixelSourceScalar source,
			List<VariableAdapter> variables)
			throws QueryConfigurationException {

		VariableBufferScalar binding = scalarMap.get(id);
		if (binding == null) {
			ensureUniqueVars(variables);
			binding = new VariableBufferScalar(variables);
			scalarMap.put(id, binding);
			add(getShape(variables), binding);
		}
		binding.addSource(source);
	}

	/**
	 * Bind a pixel source to an output variable.
	 *
	 * @param source The input.
	 * @param variables The output(s). The number of outputs should match the
	 *        rank of the source.
	 * @throws QueryConfigurationException If the shapes of the variables are
	 *         inconsistent.
	 */
	public void bind(String id, PixelSourceVector source,
			List<VariableAdapter> variables)
			throws QueryConfigurationException {
		// TODO: check that the source is the same size as the variables?
		VariableBufferVector binding = vectorMap.get(id);
		if (binding == null) {
			ensureUniqueVars(variables);
			binding = new VariableBufferVector(variables);
			vectorMap.put(id, binding);
			add(getShape(variables), binding);
		}
		binding.addSource(source);
	}

	private void ensureUniqueVars(List<VariableAdapter> variables)
			throws QueryConfigurationException {
		for (VariableAdapter var : variables) {
			if (uniqueVars.contains(var)) {
				throw new QueryConfigurationException(String.format(
						"Variable %s can't be bound more than once.", var));
			}
		}
		uniqueVars.addAll(variables);
	}

	private VectorInt getShape(List<VariableAdapter> variables) throws QueryConfigurationException {
		VectorInt shape = null;
		for (VariableAdapter var : variables) {
			if (shape != null && !shape.equals(var.getShape())) {
				throw new QueryConfigurationException(String.format(
						"Cell is being bound to variables with different " +
						"shapes."));
			} else {
				shape = var.getShape();
			}
		}
		return shape;
	}

	public int size() {
		return map.size();
	}

	public Set<VectorInt> keys() {
		return map.keySet();
	}

	public Collection<Collection<Binding>> values() {
		return map.values();
	}

}
