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
