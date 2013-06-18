package org.vpac.ndg.storage.model;

import org.vpac.ndg.common.datamodel.RunningTaskState;

public class TimeSliceLock {
	private String id;
	private String timesliceId;
	private String processId;
	private String operation;
	private String user;
	private RunningTaskState state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public RunningTaskState getState() {
		return state;
	}

	public void setState(RunningTaskState state) {
		this.state = state;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getTimesliceId() {
		return timesliceId;
	}

	public void setTimesliceId(String timesliceId) {
		this.timesliceId = timesliceId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return String.format("Lock(id=%s, proc=%s)", id, processId);
	}

}
