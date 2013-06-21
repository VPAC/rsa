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

package org.vpac.ndg.storage.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.CommandUtil;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.common.datamodel.GdalFormat;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.rasterservices.ProcessException;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storagemanager.GraphicsFile;
import org.vpac.ndg.storagemanager.IGraphicsFile;

public class BandUtil {

	final Logger log = LoggerFactory.getLogger(BandUtil.class);

	CommandUtil commandUtil;

	@Autowired
	DatasetUtil datasetUtil;
	@Autowired
	TimeSliceUtil timeSliceUtil;

	@Autowired
	NdgConfigManager ndgConfigManager;
	@Autowired
	BandDao bandDao;
	
	
	public BandUtil() {
		commandUtil = new CommandUtil();
	}

	// Could make this configurable, but nothing other than NetCDF will work
	// with NCML files
	private final GdalFormat FORMAT = GdalFormat.NC;
	private final URL seedurl = BandUtil.class.getResource("/blank_seed.nc");

	/**
	 * Create a blank tile to suit a band.
	 * 
	 * @param ds The dataset that the band belongs to.
	 * @param band The band to create a tile for.
	 * @throws IOException
	 * @see BandUtil#getBlankTilePath(Dataset, Band)
	 */
	public void createBlankTile(Dataset ds, Band band) throws IOException {
		Path path = getBlankTilePath(ds, band);

		log.debug("Creating blank tile for band {} in dataset {}", band, ds);

		if (isEmpty(band)) {
			throw new IllegalArgumentException(String.format(
					"Can't create blank tile for under-defined band %s. Try " +
					"importing some data into the band first.", band));
		}

		if (!Files.exists(path.getParent()))
			Files.createDirectories(path.getParent());

		if (Files.exists(path)) {
			log.warn("Re-creating blank tile for band {} in dataset {}", band, ds);
			Files.delete(path);
		}

		ArrayList<String> command = new ArrayList<>();
		command.add("gdalwarp");

		command.add("-of");
		command.add(FORMAT.toGdalString());

		// Creation options (e.g. compression)
		for (String co : FORMAT.getCreationOptions()) {
			command.add("-co");
			command.add(co);
		}

		// Target file must have same nodata, projection and data type as band.

		// Target nodata value. Will not be null (due to isEmpty check above).
		command.add("-dstnodata");
		if (!band.getNodata().isEmpty()) {
			command.add(band.getNodata());
		} else {
			// Failsafe: fill with 0.
			command.add("0");
		}

		// Data type (int16, etc.) Will not be null (due to isEmpty check above).
		command.add("-ot");
		command.add(band.getType().getGdalDataType());

		// Target spatial reference system
		command.add("-t_srs");
		command.add(ndgConfigManager.getConfig().getTargetProjection());

		// Target resolution
		double res = ds.getResolution().toDouble();
		String resolution = Double.toString(res);
		command.add("-tr");
		command.add(resolution);
		command.add(resolution);
		log.debug("Resolution: {}", resolution);

		// Target extents. These just match the size of the tiles at the
		// dataset's resolution. The actual location is irrelevant.
		Map<CellSize, Integer> resolutionList = ndgConfigManager.getConfig().getResolutionList();
		int cellsPerTile = resolutionList.get(ds.getResolution());
		String size = Double.toString(cellsPerTile * res);
		command.add("-te");
		command.add("0");
		command.add("0");
		command.add(size);
		command.add(size);
		log.debug("Size: {}", size);

		// Source dataset (a resource)
		command.add(seedurl.getPath());
		log.debug("Seed path: {}", seedurl);
		// Target file
		command.add(path.toString());

		try {
			commandUtil.start(command);			
		} catch (ProcessException | InterruptedException | IOException e) {
			throw new IOException(String.format("Failed to create blank tile for %s", band), e);
		}

		log.info("Created blank tile {}", path);
	}

	/**
	 * Get a blank tile. The resulting file will have the right data type,
	 * resolution, size, projection and nodata value for the specified band.
	 * However the extents will be bogus, and will need to be overridden. Blank
	 * tiles can be used in place of missing tiles in aggregations.
	 * 
	 * @param ds The dataset that the band belongs to.
	 * @param band The band to get the tile of.
	 * @return The path to the blank tile.
	 * @see BandUtil#createBlankTile(Dataset, Band)
	 */
	public Path getBlankTilePath(Dataset ds, Band band) {
		Path dir = datasetUtil.getPath(ds);
		return dir.resolve(String.format("%s_blank.nc", band.getName()));
	}

	/**
	 * Gets a blank tile that can be used in aggregations. If it does not exist,
	 * it will be created.
	 * 
	 * @param ds The dataset that the band belongs to.
	 * @param band The band to get the tile of.
	 * @return The blank file.
	 * @throws IOException If the file could not be created.
	 */
	public IGraphicsFile getBlankTile(Dataset ds, Band band) throws IOException {
		Path path = getBlankTilePath(ds, band);
		if (Files.notExists(path)) {
			createBlankTile(ds, band);
		}
		return new GraphicsFile(path);
	}

	public boolean isEmpty(Band band) {
		return band.getNodata() == null || band.getType() == null;
	}

	public DatasetUtil getDatasetUtil() {
		return datasetUtil;
	}

	public void setDatasetUtil(DatasetUtil datasetUtil) {
		this.datasetUtil = datasetUtil;
	}

	public NdgConfigManager getNdgConfigManager() {
		return ndgConfigManager;
	}

	public void setNdgConfigManager(NdgConfigManager ndgConfigManager) {
		this.ndgConfigManager = ndgConfigManager;
	}

	public void delete(Band band) throws IOException{

		Dataset ds = bandDao.getParentDataset(band.getId());
		Path blankBandTilePath = getBlankTilePath(ds, band);
		List<Path> bandTilePaths = getBandTilePaths(ds, band);
		// Delete all dataset tiles from storagepool
		FileUtils.deleteIfExists(blankBandTilePath);
		for(Path p : bandTilePaths) {
			FileUtils.deleteIfExists(p);
		}

		bandDao.delete(band);
	}

	private List<Path> getBandTilePaths(Dataset ds, Band band) {
		List<Path> bandFiles = new ArrayList<Path>();

		List<TimeSlice> tsList = datasetUtil.getDatasetDao().getTimeSlices(ds.getId());
		for(TimeSlice ts : tsList) {
			Path tsPath = timeSliceUtil.getFileLocation(ts);
			for(File f : tsPath.toFile().listFiles()) {
				if(f.getName().startsWith(band.getName() + "_"))
					bandFiles.add(f.toPath());
			}
		}
		return bandFiles;
	}

	public void update(Band newBand) throws IOException {

		Dataset ds = bandDao.getParentDataset(newBand.getId());
		Band oldBand = bandDao.retrieve(newBand.getId());

		Path blankBandTilePath = getBlankTilePath(ds, oldBand);
		List<Path> bandTilePaths = getBandTilePaths(ds, oldBand);
		// Delete all dataset tiles from storagepool
		FileUtils.deleteIfExists(blankBandTilePath);
		createBlankTile(ds, newBand);
		for(Path p : bandTilePaths) {
			Path newBandFile = Paths.get(p.toString().replace(oldBand.getName(), newBand.getName()));
			FileUtils.move(p, newBandFile);
		}

		
		bandDao.update(newBand);
	}

}
