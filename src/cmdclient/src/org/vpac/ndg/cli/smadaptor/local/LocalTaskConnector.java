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
