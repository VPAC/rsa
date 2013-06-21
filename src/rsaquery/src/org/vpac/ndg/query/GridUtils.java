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
import java.util.EnumSet;
import java.util.Set;

import org.vpac.ndg.query.coordinates.GridProjected;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

import ucar.nc2.constants.AxisType;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.NetcdfDataset.Enhance;

public class GridUtils {

	private Set<Enhance> CSYS_ENHANCEMENTS = EnumSet.of(Enhance.CoordSystems);

	/**
	 * Find the grid of a dataset.
	 * @param ds The dataset to query.
	 * @throws IOException If the dataset can not be read.
	 */
	public GridProjected findBounds(NetcdfDataset ds) throws IOException {

		if (ds.enhanceNeeded(CSYS_ENHANCEMENTS))
			ds.enhance(CSYS_ENHANCEMENTS);

		if (ds.getCoordinateSystems().size() == 0) {
			// TODO: support this.
			throw new UnsupportedOperationException(
					"No coordinate system; not supported yet.");
		}

		CoordinateSystem srs = ds.getCoordinateSystems().get(0);
		srs.getProjection();

		VectorReal min = VectorReal.createEmpty(2);
		VectorReal max = VectorReal.createEmpty(2);
		VectorInt shape = VectorInt.createEmpty(2);
		if (srs.isGeoXY()) {
			CoordinateAxis x = srs.findAxis(AxisType.GeoX);
			min.setX(x.getMinValue());
			max.setX(x.getMaxValue());
			shape.setX(x.getSize());
			CoordinateAxis y = srs.findAxis(AxisType.GeoY);
			min.setY(y.getMinValue());
			max.setY(y.getMaxValue());
			shape.setY(y.getSize());

		} else {
			CoordinateAxis lon = srs.findAxis(AxisType.Lon);
			min.setX(lon.getMinValue());
			max.setX(lon.getMaxValue());
			shape.setX(lon.getSize());
			CoordinateAxis lat = srs.findAxis(AxisType.Lat);
			min.setY(lat.getMinValue());
			max.setY(lat.getMaxValue());
			shape.setY(lat.getSize());
		}
		// Time is not handled here: the grid can't describe irregularly-spaced
		// data.

		// Determine resolution from bounds and number of elements:
		//
		//     res = (max - min) / (n - 1)
		//
		// Note that the extents don't cover the last cell (or perhaps they are
		// cell-centre-aligned), so when doing the calculation we need to
		// subtract one from the number of elements.
		VectorReal resolution = max.subNew(min).div(shape.subNew(1));
		min.sub(resolution.divNew(2));
		max.add(resolution.divNew(2));

		BoxReal bounds = new BoxReal(2);
		bounds.setMin(min);
		bounds.setMax(max);

		return new GridProjected(bounds, resolution, srs);
	}
}
