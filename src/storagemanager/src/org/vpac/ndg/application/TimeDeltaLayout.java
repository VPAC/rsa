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
