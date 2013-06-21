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

package org.vpac.ndg.storage.dao;

import java.util.List;

import org.vpac.ndg.common.datamodel.RunningTaskState;
import org.vpac.ndg.storage.model.TimeSliceLock;

public interface TimeSliceLockDao {
	/**
	 * Try to get a read lock on a list of time slices. If any of the locks
	 * can't be obtained, none of them will be.
	 *
	 * @return A list of time slice locks to match the input. Does not return
	 *         null.
	 * @throws IllegalMonitorStateException If the lock could not be obtained
	 *         due to a conflicting write lock.
	 */
	List<TimeSliceLock> tryReadLock(List<String> timeSliceIds,
			String processId, String operation, String hostname)
			throws IllegalMonitorStateException;

	/**
	 * Try to get a write lock on a list of time slices. If any of the locks
	 * can't be obtained, none of them will be.
	 *
	 * @return A list of time slice locks to match the input. Does not return
	 *         null.
	 * @throws IllegalMonitorStateException If the lock could not be obtained
	 *         due to a conflicting lock.
	 */
	List<TimeSliceLock> tryWriteLock(List<String> timeSliceIds,
			String processId, String operation, String hostname)
			throws IllegalMonitorStateException;

	/**
	 * Unlock list of time slices.
	 *
	 * @throws IllegalMonitorStateException If any of the time slices are not
	 *         locked.
	 */
	void unlock(List<TimeSliceLock> locks)
			throws IllegalMonitorStateException;

	/**
	 * Unlock a single time slice.
	 *
	 * @throws IllegalMonitorStateException If the time slice is not locked.
	 */
	void unlock(TimeSliceLock lock)
			throws IllegalMonitorStateException;

	/**
	 * Update the state of a collection of tokens in one transaction.
	 */
	void updateRunningState(List<TimeSliceLock> lockTokens,
			RunningTaskState state);

	List<TimeSliceLock> findByTimeSlice(String timeSliceId);

	List<TimeSliceLock> findByProcess(String processId);

	List<TimeSliceLock> listOrphaned();

	/**
	 * Assigns ownership of a single orphaned lock.
	 * @param adopterProcessId The ID of the process that is adopting the lock.
	 * @return The adopted lock, or null if none were available for adoption.
	 */
	TimeSliceLock adoptOne(String adopterProcessId)
			throws InterruptedException;

	/**
	 * Check if the lock token has lock the timeslice or not.
	 * @param lockToken The lock token to check.
	 * @return Returns true if the timeslice has been locked by token, 
	 * otherwise returns false.
	 */
	boolean isLocked(TimeSliceLock lockToken);
}
