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

package org.vpac.ndg.task;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Tile;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.rasterdetails.RasterDetails;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.ndg.storagemanager.GraphicsFile;

public class TileUpdater extends Task {

	final private Logger log = LoggerFactory.getLogger(TileUpdater.class);

	private List<TileBand> source;
	private List<TileBand> target;
	private TimeSlice timeSlice;	
	private TaskPipeline innerTaskPipeline = new TaskPipeline(false);
	private Path tempDir;
	private String srcnodata;
	private String dstnodata;
	private RasterDetails datatype;
	TimeSliceDao timeSliceDao;
	TimeSliceUtil timeSliceUtil;
	TileManager tileManager;
	
	public TileUpdater() {
		this(Constant.TASK_DESCRIPTION_TILEUPDATER);
	}

	public TileUpdater(String description) {
		super(description);
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
		tileManager = (TileManager) appContext.getBean("tileManager");
	}

	@Override
	public void initialise() throws TaskInitialisationException {
		// Nothing to do.
	}

	@Override
	public void execute() throws TaskException {
		for(TileBand tileband: source) {			
			Tile t = tileband.getTile();
			Dataset dataset = timeSliceDao.getParentDataset(timeSlice.getId());
			Box tileBounds = tileManager.getNngGrid().getBounds(t.getIndex(), dataset.getResolution());

			boolean isExistingTile;
			if (timeSliceUtil.isEmpty(timeSlice)) {
				isExistingTile = false;
			} else {
				Box existingBounds = timeSlice.getBounds();
				isExistingTile = existingBounds.intersects(tileBounds);
			}

			if (tileband.existsInStoragepool() && isExistingTile) {
				log.trace("Found tile {}", t);
				// Find location of existing tile
				Path defaultFileLocationForPreviousUpload = tileband.getDefaultFileLocation();
				// Get new and existing tile images 
				GraphicsFile newImage = new GraphicsFile(tileband.getFileLocation());
				GraphicsFile oldImage = new GraphicsFile(defaultFileLocationForPreviousUpload);
				log.trace("Old tile: {}", defaultFileLocationForPreviousUpload);
				// Construct source from new and existing tile images 
				List<GraphicsFile> sourceTiles = new ArrayList<GraphicsFile>();
				sourceTiles.add(oldImage);
				sourceTiles.add(newImage);
				// Construct target for the composite result of new and existing tile
				GraphicsFile targetTile = new GraphicsFile();
				String transformTileFilename = tileband.getTileNameWithSuffixAndExtension(Constant.SUFFIX_COMPOSITE);
				Path transformTileFileLocation = tempDir.resolve(transformTileFilename);
				targetTile.setFileLocation(transformTileFileLocation); // targetTile.setFileLocation("/var/tmp/.../Band1_tile_x1_y1.nc.composite");
				tileband.setFileLocation(targetTile.getFileLocation());
				
				Transformer mosaickingTask = new Transformer("Composite new tile on top of exisiting tile " + t.getIndex());
				// Set up source image
				mosaickingTask.setSource(sourceTiles);
				// Set up target image
				mosaickingTask.setTarget(targetTile);
				mosaickingTask.setSrcnodata(srcnodata);
				mosaickingTask.setDstnodata(dstnodata);
				mosaickingTask.setDatatype(datatype);
				mosaickingTask.setCleanupSource(false);
				mosaickingTask.setCleanupTarget(false);
				innerTaskPipeline.addTask(mosaickingTask);
			}
			else {
				innerTaskPipeline.addTask(new NoOperation("Dummy task"));
				log.trace("{} not found", t);
			}
			target.add(tileband);
		}

		try {
			// Run each specific initialisation on each task in inner pipeline
			innerTaskPipeline.initialise();
		} catch (TaskInitialisationException e) {
			throw new TaskException(e);
		}

		// Run each specific task in inner pipeline
		innerTaskPipeline.run();	
		return;
	}

	@Override
	public void rollback() {
		// Call each child task roll back 
		innerTaskPipeline.rollback();
	}

	@Override
	public void finalise() {	
		// Call each child task finalise
		innerTaskPipeline.finalise();
	}

	public void setSource(List<TileBand> source) {
		this.source = source;
	}

	public List<TileBand> getSource() {
		return source;
	}

	public void setTarget(List<TileBand> target) {
		this.target = target;
	}

	public List<TileBand> getTarget() {
		return target;
	}

	public void setTimeSlice(TimeSlice timeSlice) {
		this.timeSlice = timeSlice;
	}

	public TimeSlice getTimeSlice() {
		return timeSlice;
	}

	public void setInnerTaskPipeline(TaskPipeline innerTaskPipeline) {
		this.innerTaskPipeline = innerTaskPipeline;
	}

	public TaskPipeline getInnerTaskPipeline() {
		return innerTaskPipeline;
	}

	public void setTemporaryLocation(Path temporaryLocation) {
		this.tempDir = temporaryLocation;
	}

	public Path getTemporaryLocation() {
		return tempDir;
	}

	public String getSrcnodata() {
		return srcnodata;
	}

	public void setSrcnodata(String srcnodata) {
		this.srcnodata = srcnodata;
	}

	public String getDstnodata() {
		return dstnodata;
	}

	public void setDstnodata(String dstnodata) {
		this.dstnodata = dstnodata;
	}

	public RasterDetails getDatatype() {
		return datatype;
	}

	public void setDatatype(RasterDetails datatype) {
		this.datatype = datatype;
	}

}
