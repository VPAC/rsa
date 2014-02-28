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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.Utils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.common.datamodel.Format;
import org.vpac.ndg.common.datamodel.GdalFormat;
import org.vpac.ndg.common.datamodel.TaskType;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.datamodel.AggregationDefinition;
import org.vpac.ndg.datamodel.AggregationDefinition.VarDef;
import org.vpac.ndg.datamodel.AggregationType;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.NestedGrid;
import org.vpac.ndg.geometry.Projection;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.lock.TimeSliceDbReadWriteLock;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.ndg.storagemanager.GraphicsFile;

import ucar.nc2.time.CalendarDate;
import ucar.nc2.time.CalendarDateUnit;

/**
 * This tool executes a series of tasks to export data from the RSA.
 * 
 * @author hsumanto
 * @author adfries
 *
 */
public class Exporter extends Application {

	final private Logger log = LoggerFactory.getLogger(Exporter.class);

	private String datasetId;
	private List<String> bandIds;
	private List<String> bandNamesFilter;
	private Date start;
	private Date end;
	private Box extents;
	private Box internalExtents;
	private String targetProjection;
	private int targetEpsgId;
	private CellSize targetResolution;
	private TimeSliceDbReadWriteLock lock;
	private Boolean useBilinearInterpolation;
	private GdalFormat format;
	
	private Dataset dataset;
	// It's OK to hold a direct reference to the time slices here, because this
	// application doesn't modify them.
	private List<TimeSlice> timeSlices;

	DatasetDao datasetDao;
	TimeSliceDao timeSliceDao;
	TimeSliceUtil timeSliceUtil;
	TileManager tileManager;
	NdgConfigManager ndgConfigManager;

	public Exporter() {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		datasetDao = (DatasetDao) appContext.getBean("datasetDao");
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
		tileManager = (TileManager) appContext.getBean("tileManager");
		ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");
		targetResolution = null;
		format = GdalFormat.NC;
	}

	@Override
	protected void initialise() throws TaskInitialisationException {
		super.initialise();

		log.info("Initialising export of dataset {}", datasetId);

		if (datasetId == null) {
			throw new TaskInitialisationException("No dataset specified.");
		}

		dataset = datasetDao.retrieve(datasetId);
		if(dataset == null) {
			// Capture if dataset not exist
			throw new TaskInitialisationException(String.format("Dataset with ID = \"%s\" not found.", datasetId));			
		}

		log.info("Dataset: {}", dataset);

		// Filter time slice list.
		timeSlices = datasetDao.findTimeSlices(datasetId, start, end);
		Collections.sort(timeSlices);
		if (timeSlices.size() == 0)
			throw new TaskInitialisationException("No time slices to export.");

		// Find target projection ID
		if (getTargetProjection() == null) {
			targetEpsgId = Projection.getDefaultMapEpsg();
		} else {
			try {
				targetEpsgId = Integer.parseInt(getTargetProjection().split(":")[1]);
			} catch (RuntimeException e) {
				throw new TaskInitialisationException("Could not parse projection.", e);
			}
		}

		// Collect extents.
		CellSize resolution = dataset.getResolution();
		NestedGrid nng = tileManager.getNngGrid();
		if (extents != null) {
			// To get the tile list, the extents must be transformed to internal
			// coordinates.
			internalExtents = Projection.transform(extents, resolution,
					targetEpsgId, Projection.getDefaultMapEpsg());
			internalExtents = nng.alignToGrid(internalExtents, resolution);

		} else {
			// Otherwise, find the union of all time slices.
			internalExtents = timeSliceUtil.aggregateBounds(timeSlices);
			if (internalExtents == null) {
				throw new TaskInitialisationException(
						"None of the selected time slices contain any data, " +
						"therefore the bounds could not be inferred.");
			}
			internalExtents = nng.alignToGrid(internalExtents, resolution);

			// Transform to external extents.
			extents = Projection.transform(internalExtents, resolution,
					Projection.getDefaultMapEpsg(), targetEpsgId);
		}

		// Get locks for all timeslices.
		// FIXME: This should happen before finding timeslice bounds.
		lock = new TimeSliceDbReadWriteLock(TaskType.Export.toString());
		for (TimeSlice ts : timeSlices)
			lock.getTimeSliceIds().add(ts.getId());
	}

	/**
	 * Filter bands based on the specified band names filter.
	 * @param bands The bands to be filtered.
	 * @param bandnamesFilter The band names used to filter the bands. 
	 * @return Returns only bands whose name specified in filter.
	 */
	private List<Band> filterBand(List<Band> bands, List<String> bandnamesFilter) {
		// If no filter then returns all the bands
		if(bandNamesFilter == null || bandnamesFilter.size() == 0) {
			return bands;
		}

		// Returns only band whose name is specified in filter
		List<Band> selectedBands = new ArrayList<>();
		for(Band band: bands) {
			if(bandnamesFilter.contains(band.getName())) {
				selectedBands.add(band);
			}
		}
		
		return selectedBands;
	}
	
	/**
	 * Initialise the import process.
	 * @throws TaskException
	 */
	protected void createTasks() throws TaskInitialisationException {

		List<Band> selectedBands;
		if (bandIds == null) {
			selectedBands = datasetDao.getBands(datasetId);
		} else {
			selectedBands = datasetDao.findBands(datasetId, bandIds);
		}

		// Select only band whose name is specified in filter
		selectedBands = filterBand(selectedBands, bandNamesFilter);

		if (selectedBands.size() == 0) {
			throw new TaskInitialisationException("No bands to export.");
		}

		List<String> bandNames = new ArrayList<>();
		for (Band b : selectedBands) {
			bandNames.add(b.getName());
		}

		// STEP 2: Aggregate by time (see cubeBuilder below).
		// Construct a series of reprojection tasks that will result in one
		// image per band per time slice.
		List<ScalarReceiver<AggregationDefinition>> multiBandTimeSlices = new ArrayList<>();
		List<List<GraphicsFile>> allFiles = new ArrayList<>();
		for (TimeSlice ts : timeSlices) {

			// STEP 1: Aggregate by band (see multibandBuilder below).
			// For each band in this time slice, transform the tiles into the
			// new projection and combine into one file.
			List<GraphicsFile> transformedBands = new ArrayList<>();
			for (Band b : selectedBands) {
				GraphicsFile targetImage = createPartialPipeline(ts, b);
				transformedBands.add(targetImage);
			}
			allFiles.add(transformedBands);

			// Once all the bands of this time slice have been transformed,
			// collate them into a multi-band dataset.
			ScalarReceiver<AggregationDefinition> multibandAggregation = new ScalarReceiver<>();
			NcmlBuilder multibandBuilder = new NcmlBuilder();
			multibandBuilder.setRawSource(transformedBands);
			multibandBuilder.setType(AggregationType.UNION);
			multibandBuilder.setBandNames(bandNames);
			multibandBuilder.setTarget(multibandAggregation);
			getTaskPipeline().addTask(multibandBuilder);

			// Add the band aggregation to a list so it can be aggregated in
			// time later.
			multiBandTimeSlices.add(multibandAggregation);
		}

		String name = getOutputName() + Format.NCML.getExtension();
		Path ncmlpath = getWorkingDirectory().resolve(name);

		// Once all the tiles and bands have been combined as mutli-band time
		// slices, collate all times into one big data cube.

		List<CalendarDate> dates = new ArrayList<>();
		CalendarDateUnit timeUnits = timeSliceUtil.computeTimeMapping(
				timeSlices, dates);
		List<String> coordValues = timeSliceUtil.datesToCoordValues(
				timeUnits, dates);

		ScalarReceiver<AggregationDefinition> timeAggregation = new ScalarReceiver<>();
		NcmlBuilder cubeBuilder = new NcmlBuilder();
		cubeBuilder.setType(AggregationType.JOIN_NEW);
		cubeBuilder.setBandNames(bandNames);
		cubeBuilder.setNewDimension(
				VarDef.newDimension("time", "int", timeUnits.toString()));
		cubeBuilder.setCoordinateValues(coordValues);
		cubeBuilder.setNestedSource(multiBandTimeSlices);
		cubeBuilder.setTarget(timeAggregation);
		cubeBuilder.setTargetPath(ncmlpath);
		getTaskPipeline().addTask(cubeBuilder);

		// Combine the output into one file, and move it to the pickup directory
		// for download.

		// TODO: Now that the NCML file has been generated, it could be
		// converted into a monolithic .nc file. In that case, the resulting .nc
		// file could be placed in the pickup directory for download *instead
		// of* the .zip.
		// TODO: If only one band of one timeslice is being exported, there
		// would only be one file anyway - so we could skip the creation of the
		// NCML.

		// TODO: If the task pipeline fails, the output directory should be
		// deleted. Perhaps the creation of this directory should be moved into
		// its own task? Or, we could add a rollback method to Application. 

		Path outputdir = getOutputDirectory(getTaskId());
		try {
			outputdir = FileUtils.getTargetLocation(getTaskId());
			Files.createDirectory(outputdir);
		} catch (IOException e) {
			log.error("Could not create directory: {}", e);
			throw new TaskInitialisationException(
					"Could not create output directory.");
		}

		Compressor compressor = new Compressor();
		for (List<GraphicsFile> gs : allFiles) {
			compressor.addSource(gs);
		}
		compressor.addSource(ncmlpath);
		compressor.setTarget(getOutputPath());
		compressor.setZipDirName(getOutputName());
		getTaskPipeline().addTask(compressor);
	}

	/**
	 * Perform the export only if can get all read locks on all specified timeslices.
	 * If unable to get all read locks on all specified timeslice then throws exception
	 * as other task is currently modifying the specified timeslices. 
	 */
	@Override
	protected void preExecute() throws TaskException {
		if (!ndgConfigManager.getConfig().isFilelockingOn()) {
			log.debug("Executing pipeline without locking.");
			return;
		}

		if (!lock.readLock().tryLock()) {
			// Unable to get write lock for the timeslice as 
			// other task is currently modifying the same timeslice
			throw new TaskException("Unable to perform task as the timeslice is currently locked by other task.");
		}
	}

	@Override
	protected void postExecute() {
		log.debug("Finished executing task pipeline.");
		if (!ndgConfigManager.getConfig().isFilelockingOn()) {
			return;
		}

		lock.readLock().unlock();
	}

	/**
	 * @param taskId The ID of the export task.
	 * @return The path of the output directory for that task.
	 */
	public static Path getOutputDirectory(String taskId) {
		return FileUtils.getTargetLocation(taskId);
	}

	/**
	 * @param taskId
	 *            The ID of the export task.
	 * @return The path to the output file for that task.
	 * @throws TaskException
	 *             If the file could not be found. This may happen if the task
	 *             does not run to completion, or if this method is called for
	 *             an invalid export task ID.
	 */
	public static Path findOutputPath(String taskId) throws TaskException {
		Path outputDir = getOutputDirectory(taskId);
		try (DirectoryStream<Path> dlist = Files.newDirectoryStream(outputDir)) {
			for (Path f : dlist) {
				if (Files.exists(f))
					return f;
			}
		} catch (IOException e) {
			throw new TaskException("Could not list output directory.", e);
		}
		throw new TaskException(String.format(
				"No file has been produced for task \"%s\".", taskId));
	}

	/**
	 * Like {@link #findOutputPath(String)}, but works even when the file
	 * doesn't exist yet. This method should be used internally;
	 * {@link #findOutputPath(String)} should be used by code that calls
	 * Exporter.
	 *
	 * @return The path to the output file for this application.
	 */
	protected Path getOutputPath() {
		return getOutputDirectory(getTaskId()).resolve(getOutputName() + ".zip");
	}

	protected String getOutputName() {
		String res = dataset.getResolution().toString();
		if (getTargetResolution() != null) {
			res = getTargetResolution().toString();
		}
		return String.format("%s_%s", dataset.getName().replace(' ', '_'), res);
	}

	/**
	 * Create a pipeline to reproject one band of one timeslice. This adds the
	 * necessary tasks to the shared task pipeline.
	 *
	 * @param ts
	 *            The timeslice to operate on.
	 * @param band
	 *            The band to operate on.
	 * @return A handle to the output file (which will not exist until the tasks
	 *         run).
	 * @throws TaskInitialisationException 
	 */
	protected GraphicsFile createPartialPipeline(TimeSlice ts, Band band) throws
			TaskInitialisationException {

		// Create an empty container to capture all tilebands
		List<TileBand> unfilteredtilebands = new ArrayList<TileBand>();

		//
		// TASK 1: Create tile band for all bands
		//
		TileBandCreator tilebandCreator = new TileBandCreator();
		tilebandCreator.setSource(internalExtents);
		tilebandCreator.setTimeSlice(ts);
		tilebandCreator.setBand(band);
		tilebandCreator.setTarget(unfilteredtilebands);

		//
		// TASK 2: Remove tile bands that don't exist.
		//
		List<TileBand> tilebands = new ArrayList<TileBand>();

		TileBandFilter tileBandFilter = new TileBandFilter("Filtering tiles");
		tileBandFilter.setSource(unfilteredtilebands);
		tileBandFilter.setTarget(tilebands);

		DateFormat formatter = Utils.getTimestampFormatter();
		String targetName = String.format("%s_%s_%s", getOutputName(),
				formatter.format(ts.getCreated()), band.getName());


		//
		// TASK 3: Create a VRT mosaic of the input tiles. This just creates a
		// temporary .vrt files. This is necesary because GDAL can't warp
		// directly to large NetCDF files; see
		// http://trac.osgeo.org/gdal/ticket/4484#comment:21
		//
		Path vrtMosaicLoc = getWorkingDirectory().resolve(
				targetName + "_agg" + Constant.EXT_VRT);
		GraphicsFile vrtMosaicFile = new GraphicsFile(vrtMosaicLoc);

		List<GraphicsFile> warpInputs = new ArrayList<>();

		VrtBuilder vrtBuilder = new VrtBuilder();
		vrtBuilder.setSource(tilebands);
		vrtBuilder.setTarget(vrtMosaicFile);
		vrtBuilder.setOutputBucket(warpInputs);
		vrtBuilder.setTimeSlice(ts);
		vrtBuilder.setBand(band);
		// Don't copy to storage pool, because this is a read op!
		vrtBuilder.setCopyToStoragePool(false);
		vrtBuilder.setTemporaryLocation(getWorkingDirectory());


		//
		// TASK 4: "Transform" mosaic VRT into target SRS. This creates a new VRT,
		// and adds metadata to it, but doesn't actually to the warp. This is
		// necesary because GDAL can't warp directly to large NetCDF files; see
		// http://trac.osgeo.org/gdal/ticket/4484#comment:21
		//
		Path vrtWarpLoc = getWorkingDirectory().resolve(
				targetName + "_warp" + Constant.EXT_VRT);
		GraphicsFile vrtWarpFile = new GraphicsFile(vrtWarpLoc);

		// This must be in EXTERNAL projection
		if (getTargetProjection() != null) {
			vrtWarpFile.setEpsgId(targetEpsgId);
		}
		vrtWarpFile.setResolution(dataset.getResolution());
		vrtWarpFile.setFormat(GdalFormat.VRT);
		
		if (getTargetProjection() != null) {
			vrtWarpFile.setResolution(targetResolution);
		}

		Transformer initialTransform = new Transformer();
		initialTransform.setSource(warpInputs);
		initialTransform.setTarget(vrtWarpFile);
		if (extents != null) {
			// This must be in EXTERNAL coordinates
			initialTransform.setExtents(extents);
		}
		if (useBilinearInterpolation != null)
			initialTransform.setUseBilinearInterpolation(useBilinearInterpolation);
		else
			initialTransform.setUseBilinearInterpolation(band.isContinuous());
		// Set the dstnodata for the export
		initialTransform.setDstnodata(band.getNodata());
		initialTransform.setCleanupSource(false);
		initialTransform.setCleanupTarget(false);
		// Allow transaction to proceed with no input image
		initialTransform.setCheckSource(false);


		//
		// TASK 5: Translate temporary VRT to specified format. This is where the
		// warp happens - despite not using the gdalwarp command!
		//
		Path path = getWorkingDirectory().resolve(
				targetName + format.getExtension());
		GraphicsFile exportedImage = new GraphicsFile(path);

		// This must be in EXTERNAL projection
		if (getTargetProjection() != null) {
			exportedImage.setEpsgId(targetEpsgId);
		}
		
		//if the target resolution is set, use it. Otherwise set to the resolution of the dataset
		//note that the resolution of the output data has already been determined by the prior
		//Transformer task
		if (targetResolution != null) {
			exportedImage.setResolution(targetResolution);
		} else {
			exportedImage.setResolution(dataset.getResolution());
		}
		
		exportedImage.setFormat(format);

		Translator ncTranslator = new Translator();
		ncTranslator.setSource(vrtWarpFile);
		ncTranslator.setTarget(exportedImage);
		// Set the nodata for the export
		ncTranslator.setNodata(band.getNodata());
		ncTranslator.setCleanupSource(false);
		ncTranslator.setCleanupTarget(false);
		// Allow transaction to proceed with no input image
		ncTranslator.setCheckSource(false);


		// ADD TASK
		getTaskPipeline().addTask(tilebandCreator);
		getTaskPipeline().addTask(tileBandFilter);
		getTaskPipeline().addTask(vrtBuilder);
		getTaskPipeline().addTask(initialTransform);
		getTaskPipeline().addTask(ncTranslator);

		return exportedImage;
	}

	public String getDatasetId() {
		return datasetId;
	}

	public void setDatasetId(String datasetId) {
		this.datasetId = datasetId;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Box getExtents() {
		return extents;
	}

	public void setExtents(Box extents) {
		this.extents = extents;
	}

	public String getTargetProjection() {
		return targetProjection;
	}

	public void setTargetProjection(String targetProjection) {
		this.targetProjection = targetProjection;
	}

	public CellSize getTargetResolution() {
		return targetResolution;
	}

	public void setTargetResolution(CellSize targetResolution) {
		this.targetResolution = targetResolution;
	}
	
	public List<String> getBandIds() {
		return bandIds;
	}

	public void setBandIds(List<String> bandIds) {
		this.bandIds = bandIds;
	}

	@Override
	protected String getJobName() {
		return Constant.TOOL_EXPORT;
	}

	@Override
	protected TaskType getTaskType() {
		return TaskType.Export;
	}

	@Override
	protected String getJobDescription() {
		if (dataset != null)
			return String.format("Exporting %s", getOutputName());
		else
			return String.format("Exporting %s", datasetId);
	}

	public List<String> getBandNamesFilter() {
		return bandNamesFilter;
	}

	public void setBandNamesFilter(List<String> bandNamesFilter) {
		this.bandNamesFilter = bandNamesFilter;
	}

	public Boolean getUseBilinearInterpolation() {
		return useBilinearInterpolation;
	}

	public void setUseBilinearInterpolation(Boolean useBilinearInterpolation) {
		this.useBilinearInterpolation = useBilinearInterpolation;
	}

	public GdalFormat getFormat() {
		return format;
	}

	public void setFormat(GdalFormat format) {
		this.format = format;
	}

	
}
