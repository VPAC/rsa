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
