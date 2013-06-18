package org.vpac.ndg.task;

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
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;

/**
 * The task of this class is to create a list of tile bands out of tile extents and available bands.
 * @author hsumanto
 *
 */
public class TileBandCreator extends Task {

	final private Logger log = LoggerFactory.getLogger(TileBandCreator.class);

	private Box source;
	private List<TileBand> target;
	private Band band;
	private TimeSlice timeSlice;
	TimeSliceDao timeSliceDao;
	BandDao bandDao;
	TileManager tileManager;

	public TileBandCreator() {
		super(Constant.TASK_DESCRIPTION_TILEBANDCREATOR);
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
		bandDao = (BandDao) appContext.getBean("bandDao");
		tileManager = (TileManager) appContext.getBean("tileManager");
	}

	public TileBandCreator(String description) {
		super(description);
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
		bandDao = (BandDao) appContext.getBean("bandDao");
		tileManager = (TileManager) appContext.getBean("tileManager");
	}

	@Override
	public void initialise() throws TaskInitialisationException {
		// Perform validation 
		if(source == null) {
			throw new TaskInitialisationException(getDescription(), "Source extents not specified.");
		}
		
		if(target == null) {
			throw new TaskInitialisationException(getDescription(), "Target tileband not specified.");
		}
		
		if(getTimeSlice() == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_TIMESLICE_NOT_SPECIFIED);
		}
		
		Dataset ds = timeSliceDao.getParentDataset(getTimeSlice().getId());
		if(ds == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_TIMESLICE_PARENT_NOT_SPECIFIED);
		}
		
		if(band == null || (bandDao.find(ds.getId(), band.getName()) == null)) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_DATASET_BANDS_NOT_SPECIFIED);
		}
	}	
	
	@Override
	public void execute() throws TaskException {
		// Perform some neccessary validation
		Dataset ds = timeSliceDao.getParentDataset(getTimeSlice().getId());
		if(ds == null) {
			throw new TaskException(getDescription(), Constant.ERR_TIMESLICE_PARENT_NOT_SPECIFIED);
		}
		
		if(band == null || (bandDao.find(ds.getId(), band.getName()) == null)) {
			throw new TaskException(getDescription(), Constant.ERR_DATASET_BANDS_NOT_SPECIFIED);
		}

		// Get all the tiles which covers the specified bounds
		List<Tile> tiles = tileManager.getTiles(source, ds.getResolution());

		// Create tileband for each tile
		for(Tile tile : tiles) {	
			TileBand tileband = new TileBand(tile, band, timeSlice); 
			target.add(tileband);	
			log.trace("{}", tileband);
			//log.debug("tileband resolution: {}", timeSlice.getParent().getResolution());
		}
	}

	@Override
	public void rollback() {
		// Do nothing		
	}

	@Override
	public void finalise() {
		// Do nothing		
	}

	public void setSource(Box source) {
		this.source = source;
	}

	public Box getSource() {
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

	public Band getBand() {
		return band;
	}

	public void setBand(Band band) {
		this.band = band;
	}

}
