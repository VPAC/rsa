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

package org.vpac.ndg.cli.smadaptor;

import java.io.IOException;
import java.nio.file.Path;

import org.vpac.ndg.query.QueryDefinition;

public interface DataQuery {

	public void setExtents(String x1, String y1, String x2, String y2);
	public void setTimeRange(String start, String end);

	/**
	 * @param definition The path to the serialised {@link QueryDefinition query
	 *        definition}.
	 */
	public void setQueryPath(Path definition);

	/**
	 * Run a query on an RSA dataset. Note that this starts execution on a
	 * worker thread; the result and error state should be checked by inspecting
	 * the progress object.
	 * 
	 * @throws IOException If the output can't be written to, or if the
	 *         requested data can't be found or read from.
	 */
	public String start() throws IOException;

	/**
	 * @param threads The number of worker threads to use during operation.
	 */
	public void setThreads(String threads);
	public void setOutputPath(Path outputFile);
	public void setNetcdfVersion(String netcdf4);
}
