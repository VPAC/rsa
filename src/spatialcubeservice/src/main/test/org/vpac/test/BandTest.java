package org.vpac.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.*;
import org.springframework.web.client.RestTemplate;
import org.vpac.web.model.response.BandCollectionResponse;
import org.vpac.web.model.response.BandResponse;
import org.vpac.web.model.response.DatasetCollectionResponse;
import org.vpac.web.model.response.DatasetResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
public class BandTest extends TestCase {
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
	public void testGetAllBands() {
		String testURL = "http://localhost:8080/SpatialCubeService/Band.xml?datasetId={id}";
		BandCollectionResponse getBandsResponse = restTemplate.getForObject(testURL, BandCollectionResponse.class, testDatasetId);
		if(getBandsResponse.getItems().size() == 0) {
			String datasetId = testDatasetId;
			String name = "FooBand";
			String type = "FLOAT32";
			String isMetadata = "true";
			BandResponse response = testCreateBand(datasetId, name, type, isMetadata);
			assertNotNull(response.getId());
		} else {
			assertNotSame(getBandsResponse.getItems().size(), 0);
		}
	}

	@Test
	public void testGetAllBandsForDummyResponse() {
		BandCollectionResponse response;
		String testURL = "http://localhost:8080/SpatialCubeService/Band.xml?datasetId=DummyDatasetId";
		response = restTemplate.getForObject(testURL, BandCollectionResponse.class);
		assertSame(response.getItems().size(), 0);
		// Should be empty list
	}

	@Test(expected=Exception.class)
	public void testNoDatasetId() {
		BandCollectionResponse response;
		String testURL = "http://localhost:8080/SpatialCubeService/Band/DummyBand1.xml";
		response = restTemplate.getForObject(testURL, BandCollectionResponse.class);
		assertNotSame(response.getItems().size(), 0);
		// Should be resulted in 404 (Not Found)
	}

	@Test
	public void testCreateBandNormal() {
		String testURL = "http://localhost:8080/SpatialCubeService/Band.xml?datasetId={id}";
		BandCollectionResponse getBandsResponse = restTemplate.getForObject(testURL, BandCollectionResponse.class, testDatasetId);
		if(getBandsResponse.getItems().size() == 0) {
			String datasetId = "2c9f85243827bc64013827bc67b20006";
			String name = "FooBand";
			String type = "FLOAT32";
			String isMetadata = "true";
			BandResponse response = testCreateBand(datasetId, name, type, isMetadata);
			assertNotNull(response.getId());
			assertThat(response.getName(), is(name));
		} else {
			assertNotSame(getBandsResponse.getItems().size(), 0);
		}
	}

	@Test(expected=Exception.class)
	public void testCreateBandAbnormaltype() {
		String datasetId = "2c9f85243827bc64013827bc67b20006";
		String name = "test";
		String type = "NOTYPEFLOAT32";
		String isMetadata = "true";
		BandResponse response = testCreateBand(datasetId, name, type, isMetadata);
		assertNotNull(response);
	}

	@Test(expected=Exception.class)
	public void testCreateBandAbnormalMetadata() {
		String datasetId = "2c9f85243827bc64013827bc67b20006";
		String name = "test";
		String type = "FLOAT32";
		String isMetadata = "NOTFALSEORTRUE";
		BandResponse response = testCreateBand(datasetId, name, type, isMetadata);
		assertNotNull(response);
	}

	private BandResponse testCreateBand(String datasetId, String name, String type, String isMetadata) {
		String testURL = "http://localhost:8080/SpatialCubeService/Band.xml?datasetId={datadsetId}&name={name}&type={type}&metadata={isMetadata}";
		return restTemplate.postForObject(testURL, null, BandResponse.class, datasetId, name, type, isMetadata);
	}

	private DatasetResponse testCreateDataset(String name, String resolution, String dataAbstract) {
		String testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml?name={name}&resolution={resolution}&dataAbstract={dataAbstract}&precision=1";
		return restTemplate.postForObject(testURL, null, DatasetResponse.class, name, resolution, dataAbstract);
	}
}