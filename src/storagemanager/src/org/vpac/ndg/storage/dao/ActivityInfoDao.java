package org.vpac.ndg.storage.dao;

import java.util.List;

import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.storage.model.ActivityInfo;

public interface ActivityInfoDao {
	public void save(ActivityInfo activity);
	public ActivityInfo retrieve(String id);
	List<ActivityInfo> search(TaskState state, int page, int pageSize);
}
