package org.vpac.ndg.storage.dao;

import java.util.List;

import org.vpac.ndg.storage.model.JobProgress;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;

public interface JobProgressDao {
	void save(JobProgress job);
	JobProgress retrieve(String id);
	List<JobProgress> search(TaskType type, TaskState state, int page, int pageSize);
}
