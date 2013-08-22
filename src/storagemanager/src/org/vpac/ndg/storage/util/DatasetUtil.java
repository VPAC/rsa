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

package org.vpac.ndg.storage.util;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Dataset;

public class DatasetUtil {

	final Logger log = LoggerFactory.getLogger(DatasetUtil.class);

	public static int DATASET_NAME_MAX_LEN = 255;
	DatasetDao datasetDao;
	
	public DatasetDao getDatasetDao() {
		return datasetDao;
	}

	public void setDatasetDao(DatasetDao datasetDao) {
		this.datasetDao = datasetDao;
	}

	public DatasetUtil() {
	}

	/**
	 * Gets the root of a dataset on the filesystem.
	 *
	 * @param ds
	 *            The dataset to find.
	 * @return The directory where the dataset is stored. All the time slices,
	 *         bands and tiles are stored below this.
	 */
	public Path getPath(Dataset ds) {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		NdgConfigManager ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");

		Path storagePoolPath = Paths.get(ndgConfigManager.getConfig().getDefaultStoragePool());

		String datasetName = ds.getName().trim();
		if(ds.getName().length() > DATASET_NAME_MAX_LEN) {
			datasetName = datasetName.substring(0, DATASET_NAME_MAX_LEN);
		}
		datasetName = datasetName.replace(' ', '_');
		String dsStorageLocation = datasetName + "_" + ds.getResolution();
		return storagePoolPath.resolve(dsStorageLocation);
	}

	/**
	 * Deletes all dataset tiles from storagepool and also
	 * deletes dataset from database.
	 * @param ds The dataset to delete.
	 * @throws IOException
	 */
	public void deleteDataset(Dataset ds) throws IOException {
		Path dsPath = getPath(ds);
		// Delete all dataset tiles from storagepool
		try {
			FileUtils.removeDirectory(dsPath);
		} catch (NoSuchFileException e) {
			log.warn("Could not find dataset directory {}. Continuing with deletion anyway", dsPath);
		}
		// Delete dataset from database
		datasetDao.delete(ds);
	}
	
	/**
	 * Deletes all dataset tiles from storagepool and also
	 * deletes dataset from database.
	 * @param newDs The new dataset to update to.
	 * @throws IOException
	 */
	public void update(Dataset newDs) throws IOException {
		Dataset oldDs = datasetDao.retrieve(newDs.getId());
		Path oldDsPath = getPath(oldDs);

		Path newDsPath = Paths.get(oldDsPath.toString().replace(oldDs.getName(), newDs.getName()));
		
		FileUtils.move(oldDsPath, newDsPath);
		
		datasetDao.update(newDs);
	}
}
