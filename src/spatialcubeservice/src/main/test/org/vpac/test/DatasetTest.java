package org.vpac.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
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

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
public class DatasetTest extends TestCase {
	private static String TestDatasetName = "DatasetTest";
	private String testDatasetId;
	
	@Autowired
	protected RestTemplate restTemplate;
	
	@Before
	public void setUp() {
		String name = TestDatasetName;
		String resolution = "500m";
		String dataAbstract = "testAbs";

		String checkUrl = "http://localhost:8080/SpatialCubeService/Dataset.xml?name={name}&resolution={resolution}";
		DatasetCollectionResponse getResponse = restTemplate.getForObject(checkUrl, DatasetCollectionResponse.class, name, resolution);

		if(getResponse.getItems().size() == 0) {
			DatasetResponse response = testCreateDataset(name, resolution, dataAbstract);
			testDatasetId = response.getId();
		} else {
			testDatasetId = getResponse.getItems().get(0).getId();
		}
	}

	@Test
	public void testGetAllDataset() {
		DatasetCollectionResponse response;
		String testURL;
		testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml";
		response = restTemplate.getForObject(testURL, DatasetCollectionResponse.class);
		assertNotSame(response.getItems().size(), is(0));
		
		testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml?page=0&pageSize=2";
		response = restTemplate.getForObject(testURL, DatasetCollectionResponse.class);
		assertThat(response.getItems().size(), lessThanOrEqualTo(2));
	}
	
	@Test(expected=Exception.class)
	public void testPageParameterValidatingPage() {
		DatasetCollectionResponse response;
		String testURL;
		testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml?page=-1&pageSize=2";
		response = restTemplate.getForObject(testURL, DatasetCollectionResponse.class);
		assertThat(response.getItems().size(), is(0));
		// Should be resulted in 500 (Internal Server Error) because page is rejected.
	}

	@Test(expected=Exception.class)
	public void testPageParameterValidatingPageSize() {
		DatasetCollectionResponse response;
		String testURL;
		testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml?page=1&pageSize=-2";
		response = restTemplate.getForObject(testURL, DatasetCollectionResponse.class);
		assertThat(response.getItems().size(), is(2));
		// Should be resulted in 500 (Internal Server Error) because pageSize is rejected.
	}
	
	@Test
	public void testGetDatasetById() {
		String testURL = "http://localhost:8080/SpatialCubeService/Dataset/{id}.xml";
		DatasetResponse response = restTemplate.getForObject(testURL, DatasetResponse.class, testDatasetId); 
		assertEquals(response.getId(), testDatasetId);
	}
	
/*	@Test
	public void testGetDatasetToDummyResponse() {
		String testURL = "http://localhost:8080/SpatialCubeService/Dataset/{id}.xml";
		DummyResponse response = restTemplate.getForObject(testURL, DummyResponse.class, testDateasetId); 
		assertThat(response.getId(), is(testDateasetId));
	}
*/
	@Test
	public void testSearchDataset() {
		String searchString = "11";
		String testURL;
		DatasetCollectionResponse response;
		testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml?name=" + searchString;
		response = restTemplate.getForObject(testURL, DatasetCollectionResponse.class);
		for(DatasetResponse dr : response.getItems())
			assertTrue(dr.getName().contains(searchString));
		
		testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml?name=11&page=0&pageSize=1";
		response = restTemplate.getForObject(testURL, DatasetCollectionResponse.class);
		assertThat(response.getItems().size(), is(0));

		testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml?name=sdfafasdfsfsdafa&page=0&pageSize=1";
		response = restTemplate.getForObject(testURL, DatasetCollectionResponse.class);
		assertThat(response.getItems().size(), is(0));
	}
	
	@Test
	public void testCreateDataset() {
		String name = TestDatasetName;
		String resolution = "10m";
		String dataAbstract = "testAbs";

		String checkUrl = "http://localhost:8080/SpatialCubeService/Dataset.xml?name={name}&resolution={resolution}";
		DatasetCollectionResponse getResponse = restTemplate.getForObject(checkUrl, DatasetCollectionResponse.class, name, resolution);

		if(getResponse.getItems().size() == 0) {
			DatasetResponse response = testCreateDataset(name, resolution, dataAbstract);
			assertNotNull(response.getId());
			assertThat(response.getName(), is(name));
			assertThat(response.getDataAbstract(), is(dataAbstract));
		}
	}

/*
Seems like the annotation and the way we create dataset by JSON is having issue, 
THUS COMMENTED THIS CODE AT THE MOMENT, ALTERNATIVELY JSON TEST CAN BE CONDUCTED USING /WEB_INF/pages/DatasetForm.jsp
org.springframework.validation.BindException: org.springframework.validation.BeanPropertyBindingResult: 4 errors
Field error in object 'datasetRequest' on field 'precision': rejected value [null]; codes [NotNull.datasetRequest.precision,NotNull.precision,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [datasetRequest.precision,precision]; arguments []; default message [precision]]; default message [may not be null]
Field error in object 'datasetRequest' on field 'dataAbstract': rejected value [null]; codes [NotNull.datasetRequest.dataAbstract,NotNull.dataAbstract,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [datasetRequest.dataAbstract,dataAbstract]; arguments []; default message [dataAbstract]]; default message [may not be null]
Field error in object 'datasetRequest' on field 'name': rejected value [null]; codes [NotNull.datasetRequest.name,NotNull.name,NotNull.java.lang.String,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [datasetRequest.name,name]; arguments []; default message [name]]; default message [may not be null]
Field error in object 'datasetRequest' on field 'resolution': rejected value [null]; codes [NotNull.datasetRequest.resolution,NotNull.resolution,NotNull.org.vpac.ndg.common.datamodel.CellSize,NotNull]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [datasetRequest.resolution,resolution]; arguments []; default message [resolution]]; default message [may not be null]
	@Test
	public void testCreateDatasetByJson() {
		String name = "testCreateDatasetByJson";
		String resolution = "m500";
		String dataAbstract = "testAbs";

		String checkUrl = "http://localhost:8080/SpatialCubeService/Dataset.xml?name={name}&resolution={resolution}";
		DatasetCollectionResponse getResponse = restTemplate.getForObject(checkUrl, DatasetCollectionResponse.class, name, resolution);

		if(getResponse.getItems().size() == 0) {
			List<MediaType> mediaTypes = new ArrayList<MediaType>();
			mediaTypes.add(MediaType.APPLICATION_JSON);

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(mediaTypes);
			String requestJson = String.format("{name : \'%s\', resolution: \'%s\', dataAbstract : \'%s\', precision : \'1\'}", name, resolution, dataAbstract);
			HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
			String testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml";
			DatasetResponse response = restTemplate.postForObject(testURL, entity, DatasetResponse.class);
			assertThat(response.getName(), is("test"));
		}
	}
*/

	@Test(expected=Exception.class)
	public void testCreateDatasetAbnormalResolution() {
		String name = TestDatasetName;
		String resolution = "50m";
		String dataAbstract = "testAbs";
		@SuppressWarnings("unused")
		DatasetResponse response = testCreateDataset(name, resolution, dataAbstract);
		// Should be resulted in 500 (Internal Server Error) because resolution is rejected.
	}

	private DatasetResponse testCreateDataset(String name, String resolution, String dataAbstract) {
		String testURL = "http://localhost:8080/SpatialCubeService/Dataset.xml?name={name}&resolution={resolution}&dataAbstract={dataAbstract}&precision=1";
		return restTemplate.postForObject(testURL, null, DatasetResponse.class, name, resolution, dataAbstract);
	}
}