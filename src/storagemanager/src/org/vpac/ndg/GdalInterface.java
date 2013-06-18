package org.vpac.ndg;

import java.nio.file.Path;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A thin wrapper around GDAL functions. Don't call the functions directly, or
 * GDAL may not be initialised properly!
 * @author Alex Fraser
 */
public class GdalInterface {

	static final Logger log = LoggerFactory.getLogger(GdalInterface.class);

	static {
		try {
			log.debug("Initializing GDAL...");
			gdal.AllRegister();
			log.debug("GDAL loaded; version {}", gdal.VersionInfo());
			if (log.isTraceEnabled()) {
				log.trace("GDAL drivers:");
				for (int i = 0; i < gdal.GetDriverCount(); i++) {
					log.trace("\t{}", gdal.GetDriver(i).getShortName());
				}
			}

		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			log.error("Initial SessionFactory creation failed: {}", ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public Dataset open(Path file) {
		return gdal.Open(file.toString(), gdalconstConstants.GA_ReadOnly);
	}
}
