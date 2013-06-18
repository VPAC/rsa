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
