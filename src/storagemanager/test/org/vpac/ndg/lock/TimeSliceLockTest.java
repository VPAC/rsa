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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.TestUtil;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
public class TimeSliceLockTest {

	final Logger log = LoggerFactory.getLogger(TimeSliceLockTest.class);

	@Autowired
	TestUtil testUtil;
	@Autowired
	DatasetDao datasetDao;
	@Autowired
	TimeSliceDao timeSliceDao;

	TimeSliceDbReadWriteLock lock1;
	TimeSliceDbReadWriteLock lock2;
	List<TimeSlice> tss;

	@Before
	public void setUp() throws Exception {
		testUtil.initialiseDataForExport();
		Dataset ds = datasetDao.findDatasetByName(
				testUtil.getExportDatasetName(), testUtil.getExportDatasetRes());
		tss = datasetDao.getTimeSlices(ds.getId());

		lock1 = new TimeSliceDbReadWriteLock("test");
		lock2 = new TimeSliceDbReadWriteLock("test");
		for (TimeSlice ts : tss) {
			lock1.getTimeSliceIds().add(ts.getId());
			lock2.getTimeSliceIds().add(ts.getId());
		}
	}

	@After
	public void tearDown() throws Exception {
		// Safeguards to prevent the DB from becoming inconsistent
		log.info("Cleaning up locks after test");
		try {
			lock1.readLock().unlock();
		} catch (IllegalMonitorStateException e) {
		}
		try {
			lock1.writeLock().unlock();
		} catch (IllegalMonitorStateException e) {
		}
		try {
			lock2.readLock().unlock();
		} catch (IllegalMonitorStateException e) {
		}
		try {
			lock2.writeLock().unlock();
		} catch (IllegalMonitorStateException e) {
		}
	}

	int getLockCount() {
		TimeSlice ts = tss.get(0);
		TimeSlice freshTs = timeSliceDao.retrieve(ts.getId());
		return freshTs.getLockCount();
	}

	char getLockMode() {
		TimeSlice ts = tss.get(0);
		TimeSlice freshTs = timeSliceDao.retrieve(ts.getId());
		return freshTs.getLockMode();
	}

	@Test
	public void testSingleThreadReadLock() {
		assertEquals(0, getLockCount());

		assertTrue(lock1.readLock().tryLock());
		assertEquals(1, getLockCount());
		assertTrue(getLockMode() == 'r');

		assertTrue(lock2.readLock().tryLock());
		assertEquals(2, getLockCount());

		lock1.readLock().unlock();
		assertEquals(1, getLockCount());

		lock2.readLock().unlock();
		assertEquals(0, getLockCount());

		try {
			lock1.readLock().unlock();
			assertTrue(false);
		} catch (IllegalMonitorStateException e) {
			// Expect IllegalMonitorStateException as trying to unlock unlocked
			// lock
			assertEquals(0, getLockCount());
		}
	}


	@Test
	public void testSingleThreadWriteLock() {
		assertEquals(0, getLockCount());

		assertTrue(lock1.writeLock().tryLock());
		assertEquals(1, getLockCount());
		assertTrue(getLockMode() == 'w');

		assertFalse(lock2.writeLock().tryLock());
		assertEquals(1, getLockCount());

		lock1.writeLock().unlock();
		assertEquals(0, getLockCount());

		try {
			lock1.writeLock().unlock();
			assertTrue(false);
		} catch (IllegalMonitorStateException e) {
			assertEquals(0, getLockCount());
		}
	}


	@Test
	public void testSingleThreadWriteRead() {
		assertEquals(0, getLockCount());

		assertTrue(lock1.writeLock().tryLock());
		assertFalse(lock2.readLock().tryLock());
		assertEquals(1, getLockCount());
		assertTrue(getLockMode() == 'w');				

		lock1.writeLock().unlock();
		assertEquals(0, getLockCount());

	}


	@Test
	public void testSingleThreadReadWrite() {
		assertEquals(0, getLockCount());

		assertTrue(lock1.readLock().tryLock());
		assertFalse(lock2.writeLock().tryLock());
		assertEquals(1, getLockCount());
		assertTrue(getLockMode() == 'r');				

		lock1.readLock().unlock();
		assertEquals(0, getLockCount());
	}	


	volatile int global_count;
	final int ITERATIONS = 100;

	class Incrementor implements Runnable {

		int own_count = 0;
		int iterations;
		ReadWriteLock lock;

		public Incrementor(ReadWriteLock lock, int iterations) {
			this.lock = lock;
			this.iterations = iterations;
		}

		@Override
		public void run() {
			try {
				while (own_count < iterations) {
					if (lock.writeLock().tryLock()) {
						try {
							int count = global_count;
							Thread.sleep(5);
							global_count = count + 1;
							own_count++;
							System.err.println(String.format(
									"own: %d, global: %d",
									own_count, global_count));
						} finally {
							lock.writeLock().unlock();
						}
					} else {
//						System.err.println("missed");
					}
				}
			} catch (InterruptedException e) {
				System.err.println("Thread interrupted.");
			}
		}
	};

	@Test
	public void testMultiThreadedIncrement() throws InterruptedException {
		assertEquals(0, getLockCount());

		global_count = 0;
		Runnable incr1 = new Incrementor(lock1, ITERATIONS);
		Runnable incr2 = new Incrementor(lock2, ITERATIONS);
		Thread thread1 = new Thread(incr1);
		Thread thread2 = new Thread(incr2);
		thread1.start();
		thread2.start();

		thread1.join();
		thread2.join();

		assertEquals(global_count, ITERATIONS * 2);

	}
}
