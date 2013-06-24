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

package org.vpac.ndg.task;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storagemanager.GraphicsFile;

/**
 * The task of this class is to create a list of tile bands from the given tiles.
 * @author hsumanto
 *
 */
public class GraphicsFileCreator extends Task {

	Logger log = LoggerFactory.getLogger(GraphicsFileCreator.class);

	private List<TileBand> source;
	private List<GraphicsFile> target;

	public GraphicsFileCreator(String description) {
		super(description);
	}

	@Override
	public void initialise() throws TaskInitialisationException {
		if(source == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}

		if(target == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_TARGET_DATASET_NOT_SPECIFIED);
		}
	}	
	
	@Override
	public void execute() throws TaskException {
		if(source.isEmpty()) {
			throw new TaskException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}

		log.debug("Converting {} TileBands.", source.size());
		log.debug("If it exists, the first tile will be {}",
				source.get(0).getDefaultFileLocation());

		for(TileBand tileband: source) {
			Path tileFileLocation = tileband.getDefaultFileLocation();

			// Make sure tile exists
			if(!Files.exists(tileFileLocation)) {
//				throw new TaskException(getDescription(), String.format(Constant.ERR_TILE_NOT_EXIST, tileFileLocation.toAbsolutePath()));
				// When there is no tileband exist, just ignore it
				continue;
			}
			GraphicsFile tileImage = new GraphicsFile(tileFileLocation);
			target.add(tileImage);
		}

		log.debug("Produced {} GraphicsFiles.", target.size());
	}

	@Override
	public void rollback() {
		// Do nothing		
	}

	@Override
	public void finalise() {
		// Do nothing		
	}

	public void setSource(List<TileBand> source) {
		this.source = source;
	}

	public List<TileBand> getSource() {
		return source;
	}

	public void setTarget(List<GraphicsFile> target) {
		this.target = target;
	}

	public List<GraphicsFile> getTarget() {
		return target;
	}

}
