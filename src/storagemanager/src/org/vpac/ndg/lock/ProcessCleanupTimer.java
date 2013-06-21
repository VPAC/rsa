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

package org.vpac.ndg.lock;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.storage.dao.ProcessDao;
import org.vpac.ndg.storage.dao.TimeSliceLockDao;
import org.vpac.ndg.storage.model.TimeSliceLock;
import org.vpac.ndg.storage.util.TimeSliceUtil;

public enum ProcessCleanupTimer {

	INSTANCE;

	private final Logger log = LoggerFactory
			.getLogger(ProcessCleanupTimer.class);

	// How frequently the timer runs
	public static final int DEFAULT_INTERVAL = 1;
	// Number of heartbeats that can be missed before a process is assumed to be
	// dead.
	public static final int MISSED_HEARTBEATS = 3;

	private int minutes;
	private Timer timer;
	private int counter;

	ProcessDao processDao;
	TimeSliceUtil timeSliceUtil;
	TimeSliceLockDao timeSliceLockDao;
	NdgConfigManager ndgConfigManager;

	private ProcessCleanupTimer() {
		ApplicationContext appContext = ApplicationContextProvider
				.getApplicationContext();
		processDao = (ProcessDao) appContext.getBean("processDao");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
		timeSliceLockDao = (TimeSliceLockDao) appContext.getBean("timeSliceLockDao");
		ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");
		this.minutes = DEFAULT_INTERVAL;
		counter = 0;
	}

	private void start() {
		timer = new Timer(true);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				log.debug("Clean up running tasks table");

				TimeSliceLock lockToken;

				String procId = ProcessUpdateTimer.INSTANCE.acquire();
				try {
					// Delete older PIDs from Process (heart beat) table
					int deleted = processDao.deleteStale();
					log.debug("Delete {} older process", deleted);

					try {
						lockToken = timeSliceLockDao.adoptOne(procId);
						if (lockToken == null)
							return;
						if(!timeSliceLockDao.isLocked(lockToken)) {
							throw new IllegalMonitorStateException(String.format(
									"Time slice %s still not locked even after acquiring lock.", lockToken.getTimesliceId()));
						}
					} catch (InterruptedException e) {
						log.warn("Failed to adopt lock: {}", e.getMessage());
						return;
					}
					log.trace("Cleaning up lock {}", lockToken);
					timeSliceUtil.cleanOne(lockToken);
					try {
						log.trace("Cleaning up lock {}", lockToken);
						timeSliceLockDao.unlock(lockToken);
					} catch (IllegalMonitorStateException e) {
						log.warn("Failed to release lock after adoption and cleanup: {}", e.getMessage());
						throw e;
					}
				}
				finally {
					ProcessUpdateTimer.INSTANCE.release();
				}
			}

		}, 0, minutes * 60 * 1000);
	}

	public synchronized void acquire() {
		log.trace("Acquire (counter: {} -> {}).", counter, counter + 1);
		if (counter == 0) {
			start();
		}
		counter++;
	}

	public synchronized void release() {
		log.trace("Release (counter: {} -> {}).", counter, counter - 1);
		if (counter <= 0)
			throw new IllegalStateException("Can't release cleanup timer: reference count is already zero.");

		if (counter == 1) {
			shutdown();
		}
		counter--;
	}

	/**
	 * Stop the timer.
	 */
	private void shutdown() {
		if (timer != null) {
			timer.cancel();
		}
	}
}
