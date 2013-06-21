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

package org.vpac.ndg.storage.dao;

import java.util.Date;
import java.util.List;

import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;

public interface DatasetDao extends BasicDao<Dataset> {
	List<Dataset> getAll();
	List<Band> getBands(String datasetId);
	List<TimeSlice> getTimeSlices(String datasetId);
	TimeSlice findTimeSlice(String datasetId, Date creationTime);
	List<TimeSlice> findTimeSlices(String datasetId, Date startDate, Date endDate);
	Dataset findDatasetByName(String name, CellSize resolution);
	List<Dataset> search(String name, int page, int pageSize);
	public List<Dataset> search(String name, CellSize resolution);
	void addBand(String datasetId, Band band);
	TimeSlice addTimeSlice(String datasetId, TimeSlice ts);
	void addAllBands(String id, List<Band> bands);
	void addAllSlices(String datasetId, List<TimeSlice> slices);
	List<Band> findBands(String datasetId, List<String> bandIds);
	List<Band> findBandsByName(String id, List<String> bands);
}
