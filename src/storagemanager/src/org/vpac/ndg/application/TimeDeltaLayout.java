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

package org.vpac.ndg.application;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class TimeDeltaLayout extends PatternLayout {

	long lastTime = -1;

	@Override
	public String doLayout(ILoggingEvent event) {

		long ctime = event.getTimeStamp();
		long startTime = event.getLoggerContextVO().getBirthTime();
		if (lastTime == -1)
			lastTime = event.getLoggerContextVO().getBirthTime();

		float elapsedTime = (float)(ctime - startTime);
		elapsedTime /= 1000f;

		float delta = (float)(ctime - lastTime);
		delta /= 1000f;
		lastTime = ctime;

		return String.format("%5.1fs (%+5.1fs) %s", elapsedTime, delta,
				super.doLayout(event));
	}

}
