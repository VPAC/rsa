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
