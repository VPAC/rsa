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

package org.vpac.ndg.cli.smadaptor.remote;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.vpac.ndg.cli.smadaptor.BandConnector;
import org.vpac.ndg.storage.model.Band;
import org.vpac.web.model.response.BandCollectionResponse;
import org.vpac.web.model.response.BandResponse;

public class RemoteBandConnector implements BandConnector {
	public static String GET_ALL_BANDS_URL = "/SpatialCubeService/Band.xml?datasetId={datasetId}";
	public static String GET_BAND_BY_ID_URL = "/SpatialCubeService/Band/{id}.xml";
	public static String DELETE_BAND_BY_ID_URL = "/SpatialCubeService/Band/Delete/{id}.xml";
	public static String CREATE_OR_UPDATE_BAND_URL = "/SpatialCubeService/Band.xml?id={id}&datasetId={datasetId}&name={name}&dataType={dataType}&nodata={nodata}&continuous={continuous}&metadata={metadata}";

	private String baseUri;
	@Autowired
	protected RestTemplate restTemplate;
	
	@Override
	public List<Band> list(String datasetId) {
		BandCollectionResponse response = restTemplate.getForObject(baseUri + GET_ALL_BANDS_URL, BandCollectionResponse.class, datasetId);
		return response.toBand();
	}

	@Override
	public Band createBand(String dsid, String name, 
			String datatype,
			String nodata,
			String isContinuous,
			String isMetadata) {

		BandResponse response = restTemplate.postForObject(baseUri + CREATE_OR_UPDATE_BAND_URL, null, BandResponse.class, null, dsid, name, datatype, nodata, isContinuous, isMetadata);
		return response.toBand();
	}

	@Override
	public void delete(String id) throws IOException {
		restTemplate.postForObject(baseUri + DELETE_BAND_BY_ID_URL, null, BandResponse.class, id);
	}

	@Override
	public void update(Band newBand) throws IOException {
		restTemplate.postForObject(baseUri + CREATE_OR_UPDATE_BAND_URL, null, BandResponse.class, newBand.getId(), null, newBand.getName(), newBand.getType(), newBand.getNodata(), newBand.isContinuous(), newBand.isMetadata());
	}

	@Override
	public Band retrieve(String bandId) {
		BandResponse response = restTemplate.getForObject(baseUri + GET_BAND_BY_ID_URL, BandResponse.class, bandId);
		return response.toBand();
	}

	@Override
	public void updateInfo(Band newBand) {
		restTemplate.postForObject(baseUri + CREATE_OR_UPDATE_BAND_URL, null, BandResponse.class, newBand.getId(), null, newBand.getName(), newBand.getType(), newBand.getNodata(), newBand.isContinuous(), newBand.isMetadata());
	}
	
	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
}
