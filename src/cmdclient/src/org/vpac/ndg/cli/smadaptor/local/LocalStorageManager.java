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
