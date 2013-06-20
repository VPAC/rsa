package org.vpac.ndg.task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.rasterservices.FileInformation;
import org.vpac.ndg.rasterservices.NcmlCreator;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.ndg.storagemanager.GraphicsFile;

public class TileAggregator extends Task {

	final private Logger log = LoggerFactory.getLogger(TileAggregator.class);

	private List<TileBand> source;
	private ScalarReceiver<GraphicsFile> target;
	private GraphicsFile vrtFile;
	private TimeSlice timeSlice;
	private Band band;
	private List<Path> tmpFileList; // Store the tmp ncml files in tmp directory
	private Path temporaryLocation;
	TimeSliceDao timeSliceDao;
	TimeSliceUtil timeSliceUtil;
	NdgConfigManager ndgConfigManager;
	
	public TileAggregator() {
		this(Constant.TASK_DESCRIPTION_TILEAGGREGATOR);
	}

	public TileAggregator(String description) {
		super(description);
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
		ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
	}

	@Override
	public void initialise() throws TaskInitialisationException {
		if (getSource() == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}

		if (getTarget() == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_TARGET_DATASET_NOT_SPECIFIED);
		}

		// If temporaryLocation is null, create temporary location
		if(temporaryLocation == null) {			
			try {
				temporaryLocation = FileUtils.createTmpLocation();
			} catch (IOException e) {
				log.error("Could not create temporary directory: {}", e);
				throw new TaskInitialisationException(String.format("Error encountered when create temporary directory: %s", temporaryLocation));
			}
			log.info("Temporary Location: {}", temporaryLocation);
		}

		tmpFileList = new ArrayList<Path>();
	}

	public void revalidateBeforeExecution() throws TaskException {
		if(getSource().isEmpty()) {
			throw new TaskException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}

		for (TileBand tileband : source) {
			if (!tileband.getBand().equals(band)) {
				throw new TaskException(String.format(
						"Band \"%s\" not found in dataset.",
						tileband.getBand().getName()));
			}
		}
	}

	@Override
	public void execute() throws TaskException {
		revalidateBeforeExecution();

		List<Path> tileList = new ArrayList<Path>();
		for (TileBand tileband : source) {
			tileList.add(tileband.getFileLocation());
		}

		Path vrtFileLocation = vrtFile.getFileLocation();
		FileInformation vrtInfo = FileInformation.read(vrtFileLocation);

		String ncmlFilename = band.getName() + "_agg" + Constant.EXT_NCML;			
		Path ncmlFileLocation = temporaryLocation.resolve(ncmlFilename);
		log.debug("ncml location: {}", ncmlFileLocation);		

		GraphicsFile ncmlFile = new GraphicsFile(ncmlFileLocation);
		target.set(ncmlFile);

		try {
			log.debug("{}", ncmlFile.getFileLocation());
			NcmlCreator.createNcml(vrtInfo, tileList, ncmlFile.getFileLocation());
		} catch (IOException e) {
			String msg = String.format(Constant.ERR_TASK_EXCEPTION, getDescription(), e.getMessage());
			throw new TaskException(msg);
		}

		// Copy file to storage pool.
		// TODO: This should be done in the Committer!!
		Path from = ncmlFile.getFileLocation();
		Path to = timeSliceUtil.getFileLocation(timeSlice).resolve(from.getFileName());
		try {
			// Store the tmp ncml file for removal later
			tmpFileList.add(from);
			// Copy into ncml file into storagepool
			FileUtils.copy(from, to);
			// Set the new location in storage pool
			ncmlFile.setFileLocation(to);
		} catch (IOException e) {
			throw new TaskException(String.format(Constant.ERR_COPY_FILE_FAILED, from, to));
		}

	}

	@Override
	public void rollback() {
		// Delete ncml file
		if(target != null && target.get().deleteIfExists()) {
			log.debug("CLEANUP: File deletion = {}", target.get().getFileLocation());
		}
	}

	@Override
	public void finalise() {
		for(Path tmpNcml: tmpFileList) {
			// Delete vrt file in temporary storage
			if(FileUtils.deleteIfExists(tmpNcml)) {
				log.debug("CLEANUP: File deletion = {}", tmpNcml);
			}						
		}		
	}

	public void setSource(List<TileBand> source) {
		this.source = source;
	}

	public List<TileBand> getSource() {
		return source;
	}

	public void setTarget(ScalarReceiver<GraphicsFile> target) {
		this.target = target;
	}

	public ScalarReceiver<GraphicsFile> getTarget() {
		return target;
	}

	public void setVrt(GraphicsFile vrtFile2) {
		this.vrtFile = vrtFile2;
	}

	public GraphicsFile getVrtList() {
		return vrtFile;
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

	public List<Path> getTmpFileList() {
		return tmpFileList;
	}	

	/**
	 * Set the temporary location for storing temporary .ncml file.
	 * @param temporaryLocation The specified temporary location.
	 */
	public void setTemporaryLocation(Path temporaryLocation) {
		this.temporaryLocation = temporaryLocation;
	}
}
