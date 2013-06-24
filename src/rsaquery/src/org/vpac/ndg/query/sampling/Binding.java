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

package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxInt;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

import ucar.nc2.NetcdfFileWriter;

public interface Binding {

	/**
	 * Read a value from the pixel source into the buffer.
	 */
	void transfer(VectorReal coFrom, VectorInt coTo, int sourceIndex) throws IOException;

	/**
	 * Write the buffer to the target variable.
	 */
	void commit(NetcdfFileWriter output) throws IOException;

	/**
	 * @param bounds The bounds of the current tile.
	 */
	void setBounds(BoxInt bounds);
}
