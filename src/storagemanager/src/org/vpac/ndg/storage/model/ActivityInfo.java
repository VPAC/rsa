package org.vpac.ndg.storage.model;

import java.io.Serializable;
import java.util.Date;

import org.vpac.ndg.Utils;
import org.vpac.ndg.common.datamodel.TaskState;

public class ActivityInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String logCommand;
	private String logMessage;
	private Date logTime;
	private TaskState state;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogCommand() {
		return logCommand;
	}
	public void setLogCommand(String logCommand) {
		this.logCommand = logCommand;
	}
	public String getLogMessage() {
		return logMessage;
	}
	public void setLogMessage(String logMessage) {
		this.logMessage = logMessage;
	}
	public Date getLogTime() {
		return logTime;
	}
	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}
	public TaskState getState() {
		return state;
	}
	public void setState(TaskState state) {
		this.state = state;
	}

	public ActivityInfo() {
	}

	public ActivityInfo(String logCommand, String logMessage) {
		this(logCommand, logMessage, Utils.now(), TaskState.RUNNING);
	}

	public ActivityInfo(String logCommand, String logMessage, TaskState state) {
		this(logCommand, logMessage, Utils.now(), state);
	}

	public ActivityInfo(String logCommand, String logMessage, Date logTime, TaskState state) {
		setLogCommand(logCommand);
		setLogMessage(logMessage);
		setLogTime(logTime);
		setState(state);
	}
}
