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

/**
 * Filters a list of tile bands to find the ones that actually exist.
 * @author hsumanto
 * @author adfries
 */
public class TileBandFilter extends Task {

	Logger log = LoggerFactory.getLogger(TileBandFilter.class);

	private List<TileBand> source;
	private List<TileBand> target;

	public TileBandFilter(String description) {
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
		if (source.isEmpty()) {
			throw new TaskException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}

		log.debug("Filtering {} TileBands.", source.size());
		log.debug("If it exists, the first tile will be {}",
				source.get(0).getDefaultFileLocation());

		for (TileBand tileband: source) {
			Path tileFileLocation = tileband.getDefaultFileLocation();
			if (Files.exists(tileFileLocation))
				target.add(tileband);
		}

		log.debug("Produced {} TileBands.", target.size());
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

	public void setTarget(List<TileBand> target) {
		this.target = target;
	}

	public List<TileBand> getTarget() {
		return target;
	}

}
