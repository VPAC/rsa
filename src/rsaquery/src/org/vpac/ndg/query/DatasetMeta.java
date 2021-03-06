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

package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.coordinates.HasCoordinateSystem;
import org.vpac.ndg.query.coordinates.HasGridProjected;

import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Provides a common way to access various metadata of a dataset.
 * @author Alex Fraser
 */
public interface DatasetMeta extends HasGridProjected, HasCoordinateSystem {

	String getName();

	/**
	 * Close the underlying dataset.
	 */
	void close() throws IOException;

	/**
	 * @return The wrapped dataset.
	 */
	NetcdfDataset getDataset();

	Variable findVariable(String name);

	VariableAdapter getVariableAdapter(String name)
			throws QueryConfigurationException;

}
