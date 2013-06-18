package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

import ucar.nc2.dataset.CoordinateSystem;

/**
 * Defines a geospatial raster grid.
 * @author Alex Fraser
 */
public class GridProjected extends Grid {

	private CoordinateSystem srs;

	public GridProjected(BoxReal bounds, VectorReal resolution,
			CoordinateSystem srs) {

		super(bounds, resolution);
		this.srs = srs;
	}

	/**
	 * @return The coordinate system used by the grid.
	 */
	public CoordinateSystem getSrs() {
		return srs;
	}

	public void setSrs(CoordinateSystem srs) {
		this.srs = srs;
	}

}
