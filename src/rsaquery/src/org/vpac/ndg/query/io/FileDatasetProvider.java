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

package org.vpac.ndg.query.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.DatasetUtils;
import org.vpac.ndg.query.GridUtils;
import org.vpac.ndg.query.coordinates.GridProjected;
import org.vpac.ndg.query.coordinates.QueryCoordinateSystem;
import org.vpac.ndg.query.coordinates.TimeAxis;
import org.vpac.ndg.query.math.BoxReal;

import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Opens datasets on the local filesystem using URIs like
 * <em>../data/test.nc</em> or <em>file:///data/test.nc</em>
 * 
 * @author Alex Fraser
 * 
 */
public class FileDatasetProvider implements DatasetProvider {

	final Logger log = LoggerFactory.getLogger(FileDatasetProvider.class);

	private DatasetUtils datasetUtils;
	private GridUtils gridUtils;

	public FileDatasetProvider() {
		datasetUtils = new DatasetUtils();
		gridUtils = new GridUtils();
	}

	@Override
	public boolean canOpen(String uri) {
		// Just check whether the URI makes sense for this provider - not
		// whether the file exists.
		URI parsedUri;
		try {
			parsedUri = new URI(uri);
		} catch (URISyntaxException e) {
			return false;
		}

		String scheme = parsedUri.getScheme();
		if (scheme != null) {
			// If the scheme is non-null, then we have to check it's for local
			// files. Note that in this case, it is impossible to refer to a
			// relative file: a relative URI has no scheme.
			if (!scheme.equals("file")) {
				return false;
			}

			String authority = parsedUri.getAuthority();
			if (authority != null) {
				// Ensure the user isn't trying to access a remote resource.
				if (!authority.equals("") && !authority.equals("localhost"))
					return false;
			}
		}

		if (parsedUri.getPath() == null)
			return false;

		return true;
	}

	@Override
	public NetcdfDataset open(String uri, String referential,
			BoxReal boundsHint, DateTime minTimeHint, DateTime maxTimeHint,
			List<String> bands) throws IOException {

		// NOTE: extents hints are not used here; this is a simple opening
		// mechanism.
		return open(uri, referential);
	}

	public NetcdfDataset open(String uri, String referential)
			throws IOException {

		URI parsedUri;
		try {
			parsedUri = new URI(uri);
		} catch (URISyntaxException e) {
			throw new IOException("Could not open dataset", e);
		}

		String path = parsedUri.getPath();
		File resolvedFile = resolve(referential, path);
		log.trace("Opening dataset {}", resolvedFile);
		return NetcdfDataset.openDataset(resolvedFile.getAbsolutePath());
	}

	protected File resolve(String referential, String path) {
		File file = new File(path);
		if (file.isAbsolute())
			return file;
		else
			return new File(referential, path);
	}

	@Override
	public DatasetMetadata queryMetadata(String uri, String referential)
			throws IOException {

		NetcdfDataset ds = open(uri, referential);
		try {
			DatasetMetadata meta = new DatasetMetadata();

			ds.enhance();
			if (ds.getCoordinateSystems().size() == 0) {
				throw new IOException(String.format(
						"Dataset %s has no coordinate system.", uri));
			}

			GridProjected grid = gridUtils.findBounds(ds);
			TimeAxis timeAxis = datasetUtils.findTimeCoordinates(ds);
			meta.setCsys(new QueryCoordinateSystem(grid, timeAxis));

			List<Variable> vars = ds.getVariables();
			List<String> varNames = new ArrayList<String>(vars.size());
			for (Variable var : vars) {
				varNames.add(var.getFullName());
			}
			meta.setVariables(varNames);

			return meta;

		} finally {
			ds.close();
		}
	}

}
