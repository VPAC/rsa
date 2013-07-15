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

package org.vpac.ndg.query;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.QueryDefinition.DatasetInputDefinition;
import org.vpac.ndg.query.QueryDefinition.FilterDefinition;
import org.vpac.ndg.query.QueryDefinition.GridDefinition;
import org.vpac.ndg.query.QueryDefinition.SamplerDefinition;
import org.vpac.ndg.query.QueryDefinition.VariableDefinition;
import org.vpac.ndg.query.coordinates.QueryCoordinateSystem;
import org.vpac.ndg.query.sampling.Cell;

import ucar.nc2.dataset.CoordinateSystem;

public class QueryDefinitionProprocessor {

	Logger log = LoggerFactory.getLogger(QueryDefinitionProprocessor.class);

	private QueryDefinition qd;
	private Map<String, DatasetInputDefinition> inputMap;
	private Map<String, FilterDefinition> filterMap;
	private Map<String, List<String>> filterOutputMap;
	private Set<NodeReference> inputVariableReferences;

	ClassLoader loader;
	Resolve resolve;

	public QueryDefinitionProprocessor() {
		resolve = new Resolve();
		loader = Thread.currentThread().getContextClassLoader();

		inputMap = new HashMap<String, DatasetInputDefinition>();
		filterMap = new HashMap<String, FilterDefinition>();
		filterOutputMap = new HashMap<String, List<String>>();
		inputVariableReferences = new HashSet<NodeReference>();
	}

	public QueryDefinition getQueryDefinition() {
		return qd;
	}

	public void setQueryDefinition(QueryDefinition qdOrig)
			throws QueryConfigurationException {

		qd = qdOrig.copy();

		Set<String> ids = new HashSet<String>();

		// Output
		ids.add(qd.output.id);

		// Datasets
		for (DatasetInputDefinition did : qd.inputs) {
			if (ids.contains(did.id)) {
				throw new QueryConfigurationException(String.format(
						"Duplicate ID \"%s\"", did.id));
			}
			ids.add(did.id);
			inputMap.put(did.id, did);
		}

		// Filters
		for (FilterDefinition fd : qd.filters) {
			if (ids.contains(fd.id)) {
				throw new QueryConfigurationException(String.format(
						"Duplicate ID \"%s\"", fd.id));
			}
			ids.add(fd.id);
			filterMap.put(fd.id, fd);
		}

	}

	public void guessGrid(Collection<DatasetInput> inputDatasets)
			throws QueryConfigurationException {

		if (qd.output.grid != null && qd.output.grid.ref != null)
			return;

		if (qd.output.grid == null)
			qd.output.grid = new GridDefinition();

		CoordinateSystem srs = null;
		for (DatasetInput di : inputDatasets) {
			QueryCoordinateSystem csys = di.getCoordinateSystem();
			if (srs == null) {
				srs = csys.getGrid().getSrs();
				qd.output.grid.ref = String.format("#%s", di.getName());
				log.info("Grid reference not specified. Using grid defined by {}", qd.output.grid.ref);
			} else if (!srs.equals(csys.getGrid().getSrs())) {
				throw new QueryConfigurationException("Input datasets have " +
						"differing coordinate systems. Please select one by " +
						"specifying the grid parameter the output dataset.");
			}
		}
	}

	public void gatherFilterInfo() throws QueryConfigurationException {
		for (FilterDefinition fd : qd.filters) {
			Class<?> cls;
			try {
				cls = loader.loadClass(fd.classname);
			} catch (ClassNotFoundException e) {
				throw new QueryConfigurationException(String.format(
						"Could not find filter class \"%s\".", fd.classname));
			}

			List<String> outputNames = new ArrayList<String>();
			for (Field f : cls.getFields()) {
				if (Cell.class.isAssignableFrom(f.getType()))
					outputNames.add(f.getName());
			}
			filterOutputMap.put(fd.classname, outputNames);
		}
	}


	/**
	 * Allows filter definitions to be sorted based on distance from output.
	 * @author Alex Fraser
	 */
	class FilterComparator implements Comparable<FilterComparator> {
		Integer depth;
		FilterDefinition fd;
		Set<String> downstreamConnections;

		FilterComparator(FilterDefinition fd) {
			this.fd = fd;
			depth = null;
			downstreamConnections = new HashSet<String>();
		}

		int calculateDepth(List<FilterComparator> path,
				Map<String, FilterComparator> fcs)
				throws QueryConfigurationException {

			if (this.depth != null)
				return this.depth;

			if (path.contains(this)) {
				throw new QueryConfigurationException(String.format(
						"Circular reference to filter #%s", fd.id));
			}

			int depth = -1;
			for (String targetId : downstreamConnections) {
				FilterComparator fc = fcs.get(targetId);
				if (fc == null) {
					// No target filter; target must be an output node (root).
					depth = Math.max(depth, 1);
				} else {
					// Target is filter; recurse.
					path.add(this);
					try {
						int targetDepth = fc.calculateDepth(path, fcs);
						depth = Math.max(depth, targetDepth + 1);
					} finally {
						path.remove(this);
					}
				}
			}

			this.depth = depth;
			return this.depth;
		}

		@Override
		public int compareTo(FilterComparator other) {
			// Compare in reverse, so that deeper filters are created first.
			return Integer.compare(other.depth, depth);
		}

		@Override
		public String toString() {
			return String.format("(#%s, %d)", fd.id, depth);
		}
	}

	/**
	 * Sort filters into descending order of distance from the output. This
	 * is the order in which filters should be instantiated to ensure
	 * dependencies are available.
	 * @param fds The filter definitions to sort.
	 * @throws QueryConfigurationException If the order could not be determined.
	 */
	public void sortFilters() throws QueryConfigurationException {

		Map<String, FilterComparator> fcs = new HashMap<String, FilterComparator>();
		// Create filter comparators.
		for (FilterDefinition fd : filterMap.values()) {
			FilterComparator fc = new FilterComparator(fd);
			fcs.put(fd.id, fc);
		}

		// Find downstream connections that link to output dataset.
		try {
			for (VariableDefinition vd : qd.output.variables) {
				if (vd.ref == null) {
					log.warn("Output variable {} has no input connection.", vd.name);
					continue;
				}
				NodeReference nr = resolve.decompose(vd.ref);
				FilterComparator fc = fcs.get(nr.nodeId);
				if (fc == null) {
					throw new QueryConfigurationException(String.format(
							"Variable \"%s\" is bound to non-existant filter " +
							"\"%s\"", vd.name, nr.nodeId));
				}
				fc.downstreamConnections.add(qd.output.id);
			}
		} catch (Exception e) {
			throw new QueryConfigurationException(String.format(
					"Could not configure output dataset: %s", e.getMessage()),
					e);
		}

		// Find downstream connections that link to other filters.
		for (FilterDefinition fd : filterMap.values()) {
			try {
				// Populate reverse look-up tables.
				if (fd.samplers != null) {
					for (SamplerDefinition sd1 : fd.samplers) {
						try {
							if (sd1.children != null) {
								for (SamplerDefinition sd2 : sd1.children) {
									String conn = resolve.decompose(sd2.ref).nodeId;
									FilterComparator otherFc = fcs.get(conn);
									if (otherFc != null) {
										otherFc.downstreamConnections.add(fd.id);
									}
								}
							} else {
								String conn = resolve.decompose(sd1.ref).nodeId;
								FilterComparator otherFc = fcs.get(conn);
								if (otherFc != null) {
									otherFc.downstreamConnections.add(fd.id);
								}
							}
						} catch (Exception e) {
							throw new QueryConfigurationException(String.format(
									"Could not configure sampler \"%s\": %s", sd1.name,
									e.getMessage()), e);
						}
					}
				}
			} catch (Exception e) {
				throw new QueryConfigurationException(String.format(
						"Could not configure filter \"%s\": %s", fd.id,
						e.getMessage()), e);
			}
		}

		// Calculate depth of each filter.
		List<FilterComparator> connectedFilters = new ArrayList<FilterComparator>();
		List<FilterComparator> path = new ArrayList<FilterComparator>();
		for (FilterComparator fc : fcs.values()) {
			path.clear();
			fc.calculateDepth(path, fcs);
			if (fc.depth >= 0)
				connectedFilters.add(fc);
			else
				log.info("Ignoring filter #{}: no downstream connections", fc.fd.id);
		}

		// Sort based on depth.
		FilterComparator[] sortedComparators = connectedFilters.toArray(new FilterComparator[connectedFilters.size()]);
		Arrays.sort(sortedComparators);
		List<FilterDefinition> sortedFilters = new ArrayList<QueryDefinition.FilterDefinition>(sortedComparators.length);
		for (FilterComparator fc : sortedComparators) {
			log.debug("Sorted filter: {}", fc);
			sortedFilters.add(fc.fd);
		}

		qd.filters = sortedFilters;
	}

	public void expandReferences(DatasetStore datasetStore)
			throws QueryConfigurationException {
		// Only filters support expansion at the moment. Output dataset variable
		// expansion may be implemented later.
		for (FilterDefinition fd : qd.filters) {
			List<SamplerDefinition> sds = new ArrayList<SamplerDefinition>();
			for (SamplerDefinition sdOrig : fd.samplers) {
				List<NodeReference> refs = expandReferences(datasetStore, sdOrig);
				SamplerDefinition sdNew = new SamplerDefinition();
				sdNew.name = sdOrig.name;
				sdNew.slices = sdOrig.slices;
				sdNew.children = new ArrayList<SamplerDefinition>();
				for (NodeReference nr : refs) {
					SamplerDefinition sdNewChild = new SamplerDefinition();
					sdNewChild._nodeRef = nr;
					sdNew.children.add(sdNewChild);
				}
				sds.add(sdNew);
			}
			// Discard old samplers and replace with expanded references.
			fd.samplers = sds;
		}
	}

	/**
	 * @param sd A sampler definition.
	 * @return A list of all references that are declared by a
	 *         SamplerDefinition.
	 * @throws QueryConfigurationException If the sampler is poorly defined, or
	 *         if the datasets referred to are undefined.
	 */
	private List<NodeReference> expandReferences(DatasetStore datasetStore, SamplerDefinition sd)
			throws QueryConfigurationException {

		List<NodeReference> refs = new ArrayList<NodeReference>();
		if (sd.ref != null) {
			refs.addAll(expandReference(datasetStore, sd.ref));
		}
		if (sd.children != null) {
			for (SamplerDefinition sdChild : sd.children) {
				refs.addAll(expandReference(datasetStore, sdChild.ref));
			}
		}
		return refs;
	}

	/**
	 * Expands a reference by matching it against all variable names in the
	 * dataset.
	 * 
	 * @param ref e.g. <em>#input/Band[0-9]+</em>
	 * @return e.g. <em>[#input/Band10, #input/Band20, #input/Band30]</em>
	 * @throws QueryConfigurationException If the expression is invalid or if
	 *         the dataset can't be found.
	 */
	private List<NodeReference> expandReference(DatasetStore datasetStore, String ref)
			throws QueryConfigurationException {

		NodeReference nr = resolve.decompose(ref);
		List<String> outputNames = null;
		boolean isDatasetReference;

		DatasetInput dataset = null;
		FilterDefinition fd = null;
		try {
			dataset = (DatasetInput) datasetStore.getDataset(nr.nodeId);
		} catch (ClassCastException e) {
			throw new QueryConfigurationException(String.format(
					"Dataset \"%s\" is not an input dataset.", nr.nodeId));
		} catch (QueryConfigurationException e) {
			// Will check for null below.
		}
		fd = filterMap.get(nr.nodeId);

		if (dataset != null) {
			isDatasetReference = true;
			outputNames = dataset.getOutputNames();
		} else if (fd != null) {
			isDatasetReference = false;
			outputNames = filterOutputMap.get(fd.classname);
		} else {
			throw new QueryConfigurationException(String.format(
					"\"%s\" is not a filter or dataset id.", nr.nodeId));
		}

		List<NodeReference> refs = new ArrayList<NodeReference>();
		Pattern re;
		try {
			re = Pattern.compile(nr.socketName);
		} catch (PatternSyntaxException e) {
			throw new QueryConfigurationException(String.format(
					"Could not compile regular expression from \"%s\"", nr), e);
		}
		for (String varName : outputNames) {
			Matcher match = re.matcher(varName);
			if (!match.matches())
				continue;
			NodeReference newRef = new NodeReference();
			newRef.nodeId = nr.nodeId;
			newRef.socketName = varName;
			refs.add(newRef);
			if (isDatasetReference)
				inputVariableReferences.add(newRef);
		}
		return refs;
	}

	public Set<NodeReference> getInputVariableReferences() {
		return inputVariableReferences;
	}
}
