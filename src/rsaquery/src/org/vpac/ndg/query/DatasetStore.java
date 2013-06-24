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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;

/**
 * A searchable store of datasets.
 * @author Alex Fraser
 */
public class DatasetStore {

	final Logger log = LoggerFactory.getLogger(DatasetStore.class);

	protected Map<String, DatasetMeta> datasets;
	protected Set<DatasetInput> inputDatasets;

	Resolve resolve;

	public DatasetStore() {
		datasets = new HashMap<String, DatasetMeta>();
		inputDatasets = new HashSet<DatasetInput>();
		resolve = new Resolve();
	}

	public void addDataset(DatasetInput dataset) {
		datasets.put(dataset.getName(), dataset);
		inputDatasets.add(dataset);
	}

	public void addDataset(DatasetOutput dataset) {
		datasets.put(dataset.getName(), dataset);
	}

	/**
	 * @param ref A reference, e.g. <em>#infile</em>.
	 * @return The matching dataset
	 * @throws QueryConfigurationException If the dataset can't be found, or if
	 *         the reference is invalid.
	 */
	public DatasetMeta findDataset(String ref)
			throws QueryConfigurationException {
		return getDataset(resolve.decompose(ref).nodeId);
	}

	/**
	 * @param id The name of the dataset, e.g. <em>infile</em>.
	 * @return The matching dataset.
	 * @throws QueryConfigurationException If the dataset can't be found.
	 */
	public DatasetMeta getDataset(String id) throws
			QueryConfigurationException {

		DatasetMeta dataset = datasets.get(id);
		if (dataset == null) {
			throw new QueryConfigurationException(String.format(
					"Dataset \"%s\" is not defined.", id));
		}
		return dataset;
	}

	public Collection<DatasetInput> getInputDatasets() {
		return inputDatasets;
	}

	public Dimension findDimension(String ref)
			throws QueryConfigurationException {

		NodeReference nr = resolve.decompose(ref);

		if (nr.socketName == null)
			throw new QueryConfigurationException(String.format(
					"Dimension name not specified in \"%s\".", ref));

		NetcdfFile sourceDataset = getDataset(nr.nodeId).getDataset();
		Dimension dimension = sourceDataset.findDimension(nr.socketName);
		if (dimension == null) {
			throw new QueryConfigurationException(String.format(
					"Dimension \"%s\" can't be found in dataset \"%s\".",
					nr.socketName, nr.nodeId));
		}
		return dimension;
	}

	/**
	 * Marks an input variable as one that will eventually be bound to a filter.
	 * Bands that are not marked like this may not be read from disk.
	 * 
	 * @param ref The band to tag, e.g. "#inputA/Band1".
	 * @throws QueryConfigurationException if the band or dataset can't be
	 *         found.
	 */
	public void requestInputBand(NodeReference nr)
			throws QueryConfigurationException {

		DatasetMeta dm = getDataset(nr.nodeId);

		if (nr.socketName == null)
			throw new QueryConfigurationException("Input band not specified");

		DatasetInput di;
		try {
			di = (DatasetInput) dm;
		} catch (ClassCastException e) {
			throw new QueryConfigurationException(String.format(
					"Can't tag output bands (specified band was %s).", nr));
		}
		di.requestBand(nr.socketName);
	}

	public void closeAll() throws IOException {
		for (DatasetMeta dataset : datasets.values()) {
			try {
				dataset.close();
			} catch (IOException e) {
				log.error("Could not close dataset.", e);
			} catch (RuntimeException e) {
				log.error("Could not close dataset.", e);
			}
		}
	}
}
