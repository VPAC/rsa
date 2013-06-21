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

import java.util.ArrayList;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.common.datamodel.RunningTaskState;
import org.vpac.ndg.storage.model.Process;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.TimeSliceLock;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class TimeSliceLockDaoImpl extends CustomHibernateDaoSupport implements
		TimeSliceLockDao {

	final Logger log = LoggerFactory.getLogger(TimeSliceLockDaoImpl.class);

	@Autowired
	TimeSliceDao timeSliceDao;
	@Autowired
	ProcessDao processDao;

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public TimeSliceLock create(TimeSliceLock tsl){
		getHibernateTemplate().save(tsl);
		return tsl;
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(TimeSliceLock tsl){
		getHibernateTemplate().update(tsl);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(TimeSliceLock tsl) {
		getHibernateTemplate().delete(tsl);
	}

	@Transactional
	public TimeSliceLock retrieve(String id){
		return getHibernateTemplate().get(TimeSliceLock.class, id);
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<TimeSliceLock> tryReadLock(List<String> timeSliceIds,
			String processId, String operation, String user)
			throws IllegalMonitorStateException {
		// Just iterate over the ids in one transaction. If any can't be
		// acquired, roll the whole transaction back.

		List<TimeSliceLock> locks = new ArrayList<>();
		for (String tsid : timeSliceIds) {
			TimeSliceLock tslock = tryReadLock(tsid, processId, operation,
					user);
			locks.add(tslock);
		}
		return locks;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	TimeSliceLock tryReadLock(String timeSliceId, String processId,
			String operation, String user)
			throws IllegalMonitorStateException {
		// First lock the timeslice row. Set scope to prevent cascading.
		TimeSlice ts = (TimeSlice) getSession().load(TimeSlice.class,
				timeSliceId, new LockOptions(LockMode.PESSIMISTIC_WRITE));

		// Check status. Can't lock if there is a write lock, but can if there
		// are read locks.
		if (ts.getLockCount() > 0 && ts.getLockMode() == 'w') {
			// Throw unchecked exception to force rollback.
			// http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/transaction.html#transaction-declarative-rolling-back
			log.debug("{} is write-locked", ts);
			throw new IllegalMonitorStateException("Already locked.");
		}

		log.trace("Incrementing read lock for {}; was {}", ts.getLockCount());
		ts.setLockCount(ts.getLockCount() + 1);
		ts.setLockMode('r');
		getSession().saveOrUpdate(ts);

		// Now that the lock has been obtained, create some metadata to allow
		// others to trace the lock. This is very important: without this, it's
		// impossible to distinguish between a valid lock and one that belonged
		// to a machine that has crashed.
		TimeSliceLock tslock = new TimeSliceLock();
		tslock.setTimesliceId(ts.getId());
		tslock.setProcessId(processId);
		tslock.setOperation(operation);
		tslock.setUser(user);
		tslock.setState(RunningTaskState.RUNNING);
		create(tslock);
		return tslock;
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public List<TimeSliceLock> tryWriteLock(List<String> timeSliceIds,
			String processId, String operation, String user)
			throws IllegalMonitorStateException {
		// Just iterate over the ids in one transaction. If any can't be
		// acquired, roll the whole transaction back.

		List<TimeSliceLock> locks = new ArrayList<>();
		for (String tsid : timeSliceIds) {
			TimeSliceLock tslock = tryWriteLock(tsid, processId, operation,
					user);
			locks.add(tslock);
		}
		return locks;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	TimeSliceLock tryWriteLock(String timeSliceId, String processId,
			String operation, String user)
			throws IllegalMonitorStateException {
		// First lock the timeslice row. Set scope to prevent cascading.
		TimeSlice ts = (TimeSlice) getSession().load(TimeSlice.class,
				timeSliceId, new LockOptions(LockMode.PESSIMISTIC_WRITE));

		// Check status. We can't lock if there are *any* other locks held -
		// read or write.
		if (ts.getLockCount() > 0) {
			// Throw unchecked exception to force rollback.
			// http://static.springsource.org/spring/docs/3.1.x/spring-framework-reference/html/transaction.html#transaction-declarative-rolling-back
			log.debug("{} is already locked (count: {})", ts, ts.getLockCount());
			throw new IllegalMonitorStateException("Already locked.");
		}

		log.trace("Incrementing write lock for {}; was {}", ts.getLockCount());
		ts.setLockCount(ts.getLockCount() + 1);
		ts.setLockMode('w');
		getSession().saveOrUpdate(ts);

		// Now that the lock has been obtained, create some metadata to allow
		// others to trace the lock. This is very important: without this, it's
		// impossible to distinguish between a valid lock and one that belonged
		// to a machine that has crashed.
		TimeSliceLock tslock = new TimeSliceLock();
		tslock.setTimesliceId(ts.getId());
		tslock.setProcessId(processId);
		tslock.setOperation(operation);
		tslock.setUser(user);
		tslock.setState(RunningTaskState.RUNNING);
		create(tslock);
		return tslock;
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void unlock(List<TimeSliceLock> locks) {
		// Just iterate over the ids in one transaction.
		for (TimeSliceLock lock : locks) {
			unlock(lock);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void unlock(TimeSliceLock lock) {
		// First lock the timeslice row. Set scope to prevent cascading.
		TimeSlice ts = (TimeSlice) getSession().load(TimeSlice.class,
				lock.getTimesliceId(),
				new LockOptions(LockMode.PESSIMISTIC_WRITE));

		// Check status.
		if (ts.getLockCount() <= 0) {
			throw new IllegalMonitorStateException(String.format(
					"Time slice %s is not locked.", ts));
		}

		log.trace("Decrementing read lock for {}", ts);
		ts.setLockCount(ts.getLockCount() - 1);
		getSession().saveOrUpdate(ts);

		delete(lock);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public void updateRunningState(List<TimeSliceLock> lockTokens,
			RunningTaskState state) {
		for (TimeSliceLock lockToken : lockTokens) {
			lockToken.setState(state);
			log.debug("update running state(id: {} )", lockToken.getId());
			update(lockToken);
		}
	}

	@Transactional
	@Override
	public List<TimeSliceLock> findByTimeSlice(String timeSliceId) {
		@SuppressWarnings("unchecked")
		List<TimeSliceLock> list = getHibernateTemplate().find(
				"FROM TimeSliceLock as lock WHERE lock.timesliceId = ?",
				timeSliceId);

		return list;
	}

	@Transactional
	@Override
	public List<TimeSliceLock> findByProcess(String processId) {
		@SuppressWarnings("unchecked")
		List<TimeSliceLock> list = getHibernateTemplate().find(
				"FROM TimeSliceLock as lock WHERE lock.processId = ?",
				processId);

		return list;
	}

	@Transactional
	@Override
	public List<TimeSliceLock> listOrphaned() {
		@SuppressWarnings("unchecked")
		List<TimeSliceLock> list = getHibernateTemplate().find(
				"FROM TimeSliceLock as lock WHERE not exists " +
				"(FROM Process as p WHERE p.id = lock.processId)");

		return list;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	public TimeSliceLock adoptOne(String adopterProcessId)
			throws InterruptedException {
		List<TimeSliceLock> lockList = listOrphaned();
		if (lockList.size() == 0)
			return null;

		// Lock the row of the token and try to update it.
		TimeSliceLock lockToken = lockList.get(0);
		lockToken = (TimeSliceLock) getSession().load(TimeSliceLock.class,
				lockToken.getId(), new LockOptions(LockMode.PESSIMISTIC_WRITE));

		Process proc = processDao.retrieve(lockToken.getProcessId());
		if (proc != null) {
			throw new InterruptedException(String.format("Lock %s was " +
					"apparently taken by another process.", lockToken.getId()));
		}

		lockToken.setProcessId(adopterProcessId);
		update(lockToken);
		return lockToken;
	}

	public boolean isLocked(TimeSliceLock lockToken) {
		boolean bResult = false;
		if(lockToken.getTimesliceId() == null || lockToken.getTimesliceId().isEmpty()) {
			return bResult;
		}

		TimeSlice ts = timeSliceDao.retrieve(lockToken.getTimesliceId());
		if(ts == null) {
			return bResult;
		}

		if (ts.getLockCount() < 0) {
			// Lock count not initialized correctly
			throw new IllegalMonitorStateException(String.format(
					"Time slice %s lock count not initialised.", ts));
		} else if (ts.getLockCount() == 0) {
			// time slice not locked
			bResult = false;
		} else {
			// time slice locked
			bResult = true;
		}

		return bResult;
	}
}
