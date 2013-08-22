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

package org.vpac.ndg.exceptions;

import org.vpac.ndg.application.Constant;

/**
 * Exception class when a failure is detected when performing task.
 * @author hsumanto
 *
 */
public class TaskException extends Exception {
	private static final long serialVersionUID = 1L;

	public TaskException() {
		super();
	}

	public TaskException(String message, String cause) {
		super(String.format(Constant.ERR_TASK_EXCEPTION, message, cause));
	}	
	
	public TaskException(String message, Throwable cause) {
		super(String.format(Constant.ERR_TASK_EXCEPTION, message, cause));
	}

	public TaskException(String message) {
		super(message);
	}

	public TaskException(Throwable cause) {
		super(cause);
	}	
}
