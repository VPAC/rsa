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

package org.vpac.ndg.lock;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.storage.dao.ProcessDao;
import org.vpac.ndg.storage.dao.TimeSliceLockDao;
import org.vpac.ndg.storage.model.Process;
import org.vpac.ndg.storage.model.TimeSliceLock;
import org.vpac.ndg.storage.util.TimeSliceUtil;

public enum ProcessUpdateTimer {

	INSTANCE;

	final Logger log = LoggerFactory.getLogger(ProcessUpdateTimer.class);

	// Frequency of heart beats. These indicate to other processes that this
	// process is still alive. If the heart beat stops, the locks will expire.
	public static final int DEFAULT_INTERVAL = 1;

	private int minutes;
	private String processId;
	private Timer timer;
	private int counter;

	TimeSliceUtil timeSliceUtil;
	TimeSliceLockDao timeSliceLockDao;
	ProcessDao processDao;

	private ProcessUpdateTimer() {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		processDao = (ProcessDao) appContext.getBean("processDao");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
		timeSliceLockDao = (TimeSliceLockDao) appContext.getBean("timeSliceLockDao");

		this.minutes = DEFAULT_INTERVAL;
		counter = 0;
	}

	public synchronized String acquire() {
		log.trace("Acquire (counter: {} -> {}).", counter, counter + 1);
		String newProcess = getProcessId();
		if (counter == 0) {
			start();
		}
		counter++;
		return newProcess;
	}

	public synchronized void release() {
		log.trace("Release (counter: {} -> {}).", counter, counter - 1);
		if (counter <= 0)
			throw new IllegalStateException("Can't release heartbeat timer: reference count is already zero.");

		if (counter == 1) {
			shutdown();
		}
		counter--;
	}

	public synchronized String getProcessId() {
		if (processId == null) {
			// Create new instance of process monitor
			Process process = new Process();
			processDao.create(process);
			processId = process.getId();
			log.trace("Created new process. ID = {}", processId);
		}
		return processId;
	}

	private void start() {
		timer = new Timer(true);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// Update the same instance of process monitor
				processDao.update(processId);
				Process process = processDao.retrieve(processId);
				log.info("Heartbeat: {}", process.getLatest());
			}
		}, 0, minutes * 60 * 1000);
	}

	/**
	 * Remove the process and stop the timer.
	 * Note that this won't happen if the machine crashes; in
	 * that case, the process will expire after a certain timeout 
	 * (see ProcessCleanupTimer). 
	 */
	private void shutdown() {
		log.info("Deleting own process.");
		if (timer != null)
			timer.cancel();
		if (processId != null) {
			processDao.delete(processId);
			log.debug("{} is deleted.", processId);
		}

		log.debug("Finding locks owned by deleted {}.", processId);
		List<TimeSliceLock> lockTokens = timeSliceLockDao
				.findByProcess(processId);
		int ncleaned = timeSliceUtil.clean(lockTokens);
		if (ncleaned > 0)
			log.info("Cleaned up {} locks on shutdown.\n", ncleaned);
		processId = null;
	}
}
