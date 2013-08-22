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

package org.vpac.ndg.cli.smadaptor.local;

import org.springframework.context.ApplicationContext;
import org.vpac.ndg.cli.smadaptor.BandConnector;
import org.vpac.ndg.cli.smadaptor.DataCleanup;
import org.vpac.ndg.cli.smadaptor.DataDownloader;
import org.vpac.ndg.cli.smadaptor.DataExport;
import org.vpac.ndg.cli.smadaptor.DataImport;
import org.vpac.ndg.cli.smadaptor.DataQuery;
import org.vpac.ndg.cli.smadaptor.DataUpload;
import org.vpac.ndg.cli.smadaptor.DatasetConnector;
import org.vpac.ndg.cli.smadaptor.StorageManager;
import org.vpac.ndg.cli.smadaptor.TaskConnector;
import org.vpac.ndg.cli.smadaptor.TimesliceConnector;

public class LocalStorageManager implements StorageManager {

	ApplicationContext appContext;
	
	public LocalStorageManager(ApplicationContext appContext) {
		this.appContext = appContext;
	}
	
	@Override
	public DatasetConnector getDatasetConnector() {
		return (DatasetConnector) appContext.getBean("datasetConnector");
	}

	@Override
	public DataUpload getDataUploader() {
		return (DataUpload) appContext.getBean("dataUpload");
	}

	@Override
	public DataImport getDataImporter() {
		return (DataImport) appContext.getBean("dataImport");
	}

	@Override
	public DataExport getDataExporter() {
		return (DataExport) appContext.getBean("dataExport");
	}

	@Override
	public DataQuery getDataQuery() {
		return (DataQuery) appContext.getBean("dataQuery");
	}

	@Override
	public DataCleanup getDataCleanup() {
		return (DataCleanup) appContext.getBean("dataCleanup");
	}

	@Override
	public TimesliceConnector getTimesliceConnector() {
		return (TimesliceConnector) appContext.getBean("timesliceConnector");
	}

	@Override
	public BandConnector getBandConnector() {
		return (BandConnector) appContext.getBean("bandConnector");
	}

	@Override
	public TaskConnector getTaskConnector() {
		return (TaskConnector) appContext.getBean("taskConnector");		
	}

	@Override
	public DataDownloader getDataDownloader() {
		return (DataDownloader) appContext.getBean("dataDownloader");
	}
}
