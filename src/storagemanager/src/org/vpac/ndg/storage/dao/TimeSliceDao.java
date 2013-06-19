package org.vpac.ndg.storage.dao;

import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;

public interface TimeSliceDao extends BasicDao<TimeSlice> {
	Dataset getParentDataset(String timeSliceId);
}
