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

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Projection;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.lock.TimeSliceDbReadWriteLock;
import org.vpac.ndg.lock.HasRunningState;
import org.vpac.ndg.rasterdetails.RasterDetails;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.dao.UploadDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.Upload;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.ndg.storage.util.UploadUtil;
import org.vpac.ndg.storagemanager.GraphicsFile;

/**
 * This is the tool to be used to import user specified dataset into ULA.
 * The class will create the import process and execute the import process as 
 * a transaction using command pattern design.
 * 
 * 
 * @author hsumanto
 * @author adfries
 *
 */
public class Importer extends Application {
	
	final private Logger log = LoggerFactory.getLogger(Importer.class);

	private String uploadId;
	private String bandId;
	private TimeSlice timeSlice;
	private Dataset dataset;
	private Band band;
	private Path primaryFile;
	private Path uploadDir;
	private String srcnodata;
	private TimeSliceDbReadWriteLock lock;
	private List<TileBand> mergedTilebands;
	private ScalarReceiver<HasRunningState> lockBox;
	private Boolean useBilinearInterpolation;
	
	UploadDao uploadDao;
	TimeSliceDao timeSliceDao;
	TimeSliceUtil timeSliceUtil;
	BandDao bandDao;
	UploadUtil uploadUtil;
	TileManager tileManager;
	NdgConfigManager ndgConfigManager;

	/**
	 * Default constructor for ImportTool.
	 */
	public Importer() {
		srcnodata = null;
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		uploadDao = (UploadDao) appContext.getBean("uploadDao");
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
		bandDao = (BandDao) appContext.getBean("bandDao");
		uploadUtil = (UploadUtil) appContext.getBean("uploadUtil");
		tileManager = (TileManager) appContext.getBean("tileManager");
		ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");
	}

	/**
	 * <p>Finds the file that is most likely to be the "primary" file of a dataset;
	 * i.e. the file that should be passed to GDAL for processing. All the other
	 * files are assumed to be referred to by the primary file. For example,
	 * if the upload directory contains:</p>
	 * <ul>
	 * <li>foo1.nc</li>
	 * <li>foo2.nc</li>
	 * <li>foo3.nc</li>
	 * <li>foo.ncml</li>
	 * </ul>
	 * <p>Then this function would return "foo.ncml".</p>
	 * @param dir The directory to search in.
	 * @return The primary file.
	 * @throws IOException 
	 */
	private Path findPrimaryFile(Path dir) throws IOException {

		Map<String, Integer> scoreMap = new HashMap<String, Integer>();
		// Aggregate files trump
		scoreMap.put(".vrt", 100);
		scoreMap.put(".ncml", 100);
		// Regular primary files
		scoreMap.put(".tif", 10);  // GeoTIFF
		scoreMap.put(".tiff", 10); // GeoTIFF
		scoreMap.put(".nc", 10);   // NetCDF
		scoreMap.put(".shp", 10);  // Shapefile
		scoreMap.put(".asc", 10);  // Arcinfo ASCII Grid

		Path bestMatch = null;
		int bestScore = 0;

		try (DirectoryStream<Path> dlist = Files.newDirectoryStream(dir)) {
			for (Path file : dlist) {
				if (Files.isDirectory(file))
					continue;

				if (bestMatch == null)
					bestMatch = file;

				int extLoc = file.toString().lastIndexOf(".");
				String extension = file.toString().substring(extLoc).toLowerCase();
				Integer score = scoreMap.get(extension);
				if (score != null && score > bestScore) {
					bestMatch = file;
					bestScore = score;
				}
			}
		}

		return bestMatch;
	}

	@Override
	protected void initialise() throws TaskInitialisationException {
		super.initialise();

		log.info("Initialising import {}", uploadId);

		Upload upload = uploadDao.retrieve(uploadId);

		timeSlice = timeSliceDao.retrieve(upload.getTimeSliceId());
		dataset = timeSliceDao.getParentDataset(upload.getTimeSliceId());

		log.info("Dataset: {}", dataset);
		log.info("Timeslice: {}", timeSlice);

		if (bandId == null) {
			throw new TaskInitialisationException("No band specified.");
		}
		band = bandDao.retrieve(bandId);
		if (band == null) {
			throw new TaskInitialisationException(String.format("Band with ID = \"%s\" not found.", bandId));
		}

		// Set up input image
		try {
			uploadDir = uploadUtil.getDirectory(upload);
			primaryFile = findPrimaryFile(uploadDir);
			if(primaryFile == null) {
				throw new TaskInitialisationException("Could not find primary file inside upload directory: " + uploadDir);
			}
		} catch (IOException e) {
			throw new TaskInitialisationException("Could not find primary file.", e);
		}

		// Get lock for timeslice
		lock = new TimeSliceDbReadWriteLock(TaskType.Import.toString());
		lock.getTimeSliceIds().add(timeSlice.getId());
	}

	/**
	 * Initialise the import process.
	 * @throws TaskInitialisationException 
	 */
	@Override
	protected void createTasks() throws TaskInitialisationException {
		GraphicsFile sourceImage = new GraphicsFile(primaryFile);
		List<GraphicsFile> sourceList = new ArrayList<GraphicsFile>();
		sourceList.add(sourceImage);


		String dstnodata = determineNoDataValue(sourceImage);
		RasterDetails datatype = determineDatatype(sourceImage);

		// Find the bounds of the input.
		// TODO: How to check if the bounds are invalid?
		Box bounds = sourceImage.getBounds();
		int targetProjection = Projection.getDefaultMapEpsg();
		int sourceProjection = sourceImage.getEpsgId();
		Box transformedBounds = null;
		if (sourceProjection == -1) {
			// If can't find EPSG ID then try to work out input projection from source gdal CRS string
			String sourceCRS = sourceImage.getFileInformation().getSrcCRS();
			if(!sourceCRS.isEmpty()) {
				transformedBounds = Projection.transform(bounds, dataset.getResolution(), sourceCRS, targetProjection);
			}
		} else {
			transformedBounds = Projection.transform(bounds, dataset.getResolution(), sourceProjection, targetProjection);
		}

		if(transformedBounds == null) {
			// If still can't work out input projection from CRS string
			throw new TaskInitialisationException("Could not determine projection of input.");
		}

		// Align to NNG Grid
		transformedBounds = tileManager.getNngGrid().alignToGrid(transformedBounds, dataset.getResolution());

		// Create an empty container to capture all tilebands
		List<TileBand> tilebands = new ArrayList<TileBand>(); 

		// TASK 1: Create tile band for all bands on image extents
		TileBandCreator tilebandCreator = new TileBandCreator();
		tilebandCreator.setSource(transformedBounds);
		tilebandCreator.setTarget(tilebands);
		tilebandCreator.setTimeSlice(timeSlice);
		tilebandCreator.setBand(band);

		// TASK 2: Use the transformer task to re-project and cut several tiles
		TileTransformer tileTransformer = new TileTransformer();
		tileTransformer.setSource(sourceList);
		tileTransformer.setTarget(tilebands);
		tileTransformer.setEpsgId(targetProjection);
		tileTransformer.setTemporaryLocation(getWorkingDirectory());
		if (getUseBilinearInterpolation() != null)
			tileTransformer.setUseBilinearInterpolation(getUseBilinearInterpolation());
		else
			tileTransformer.setUseBilinearInterpolation(band.isContinuous());
		tileTransformer.setSrcnodata(srcnodata);
		tileTransformer.setDstnodata(dstnodata);
		tileTransformer.setDatatype(datatype);

		// TASK 3: Create a vrt for the tiles
		Path vrtFileLoc = getWorkingDirectory().resolve(band.getName() +
				"_agg" + Constant.EXT_VRT);

		GraphicsFile vrtFile = new GraphicsFile(vrtFileLoc);

		VrtBuilder vrtBuilder = new VrtBuilder();
		vrtBuilder.setSource(tilebands);
		vrtBuilder.setTarget(vrtFile);
		vrtBuilder.setTimeSlice(timeSlice);
		vrtBuilder.setBand(band);
		vrtBuilder.setCopyToStoragePool(true);
		vrtBuilder.setTemporaryLocation(getWorkingDirectory());

		// Create an empty container to capture all ncml files
		ScalarReceiver<GraphicsFile> ncmlFile = new ScalarReceiver<>();

		// TASK 4: Create an ncml aggregation spatially for the tiles
		TileAggregator tileAggregator = new TileAggregator();
		tileAggregator.setSource(tilebands);
		tileAggregator.setTarget(ncmlFile);
		tileAggregator.setTimeSlice(timeSlice);
		tileAggregator.setBand(band);
		tileAggregator.setVrt(vrtFile);
		tileAggregator.setTemporaryLocation(getWorkingDirectory());

		// Create an empty container to capture all merged tilebands
		mergedTilebands = new ArrayList<TileBand>(); 		

		// TASK 5: Insert tiles into the TimeSlice.
		// If TimeSlice already existed, the updated tiles will be merged with
		// the originals. Note that the TileUpdater does not write to the
		// database yet - that is done in Committer. So any changes to the
		// TimeSlice will need to be done in detached mode (Hibernate feature).
		TileUpdater tileUpdater = new TileUpdater();
		tileUpdater.setSource(tilebands);
		tileUpdater.setTarget(mergedTilebands);					
		tileUpdater.setTimeSlice(timeSlice);
		tileUpdater.setTemporaryLocation(getWorkingDirectory());
		// The new tile and old tile should have dstnodata as nodata value
		tileUpdater.setSrcnodata(dstnodata);
		// The composite tile should have dstnodata as its nodata value
		tileUpdater.setDstnodata(dstnodata);
		tileUpdater.setDatatype(datatype);

		// TASK 6: Commit the new tiles into storagepool
		Committer committer = new Committer();
		committer.setSource(mergedTilebands);
		committer.setBounds(transformedBounds);
		committer.setBand(band);
		committer.setDstnodata(dstnodata);
		committer.setDatatype(datatype);
		committer.setDataset(dataset);
		committer.setTarget(timeSlice);
		lockBox = new ScalarReceiver<>();
		committer.setTaskMonitor(lockBox);

		// ADD TASK 
		getTaskPipeline().addTask(tilebandCreator);
		getTaskPipeline().addTask(tileTransformer);
		if( ndgConfigManager.getConfig().isGenerateImportTileAggregation()) {
			getTaskPipeline().addTask(vrtBuilder);
			getTaskPipeline().addTask(tileAggregator);
		}
		getTaskPipeline().addTask(tileUpdater);
		getTaskPipeline().addTask(committer);
	}

	/**
	 * Perform the import only if can get write lock for the timeslice.
	 * If unable to get write lock for the timeslice then throws exception
	 * as other task is currently modifying the same timeslice. 
	 */
	@Override
	protected void preExecute() throws TaskException {
		if (!ndgConfigManager.getConfig().isFilelockingOn()) {
			log.debug("Executing pipeline without locking.");
			return;
		}

		if (!lock.writeLock().tryLock()) {
			// Unable to get write lock for the timeslice as 
			// other task is currently modifying the same timeslice
			throw new TaskException("Unable to perform task as the timeslice is currently locked by other task.");
		}

		lockBox.set(lock);
	}

	@Override
	protected void postExecute() {
		log.debug("Finished executing task pipeline.");
		if (!ndgConfigManager.getConfig().isFilelockingOn()) {
			return;
		}

		lock.writeLock().unlock();
	}

	@Override
	protected void finalise() {
		super.finalise();

		if (TaskState.FINISHED == getTaskPipeline().getProgress().getState()) {
			log.debug("Cleaning up previous backup tiles and upload directory after import");

			// Clean up previous tile backup after import is finished
			cleanupOldBackupTiles();

			// Clean up upload directory after import is finished
			cleanupUploadDirectory();
		}
	}

	/**
	 * Clean up previous tile backup when import is finished.
	 * Note: This task is to be performed only when all tasks in task
	 * pipeline have been executed (when task pipeline state is 
	 * TaskState.FINISHED).
	 */
	private void cleanupOldBackupTiles() {
		for (TileBand tb: mergedTilebands) {
			Path backupTile = null;
			try {
				backupTile = tb.getDefaultOldFileLocation();
				if(Files.deleteIfExists(backupTile)) {
					log.debug("Old backup tile is deleted {}", backupTile);
				}
			} catch (TaskException e) {
				log.error("Failed deleting {}\nCause: {}", backupTile, e);
			} catch (IOException e) {
				log.error("Failed deleting {}\nCause: {}", backupTile, e);
			}
		}
	}

	/**
	 * Clean up upload directory (the directory where the imported file
	 * is uploaded) when import is finished.
	 */
	private void cleanupUploadDirectory() {
		try {
			FileUtils.removeDirectory(uploadDir);
			log.debug("Upload directory is deleted {}", uploadDir);
		} catch (IOException e) {
			log.error("Failed to delete upload directory: {}", e);
		}
	}

	/**
	 * Figure out what nodata value to use for the output files.
	 * @param sourceImage The input image.
	 * @return The nodata value.
	 */
	private String determineNoDataValue(GraphicsFile sourceImage)
			throws TaskInitialisationException {
		// This is much more complicated than determineDatatype(), because the
		// value can come from several different sources:
		//  1. The band metadata, as stored in the database. This has the
		//     highest priority, because the nodata value must be consistent
		//     across the whole band.
		//  2. User-specified source nodata (-srcnodata on command line).
		//  3. Intrinsic nodata value of source file.

		boolean srcNodataExist = srcnodata != null && !srcnodata.isEmpty();
		boolean bandNodataExist = band.getNodata() != null && !band.getNodata().isEmpty();
		boolean inputNodataExist = sourceImage.getNoData() != null && !sourceImage.getNoData().isEmpty();

		// Determine srcnodata and dstnodata for transformation.
		String dstnodata;
		if (!srcNodataExist && !bandNodataExist) {
			if (!inputNodataExist) {
				// if [-srcnodata] not specified & band without nodata value
				// and primaryFile also without nodata value, can't allow this import
				throw new TaskInitialisationException("NoData value could not be found from input file and band.\nPlease specify -srcnodata for import.");
			}
			// if [-srcnodata] not specified & band without nodata value
			// and primaryFile has nodata value, use this as srcnodata and dstnodata
			srcnodata = sourceImage.getNoData();
			dstnodata = sourceImage.getNoData();
		} else if (!srcNodataExist && bandNodataExist) {
			if (inputNodataExist) {
				// if [-srcnodata] not specified 
				// but primaryFile has nodata value, use it as srcnodata
				srcnodata = sourceImage.getNoData();
			}
			// if [-srcnodata] not specified but band has nodata value
			// use it as dstnodata
			dstnodata = band.getNodata();
		} else if (srcNodataExist && bandNodataExist) {
			// if [-srcnodata] specified and band has nodata value
			dstnodata = band.getNodata();
		} else if (srcNodataExist && !bandNodataExist) {
			// if [-srcnodata] specified and band without nodata value
			dstnodata = srcnodata;
		} else {
			dstnodata = null;
		}
		return dstnodata;
	}

	/**
	 * Figure out what data type will be written to the output files.
	 * @param sourceImage The image being imported.
	 * @return The data type.
	 */
	private RasterDetails determineDatatype(GraphicsFile sourceImage) {
		if (band.getType() != null) {
			// The type of a band should not change after it has been
			// initialised, so ignore the source dataset.
			return band.getType();
		} else {
			return sourceImage.getFileInformation().getDataType();
		}
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public void setBand(String bandId) {
		this.bandId = bandId;
	}

	@Override
	protected String getJobName() {
		return Constant.TOOL_IMPORT;
	}

	@Override
	protected TaskType getTaskType() {
		return TaskType.Import;
	}

	@Override
	protected String getJobDescription() {
		if (dataset != null) {
				return String.format("Importing %s into %s as %s",
						uploadId, dataset, band);
		} else {
			return String.format("Importing %s", uploadId);
		}
	}

	public String getSrcnodata() {
		return srcnodata;
	}

	public void setSrcnodata(String srcnodata) {
		this.srcnodata = srcnodata;
	}

	public Boolean getUseBilinearInterpolation() {
		return useBilinearInterpolation;
	}

	public void setUseBilinearInterpolation(Boolean useBilinearInterpolation) {
		this.useBilinearInterpolation = useBilinearInterpolation;
	}

}
