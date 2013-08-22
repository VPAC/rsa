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

package org.vpac.ndg.cli.smadaptor;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.storage.model.Dataset;

public interface DatasetConnector {

	List<Dataset> list();

	String create(String name, String abs, String resolution,
			String precision);

	void delete(String identifier) throws IOException;

	List<Dataset> searchDataset(String name, CellSize resolution);

	/**
	 * Find a dataset.
	 * @param identifier The dataset ID, or path in the form "name/resolution".
	 * @return The dataset, or null if it can't be found.
	 */
	Dataset getDataset(String identifier);

	/**
	 * @return A CDL representation of the dataset, like the output of
	 * 
	 * <pre>
	 * ncdump - h
	 * </pre>
	 */
	String getCdl(String identifier, Box extents, Date startDate, Date endDate)
			throws IOException;

	/**
	 * @return An NCML representation of the dataset.
	 * @throws IOException 
	 */
	String getNcml(String identifier, Box extents, Date startDate, Date endDate)
			throws IOException;

	String getLocation(String id);

	Dataset get(String datasetId);

	void updateInfo(Dataset newDataset);

	void update(Dataset newDataset) throws IOException;
}
