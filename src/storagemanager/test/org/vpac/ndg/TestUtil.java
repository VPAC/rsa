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

package org.vpac.ndg;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.common.datamodel.GdalFormat;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Tile;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.dao.UploadDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.Upload;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.ndg.storage.util.UploadUtil;
import org.vpac.ndg.task.Importer;

public class TestUtil {

	final private Logger log = LoggerFactory.getLogger(TestUtil.class);

	@Autowired
	DatasetDao datasetDao;
	@Autowired
	TimeSliceDao timeSliceDao;
	@Autowired
	UploadDao uploadDao;
	@Autowired
	BandDao bandDao;
	@Autowired
	UploadUtil uploadUtil;
	@Autowired
	TimeSliceUtil timeSliceUtil;
	@Autowired
	TileManager tileManager;

	/**
	 * Run import test. Check if import is OK. Note: The specified dataset will
	 * be copied into temporary location and then import will be performed on
	 * this temporary copy.
	 * 
	 * @param datasetName
	 *            The specified dataset name.
	 * @param datasetAcquisitionTime
	 *            The specified dataset acquisition time.
	 * @param bandName
	 *            The specified band name.
	 * @param datasetPath
	 *            The specified path to the dataset.
	 * @param continuous
	 *            Whether the data is in the continuous domain (e.g. float).
	 *            This will affect how the data is interpolated.
	 * @param targetResolution
	 *            The target resolution intended for the import.
	 * @param precision
	 *            The dataset precision.
	 */
	public void runImport(String datasetName, String datasetAcquisitionTime,
			String bandName, Path datasetPath, boolean continuous,
			CellSize targetResolution, long precision) throws Exception {

		final String BAND_NAME = bandName;

		log.debug("Performing Import Test");
		// Src dataset
		if (!Files.exists(datasetPath))
			throw new IOException("The file doesn't exist in (" + datasetPath
					+ ")");

		if(datasetName.isEmpty()) {
			throw new IllegalArgumentException("The dataset has no name");
		}

		Dataset dataset = datasetDao.findDatasetByName(datasetName,
				targetResolution);
		if (dataset == null) {
			dataset = new Dataset(datasetName, targetResolution, precision);
			datasetDao.create(dataset);
		}

		// Find or create a band.
		Band band = bandDao.find(dataset.getId(), BAND_NAME);
		if (band == null) {
			band = new Band(BAND_NAME, continuous, false);
			datasetDao.addBand(dataset.getId(), band);
		}

		Date date = Utils.parseDate(datasetAcquisitionTime);
		TimeSlice ts = datasetDao.findTimeSlice(dataset.getId(), date);

		if (ts == null) {
			ts = new TimeSlice(date);
			datasetDao.addTimeSlice(dataset.getId(), ts);
		}

		Upload upload = new Upload(ts.getId());
		uploadDao.create(upload);

		// Upload src dataset into upload location
		uploadFile(upload, datasetPath);

		// Import uploaded dataset
		importFile(upload, band);
	}

	/**
	 * Similar to runImport but with benchmarking.
	 * @param srcnodata 
	 */
	public void runImportWithBenchmark(String datasetName,
			String datasetAcquisitionTime, String bandName, Path datasetPath,
			boolean continuous, CellSize targetResolution, long precision,
			long[] benchmark, String srcnodata) throws Exception {

		Date st = Utils.now();
		final String BAND_NAME = bandName;

		log.debug("Performing Import Test");
		// Src dataset
		if (!Files.exists(datasetPath))
			throw new IOException("The file doesn't exist in (" + datasetPath + ")");

		Dataset dataset = datasetDao.findDatasetByName(datasetName,
				targetResolution);
		if (dataset == null) {
			dataset = new Dataset(datasetName, targetResolution, precision);
			datasetDao.create(dataset);
		}

		// Find or create a band.
		Band band = bandDao.find(dataset.getId(), BAND_NAME);
		if (band == null) {
			band = new Band(BAND_NAME, continuous, false);
			datasetDao.addBand(dataset.getId(), band);
		}

		Date date = Utils.parseDate(datasetAcquisitionTime);
		TimeSlice ts = datasetDao.findTimeSlice(dataset.getId(), date);

		if (ts == null) {
			ts = new TimeSlice(date);
			datasetDao.addTimeSlice(dataset.getId(), ts);
		}

		Date etAdministrative = Utils.now();
		benchmark[0] += etAdministrative.getTime() - st.getTime();
		
		Upload upload = new Upload(ts.getId());
		uploadDao.create(upload);

		// Upload src dataset into upload location
		uploadFile(upload, datasetPath);
		
		Date etUpload = Utils.now();
		benchmark[1] += etUpload.getTime() - etAdministrative.getTime();

		// Import uploaded dataset
		importFile(upload, band, srcnodata);

		Date etImport = Utils.now();
		benchmark[2] += etImport.getTime() - etUpload.getTime();
	}

	/**
	 * For the specified upload, create upload directory when applicable and
	 * then copy the source file into upload location.
	 * 
	 * @param upload
	 *            The specified upload.
	 * @param srcFile
	 *            The specified source file.
	 * @throws IOException
	 */
	public void uploadFile(Upload upload, Path srcFile) throws IOException {
		Path uploadDir = uploadUtil.getDirectory(upload);
		if (!Files.exists(uploadDir))
			Files.createDirectory(uploadDir);
		Path uploadedFile = uploadDir.resolve(srcFile.getFileName());
		// Copy src file into upload location
		Files.copy(srcFile, uploadedFile, StandardCopyOption.REPLACE_EXISTING);
	}

	public void importFile(Upload upload, Band band)
			throws TaskInitialisationException, TaskException {

		importFile(upload, band, null);
	}
	
	/**
	 * Import uploaded dataset.
	 *
	 * @param upload The specified upload.
	 * @param band The specified dataset band.
	 * @param srcnodata Nodata value to use, or null to use the value specified
	 *        in the source file metadata.
	 * @throws TaskInitialisationException
	 * @throws TaskException
	 */
	public void importFile(Upload upload, Band band, String srcnodata)
			throws TaskInitialisationException, TaskException {
		// Import uploaded dataset
		Importer importer = new Importer();
		importer.setUploadId(upload.getFileId());
		importer.setBand(band.getId());
		importer.setSrcnodata(srcnodata);
		importer.configure();
		importer.call();
	}

	/**
	 * Perform bacth import on the specified dataset directly from source data
	 * directory.
	 * 
	 * @param datasetName
	 *            The dataset name.
	 * @param targetResolution
	 *            The target resolution for import.
	 * @param continuous
	 *            The dataset domain type.
	 * @param srcDirectory
	 *            The dataset source directory.
	 * @throws Exception
	 */
	public void batchImport(final String datasetName,
			final CellSize targetResolution, final long precision,
			final boolean continuous, Path srcDirectory, final String srcnodata) throws Exception {

		if (!Files.isDirectory(srcDirectory))
			throw new IOException("The directory wasn't found in ("
					+ srcDirectory + ")");
		log.debug("BATCH IMPORT FROM: {}", srcDirectory);

		final int counter = 1;
		final long[] benchmark = { 0,0,0 };
		Files.walkFileTree(srcDirectory, new SimpleFileVisitor<Path>() {
			/**
			 * Import netcdf dataset
			 */
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {

				String fileName = file.getFileName().toString();

				if (!(
						fileName.endsWith(GdalFormat.NC.getExtension()) ||
						fileName.endsWith(GdalFormat.GEOTIFF.getExtension())	
					)) {
					// IGNORE NON-NETCDF or NON-TIF dataset
					return FileVisitResult.CONTINUE;
				}

				log.debug("IMPORT{}: {}", counter, file.toString());
				Path datasetPath = file.toAbsolutePath();

				// Parse dataset acquisition time from filename or metadata.xml
				String datasetAcquisitionTime = FileUtils
						.getAcquisitionTimeStr(datasetPath);
				// If applicable, parse band name from filename
				String bandName = FileUtils.getBandNameStr(datasetPath);
				log.debug(String.format(
						"%s, Acquisition time = %s, Band name = %s",
						datasetPath.toString(), datasetAcquisitionTime,
						bandName));
				try {
					runImportWithBenchmark(datasetName, datasetAcquisitionTime, bandName,
							datasetPath, continuous, targetResolution,
							precision, benchmark, srcnodata);
				} catch (IOException e) {
					throw e;
				} catch (Exception e) {
					throw new IOException(e);
				}

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException e)
					throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});

		// The , symbol is used to group numbers
		NumberFormat formatter = new DecimalFormat("###,###,###,###,###,###");
		String s0 = formatter.format(benchmark[0]);
		String s1 = formatter.format(benchmark[1]);
		String s2 = formatter.format(benchmark[2]);
		String sTotal = formatter.format(benchmark[0] + benchmark[1] + benchmark[2]);
		
		int padded = 20;
		s0 = String.format("%1$" + padded + "s", s0);
		s1 = String.format("%1$" + padded + "s", s1);
		s2 = String.format("%1$" + padded + "s", s2);
		sTotal = String.format("%1$" + padded + "s", sTotal);
		
		log.debug("\nBENCHMARKING RESULT (in ms):");
		log.debug(String.format("%s (Adminitrative)\n%s (Upload)\n%s (Import)\n====================================\n%s (Total)", 
				s0, s1, s2, sTotal));
		
		formatter = new DecimalFormat("0.00");
		s0 = formatter.format(benchmark[0]/60000.0);
		s1 = formatter.format(benchmark[1]/60000.0);
		s2 = formatter.format(benchmark[2]/60000.0);
		sTotal = formatter.format((benchmark[0] + benchmark[1] + benchmark[2])/60000);
		
		padded = 20;
		s0 = String.format("%1$" + padded + "s", s0);
		s1 = String.format("%1$" + padded + "s", s1);
		s2 = String.format("%1$" + padded + "s", s2);
		sTotal = String.format("%1$" + padded + "s", sTotal);

		log.debug("\nBENCHMARKING RESULT (in minute):");
		log.debug(String.format("%s (Adminitrative)\n%s (Upload)\n%s (Import)\n====================================\n%s (Total)", 
				s0, s1, s2, sTotal));
	}

	public void batchImportSpecial(final String datasetName,
			final CellSize targetResolution, final long precision,
			final boolean continuous, Path srcDirectory,
			final String srcFileExt, final String srcFilePrefix,
			final String srcnodata, final String bandName) throws Exception {

		if (!Files.isDirectory(srcDirectory))
			throw new IOException("The directory wasn't found in ("
					+ srcDirectory + ")");
		log.debug("BATCH IMPORT FROM: {}", srcDirectory);

		final int counter = 1;
		final long[] benchmark = { 0,0,0 };
		Files.walkFileTree(srcDirectory, new SimpleFileVisitor<Path>() {
			/**
			 * Import netcdf dataset
			 */
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {

				String fileName = file.getFileName().toString();
				String srcFileSuffix = srcFileExt;
				if(bandName != null) {
					srcFileSuffix = bandName + srcFileExt;
				}

				if (!(fileName.endsWith(srcFileSuffix) && fileName.startsWith(srcFilePrefix))) {
					// IGNORE
					return FileVisitResult.CONTINUE;
				}

				log.debug("IMPORT{}: {}", counter, file.toString());
				Path datasetPath = file.toAbsolutePath();

				// Parse dataset acquisition time from filename or metadata.xml
				String datasetAcquisitionTime = FileUtils
						.getAcquisitionTimeStr(datasetPath);
				// If applicable, parse band name from filename
				String bandName = FileUtils.getBandNameStr(datasetPath);
				log.debug(String.format(
						"%s, Acquisition time = %s, Band name = %s",
						datasetPath.toString(), datasetAcquisitionTime,
						bandName));
				try {
					runImportWithBenchmark(datasetName, datasetAcquisitionTime, bandName,
							datasetPath, continuous, targetResolution,
							precision, benchmark, srcnodata);
				} catch (Exception e) {
					log.error("{}", e);
				}

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException e)
					throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});

		// The , symbol is used to group numbers
		NumberFormat formatter = new DecimalFormat("###,###,###,###,###,###");
		String s0 = formatter.format(benchmark[0]);
		String s1 = formatter.format(benchmark[1]);
		String s2 = formatter.format(benchmark[2]);
		String sTotal = formatter.format(benchmark[0] + benchmark[1] + benchmark[2]);
		
		int padded = 20;
		s0 = String.format("%1$" + padded + "s", s0);
		s1 = String.format("%1$" + padded + "s", s1);
		s2 = String.format("%1$" + padded + "s", s2);
		sTotal = String.format("%1$" + padded + "s", sTotal);
		
		log.debug("\nBENCHMARKING RESULT (in ms):");
		log.debug(String.format("%s (Adminitrative)\n%s (Upload)\n%s (Import)\n====================================\n%s (Total)", 
				s0, s1, s2, sTotal));
		
		formatter = new DecimalFormat("0.00");
		s0 = formatter.format(benchmark[0]/60000.0);
		s1 = formatter.format(benchmark[1]/60000.0);
		s2 = formatter.format(benchmark[2]/60000.0);
		sTotal = formatter.format((benchmark[0] + benchmark[1] + benchmark[2])/60000);
		
		padded = 20;
		s0 = String.format("%1$" + padded + "s", s0);
		s1 = String.format("%1$" + padded + "s", s1);
		s2 = String.format("%1$" + padded + "s", s2);
		sTotal = String.format("%1$" + padded + "s", sTotal);

		log.debug("\nBENCHMARKING RESULT (in minute):");
		log.debug(String.format("%s (Adminitrative)\n%s (Upload)\n%s (Import)\n====================================\n%s (Total)", 
				s0, s1, s2, sTotal));
	}

	private static final String EX_DATASET_NAME = "small_landsat";
	private static final String EX_PATH = "../../data/small_landsat";
	private static final CellSize EX_RES = CellSize.m25;
	private static final boolean EX_CONTINUOUS = true;
	private static final String NODATA = "-999";

	public Dataset initialiseDataForExport(String dsName,
			CellSize dsResolution, String dsPath) throws Exception {
		// Call BatchImportTest to populate the dataset.
		long precision = Utils.parseTemporalPrecision("1 day");

		Dataset ds = datasetDao.findDatasetByName(dsName, dsResolution);
		if (ds != null) {
			// No need to import again if it already exists.
			return ds;
		}

		Path datasetSrcDir = Paths.get(dsPath);
		Path temporaryDirectory = FileUtils.createTmpLocation();
		try {
			temporaryDirectory = FileUtils.createTmpLocation();
		} catch (IOException e) {
			log.error("Could not create temporary directory: {}", e);
			throw new TaskInitialisationException(String.format("Error encountered when create temporary directory: %s", temporaryDirectory));
		}
		log.info("Temporary Location: {}", temporaryDirectory);
		batchImport(dsName, dsResolution, precision, EX_CONTINUOUS,
				datasetSrcDir, NODATA);

		// Upload contents are no longer needed.
		try {
			FileUtils.removeDirectoryContents(temporaryDirectory);
		} catch (Exception e) {
			log.debug("WARNING: unable to delete {}", temporaryDirectory);
		}
		return datasetDao.findDatasetByName(dsName, dsResolution);
	}

	public Dataset initialiseDataForExport() throws Exception {
		return initialiseDataForExport(EX_DATASET_NAME, EX_RES, EX_PATH);
	}
	
	public String getExportDatasetName() {
		return EX_DATASET_NAME;
	}

	public CellSize getExportDatasetRes() {
		return EX_RES;
	}

	public String getExportDatasetPath() {
		return EX_PATH;
	}

	/**
	 * Remove the first tile from the first time slice. This is used to test
	 * error handling when tiles are missing.
	 * @param ds
	 * @return 
	 * @throws TaskException 
	 * @throws IOException 
	 */
	public Box removeOneTile(Dataset ds) throws TaskException, IOException {
		TimeSlice ts = datasetDao.getTimeSlices(ds.getId()).get(0);
		List<Tile> tiles = tileManager.getTiles(ts.getBounds(), ds.getResolution());
		Band band = datasetDao.getBands(ds.getId()).get(0);
		TileBand tb = new TileBand(tiles.get(0), band, ts);
		Path tilePath = tb.getDefaultFileLocation();
		Files.deleteIfExists(tilePath);
		return tb.getBounds();
	}

}
