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

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.Utils;
import org.vpac.ndg.cli.smadaptor.TimesliceConnector;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.dao.TimeSliceLockDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.TimeSliceLock;
import org.vpac.ndg.storage.util.TimeSliceUtil;

public class LocalTimesliceConnector implements TimesliceConnector {

	@Autowired
	DatasetDao datasetDao;
	@Autowired
	TimeSliceDao timeSliceDao;
	@Autowired
	TimeSliceLockDao timeSliceLockDao;
	@Autowired
	TimeSliceUtil timeSliceUtil;

	@Override
	public TimeSlice get(String timesliceId) {
		return timeSliceDao.retrieve(timesliceId);
	}

	@Override
	public List<TimeSliceLock> listLocks(String timesliceId) {
		return timeSliceLockDao.findByTimeSlice(timesliceId);
	}

	@Override
	public List<TimeSlice> list(String datasetId) {
		return datasetDao.getTimeSlices(datasetId);
	}

	@Override
	public TimeSlice create(String dsid, String date, String abs) {

		Date parsedDate = Utils.parseDate(date);
		TimeSlice ts = new TimeSlice(parsedDate);
		ts.setDataAbstract(abs);
		datasetDao.addTimeSlice(dsid, ts);
		return ts;
	}

	@Override
	public void delete(String id) throws IOException {
		TimeSlice ts = null;
		ts = get(id);
		if (ts == null) {
			throw new IllegalArgumentException("Timeslice not found.");
		}
		timeSliceUtil.delete(ts);
	}

	@Override
	public Dataset getDataset(String timesliceId) {
		return timeSliceDao.getParentDataset(timesliceId);
	}

	@Override
	public String getLocation(String timesliceId) {
		TimeSlice ts = get(timesliceId);
		return timeSliceUtil.getFileLocation(ts).toString();
	}

	@Override
	public void update(TimeSlice newTs) throws IOException {
		timeSliceUtil.update(newTs);
	}

	@Override
	public void updateInfo(TimeSlice newTs) {
		timeSliceDao.update(newTs);
	}
}
