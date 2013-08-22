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

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ucar.nc2.dataset.NetcdfDataset;

/**
 * This class is intended to proxy NetcdfDataset class so that
 * close method could be intercepted for locking functionality.
 * @author hsumanto
 * @author adfries
 */
public class RsaNetcdfDataset extends NetcdfDataset {
	final Logger log = LoggerFactory.getLogger(RsaNetcdfDataset.class);

	private ReadWriteLock lock;

	public RsaNetcdfDataset(NetcdfDataset ds, ReadWriteLock	 lock)
			throws IOException {
		super(ds);
		this.lock = lock;
	}

	@Override
	public void close() throws IOException {
		try {
			super.close();
		} finally {
			lock.readLock().unlock();
			log.debug("Finished unlocking after closing dataset");
		}
	}

}
