package org.vpac.ndg.cli.smadaptor;

import java.io.IOException;
import java.util.List;

import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.TimeSliceLock;

public interface TimesliceConnector {

	public TimeSlice get(String timesliceId);

	public List<TimeSliceLock> listLocks(String timesliceId);

	public List<TimeSlice> list(String datasetId);

	public TimeSlice create(String dsid, String date, String abs);

	public void delete(String id) throws IOException;

	public String getLocation(String timesliceId);

	public Dataset getDataset(String timesliceId);

	public void update(TimeSlice newTs) throws IOException;

	public void updateInfo(TimeSlice newTs);
}
