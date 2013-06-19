package org.vpac.ndg;

import java.nio.file.Path;

import org.gdal.ogr.DataSource;
import org.gdal.ogr.ogr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A thin wrapper around GDAL functions. Don't call the functions directly, or
 * GDAL may not be initialised properly!
 * @author Alex Fraser
 */
public class OgrInterface {

	static final Logger log = LoggerFactory.getLogger(OgrInterface.class);

	static {
		try {
			log.debug("Initializing OGR...");
			ogr.RegisterAll();
			log.debug("OGR loaded.");
			if (log.isTraceEnabled()) {
				log.trace("OGR drivers:");
				for (int i = 0; i < ogr.GetDriverCount(); i++) {
					log.trace("\t{}", ogr.GetDriver(i).getName());
				}
			}

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			log.error("Initial SessionFactory creation failed: {}", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public DataSource open(Path file) {
		return ogr.Open(file.toString());
	}
}
