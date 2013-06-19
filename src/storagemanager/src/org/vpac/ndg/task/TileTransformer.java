package org.vpac.ndg.task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.rasterdetails.RasterDetails;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storagemanager.GraphicsFile;

/**
 * The task of this class is to perform transformation on source image to produce each individual target tiles.
 * @author hsumanto
 *
 */
public class TileTransformer extends Task {

	final private Logger log = LoggerFactory.getLogger(TileTransformer.class);

	private List<GraphicsFile> source;
	private List<TileBand> target;
	private TaskPipeline innerTaskPipeline = new TaskPipeline(false);
	private int epsgId = -1;
	private Path temporaryLocation;
	private String srcnodata;
	private String dstnodata;
	private RasterDetails datatype;
	private boolean useBilinearInterpolation;
	TimeSliceDao timeSliceDao;

	public TileTransformer() {
		super(Constant.TASK_DESCRIPTION_TILETRANSFORMER);
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
	}

	public TileTransformer(String description) {
		super(description);
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
	}

	@Override
	public void initialise() throws TaskInitialisationException {	
		// Perform validation on source and target fields;
		if(source == null || getSource().isEmpty()) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}

		if(target == null) {
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
	}

	@Override
	public void execute() throws TaskException {
		for(TileBand tileBand: target) {		
			Transformer makeTile = new Transformer("Make Tile " + tileBand.getTileNameWithExtention());

			GraphicsFile transformTile = new GraphicsFile();
			String transformTileFilename = tileBand.getTileNameWithExtention();
			Path transformTileFileLocation = temporaryLocation.resolve(transformTileFilename);
			transformTile.setFileLocation(transformTileFileLocation);
			transformTile.setBounds(tileBand.getBounds());
			if(epsgId != -1) {
				transformTile.setEpsgId(epsgId);
			}
			transformTile.setResolution(timeSliceDao.getParentDataset(tileBand.getTimeSlice().getId()).getResolution());			

			// Set up source image
			makeTile.setSource(source);
			// Set up target image
			makeTile.setTarget(transformTile);
			makeTile.setCleanupSource(isCleanupSource());
			makeTile.setCleanupTarget(isCleanupTarget());
			makeTile.setUseBilinearInterpolation(useBilinearInterpolation);
			makeTile.setSrcnodata(srcnodata);
			makeTile.setDstnodata(dstnodata);
			makeTile.setDatatype(datatype);

			tileBand.setFileLocation(transformTile.getFileLocation());
			innerTaskPipeline.addTask(makeTile);
		}

		try {
			// Run each specific initialisation on each task in inner pipeline
			innerTaskPipeline.initialise();
		} catch (TaskInitialisationException e) {
			throw new TaskException(e);
		}

		// Run each specific task in inner pipeline
		innerTaskPipeline.run();
	}

	@Override
	public void rollback() {
		// Call each transformer roll back 
		innerTaskPipeline.rollback();
	}

	@Override
	public void finalise() {
		// Call each transformer finalise
		innerTaskPipeline.finalise();		
	}

	public void setSource(List<GraphicsFile> source) {
		this.source = source;
	}

	public List<GraphicsFile> getSource() {
		return source;
	}

	public void setTarget(List<TileBand> target) {
		this.target = target;
	}

	public List<TileBand> getTarget() {
		return target;
	}

	public void setEpsgId(int epsgId) {
		this.epsgId = epsgId;
	}

	public int getEpsgId() {
		return epsgId;
	}

	public void setTemporaryLocation(Path temporaryLocation) {
		this.temporaryLocation = temporaryLocation;
	}

	public Path getTemporaryLocation() {
		return temporaryLocation;
	}

	public boolean isUseBilinearInterpolation() {
		return useBilinearInterpolation;
	}

	public void setUseBilinearInterpolation(boolean useBilinearInterpolation) {
		this.useBilinearInterpolation = useBilinearInterpolation;
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
