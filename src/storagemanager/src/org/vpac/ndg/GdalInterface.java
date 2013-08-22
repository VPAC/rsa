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
