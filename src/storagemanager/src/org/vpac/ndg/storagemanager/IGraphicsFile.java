package org.vpac.ndg.storagemanager;

import java.nio.file.Path;

import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.common.datamodel.GdalFormat;

/**
 * Interface required for getting details of graphics file.
 * @author hsumanto
 *
 */
public interface IGraphicsFile {
	/**
	 * @return Returns the location of the graphics file.
	 */
	public Path getFileLocation();
	/**
	 * @return Returns the bounding box of the graphics file.
	 */
	public Box getBounds();
	/**
	 * @return Returns the project of the graphics file.
	 */
	public String getSrs();
	/**
	 * @return Returns the format of the graphics file.
	 */
	public GdalFormat getFormat();
	/**
	 * @return Returns the resolution of the graphics file.
	 */
	public CellSize getResolution();
	/**
	 * @return Returns whether the graphics file is vector dataset.
	 */
	public boolean isVector();
	/**
	 * @return Returns whether the graphics file is raster dataset.
	 */
	public boolean isRaster();
}
