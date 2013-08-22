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

package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.ScalarElement;

/**
 * Addresses a single cell in a dataset. This is a mutable type, effectively
 * allowing cells to be passed by reference.
 * @author Alex Fraser
 */
public class CellScalar implements Cell {

	private ScalarElement value;
	private Prototype prototype;
	private String name;

	public CellScalar(String name, Prototype prototype) {
		this.prototype = prototype;
		value = (ScalarElement) prototype.getElement().copy();
		value.setValid(false);
		this.name = name;
	}

	/**
	 * @return The value of the current cell in the dataset.
	 */
	public ScalarElement getScalar() {
		return value;
	}

    /**
     * Write into the dataset.
     * @param value The value to write.
     */
	public void setScalar(ScalarElement value) {
		this.value = value.copy();
	}

	@Override
	public ScalarElement get() {
		return getScalar();
	}

	@Override
	public void set(Element<?> value) throws ClassCastException {
		setScalar((ScalarElement)value);
	}

	@Override
	public void unset() {
		this.value.setValid(false);
	}

	@Override
	public String toString() {
		return String.format("%s(%s)", name, value);
	}

	@Override
	public Prototype getPrototype() {
		return prototype;
	}

}
