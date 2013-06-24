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
import java.util.List;

import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;

public interface DataExport {

	public void setDatasetId(String id);
	public void setExtents(String x1, String y1, String x2, String y2);
	public void setTimeRange(String start, String end);
	public void setBandNamesFilter(List<String> bandNamesFilter);

	/**
	 * @param value Whether to use bilinear interpolation. If null, the decision
	 *        will be made based on the domain type of the band.
	 */
	public void setUseBilinearInterpolation(Boolean value);


	/**
	 * Export the data.
	 * @return The path of the file that was written to.
	 * @throws TaskInitialisationException
	 *             if the task fails to start.
	 * @throws TaskException
	 *             if the task fails to finish.
	 * @throws IOException
	 *             if the file can't be copied to the output location.
	 */
//	public Path exportSync(Path outputDir, String outputFile)
//			throws TaskInitialisationException, TaskException, IOException;

	public String start() throws TaskInitialisationException, TaskException, IOException;

}
