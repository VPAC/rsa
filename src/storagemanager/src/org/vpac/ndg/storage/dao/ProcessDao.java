package org.vpac.ndg.storage.dao;

import java.util.List;

import org.vpac.ndg.storage.model.Process;

public interface ProcessDao extends BasicDao<Process> {
	List<Process> list();
	int deleteStale();
	int deleteOthers(String id);
	void update(String processId);
	void delete(String processId);
}
