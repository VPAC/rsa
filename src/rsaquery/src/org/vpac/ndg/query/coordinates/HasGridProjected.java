package org.vpac.ndg.query.coordinates;


/**
 * Classes that implement this have a spatial coordinate system.
 * @author Alex Fraser
 */
public interface HasGridProjected {
	public GridProjected getGrid();
}
