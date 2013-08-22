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
