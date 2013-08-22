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

import java.util.List;

import org.vpac.ndg.storage.model.JobProgress;

public interface TaskConnector {

	/**
	 * @param type If not null, the list will be filtered by the given type.
	 * @param status If not null, the list will be filtered by the given status.
	 * @return A list of the tasks in the RSA.
	 */
	public List<JobProgress> list(String type, String status);

	public JobProgress get(String id);

}
