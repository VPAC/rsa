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

package org.vpac.ndg.cli.smadaptor.local;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.cli.smadaptor.TaskConnector;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;
import org.vpac.ndg.storage.dao.JobProgressDao;
import org.vpac.ndg.storage.model.JobProgress;

public class LocalTaskConnector implements TaskConnector {
	@Autowired
	JobProgressDao jobProgressDao;

	@Override
	public List<JobProgress> list(String typestr, String statusstr) {
		// TODO: Implement better paging.
		int page = 0;
		int pageSize = 100;

		TaskType type;
		if (typestr != null)
			type = TaskType.valueOf(typestr);
		else
			type = null;

		List<JobProgress> result = jobProgressDao.search(type,
				TaskState.valueOf(statusstr), page, pageSize);

		return result;
	}

	@Override
	public JobProgress get(String id) {
		return jobProgressDao.retrieve(id);
	}

}
