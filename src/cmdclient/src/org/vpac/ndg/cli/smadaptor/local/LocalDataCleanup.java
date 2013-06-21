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
