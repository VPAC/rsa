package org.vpac.ndg.cli.smadaptor.local;

import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.cli.smadaptor.DataCleanup;
import org.vpac.ndg.lock.ProcessUpdateTimer;
import org.vpac.ndg.storage.util.TimeSliceUtil;

public class LocalDataCleanup implements DataCleanup {

	@Autowired
	TimeSliceUtil timeSliceUtil;

	@Override
	public int cleanLocks(boolean force) {
		String process = ProcessUpdateTimer.INSTANCE.acquire();
		try {
			return timeSliceUtil.cleanOthers(process, force);
		}
		finally {
			ProcessUpdateTimer.INSTANCE.release();
		}
	}

}
