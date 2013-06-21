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

package org.vpac.ndg.storage.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.common.datamodel.GdalFormat;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Projection;
import org.vpac.ndg.geometry.Tile;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.ndg.storagemanager.IGraphicsFile;

/**
 * A combination of a tile, band and time slice. When combined together, these
 * three objects can be resolved to a file on disk.
 * @author hsumanto
 *
 */
public class TileBand implements IGraphicsFile {

	final private Logger log = LoggerFactory.getLogger(TileBand.class);
	
	private Tile tile;
	private Band band;
	private TimeSlice timeSlice;
	private Path fileLocation = null;
	private Path previousFileLocation = null;
	
	TimeSliceDao timeSliceDao;
	TimeSliceUtil timeSliceUtil;
	TileManager tileManager;

	/** 
	 * Create a tileband from the specified tile, band and timeslice.
	 * @param tile The specified tile.
	 * @param band The specified band.
	 * @param timeSlice The specified timeslice.
	 */
	public TileBand(Tile tile, Band band, TimeSlice timeSlice) {
		this.tile = tile;
		this.band = band;
		this.timeSlice = timeSlice;
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
		tileManager = (TileManager) appContext.getBean("tileManager");
	}

	public Tile getTile() {
		return tile;
	}

	public Band getBand() {
		return band;
	}

	public TimeSlice getTimeSlice() {
		return timeSlice;
	}

	/**
	 * Get the tileband name.
	 * @return Returns the tileband name.
	 * @throws TaskException If invalid tileband name encountered.
	 */
	private String getTileName() throws TaskException {
		return getTileNameWithSuffix(Constant.EMPTY);
	}

	/**
	 * Get the tileband name and allow suffix to be appended at the end.
	 * @param suffix The specified suffix to be appended with tileband name.
	 * @return Returns the tileband name and allow suffix to be appended at the end.
	 * @throws TaskException If invalid tileband name encountered.
	 */
	private String getTileNameWithSuffix(String suffix) throws TaskException {
		if(band == null) {
			throw new TaskException("Invalid tileband encountered: tileband has a null band.");
		}

		if(tile == null) {
			throw new TaskException("Invalid tileband encountered: tileband has a null tile.");
		}

		if(band.getName() == null || band.getName().isEmpty()) {
			throw new TaskException("Invalid tileband encountered: tileband has band without name.");
		}

		return String.format("%s_tile_x%d_y%d%s", band.getName().trim(), tile.getX(), tile.getY(), suffix.trim());
	}

	/**
	 * Get the tileband name together with its extension.
	 * @return Returns the tileband name together with its extension.
	 * @throws TaskException If invalid tileband name encountered.
	 */
	public String getTileNameWithExtention() throws TaskException {
		return getTileName() + GdalFormat.NC.getExtension();
	}

	/**
	 * Get the tileband name together with its extension and 
	 * allow specified suffix to be inserterd before the extension.
	 * @return Returns the tileband name together with its extension.
	 * @throws TaskException If invalid tileband name encountered.
	 */
	public String getTileNameWithSuffixAndExtension(String suffix) throws TaskException {
		return getTileNameWithSuffix(suffix) + GdalFormat.NC.getExtension();		
	}

	public void setFileLocation(Path fileLocation) {
		this.fileLocation = fileLocation;
	}

	@Override
	public Path getFileLocation() {
		if (fileLocation == null) {
			try {
				fileLocation = getDefaultFileLocation();
			} catch (TaskException e) {
				log.error("Could not get default file location: {}", e);
			}
		}
		return fileLocation;
	}

	public void setPreviousFileLocation(Path previousFileLocation) {
		this.previousFileLocation = previousFileLocation;
	}

	public Path getPreviousFileLocation() {
		return previousFileLocation;
	}

	@Override
	public Box getBounds() {
		return tileManager.getNngGrid()
				.getBounds(tile.getIndex(), getResolution());
	}

	@Override
	public String getSrs() {
		return Projection.getDefaultMapEpsgCode();
	}

	@Override
	public GdalFormat getFormat() {
		return GdalFormat.NC;
	}

	@Override
	public CellSize getResolution() {
		return timeSliceDao.getParentDataset(timeSlice.getId()).getResolution();
	}

	@Override
	public boolean isVector() {
		return false;
	}

	@Override
	public boolean isRaster() {
		return true;
	}	

	/**
	 * Get the tileband default location in storagepool.
	 * @return Returns the tileband default location in storagepool.
	 * @throws TaskException 
	 */
	public Path getDefaultFileLocation() throws TaskException {		
		return getDefaultFileLocation(Constant.EMPTY);
	}

	/**
	 * Get the old tileband default location in storagepool (corresponding to previous upload).
	 * @return Returns the old tileband default location in storagepool.
	 * @throws TaskException If invalid tileband name encountered.
	 */
	public Path getDefaultOldFileLocation() throws TaskException {
		return getDefaultFileLocation(Constant.SUFFIX_OLD);
	}

	/**
	 * Get the tileband (containing the specified suffix) default location in storagepool.
	 * @return Returns the tileband (containing the specified suffix) default location in storagepool.
	 * @throws TaskException If invalid tileband name encountered.
	 */
	public Path getDefaultFileLocation(String suffix) throws TaskException {
		Path result = null;
		// Make sure parent is not null
		if(timeSlice != null) {
			Path timeSliceLocation = timeSliceUtil.getFileLocation(timeSlice);
			result = timeSliceLocation.resolve(getTileNameWithSuffixAndExtension(suffix));			
		}

		return result;
	}

	/**
	 * Copy the tileband into its default location in storagepool.
	 * @throws TaskException If error is encountered.
	 */
	public void copyIntoDefaultLocationInStoragePool() throws TaskException {			
		Path from = getFileLocation();
		Path to = getDefaultFileLocation();		
		try {
			FileUtils.copy(from, to);
		} catch (IOException e) {	
			log.error("Could not copy file: {}", e);
			throw new TaskException(String.format(Constant.ERR_COPY_FILE_FAILED, from, to));
		}
		// Record old tile previous temporary location 
		setPreviousFileLocation(from);
		// Set new tile location in storage pool 
		setFileLocation(to);
	}

	/**
	 * Rename an existing tileband by adding .old extension into the tileband name.
	 * @throws TaskException If error is encountered.
	 */
	public void renameExistingTileAsOld() throws TaskException {
		Path from = getDefaultFileLocation();
		if(Files.exists(from)) {
			Path to = getDefaultOldFileLocation();
			try {
				FileUtils.move(from, to);
			} catch (IOException e) {
				log.error("Could not move file: {}", e);
				throw new TaskException(String.format(Constant.ERR_MOVE_FILE_FAILED, from, to));
			}
		}
	}
	
	/**
	 * Copy tileband to previous file location.
	 * @throws TaskException If error is encountered.
	 */
	public void copyBackToPreviousStorage() throws TaskException {		
		Path src = getFileLocation();
		Path dst = getPreviousFileLocation();
		try {
			FileUtils.copy(src, dst);
		} catch (IOException e) {
			log.error("Could not copy file: {}", e);
			throw new TaskException(String.format(Constant.ERR_COPY_FILE_FAILED, src, dst));
		}
		log.trace("TILEBAND: moving tile from = {} to = {}", src, dst);
		// Record old tile previous temporary location 
		setPreviousFileLocation(src);
		// Set new tile location in storage pool 
		setFileLocation(dst);
	}

	/**
	 * Delete tile in the previous file location.
	 */
	public void deleteTileInPreviousStorage() {
		if(FileUtils.deleteIfExists(getPreviousFileLocation())) {
			log.trace("Deleted {}", getPreviousFileLocation());
		}
	}

	/**
	 * Check if current tile exists.
	 * @return Returns true if the tile exists otherwise returns false.
	 */
	public boolean exists() {
		if(getFileLocation() == null) {
			return false;
		}

		return Files.exists(getFileLocation());
	}

	/**
	 * Check if current tile exists in default storagepool.
	 * @return Returns true if the tile exists in default storagepool,
	 * otherwise returns false.
	 */
	public boolean existsInStoragepool() {
		Path defaultFileLocation = null;
		try {
			defaultFileLocation = getDefaultFileLocation();
		} catch (TaskException e) {
			log.error("Could not get default file location: {}", e);
		}

		if(defaultFileLocation == null) {
			return false;
		}

		return Files.exists(defaultFileLocation);
	}

	@Override
	public String toString() {
		return String.format("TileBand(%s, %s, %s)",
				timeSlice, band, tile);
	}
}
