package org.vpac.ndg.storage.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.Utils;
import org.vpac.ndg.common.Default;
import org.vpac.ndg.common.datamodel.GdalFormat;
import org.vpac.ndg.common.datamodel.RunningTaskState;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.storage.dao.ProcessDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.dao.TimeSliceLockDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.TimeSliceLock;

import ucar.nc2.time.CalendarDate;
import ucar.nc2.time.CalendarDateUnit;
import ucar.nc2.time.CalendarPeriod;

public class TimeSliceUtil {

	private final Logger log = LoggerFactory.getLogger(TimeSliceUtil.class);

	@Autowired
	ProcessDao processDao;
	@Autowired
	TimeSliceDao timeSliceDao;
	@Autowired
	TimeSliceLockDao timeSliceLockDao;
	@Autowired
	DatasetUtil datasetUtil;
	@Autowired
	NdgConfigManager ndgConfigManager;
	
	public DatasetUtil getDatasetUtil() {
		return datasetUtil;
	}

	public void setDatasetUtil(DatasetUtil datasetUtil) {
		this.datasetUtil = datasetUtil;
	}

	public TimeSliceDao getTimeSliceDao() {
		return timeSliceDao;
	}

	public void setTimeSliceDao(TimeSliceDao timeSliceDao) {
		this.timeSliceDao = timeSliceDao;
	}

	public TimeSliceUtil() {
	}

	/**
	 * Create timeslice directory in storagepool if it doesn't exist.
	 * @param ts The specified timeslice.
	 * @throws IOException Error when unable to create timeslice directory.
	 */
	public void initializeLocations(TimeSlice ts) throws IOException {
		Path tsPath = getFileLocation(ts);
		if (!Files.exists(tsPath))
			Files.createDirectories(tsPath);
	}

	public Path getFileLocation(TimeSlice ts) {
		Dataset ds = timeSliceDao.getParentDataset(ts.getId());
		Path location = datasetUtil.getPath(ds);
		String relLocation = getRelativeLocation(ts);
		return location.resolve(relLocation);
	}
	
	public String getRelativeLocation(TimeSlice ts) {
		DateFormat formatter = Utils.getTimestampFormatter();
		return formatter.format(ts.getCreated());		
	}

	public boolean isEmpty(TimeSlice ts) {
		return ts.getBounds().getArea() <= 0.0;
	}

	/**
	 * @param tss A list of time slices.
	 * @return A box that encompasses all the data in the time slices, or null
	 *         if there is no data.
	 */
	public Box aggregateBounds(List<TimeSlice> tss) {
		Box bounds = null;
		for (TimeSlice ts : tss) {
			if (isEmpty(ts))
				continue;
			if (bounds == null)
				bounds = new Box(ts.getBounds());
			else
				bounds.union(ts.getBounds());
		}
		return bounds;
	}

	/**
	 * Find appropriate units for the time dimension.
	 * 
	 * @param timeSlices
	 *            The time slices to find a unit for.
	 * @param dates
	 *            A list of coordinates in the new units, which map 1:1 to the
	 *            given time slices. This should be an empty list; it will be
	 *            populated by this method.
	 * @return A description the new units.
	 */
	public CalendarDateUnit computeTimeMapping(List<TimeSlice> timeSlices,
			List<CalendarDate> dates) {

		// We need to know the smallest precision of the selected time slices,
		// so that a sensible value can be used in the exported NCML: the
		// coordinates will use units like "Days since 2011-01-01". See:
		// http://www.unidata.ucar.edu/software/netcdf/docs/BestPractices.html#Calendar%20Date/Time
		// http://www.unidata.ucar.edu/software/netcdf/time/recs.html

		Date mindate = null;
		long minprecision = Long.MAX_VALUE;
		for (TimeSlice ts : timeSlices) {
			if (mindate == null || ts.getCreated().before(mindate))
				mindate = ts.getCreated();
			Dataset ds = timeSliceDao.getParentDataset(ts.getId());
			if (minprecision == Long.MAX_VALUE || ds.getPrecision() < minprecision)
				minprecision = ds.getPrecision();
		}

		// Find an epoch: truncate the date to the nearest sensible major value,
		// e.g. to the nearest year.
		Date epoch;
		String timeUnits;

		if (minprecision < DateUtils.MILLIS_PER_SECOND) {
			// Precision of less than one second; round to milliseconds.
			epoch = DateUtils.truncate(mindate, Calendar.SECOND);
			timeUnits = String.format("Milliseconds since %s",
					DateFormatUtils.format(epoch, Default.SECOND_PATTERN));

		} else if (minprecision < DateUtils.MILLIS_PER_MINUTE) {
			// Precision of less than one minute; round to seconds.
			epoch = DateUtils.truncate(mindate, Calendar.MINUTE);
			timeUnits = String.format("Seconds since %s",
					DateFormatUtils.format(epoch, Default.MINUTE_PATTERN));

		} else if (minprecision < DateUtils.MILLIS_PER_HOUR) {
			// Precision of less than one hour; round to minutes.
			epoch = DateUtils.truncate(mindate, Calendar.HOUR);
			timeUnits = String.format("Minutes since %s",
					DateFormatUtils.format(epoch, Default.HOUR_PATTERN));

		} else if (minprecision < DateUtils.MILLIS_PER_DAY) {
			// Precision of less than one day; round to hours.
			epoch = DateUtils.truncate(mindate, Calendar.DAY_OF_MONTH);
			timeUnits = String.format("Hours since %s",
					DateFormatUtils.format(epoch, Default.DAY_PATTERN));

		} else {
			// Precision GREATER than one day; round to days.
			epoch = DateUtils.truncate(mindate, Calendar.YEAR);
			timeUnits = String.format("Days since %s",
					DateFormatUtils.format(epoch, Default.DAY_PATTERN));
		}

		CalendarDateUnit units = CalendarDateUnit.of("proleptic_gregorian",
				timeUnits);

		// Calculate a set of new coordinates for each time slice, relative to
		// the epoch.
		for (TimeSlice ts : timeSlices) {
			CalendarDate coordinate = CalendarDate.of(ts.getCreated().getTime());
			dates.add(coordinate);
		}

		return units;
	}

	public List<String> datesToCoordValues(CalendarDateUnit units,
			List<CalendarDate> dates) {

		List<String> coordValues = new ArrayList<>();
		CalendarPeriod period = units.getTimeUnit();
		for (CalendarDate value : dates) {
			int offset = period.subtract(units.getBaseCalendarDate(), value);
			coordValues.add(Integer.toString(offset));
		}
		return coordValues;
	}

	final int MAX_ADOPT_ATTEMPTS = 10;

	/**
	 * Cleans up all outstanding locks that belong to other processes.
	 * @param ownProcessId The ID of this process.
	 * @param force Whether to clean up locks that have not yet expired.
	 * @return The number of locks that were cleaned.
	 */
	public int cleanOthers(String ownProcessId, boolean force) {
		log.debug("Destroying old process entries.");
		if (force) {
			// Destroy all processes, even if they don't appear to be dead.
			int ndestroyed = processDao.deleteOthers(ownProcessId);
			log.info("Destroyed {} old process records.", ndestroyed);
		} else {
			// Destroy expired processes.
			int ndestroyed = processDao.deleteStale();
			log.info("Destroyed {} old process records.", ndestroyed);
		}

		// Try to clean up each orphaned task one at a time.
		int ncleaned = 0;
		int ntries = 0;
		while (true) {
			TimeSliceLock lockToken;
			try {
				lockToken = timeSliceLockDao.adoptOne(ownProcessId);
			} catch (InterruptedException e) {
				// It's possible but unlikely that an orphan could be adopted by
				// a different process, so retry a few times if one fails.
				ntries += 1;
				if (ntries >= MAX_ADOPT_ATTEMPTS)
					throw new IllegalStateException("Failed to adopt lock.", e);
				continue;
			}
			if (lockToken == null) {
				// No more orphans.
				break;
			}
			cleanOne(lockToken);
			timeSliceLockDao.unlock(lockToken);
			ncleaned += 1;
			ntries = 0;
		}
		return ncleaned;
	}

	public int clean(List<TimeSliceLock> lockTokens) {
		if(lockTokens.size() == 0) {
			// When no lock, no need to restore/cleanup
			return 0;
		}

		log.debug("Cleaning up temporary data belonging to locks.");
		for (TimeSliceLock lockToken : lockTokens) {
			cleanOne(lockToken);
		}
		return lockTokens.size();
	}

	/**
	 * Cleans up one lock, but does not adopt it or unlock it.
	 */
	public void cleanOne(TimeSliceLock lockToken) {
		TimeSlice ts = timeSliceDao.retrieve(lockToken.getTimesliceId());
		if (ts.getLockMode() == 'w') {
			// Clean up and then release lock.
			if (lockToken.getState() == RunningTaskState.RUNNING) {
				restore(lockToken.getTimesliceId());
			} else {
				cleanup(lockToken.getTimesliceId());
			}

		} else {
			// TODO: Might be good to delete temporary files associated with
			// this lock.
		}
	}

	public void restore(String timesliceId) {
		TimeSlice ts = timeSliceDao.retrieve(timesliceId);
		Path tsPath = getFileLocation(ts);
		try {
			Files.walkFileTree(tsPath, new SimpleFileVisitor<Path>() {
				/**
				 * Import netcdf dataset
				 */
				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {

					String fileName = file.getFileName().toString();

					if (!fileName.endsWith("_old" + GdalFormat.NC.getExtension())) {
						// IGNORE NON-BACKUP TILE
						return FileVisitResult.CONTINUE;
					}

					String restoredFilename = fileName.replace("_old", "");
					Path backupTilePath = file.toAbsolutePath();
					Path restoredTilePath = Paths.get(backupTilePath.getParent().toString(), restoredFilename);
					try {
						FileUtils.move(backupTilePath, restoredTilePath);
						log.debug("RESTORE {} as {}", backupTilePath, restoredTilePath);
					} catch (Exception e) {
						log.error("{}", e);
					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException e)
						throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			log.error("Error restoring {} caused by {}", tsPath, e);
		}
	}

	public void cleanup(String timesliceId) {
		TimeSlice ts = timeSliceDao.retrieve(timesliceId);
		Path tsPath = getFileLocation(ts);
		try {
			Files.walkFileTree(tsPath, new SimpleFileVisitor<Path>() {
				/**
				 * Import netcdf dataset
				 */
				@Override
				public FileVisitResult visitFile(Path file,
						BasicFileAttributes attrs) throws IOException {

					String fileName = file.getFileName().toString();

					if (!fileName.endsWith("_old" + GdalFormat.NC.getExtension())) {
						// IGNORE NON-BACKUP TILE
						return FileVisitResult.CONTINUE;
					}

					Path backupTilePath = file.toAbsolutePath();
					try {
						FileUtils.deleteIfExists(backupTilePath);
						log.debug("CLEAN UP {}", backupTilePath);
					} catch (Exception e) {
						log.error("{}", e);
					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException e)
						throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			log.error("Error restoring {} caused by {}", tsPath, e);
		}		
	}

	public void delete(TimeSlice ts) throws IOException {
		Path tsPath = getFileLocation(ts);
		// Delete all dataset tiles from storagepool
		try {
			FileUtils.removeDirectory(tsPath);
		} catch (NoSuchFileException e) {
			log.warn("Could not find dataset directory {}. Continuing with deletion anyway", tsPath);
		}
		
		timeSliceDao.delete(ts);
	}

	public void update(TimeSlice newTs) throws IOException {
		TimeSlice oldTs = timeSliceDao.retrieve(newTs.getId());
		Path p = getFileLocation(oldTs);

		Path newTsPath = Paths.get(p.toString().replace(oldTs.getCreated().toString(), newTs.getCreated().toString()));
		FileUtils.move(p, newTsPath);
		
		timeSliceDao.update(newTs);

	}
}
