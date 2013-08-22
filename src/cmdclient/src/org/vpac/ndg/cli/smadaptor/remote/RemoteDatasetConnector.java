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

package org.vpac.ndg.cli.smadaptor.remote;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.vpac.ndg.cli.smadaptor.DatasetConnector;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.web.model.response.DatasetCollectionResponse;
import org.vpac.web.model.response.DatasetResponse;

public class RemoteDatasetConnector implements DatasetConnector {
	public static String GET_ALL_DATASET_URL = "/SpatialCubeService/Dataset.xml";
	public static String GET_DATASET_BY_ID_URL = "/SpatialCubeService/Dataset/{id}.xml";
	public static String GET_DATASET_BY_NAME_RESOLUTION_URL = "/SpatialCubeService/Dataset/Search.xml?name={name}&resolution={resolution}";
	public static String DELETE_DATASET_BY_ID_URL = "/SpatialCubeService/Dataset/Delete/{id}.xml";
	public static String CREATE_OR_UPDATE_DATASET_URL = "/SpatialCubeService/Dataset.xml?id={id}&name={name}&resolution={resolution}&dataAbstract={abstract}&precision={precision}";

	private String baseUri;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Override
	public List<Dataset> list() {
		DatasetCollectionResponse response = restTemplate.getForObject(baseUri + GET_ALL_DATASET_URL, DatasetCollectionResponse.class);
		return response.toDatasetList();
	}

	@Override
	public String create(String name, String abs, String resolution,
			String precision) {
		DatasetResponse response = restTemplate.postForObject(baseUri + CREATE_OR_UPDATE_DATASET_URL, null, DatasetResponse.class, null, name, resolution, abs, precision);
		return response.getId();
	}

	@Override
	public void delete(String identifier) throws IOException {
		DatasetResponse response = restTemplate.postForObject(baseUri + DELETE_DATASET_BY_ID_URL, null, DatasetResponse.class, identifier);
		System.out.println("delete id:" + response.getId() + " completed.");
	}

	@Override
	public List<Dataset> searchDataset(String name, CellSize resolution) {
		DatasetCollectionResponse response = restTemplate.getForObject(baseUri + GET_DATASET_BY_NAME_RESOLUTION_URL, DatasetCollectionResponse.class, name, resolution.toHumanString());
		return response.toDatasetList();
	}

	@Override
	public Dataset getDataset(String identifier) {
		int splitLoc = identifier.lastIndexOf('/');

		if (splitLoc != -1) {
			String name = identifier.substring(0, splitLoc);
			String res = identifier.substring(splitLoc + 1);
			DatasetCollectionResponse response = restTemplate.getForObject(baseUri + GET_DATASET_BY_NAME_RESOLUTION_URL, DatasetCollectionResponse.class, name, res);
			return response.getItems().get(0).toDataset();
		} else {
			DatasetResponse response = restTemplate.getForObject(baseUri + GET_DATASET_BY_ID_URL, DatasetResponse.class, identifier);
			return response.toDataset();
		}
	}

	@Override
	public String getNcml(String identifier, Box extents, Date startDate,
			Date endDate) throws IOException {
		throw new UnsupportedOperationException("Remote NCML generation not implemented.");
	}

	@Override
	public String getCdl(String identifier, Box extents, Date startDate,
			Date endDate) throws IOException {
		throw new UnsupportedOperationException("Remote CDL generation not implemented.");
	}

	@Override
	public String getLocation(String identifier) {
		return "";
	}

	@Override
	public Dataset get(String datasetId) {
		return getDataset(datasetId);
	}

	@Override
	public void updateInfo(Dataset newDataset) {
		DatasetResponse response = restTemplate.postForObject(baseUri + CREATE_OR_UPDATE_DATASET_URL, null, DatasetResponse.class, newDataset.getId(), newDataset.getName(), newDataset.getResolution(), newDataset.getAbst(), newDataset.getPrecision());
		System.out.println("update id:" + response.getId() + " completed.");
	}

	@Override
	public void update(Dataset newDataset) throws IOException {
		DatasetResponse response = restTemplate.postForObject(baseUri + CREATE_OR_UPDATE_DATASET_URL, null, DatasetResponse.class, newDataset.getId(), newDataset.getName(), newDataset.getResolution(), newDataset.getAbst(), newDataset.getPrecision());
		System.out.println("update id:" + response.getId() + " completed.");
	}

	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
}
