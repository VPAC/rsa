package org.vpac.ndg.geometry;

import org.gdal.ogr.Geometry;
import org.gdal.ogr.ogr;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.configuration.NdgConfigManager;

/**
 * Manages the projections used in the storage manager
 * Note: by default the internal storage projection is defined by its EPSG code. 
 * @author glennf
 * @author hsumanto
 */
public class Projection {

	final static Logger log = LoggerFactory.getLogger(Projection.class);
	
	/**
	 * The EPSG ID of the spatial reference system of the internal storage. This
	 * value is defined in the ndg configuration database table with the row label
	 * 'defaultInternalProjection'
	 */ 
	public static int getDefaultMapEpsg() {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		NdgConfigManager ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");
		return ndgConfigManager.getConfig().getTargetSrsEpsgId();
	}

	/**
	 * The EPSG ID of the geographic display spatial reference system.
	 * This value is defined in the ndg configuration database table with the row label
	 * 'geographicDisplayProjection'
	 */
	public static int getDefaultGeoEpsg() {
		return 4283;		
	}
	
	/**
	 * @return The default storage projection, in a format suitable for use with
	 * GDAL (e.g. as a <a href="http://www.gdal.org/gdalwarp.html">-s_srs or
	 * -t_srs argument</a>).
	 */
	public static String getDefaultMapEpsgCode() {
		return "EPSG:" + getDefaultMapEpsg();
	}
	
	/**
	 * @return The default geographic projection, in a format suitable for use
	 * with GDAL (e.g. as a <a href="http://www.gdal.org/gdalwarp.html">-s_srs or
	 * -t_srs argument</a>).
	 */
	public static String getDefaultGeoEpsgCode() {
		return "EPSG:" + getDefaultGeoEpsg();
	}
	
	/**
	 * returns the id part of an EPSG specification. eg; for the string "EPSG:3112",
	 * the string "3112" will be returned.  Will through runtime expeception if
	 * a bad string is given.
	 * @param s the input epsg string ie; epsg:1234
	 * @return the id component of the epsd ie; 1234
	 */
	public static int getEpsgId(String s) {
		int result = -1;		
		s = s.toLowerCase();
		if (s.startsWith("epsg:") && s.length() > 5) {
			String epsgId = s.substring(5);
			result = Integer.parseInt(epsgId);
		} else {
			throw new RuntimeException("Invalid EPSG string provided (" + s + "), string must begin " +
					"with \"EPSG:\"");
		}
		return result;
	}

	/**
	 * Convert coordinates from geographic to the internal storage projection.
	 * @param in The coordinate as a lat long pair.
	 * @return An equivalent coordinate in storage projection.
	 */
	public static Point<Double> geoToMap(Point<Double> in) {				
		return transform(in, getDefaultGeoEpsg(), getDefaultMapEpsg());
	}
	
	/**
	 * Convert coordinates from the internal storage projection to geographic.
	 * @param in A coordinate in the storage projection.
	 * @return An equivalent coordinates as a lat/long pair.
	 */
	public static Point<Double> mapToGeo(Point<Double> in) {
		return transform(in, getDefaultMapEpsg(), getDefaultGeoEpsg());
	}

	/**
	 * Transform the given point using the specified transformation.
	 * @param trans The specified coordinate transformation.
	 * @param in The specified input point.
	 * @return Returns a transformed point based on the specified transformation.
	 */
	public static Point<Double> transform(CoordinateTransformation trans, Point<Double> in) {
		double[] arr = trans.TransformPoint(in.getX(), in.getY());
		return new Point<Double>(arr[0], arr[1]);
	}

	/**
	 * Transform the given point from the specified source projection into the specified target projection.
	 * @param in The specified input point.
	 * @param sourceSrsEpsgId The specified source projection EPSG Id.
	 * @param targetSrsEpsgId The specified target projection EPSG Id.
	 * @return Returns a transformed point from the specified source projection into the specified target projection.
	 */
	public static Point<Double> transform(Point<Double> in, int sourceSrsEpsgId, int targetSrsEpsgId) {
		if(sourceSrsEpsgId == targetSrsEpsgId) {
			return in;
		}

		SpatialReference sr = new SpatialReference();
		sr.ImportFromEPSG(sourceSrsEpsgId);

		SpatialReference tr = new SpatialReference();
		tr.ImportFromEPSG(targetSrsEpsgId);

		CoordinateTransformation coordTrans =  new CoordinateTransformation(sr, tr);
		double[] arr = coordTrans.TransformPoint(in.getX(), in.getY());
		return new Point<Double>(arr[0], arr[1]);
	}	

	/**
	 * Transform bounding box to another projection.
	 * 
	 * @param in
	 *            The input box.
	 * @param resolution
	 *            The resolution. This is required to account for warped box
	 *            edges.
	 * @param sourceSrsEpsgId
	 *            The source projection EPSG Id.
	 * @param targetSrsEpsgId
	 *            The target projection EPSG Id.
	 * @return The transformed box.
	 */
	public static Box transform(Box in, CellSize resolution,
			int sourceSrsEpsgId, int targetSrsEpsgId) {

		if(sourceSrsEpsgId == targetSrsEpsgId) {
			return in;
		}

		SpatialReference sr = new SpatialReference();
		sr.ImportFromEPSG(sourceSrsEpsgId);

		SpatialReference tr = new SpatialReference();
		tr.ImportFromEPSG(targetSrsEpsgId);

		CoordinateTransformation coordTrans =  new CoordinateTransformation(sr, tr);
		return transform(coordTrans, in, resolution);
	}

	/**
	 * Transform bounding box from one CRS projection to another projection.
	 * @param in
	 *            The input box.
	 * @param resolution
	 *            The resolution. This is required to account for warped box
	 *            edges.
	 * @param sourceSrs
	 *            The source projection in WKT CRS string format.
	 * @param targetSrsEpsgId
	 *            The target projection EPSG Id.
	 * @return The transformed box.
	 */
	public static Box transform(Box in, CellSize resolution,
			String sourceSrs, int targetSrsEpsgId) {
		SpatialReference sr = new SpatialReference();
		sr.ImportFromWkt(sourceSrs);

		SpatialReference tr = new SpatialReference();
		tr.ImportFromEPSG(targetSrsEpsgId);

		CoordinateTransformation coordTrans =  new CoordinateTransformation(sr, tr);
		return transform(coordTrans, in, resolution);
	}

	/**
	 * Transform bounding box to another projection.
	 * 
	 * @param coordTrans
	 *            The transformation to use.
	 * @param in
	 *            The input box.
	 * @param resolution
	 *            The resolution. This is required to account for warped box
	 *            edges.
	 * @return The transformed box.
	 */
	public static Box transform(CoordinateTransformation coordTrans, Box in,
			CellSize resolution) {

		// Build a polygon with the same shape and size as the bounding box.
		// The edges are then segmentised to account for warping - i.e. the
		// resulting edges are likely to be curved.
		// Then, a new box with straight edges is computed from the warped
		// shape.
		// TODO: The resolution should not be required here: it should be
		// possible to find the exact extents of the box using something like
		// Newton-Raphson iteration.
		// See: http://en.wikipedia.org/wiki/Newton's_method
		//
		Geometry ring = new Geometry(ogr.wkbLinearRing);
		ring.AddPoint(in.getXMin(), in.getYMin());
		ring.AddPoint(in.getXMax(), in.getYMin());
		ring.AddPoint(in.getXMax(), in.getYMax());
		ring.AddPoint(in.getXMin(), in.getYMax());
		ring.AddPoint(in.getXMin(), in.getYMin());

		Geometry poly = new Geometry(ogr.wkbPolygon);
		poly.AddGeometryDirectly(ring);

		// Chop into smaller segments.
		poly.Segmentize(resolution.toDouble() * 0.5);
		// Warp.
		poly.Transform(coordTrans);

		// Find bounds of warped shape.
		double[] bboxExt = new double[4];
		poly.GetEnvelope(bboxExt);

		// (xmin, ymin)
		Point<Double> min = new Point<Double>(bboxExt[0], bboxExt[2]);
		// (xmax, ymax)
		Point<Double> max = new Point<Double>(bboxExt[1], bboxExt[3]);

		return new Box(min, max);
	}

	/**
	 * @param epsgId The specified EPSG id.
	 * @return Returns whether the specified EPSG code is geographic coordinate system.
	 */
	public static boolean isEpsgIdGeographic(int epsgId) {
		boolean bResult = false;
		
		// Determine projection. See:
		// http://www.gdal.org/ogr/classOGRSpatialReference.html#8a5b8c9a205eedc6b88a14aa0c219969
		SpatialReference sr = new SpatialReference();
		if (epsgId >= 0) {
			sr.ImportFromEPSG(epsgId);			
			bResult = sr.IsGeographic() > 0;
		} 
		
		return bResult;
	}
	
	/**
	 * @return Returns whether the internal EPSG projection is geographic coordinate system.
	 */
	public static boolean isDefaultMapEpsgGeographic() {
		boolean bResult = isEpsgIdGeographic(getDefaultMapEpsg());
		
		return bResult;
	}
	
	/**
	 * @return Returns whether the internal EPSG projection unit in degree.
	 */
	public static boolean isDefaultMapEpsgUnitInDegree() {
		boolean bResult = isDefaultMapEpsgGeographic();
		
		return bResult;
	}
	
	/**
	 * Is the given cell size's unit matching the internal projection unit.
	 * @param cellSize The given cell size.
	 * @return Returns true if they both matches, otherwise false.
	 */
	public static boolean isUnitMatching(CellSize cellSize) {		
		boolean bCellSizeInMeter = cellSize.isUnitInMeter();		
		boolean bProjectionUnitInMeter = !Projection.isDefaultMapEpsgUnitInDegree();
		
		return bCellSizeInMeter == bProjectionUnitInMeter;
	}
	
	public static void main(String[] args) {
		Point<Double> originPt = new Point<Double>(90.0, 0.0);	
		Point<Double> endPt = new Point<Double>(180.0, -65.0);
		
		int sourceSrsEpsgId = 4283;
		int targetSrsEpsgId = 4283;
		Point<Double> transformedOriginPt = Projection.transform(originPt, sourceSrsEpsgId, targetSrsEpsgId);
		Point<Double> transformedEndPt = Projection.transform(endPt, sourceSrsEpsgId, targetSrsEpsgId);
		log.info("EPSG: " + targetSrsEpsgId);
		log.info("Origin: " + transformedOriginPt);
		log.info("End: " + transformedEndPt);
		
		targetSrsEpsgId = 3112;
		transformedOriginPt = Projection.transform(originPt, sourceSrsEpsgId, targetSrsEpsgId);
		transformedEndPt = Projection.transform(endPt, sourceSrsEpsgId, targetSrsEpsgId);
		log.info("EPSG: " + targetSrsEpsgId);
		log.info("Origin: " + transformedOriginPt);
		log.info("End: " + transformedEndPt);
		
		targetSrsEpsgId = 3577;
		transformedOriginPt = Projection.transform(originPt, sourceSrsEpsgId, targetSrsEpsgId);
		transformedEndPt = Projection.transform(endPt, sourceSrsEpsgId, targetSrsEpsgId);
		log.info("EPSG: " + targetSrsEpsgId);
		log.info("Origin: " + transformedOriginPt);
		log.info("End: " + transformedEndPt);		
	}
}
