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

package org.vpac.ndg.query.io;

import java.io.IOException;
import java.util.List;

import org.joda.time.DateTime;
import org.vpac.ndg.query.math.BoxReal;

import ucar.nc2.dataset.NetcdfDataset;

/**
 * Provides methods for opening datasets.
 *
 * Queries specify dataset resources as URIs. The framework can then ask a
 * provider if it handles URIs of that type; if it does, then it may be asked
 * to open it.
 *
 * <p>To make use of your new DatasetProvider, register it with the special
 * provider {@link ProviderRegistry}.</p>
 *
 * @author Alex Fraser
 *
 */
public interface DatasetProvider {

	/**
	 * @param uri
	 *            A resource that points to a dataset.
	 * @return true if this provider is able to open datasets identified by URIs
	 *         like the one supplied.
	 */
	boolean canOpen(String uri);

	/**
	 * Opens a dataset for reading.
	 * 
	 * @param uri The dataset to open.
	 * @param referential The identifier (location) of the root of the query.
	 * @param boundsHint The bounds to try to open. May be different to the
	 *        resulting bounds. May be null.
	 * @param maxTimeHint The last time requested. May be different to the
	 *        result. May be null.
	 * @param minTimeHint The first time requested. May be different to the
	 *        result. May be null.
	 * @return The dataset.
	 * @throws IOException if the resource could not be accessed, or if the URI
	 *         is not supported by this provider.
	 */
	NetcdfDataset open(String uri, String referential, BoxReal boundsHint,
			DateTime minTimeHint, DateTime maxTimeHint, List<String> bands)
			throws IOException;

	/**
	 * Get metadata about the dataset. It may be possible to do this without
	 * actually opening the dataset.
	 * 
	 * @param uri The dataset to open.
	 * @param referential The identifier (location) of the root of the query.
	 * @return The metadata.
	 * @throws IOException If the resource could not be accessed, or if the
	 *         required metadata could not be found.
	 */
	DatasetMetadata queryMetadata(String uri, String referential)
			throws IOException;

}
