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
