package org.vpac.ndg.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationShutdownHook extends Thread {

	final private Logger log = LoggerFactory.getLogger(ApplicationShutdownHook.class);

	Process process = null;

	public ApplicationShutdownHook(Process p) {
		process = p;
	}

	public void run() {
		if (process != null) {
			log.debug("Terminating external process");
			process.destroy();
		}
	}
}
