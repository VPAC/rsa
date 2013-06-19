package org.vpac.ndg.lock;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.common.datamodel.RunningTaskState;
import org.vpac.ndg.storage.dao.TimeSliceLockDao;
import org.vpac.ndg.storage.model.TimeSliceLock;
import org.vpac.ndg.storage.util.TimeSliceUtil;

/**
 * This class allows time slices to be locked using Java's regular ReadWriteLock
 * interface. This lock is <em>not</em> reentrant.
 *
 * @author hsumanto
 * @author adfries
 *
 */
public class TimeSliceDbReadWriteLock implements ReadWriteLock, HasRunningState {

	private final Logger log = LoggerFactory.getLogger(TimeSliceDbReadWriteLock.class);

	private final ReadLock readLock;
	private final WriteLock writeLock;
	private List<String> timeSliceIds;
	private List<TimeSliceLock> lockTokens;
	private String operation;

	TimeSliceLockDao timeSliceLockDao;
	TimeSliceUtil timeSliceUtil;


	/**
	 * Construct a DbReadWriteLock object for the specified time slices. To
	 * complete initialisation, the time slices that this lock operates on
	 * should be configured using {@link #getTimeSliceIds()}.
	 * @param operation The operation to be performed.
	 */
	public TimeSliceDbReadWriteLock(String operation) {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceLockDao = (TimeSliceLockDao) appContext.getBean("timeSliceLockDao");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");

		this.operation = operation;
		timeSliceIds = new ArrayList<>();
		readLock = new ReadLock();
		writeLock = new WriteLock();
	}

	@Override
	public Lock readLock() {
		return readLock;
	}

	@Override
	public Lock writeLock() {
		return writeLock;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Override
	public void setState(RunningTaskState state) {
		timeSliceLockDao.updateRunningState(lockTokens, state);
	}

	public List<String> getTimeSliceIds() {
		return timeSliceIds;
	}

	String getUserName() {
		String user = System.getProperty("user.name");
		String host;
		try {
			host = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e1) {
			host = "";
		}
		return String.format("%s@%s", user, host);
	}

	/**
	 * This class implements the ReadLock capability for DbReadWriteLock.
	 * 
	 * @author hsumanto
	 * @author adfries
	 */
	public class ReadLock implements Lock {

		private ProcessUpdateTimer updateTimer;

		@Override
		public void lock() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Condition newCondition() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean tryLock() throws IllegalMonitorStateException {
			log.debug("Read-locking {}", timeSliceIds);
			if (lockTokens != null) {
				throw new IllegalMonitorStateException(String.format(
						"Tried to lock twice! Time slices: %s", timeSliceIds));
			}

			try {
				// Start a heartbeat, so this lock doesn't expire prematurely.
				String proc = ProcessUpdateTimer.INSTANCE.acquire();
				updateTimer = ProcessUpdateTimer.INSTANCE;

				lockTokens = timeSliceLockDao.tryReadLock(timeSliceIds,
						proc, operation, getUserName());

				log.trace("Successfully locked {}", timeSliceIds);
				return true;

			} catch (IllegalMonitorStateException e) {
				log.trace("Failed to lock {}", timeSliceIds);
				releaseProcessUpdateTimer();
				return false;
			} catch (RuntimeException e) {
				log.trace("Error locking {}", timeSliceIds);
				releaseProcessUpdateTimer();
				throw e;
			}
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit)
				throws InterruptedException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void unlock() throws IllegalMonitorStateException {
			log.debug("Read-unlocking {}", timeSliceIds);
			if (lockTokens == null) {
				throw new IllegalMonitorStateException("Tried to unlock a " +
						"lock that is not currently held.");
			}
			// This is a little messy: the tokens are cleaned before unlocking.
			timeSliceUtil.clean(lockTokens);
			timeSliceLockDao.unlock(lockTokens);
			lockTokens = null;
			releaseProcessUpdateTimer();
		}

		private void releaseProcessUpdateTimer() {
			if (updateTimer == null) {
				return;
			}

			try {
				ProcessUpdateTimer.INSTANCE.release();
				updateTimer = null;
			} catch (RuntimeException e) {
				log.error("Error when stopping ProcessCleanupTimer: {}", e);
			}
		}
	}

	/**
	 * This class implement WriteLock capability for DbReadWriteLock.
	 * @author hsumanto
	 * @author adfries
	 */
	public class WriteLock implements Lock {

		private ProcessUpdateTimer updateTimer;
		private ProcessCleanupTimer cleanupTimer;

		@Override
		public void lock() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void lockInterruptibly() throws InterruptedException {
			throw new UnsupportedOperationException();
		}

		@Override
		public Condition newCondition() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean tryLock() throws IllegalMonitorStateException {
			log.debug("Write-locking {}", timeSliceIds);
			if (lockTokens != null) {
				throw new IllegalMonitorStateException(String.format(
						"Tried to lock twice! Time slices: %s", timeSliceIds));
			}


			try {

				// During write operations, old locks and tasks are cleaned up
				// automatically - even if they don't affect this lock.
				// TODO: Add an option to disable this.
				ProcessCleanupTimer.INSTANCE.acquire();
				cleanupTimer = ProcessCleanupTimer.INSTANCE;

				// Start a heartbeat, so this lock doesn't expire prematurely.
				String proc = ProcessUpdateTimer.INSTANCE.acquire();
				updateTimer = ProcessUpdateTimer.INSTANCE;

				lockTokens = timeSliceLockDao.tryWriteLock(timeSliceIds,
						proc, operation, getUserName());

				log.trace("Successfully locked {}", timeSliceIds);
				return true;

			} catch (IllegalMonitorStateException e) {
				log.trace("Failed to lock {}", timeSliceIds);
				releaseProcesses();
				return false;
			} catch (RuntimeException e) {
				log.trace("Error locking {}", timeSliceIds);
				releaseProcesses();
				throw e;
			}
		}

		@Override
		public boolean tryLock(long time, TimeUnit unit)
				throws InterruptedException {
			throw new UnsupportedOperationException();
		}

		@Override
		public void unlock() throws IllegalMonitorStateException {
			log.debug("Write-unlocking {}", timeSliceIds);
			if (lockTokens == null) {
				throw new IllegalMonitorStateException("Tried to unlock a " +
						"lock that is not currently held.");
			}
			// This is a little messy: the tokens are cleaned before unlocking.
			timeSliceUtil.clean(lockTokens);
			timeSliceLockDao.unlock(lockTokens);
			lockTokens = null;
			releaseProcesses();
		}

		private void releaseProcesses() {
			if(cleanupTimer != null) {
				try {
					ProcessCleanupTimer.INSTANCE.release();
					cleanupTimer = null;
				} catch (RuntimeException e) {
					log.error("Error when stopping ProcessCleanupTimer: {}", e);
				}
			}
			if(updateTimer != null) {
				try {
					ProcessUpdateTimer.INSTANCE.release();
					updateTimer = null;
				} catch (RuntimeException e) {
					log.error("Error when stopping ProcessUpdateTimer: {}", e);
				}
			}
		}
	}
}
