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

package org.vpac.ndg.datamodel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.ncml.NcMLReader;

/**
 * Constructs virtual aggregations from NCML mementos.
 * @author Alex Fraser
 */
public class AggregationOpener {

	Logger log = LoggerFactory.getLogger(AggregationOpener.class);

	/**
	 * Converts a deserialised NCML file into an open (read-only) NetcdfDataset.
	 * 
	 * @throws IOException
	 *             if any of the referenced files can't be open.
	 * @throws IllegalArgumentException
	 *             if the aggregation definition is malformed.
	 */
	public NetcdfDataset open(AggregationDefinition ncmlDataset, String locationHint)
			throws IOException, IllegalArgumentException {

		StringWriter writer = new StringWriter();
		ncmlDataset.serialise(writer);
		log.debug("Opening {}", writer);
		InputStream istream = new ByteArrayInputStream(writer.toString().getBytes());
		NetcdfDataset netcdfFile = NcMLReader.readNcML(istream, null);
		return netcdfFile;

	}

}
