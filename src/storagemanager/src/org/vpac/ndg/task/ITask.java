/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.task;

import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;

public interface ITask {
	/**
	 * Perform initialisation on the task.
	 * @throws TaskInitialisationException An error encountered during initialisation of the task.
	 */	
	public void initialise() throws TaskInitialisationException;
	/**
	 * Perform the execution of the task.
	 * @throws TaskException An error ecountered during execution of the task.
	 */
	public void execute() throws TaskException;
	/**
	 * Perform the required rollback action when task execution has failed.
	 */
	public void rollback();
	/**
	 * Perform cleaning up action required at the end of the task.
	 */
	public void finalise();
	/**
	 * Get description of the task.
	 * @return Returns the description of the task.
	 */
	public String getDescription();
}
