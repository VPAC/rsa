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

package org.vpac.ndg.query.coordinates;

import java.util.Collections;

import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.coordinates.WarpSpatial.WarpSpatialGeoxyToGeoxy;
import org.vpac.ndg.query.coordinates.WarpSpatial.WarpSpatialGeoxyToLatlon;
import org.vpac.ndg.query.coordinates.WarpSpatial.WarpSpatialLatlonToGeoxy;
import org.vpac.ndg.query.math.VectorReal;

import ucar.nc2.time.CalendarDate;
import ucar.unidata.geoloc.ProjectionImpl;

/**
 * Creates warp instances that can transform coordinates between datasets.
 * @author Alex Fraser
 */
public class WarpFactory {

//	private static Set<Enhance> NEEDED_ENHANCEMENTS = EnumSet.of(Enhance.CoordSystems);

	/**
	 * @param sourceProj The projection to transform from.
	 * @param targetProj The projection to transform to.
	 * @return A warp that transforms between projected coordinates.
	 */
	public Warp createSpatialWarp(ProjectionImpl sourceProj,
			ProjectionImpl targetProj) {

		if (sourceProj.equals(targetProj))
			return new WarpIdentity();
		if (sourceProj.isLatLon() && targetProj.isLatLon())
			return new WarpIdentity();

		if (sourceProj.isLatLon())
			return new WarpSpatialLatlonToGeoxy(targetProj);
		if (targetProj.isLatLon())
			return new WarpSpatialGeoxyToLatlon(sourceProj);

		return new WarpSpatialGeoxyToGeoxy(sourceProj, targetProj);
	}

	/**
	 * Find a warp that can convert between coordinates in two grids.
	 *
	 * @param from The grid to convert from.
	 * @param to The grid to convert to.
	 * @param ndimensions The size of the vectors to support. Note that only the
	 *        X and Y coordinates will be transformed; the others will pass
	 *        through unmodified.
	 */
	public Warp createGridWarp(GridProjected from, GridProjected to,
			int ndimensions) throws QueryConfigurationException {

		if (from.getSrs() == null || to.getSrs() == null) {
			// No projection - just translate
			return findTranslation(from, to, ndimensions, null);
		}

		ProjectionImpl projFrom = from.getSrs().getProjection();
		ProjectionImpl projTo = to.getSrs().getProjection();

		if (projFrom.equals(projTo)) {
			// Same projection - just translate
			return findTranslation(from, to, ndimensions, null);

		} else if (projFrom.isLatLon() && projTo.isLatLon()) {
			// Effectively the same projection
			return findTranslation(from, to, ndimensions, null);

		} else {
			Warp projWarp = createSpatialWarp(projFrom, projTo);
			return findTranslation(from, to, ndimensions, projWarp);
		}

	}

	/**
	 * Find a warp between two grids, assuming that both share the same
	 * coordinate system.
	 */
	protected Warp findTranslation(Grid from, Grid to,
			int ndimensions, Warp innerWarp) {
		// Same coordinate system! That makes things easy: we just need to find
		// a linear offset. Assuming the cells are all in-order and evenly
		// spaced, we just need to find the difference between the first
		// coordinates.

		VectorReal offsetIn = VectorReal.createEmpty(ndimensions, 0);
		VectorReal scaleIn = VectorReal.createEmpty(ndimensions, 1);
		VectorReal offsetOut = VectorReal.createEmpty(ndimensions, 0);
		VectorReal scaleOut = VectorReal.createEmpty(ndimensions, 1);

		offsetIn.setX(from.getBounds().getMin().getX());
		scaleIn.setX(from.getResolution().getX());
		offsetOut.setX(to.getBounds().getMin().getX());
		scaleOut.setX(to.getResolution().getX());
		if (ndimensions > 1) {
			offsetIn.setY(from.getBounds().getMin().getY());
			scaleIn.setY(from.getResolution().getY());
			offsetOut.setY(to.getBounds().getMin().getY());
			scaleOut.setY(to.getResolution().getY());
		}
		// Leave higher dimensions untouched. The coordinates that are passed in
		// to the warp will have the full dimensionality of the image, possibly
		// including time - but this type of warp does not handle temporal
		// warping.

		if (offsetIn.equals(offsetOut) && scaleIn.equals(scaleOut)) {
			return new WarpIdentity();
		} else if (scaleIn.equals(scaleOut)) {
			return new WarpOffset(offsetIn, offsetOut, scaleIn);
		} else if (innerWarp == null) {
			return new WarpOffsetWithScale(
					offsetIn, offsetOut, scaleIn, scaleOut);
		} else {
			return new WarpOffsetWithScaleAndProjection(
					offsetIn, offsetOut, scaleIn, scaleOut, innerWarp);
		}
	}

	public Warp createTemporalWarp(TimeAxis from, TimeAxis to) {
		// The values in 'from' are the values that will be requested by a
		// filter; these must be mapped to values in to.

		double[] mapping = new double[from.getValues().size()];
		for (int i = 0; i < from.getValues().size(); i++) {
			CalendarDate dateFrom = from.getValues().get(i);
			int j = Collections.binarySearch(to.getValues(), dateFrom);
			if (j >= 0) {
				// Direct mapping to an element in the target array.
				mapping[i] = (double)j;
			} else {
				// Fuzzy mapping; use nearest smaller element.
				j = -j;
				j -= 1;
				if (j < 0)
					j = 0;
				else if (j >= to.getValues().size())
					j = to.getValues().size() - 1;
				mapping[i] = (double)j;
			}
		}
		return new WarpTimeLut(mapping);
	}
}
