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

package org.vpac.ndg.task;

/**
 * A simple mutable wrapper object, to be used as a container for transferring
 * data between tasks. You could consider this class to be a collection of size
 * 1.
 *
 * @author adfries
 *
 * @param <T>
 *            The type that this container holds.
 */
public class ScalarReceiver <T> {

	private T value;

	public ScalarReceiver() {
		value = null;
	}

	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
	}

}
