package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.VectorReal;

import ucar.unidata.geoloc.LatLonPointImpl;
import ucar.unidata.geoloc.ProjectionImpl;
import ucar.unidata.geoloc.ProjectionPointImpl;

/**
 * Converts between projected coordinate systems. Note that this does not handle
 * grid resolutions.
 *
 * @author Alex Fraser
 */
public abstract class WarpSpatial implements Warp {

	@Override
	public void warp(BoxReal box) {
		// TODO: This does not account for non-linear warps. The edges of the
		// box should be subdivided.
		VectorReal ll = box.getMin().copy();
		VectorReal ur = box.getMax().copy();
		VectorReal ul = ll.copy();
		ul.setY(ur.getY());
		VectorReal lr = ur.copy();
		lr.setY(ll.getY());

		warp(ll);
		warp(ur);
		warp(ul);
		warp(lr);

		box.setMin(ll);
		box.setMax(ll);
		box.union(ur);
		box.union(ul);
		box.union(lr);
	}

	static class WarpSpatialGeoxyToGeoxy extends WarpSpatial {

		private ProjectionImpl sourceProj;
		private ProjectionImpl targetProj;

		public WarpSpatialGeoxyToGeoxy(ProjectionImpl sourceProj,
				ProjectionImpl targetProj) {

			this.sourceProj = sourceProj;
			this.targetProj = targetProj;
		}

		@Override
		public void warp(VectorReal co) {
			ProjectionPointImpl pp = new ProjectionPointImpl(
					co.getX() / 1000.0, co.getY() / 1000.0);
			LatLonPointImpl ll = new LatLonPointImpl();
			sourceProj.projToLatLon(pp, ll);
			targetProj.latLonToProj(ll, pp);
			co.setX(pp.getX() * 1000.0);
			co.setY(pp.getY() * 1000.0);
		}

	}

	static class WarpSpatialLatlonToGeoxy extends WarpSpatial {

		private ProjectionImpl targetProj;

		public WarpSpatialLatlonToGeoxy(ProjectionImpl targetProj) {
			this.targetProj = targetProj;
		}

		@Override
		public void warp(VectorReal co) {
			LatLonPointImpl ll = new LatLonPointImpl(co.getY(), co.getX());
			ProjectionPointImpl pp = new ProjectionPointImpl();
			targetProj.latLonToProj(ll, pp);
			co.setX(pp.getX());
			co.setY(pp.getY());
		}

	}

	static class WarpSpatialGeoxyToLatlon extends WarpSpatial {

		private ProjectionImpl sourceProj;

		public WarpSpatialGeoxyToLatlon(ProjectionImpl sourceProj) {
			this.sourceProj = sourceProj;
		}

		@Override
		public void warp(VectorReal co) {
			ProjectionPointImpl pp = new ProjectionPointImpl(
					co.getX(), co.getY());
			LatLonPointImpl ll = new LatLonPointImpl();
			sourceProj.projToLatLon(pp, ll);
			co.setY(ll.getLatitude());
			co.setX(ll.getLongitude());
		}

	}

}
