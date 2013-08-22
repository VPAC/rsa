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

package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.common.datamodel.RunningTaskState;
import org.vpac.ndg.storage.model.TimeSliceLock;

 
@XmlRootElement(name = "TimeSliceLock")
public class TimeSliceLockResponse {
	
	private String id;
	private String timesliceId;
	private String processId;
	private String operation;
	private String user;
	private RunningTaskState state;

	public String getId() {
		return id;
	}

	@XmlElement
	public void setId(String id) {
		this.id = id;
	}

	public String getOperation() {
		return operation;
	}

	@XmlElement
	public void setOperation(String operation) {
		this.operation = operation;
	}

	public RunningTaskState getState() {
		return state;
	}

	@XmlElement
	public void setState(RunningTaskState state) {
		this.state = state;
	}

	public String getProcessId() {
		return processId;
	}

	@XmlElement
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getTimesliceId() {
		return timesliceId;
	}

	@XmlElement
	public void setTimesliceId(String timesliceId) {
		this.timesliceId = timesliceId;
	}

	public String getUser() {
		return user;
	}

	@XmlElement
	public void setUser(String user) {
		this.user = user;
	}
	
	public TimeSliceLockResponse(TimeSliceLock lock) {
		if(lock != null) {
			this.id = lock.getId();
			this.operation = lock.getOperation();
			this.processId = lock.getProcessId();
			this.state = lock.getState();
			this.timesliceId = lock.getTimesliceId();
			this.user = lock.getUser();
		}
	}
	
	public TimeSliceLockResponse() {
	}

	public TimeSliceLock toTimeSliceLock() {
		TimeSliceLock lock = new TimeSliceLock();
		lock.setId(this.id);
		lock.setOperation(this.operation);
		lock.setState(this.state);
		lock.setTimesliceId(this.timesliceId);
		lock.setUser(this.user);
		return lock;
	}
}
