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

package org.vpac.ndg.cli.smadaptor;

import java.io.IOException;
import java.util.List;

import org.vpac.ndg.storage.model.Band;


public interface BandConnector {

	public List<Band> list(String id);

	public Band createBand(String dsid, String name, 
			String datatype,
			String nodata,
			String isContinuous,
			String isMetadata);

	public void delete(String id) throws IOException;

	public void update(Band newBand) throws IOException;

	public Band retrieve(String bandId);

	public void updateInfo(Band newBand);
	
}
