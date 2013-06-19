package org.vpac.ndg.cli.smadaptor;

public interface StorageManager {
	public DatasetConnector getDatasetConnector();
	public DataUpload getDataUploader();
	public DataImport getDataImporter();
	public DataExport getDataExporter();
	public DataQuery getDataQuery();
	public DataCleanup getDataCleanup();
	public TimesliceConnector getTimesliceConnector();
	public BandConnector getBandConnector();
	public TaskConnector getTaskConnector();
	public DataDownloader getDataDownloader();
}
