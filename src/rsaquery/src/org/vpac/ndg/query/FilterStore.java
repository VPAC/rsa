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

import java.util.HashMap;
import java.util.Map;

import org.vpac.ndg.query.sampling.PixelSource;

/**
 * A searchable store of datasets.
 * @author Alex Fraser
 */
public class FilterStore {

	protected Map<String, FilterAdapter> filters;

	Resolve resolve;

	public FilterStore() {
		filters = new HashMap<String, FilterAdapter>();
		resolve = new Resolve();
	}

	public void add(FilterAdapter filter) {
		filters.put(filter.getName(), filter);
	}

	/**
	 * @param ref A reference, e.g. <em>#blur</em>.
	 * @return The matching filter
	 * @throws QueryConfigurationException If the filter can't be found, or if
	 *         the reference is invalid.
	 */
	public FilterAdapter findFilter(String ref)
			throws QueryConfigurationException {
		return getFilter(resolve.decompose(ref).nodeId);
	}

	/**
	 * @param id The name of the dataset, e.g. <em>infile</em>.
	 * @return The matching dataset.
	 * @throws QueryConfigurationException If the dataset can't be found.
	 */
	public FilterAdapter getFilter(String id) throws
			QueryConfigurationException {

		FilterAdapter filter = filters.get(id);
		if (filter == null) {
			throw new QueryConfigurationException(String.format(
					"Filter \"%s\" is not defined (yet).", id));
		}
		return filter;
	}

	public PixelSource findOutputSocket(String ref)
			throws QueryConfigurationException {

		NodeReference nr = resolve.decompose(ref);

		if (nr.socketName == null)
			throw new QueryConfigurationException(String.format(
					"Socket name not specified in \"%s\".", ref));

		FilterAdapter filter = getFilter(nr.nodeId);
		return filter.getOutputSocket(nr.socketName);
	}

}
