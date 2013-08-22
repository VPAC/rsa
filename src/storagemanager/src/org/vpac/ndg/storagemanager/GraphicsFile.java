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

package org.vpac.ndg.storagemanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.common.datamodel.GdalFormat;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.rasterservices.FileInformation;

public class GraphicsFile implements IGraphicsFile {
	private String id;	
	private Path fileLocation = null;
	private String srs = null;
	private int epsgId = -1;
	private CellSize resolution;
	private Box bounds = null;
	private GdalFormat format = GdalFormat.NC;
	FileInformation fileInformation = null;

	// Empty constructor for Hibernate.
	public GraphicsFile() {
	}

	public GraphicsFile(Path location) {
		setFileLocation(location);
	}

	@Override
	public Box getBounds() {
		if(bounds == null) {
			if (getFileInformation() != null)
				bounds = getFileInformation().getBbox();
		}

		return bounds;
	}

	public void setBounds(Box bounds) {
		this.bounds = bounds;
	}

	@Override
	public String getSrs() {
		if (epsgId == -1) {
			if (getFileInformation() == null)
				return null;
			epsgId = getFileInformation().getEpsgId();
		}
		if (epsgId != -1) {
			// If EPSG ID is applicable
			srs = "EPSG:" + epsgId;
		} else {
			// Otherwise use gdal WKT CRS string. Note:
			// getFileInformation().getEpsgId() may have returned -1.
			srs = getFileInformation().getSrcCRS();
		}

		return srs;
	}

	@Override
	public GdalFormat getFormat() {
		return format;
	}

	@Override
	public CellSize getResolution() {
		return resolution;
	}

	@Override
	public boolean isVector() {	
		if (getFileInformation() == null)
			return false;

		return getFileInformation().getType() == FileInformation.Type.VECTOR;		
	}

	@Override
	public boolean isRaster() {	
		if (getFileInformation() == null)
			return false;

		return getFileInformation().getType() == FileInformation.Type.RASTER;		
	}

	/**
	 * Get nodata value from the graphics file.
	 * If it doesn't have nodata value then use RasterDetails default value.
	 * @return Returns no data value for the graphics file.
	 */
	public String getNoData() {
		if (getFileInformation() == null)
			return null;

		return getFileInformation().getNoData();
	}

	public void setResolution(CellSize resolution) {
		this.resolution = resolution;
	}	

	public FileInformation getFileInformation() {
		if (fileInformation == null) {
			if (Files.exists(getFileLocation())) {
				fileInformation = FileInformation.read(getFileLocation());				
			}
		}
		return fileInformation;
	}

	public void setFileLocation(Path fileLocation) {
		this.fileLocation = fileLocation;
	}

	@Override
	public Path getFileLocation() {
		return fileLocation;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setEpsgId(int epsgId) {
		this.epsgId = epsgId;
	}

	public int getEpsgId() {
		if (epsgId == -1) {
			if (getFileInformation() != null)
				epsgId = getFileInformation().getEpsgId();
		}
		return epsgId;
	}

	public boolean hasInfo() {
		return fileInformation != null;
	}

	public void setFormat(GdalFormat format) {
		this.format = format;
	}

	/**
	 * Check if the GraphicsFile exists in the file location.
	 * @return Returns true if the GraphicsFile exists in the file location.
	 */
	public boolean exists() {		
		return Files.exists(getFileLocation());
	}

	/**
	 * Delete the GraphicsFile.
	 * @return Returns true if the GraphicsFile is successfully deleted, otherwise false.
	 */
	public boolean deleteIfExists() {
		try {
			return Files.deleteIfExists(getFileLocation());
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public String toString() {
		return String.format("GraphicsFile(%s) with resolution = %s", getFileLocation(), getResolution());
	}
}
