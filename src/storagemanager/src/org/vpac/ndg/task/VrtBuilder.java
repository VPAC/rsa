package org.vpac.ndg.task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.CommandUtil;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.rasterservices.ProcessException;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.ndg.storagemanager.GraphicsFile;

public class VrtBuilder extends Task {

	final private Logger log = LoggerFactory.getLogger(VrtBuilder.class);

	private List<TileBand> source;
	private GraphicsFile target;
	private List<GraphicsFile> outputBucket;
	private Path temporaryLocation;
	private TimeSlice timeSlice;
	private Band band;
	private List<Path> tmpFileList; // Store the tmp vrt files in tmp directory
	TimeSliceUtil timeSliceUtil;
	private boolean copyToStoragePool;

	private CommandUtil commandUtil;

	public VrtBuilder() {
		this(Constant.TASK_DESCRIPTION_VRTBUILDER);
	}

	public VrtBuilder(String description) {
		super(description);
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
		commandUtil = new CommandUtil();
		copyToStoragePool = false;
	}

	@Override
	public void initialise() throws TaskInitialisationException {
		if(getSource() == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}
		
		if(getTarget() == null) {
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
		for (TileBand tileband : source) {
			if (!tileband.getBand().equals(band)) {
				throw new TaskException(String.format(
						"Band \"%s\" not found in dataset.",
						tileband.getBand().getName()));
			}
		}
	}

	public List<String> prepareCommand() {
		List<String> command = new ArrayList<String>();

		// get the input file list
		command.add("gdalbuildvrt");

		command.add("-resolution");
		command.add("highest");

		command.add(target.getFileLocation().toString());

		for(TileBand tileband: source) {
			command.add(tileband.getFileLocation().toString());
		}
	
		return command;
	}

	@Override
	public void execute() throws TaskException {
		if (source.isEmpty()) {
			// Can't work with zero input files. Just return; the output list
			// will not be populated. This is not an error.
			log.debug("Source is empty; will not create a VRT.");
			return;
		}

		revalidateBeforeExecution();

		// Prepare gdalbuildvrt for the specified band and then execute it
		List<String> command = prepareCommand();			
		try {
			commandUtil.start(command);
		} catch (ProcessException e) {
			throw new TaskException(getDescription(), e);
		} catch (InterruptedException e) {
			throw new TaskException(getDescription(), e);
		} catch (IOException e) {
			throw new TaskException(getDescription(), e);
		}

		if (outputBucket != null)
			outputBucket.add(target);

		// TODO: Copying should always be done in the Committer, not here.
		if (!isCopyToStoragePool())
			return;

		try {
			// Initialize timeSlice directory in storagepool if applicable
			timeSliceUtil.initializeLocations(timeSlice);
		} catch (IOException e) {
			log.error("Could not create timeslice directory: {}", e);
			throw new TaskException(String.format("Error encountered when creating timeslice directory: %s", timeSliceUtil.getFileLocation(timeSlice)));
		}
		Path from = target.getFileLocation();
		Path to = timeSliceUtil.getFileLocation(timeSlice).resolve(from.getFileName());
		try {
			// Store the tmp vrt file for removal later
			tmpFileList.add(from);
			// Copy into vrt file into storage pool
			FileUtils.copy(from, to);
			// Set the new location in storage pool
			target.setFileLocation(to);
		} catch (IOException e) {
			throw new TaskException(String.format(Constant.ERR_COPY_FILE_FAILED, from, to), e);
		}
	}

	@Override
	public void rollback() {
		if(target == null) {
			return;
		}

		// Delete vrt file
		if (target.deleteIfExists()) {
			log.trace("Deleted {}", target);
		}
	}

	@Override
	public void finalise() {
		for(Path tmpVrt: tmpFileList) {
			// Delete vrt file in temporary storage
			if(FileUtils.deleteIfExists(tmpVrt)) {
				log.trace("Deleted {}", tmpVrt);
			}
		}
	}

	public void setSource(List<TileBand> source) {
		this.source = source;
	}

	public List<TileBand> getSource() {
		return source;
	}

	public void setTarget(GraphicsFile vrtFile) {
		this.target = vrtFile;
	}

	public GraphicsFile getTarget() {
		return target;
	}

	public void setTimeSlice(TimeSlice timeSlice) {
		this.timeSlice = timeSlice;
	}

	public TimeSlice getTimeSlice() {
		return timeSlice;
	}

	/**
	 * Set the temporary location for storing temporary .ncml file.
	 * @param temporaryLocation The specified temporary location.
	 */
	public void setTemporaryLocation(Path temporaryLocation) {
		this.temporaryLocation = temporaryLocation;
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

	public boolean isCopyToStoragePool() {
		return copyToStoragePool;
	}

	public void setCopyToStoragePool(boolean copyToStoragePool) {
		this.copyToStoragePool = copyToStoragePool;
	}

	public List<GraphicsFile> getOutputBucket() {
		return outputBucket;
	}

	public void setOutputBucket(List<GraphicsFile> outputBucket) {
		this.outputBucket = outputBucket;
	}
}
