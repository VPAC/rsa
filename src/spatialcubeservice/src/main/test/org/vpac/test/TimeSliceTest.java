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

package org.vpac.test;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.vpac.web.model.response.DatasetCollectionResponse;
import org.vpac.web.model.response.DatasetResponse;
import org.vpac.web.model.response.TimeSliceCollectionResponse;
import org.vpac.web.model.response.TimeSliceLockCollectionResponse;
import org.vpac.web.model.response.TimeSliceResponse;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
public class TimeSliceTest extends TestCase {
	private static String TestDatasetName = "BandTest";
	private String testDatasetId;

	@Autowired
	protected RestTemplate restTemplate;

	@Before
	public void setUp() {
		String name = TestDatasetName;
		String resolution = "500m";
		String dataAbstract = "testAbs";
		String testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml?name={name}&resolution={resolution}";
		DatasetCollectionResponse getResponse = restTemplate.getForObject(testURL, DatasetCollectionResponse.class, name, resolution);
		if(getResponse.getItems().size() == 0) {
			DatasetResponse response = testCreateDataset(name, resolution, dataAbstract);
			testDatasetId = response.getId();
		} else {
			testDatasetId = getResponse.getItems().get(0).getId();
		}
	}

	@Test
	public void testGetAllTimeSlice() {
		String testURL = "http://localhost:8080/SpatialCubeService/TimeSlice.xml?datasetId={1}";
		TimeSliceCollectionResponse getTimeslicesResponse = restTemplate.getForObject(testURL, TimeSliceCollectionResponse.class, testDatasetId);
		if(getTimeslicesResponse.getItems().size() == 0) {
			String datasetId = testDatasetId;
			String creationDate = "20120905";
			TimeSliceResponse response = testCreateTimeSlice(datasetId, creationDate);
			assertNotNull(response.getId());
		} else {
			assertNotSame(getTimeslicesResponse.getItems().size(), 0);
		}
	}

	@Test
	public void testGetAllTimeSliceLock() {
		String testTimeSliceId = "1234";
		String testURL = "http://localhost:8080/SpatialCubeService/TimeSlice/Lock/{timesliceId}.xml";
		TimeSliceLockCollectionResponse response = restTemplate.getForObject(testURL, TimeSliceLockCollectionResponse.class, testTimeSliceId);
		assertNotNull(response);
	}

	
	@Test(expected=Exception.class)
	public void testNoDatasetId() {
		TimeSliceCollectionResponse response;
		String testURL;
		testURL = "http://localhost:8080/SpatialCubeService/TimeSlice.xml";
		response = restTemplate.getForObject(testURL, TimeSliceCollectionResponse.class);
		assertNotSame(response.getItems().size(), 0);
	}

	
	@Test(expected=Exception.class)
	public void testPageParameterValidatingPage() {
		TimeSliceCollectionResponse response;
		String testURL;
		testURL = "http://localhost:8080/SpatialCubeService/TimeSlice.xml?datasetId=2c9f85243827bc64013827bc67b20006&page=-1&pageSize=2";
		response = restTemplate.getForObject(testURL, TimeSliceCollectionResponse.class);
		assertNotSame(response.getItems().size(), 0);
	}

	@Test(expected=Exception.class)
	public void testPageParameterValidatingPageSize() {
		TimeSliceCollectionResponse response;
		String testURL;
		testURL = "http://localhost:8080/SpatialCubeService/TimeSlice.xml?datasetId=2c9f85243827bc64013827bc67b20006&page=1&pageSize=-2";
		response = restTemplate.getForObject(testURL, TimeSliceCollectionResponse.class);
		assertNotSame(response.getItems().size(), 0);
	}

	@Test(expected=Exception.class)
	public void testGetDatasetById() {
		TimeSliceResponse response;
		String testURL = "http://localhost:8080/SpatialCubeService/TimeSlice/NOTEXISTINGID2c9f85243827bc64013827bc67bd0008.xml"; // id doesn't exist
		response = restTemplate.getForObject(testURL, TimeSliceResponse.class);
		assertNull(response.getId());
	}

	private TimeSliceResponse testCreateTimeSlice(String datasetId, String creationDate) {
		String testURL = "http://localhost:8080/SpatialCubeService/TimeSlice.xml?datasetId={datadsetId}&creationDate={creationDate}";
		return restTemplate.postForObject(testURL, null, TimeSliceResponse.class, datasetId, creationDate);
	}

	private DatasetResponse testCreateDataset(String name, String resolution, String dataAbstract) {
		String testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml?name={name}&resolution={resolution}&dataAbstract={dataAbstract}&precision=1";
		return restTemplate.postForObject(testURL, null, DatasetResponse.class, name, resolution, dataAbstract);
	}
}