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

package org.vpac.ndg.task;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.datamodel.RunningTaskState;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.lock.HasRunningState;
import org.vpac.ndg.rasterdetails.RasterDetails;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.BandUtil;
import org.vpac.ndg.storage.util.TimeSliceUtil;

/**
 * This class is responsible for copy the tile from their temporary storage into
 * the storage pool. Once all tiles have been successfully moved into storage pool
 * then save them into database and increase upload counter for TimeSlice.
 * 
 * @author hsumanto
 *
 */
public class Committer extends Task {

	final Logger log = LoggerFactory.getLogger(Committer.class);

	private List<TileBand> source;
	private Box bounds;
	private Band band;
	private Dataset dataset;
	private String dstnodata;
	private RasterDetails datatype;
	private TimeSlice target;
	TimeSliceDao timeSliceDao;
	BandDao bandDao;
	TimeSliceUtil timeSliceUtil;
	BandUtil bandUtil;

	private ScalarReceiver<HasRunningState> taskMonitor;

	public Committer() {
		this(Constant.TASK_DESCRIPTION_COMMITTER);
	}

	public Committer(String description) {
		super(description);
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
		bandDao = (BandDao) appContext.getBean("bandDao");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
		bandUtil = (BandUtil) appContext.getBean("bandUtil");
	}

	@Override
	public void initialise() throws TaskInitialisationException {
		if (bounds == null || bounds.getArea() == 0.0) {
			throw new TaskInitialisationException(
					"Can't update timeslice: new data has zero area.");
		}

		try {
			// Initialize timeSlice directory in storagepool if applicable
			timeSliceUtil.initializeLocations(target);
		} catch (IOException e) {
			log.error("Could not create timeslice directory: {}", e);
			throw new TaskInitialisationException(String.format("Error encountered when creating timeslice directory: %s", timeSliceUtil.getFileLocation(target)));
		}
	}

	// This is the only place to write to the database during task pipeline execution
	// FIXME: This is not actually transactional because this class is not a spring bean!
	@Transactional
	@Override
	public void execute() throws TaskException {
		log.debug("TASK = {}", getDescription());

		// If nothing to commit then do nothing
		if(source.isEmpty()) {
			return;
		}

		for(TileBand tileband: source) {
			log.debug("SOURCE = {}", tileband.getFileLocation());	
			// Rename an existing tileband by adding .old extension into the tileband name.
			tileband.renameExistingTileAsOld();
			// Copy the tileband into its default location in storagepool.
			tileband.copyIntoDefaultLocationInStoragePool();		
			// Add tileband into tile list if it doesn't exist in this time slice
			log.debug("TARGET = {}", tileband.getFileLocation());						
			log.info("Committing {} tile = {}", tileband.getBand().getName(),
					tileband.getTileNameWithExtention());
		}

		// Load a new instance of the time slice to ensure the lock information
		// is not clobbered!
		TimeSlice ts = timeSliceDao.retrieve(target.getId());

		Box storedBounds;
		if (timeSliceUtil.isEmpty(ts)) {
			// This is the first time the timeslice has been written to. Just
			// overwrite the empty bounds.
			storedBounds = bounds;
		} else {
			storedBounds = ts.getBounds();
			storedBounds.union(bounds);
		}
		ts.setBounds(storedBounds);

		if (band.getNodata() == null || band.getType() == null) {
			if (band.getNodata() != null) {
				if (!band.getNodata().equals(dstnodata))
					throw new TaskException("Can't change band's nodata value.");
			}
			band.setNodata(dstnodata);

			if (band.getType() != null) {
				if (band.getType() != datatype)
					throw new TaskException("Can't change band's type.");
			}
			band.setType(datatype);

			try {
				bandUtil.createBlankTile(dataset, band);
			} catch (IOException e) {
				throw new TaskException(e);
			}
			bandDao.update(band);
		}

		timeSliceDao.update(ts);

		// Change the RunningTaskMonitor state from RUNNING into CLEANUP
		// If locking is disabled, this field may be null.
		if (taskMonitor != null && taskMonitor.get() != null)
			taskMonitor.get().setState(RunningTaskState.CLEANUP);
	}

	@Override
	public void rollback() {
		if(source == null) {
			return;
		}
		
		// Move back to old location
		for(TileBand tileband: source) {
			try {
				tileband.copyBackToPreviousStorage();
			} catch (TaskException e) {
				log.error(e.getMessage());
			}
			log.debug("ROLLBACK moving stored tile back to = {}",
					tileband.getFileLocation());
		}
	}

	@Override
	public void finalise() {
		if(source == null) {
			return;
		}
		
		// Delete old tiles
		for(TileBand tileband: source) {
			tileband.deleteTileInPreviousStorage();
		}
	}

	public void setSource(List<TileBand> source) {
		this.source = source;
	}

	public List<TileBand> getSource() {
		return source;
	}

	public void setTarget(TimeSlice target) {
		this.target = target;
	}
	
	public TimeSlice getTarget() {
		return target;
	}

	public Box getBounds() {
		return bounds;
	}

	public void setBounds(Box bounds) {
		this.bounds = bounds;
	}

	public Band getBand() {
		return band;
	}

	public void setBand(Band band) {
		this.band = band;
	}

	public String getDstnodata() {
		return dstnodata;
	}

	public void setDstnodata(String dstnodata) {
		this.dstnodata = dstnodata;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public RasterDetails getDatatype() {
		return datatype;
	}

	public void setDatatype(RasterDetails datatype) {
		this.datatype = datatype;
	}

	public void setTaskMonitor(ScalarReceiver<HasRunningState> taskMonitor) {
		this.taskMonitor = taskMonitor;
	}

}
