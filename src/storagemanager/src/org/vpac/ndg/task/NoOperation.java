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

import org.vpac.ndg.application.Constant;
import org.vpac.ndg.exceptions.TaskException;

/**
 * This is just dummy task class which doesn't perform any operation.
 * @author hsumanto
 *
 */
public class NoOperation extends Task {

	public NoOperation(String description) {
		super(Constant.TASK_DESCRIPTION_NOOP);
	}

	@Override
	public void initialise() {
		// Do nothing
	}

	@Override
	public void execute() throws TaskException {
		// Do nothing
	}

	@Override
	public void rollback() {
		// Do nothing
	}

	@Override
	public void finalise() {
		// Do nothing
	}

}
