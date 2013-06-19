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
