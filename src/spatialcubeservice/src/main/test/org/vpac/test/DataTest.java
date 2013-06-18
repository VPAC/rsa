package org.vpac.test;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.vpac.web.model.response.CleanUpResponse;
import org.vpac.web.model.response.ExportResponse;
//import org.vpac.web.model.response.ExportResponse;
//import org.vpac.web.model.response.ImportResponse;
//import org.vpac.web.model.response.TaskCollectionResponse;
//import org.vpac.web.model.response.TaskResponse;
//import static org.hamcrest.Matchers.*;
//import static org.hamcrest.MatcherAssert.*;
import org.vpac.web.model.response.QueryResponse;


@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration("file:src/main/webapp/WEB-INF/applicationContext.xml")
public class DataTest extends TestCase {
	
	@Autowired
	protected RestTemplate restTemplate;

	@Test
	public void testDummy() {
	}

	/*	
	@Test
	public void testGetTasks() {
		String testURL;
		TaskCollectionResponse response;
		testURL = "http://localhost:8080/SpatialCubeService/Data/Task.xml";
		response = restTemplate.getForObject(testURL, TaskCollectionResponse.class);
		assertNotSame(response.getItems().size(), is(0));
		testURL = "http://localhost:8080/SpatialCubeService/Data/Task.xml?searchType=Import&state=EXECUTION_ERROR";
		response = restTemplate.getForObject(testURL, TaskCollectionResponse.class);
		assertNotSame(response.getItems().size(), is(0));
		testURL = "http://localhost:8080/SpatialCubeService/Data/Task.xml?searchType=Import&state=EXECUTION_ERROR&pageSize=2";
		response = restTemplate.getForObject(testURL, TaskCollectionResponse.class);
		assertThat(response.getItems().size(), is(0));
	}
	
	@Test
	public void testGetTaskById() {
		String testURL;
		TaskResponse response;
		String id= "2c9f852437961c9f0137961ce0200005";
		testURL = "http://localhost:8080/SpatialCubeService/Data/Task/{id}.xml";
		response = restTemplate.getForObject(testURL, TaskResponse.class, id);
		assertNotSame(response.getId(), is(id));
	}

	@Test(expected=Exception.class)
	public void testErrorInStateRequest() {
		String testURL;
		TaskCollectionResponse response;
		testURL = "http://localhost:8080/SpatialCubeService/Data/Task.xml?searchType=Import&state=NOTEXISTSTATE";
		response = restTemplate.getForObject(testURL, TaskCollectionResponse.class);
		assertThat(response.getItems().size(), is(0));
	}


	@Test(expected=Exception.class)
	public void testErrorInSearchType() {
		String testURL;
		TaskCollectionResponse response;
		testURL = "http://localhost:8080/SpatialCubeService/Data/Task.xml?searchType=NOTImportOrExport&state=EXECUTION_ERROR";
		response = restTemplate.getForObject(testURL, TaskCollectionResponse.class);
		assertThat(response.getItems().size(), is(0));
	}

	// TODO : not implemented yet
	@Test(expected=Exception.class)
	public void testImport() {
		String testURL;
		ImportResponse response;
		String fileId = "test";
		testURL = "http://localhost:8080/SpatialCubeService/Data/Import.xml?fileId={fileId}";
		response = restTemplate.postForObject(testURL, null, ImportResponse.class, fileId);
		assertNotNull(response.getTaskId());
	}
	
	@Test(expected=Exception.class)
	public void testImportWithoutFileId() {
		String testURL;
		ImportResponse response;
		testURL = "http://localhost:8080/SpatialCubeService/Data/Import.xml";
		response = restTemplate.postForObject(testURL, null, ImportResponse.class);
		assertNotNull(response.getTaskId());
	}

	@Test
	public void testExport() {
		String testURL;
		ExportResponse response;
		String datasetId = "2c9f85243827bc64013827bc67b20006";
		String startDate = "1/1/2012";
		String endDate = "1/2/2012";
		String projection = "CONTINUES";
		String topX = "10";
		String topY = "10";
		String bottomX = "100";
		String bottomY = "100";
		testURL = "http://localhost:8080/SpatialCubeService/Data/Export.xml?datasetId={datasetId}&searchStartDate={startDate}&searchEndDate={endDate}&projection={projection}&topLeft.x={topX}&topLeft.y={topX}&bottomRight.x={bottomX}&bottomRight.y={bottomY}";
		response = restTemplate.postForObject(testURL, null, ExportResponse.class, datasetId, startDate, endDate, projection, topX, topY, bottomX, bottomY);
		assertNotNull(response.getTaskId());
	}

	@Test
	public void testExportFloat() {
		String testURL;
		ExportResponse response;
		String datasetId = "2c9f85243827bc64013827bc67b20006";
		String startDate = "1/1/2012";
		String endDate = "1/2/2012";
		String projection = "CONTINUES";
		String topX = "10.10";
		String topY = "10.01";
		String bottomX = "100.22";
		String bottomY = "100.93";
		testURL = "http://localhost:8080/SpatialCubeService/Data/Export.xml?datasetId={datasetId}&searchStartDate={startDate}&searchEndDate={endDate}&projection={projection}&topLeft.x={topX}&topLeft.y={topX}&bottomRight.x={bottomX}&bottomRight.y={bottomY}";
		response = restTemplate.postForObject(testURL, null, ExportResponse.class, datasetId, startDate, endDate, projection, topX, topY, bottomX, bottomY);
		assertNotNull(response.getTaskId());
	}
	
	@Test
	public void testExportFloatJson() {
		String testURL;
		String response;
		String datasetId = "2c9f85243827bc64013827bc67b20006";
		String startDate = "1/1/2012";
		String endDate = "1/2/2012";
		String projection = "EPSG:1234";
		String topX = "10.10";
		String topY = "10.01";
		String bottomX = "100.22";
		String bottomY = "100.93";
		testURL = "http://localhost:8080/SpatialCubeService/Data/Export.json?datasetId={datasetId}&searchStartDate={startDate}&searchEndDate={endDate}&projection={projection}&topLeft.x={topX}&topLeft.y={topX}&bottomRight.x={bottomX}&bottomRight.y={bottomY}";
		response = restTemplate.postForObject(testURL, null, String.class, datasetId, startDate, endDate, projection, topX, topY, bottomX, bottomY);
		System.out.println(response);
		assertNotNull(response);
	}

	@Test
	public void testFileUpload() {
		String testURL;
		FileInfoResponse response;
		testURL = "http://localhost:8080/SpatialCubeService/Data/Upload.xml";
		
		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
	    mvm.add("datasetId", "TestParameter");
	    mvm.add("fileId", "2c9f85243827bc64013827bc67b20006");
	    Resource file = new FileSystemResource(new File("C:\\tmp\\gdalapps\\gdal-data\\GDALLogoBW.svg"));
	    mvm.add("files", file);

	    response = restTemplate.postForObject(testURL, mvm, FileInfoResponse.class);
	    assertNotSame(response.getId(), is(""));
	}
*/	
	
	@Test
	public void testCleanUp() {
		String testURL = "http://localhost:8080/SpatialCubeService/Data/CleanUp.xml?force={force}";
		CleanUpResponse response = restTemplate.postForObject(testURL, null, CleanUpResponse.class, false);
		System.out.println("Clean up locks:" + response.getCount());
	}

	@Test
	public void testQuery() {
		String testURL = "http://localhost:8080/SpatialCubeService/Data/Query.xml?minX=0.0&minY=0.0&maxX=100&maxY=100&startDate=123";

		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
	    Resource file = new FileSystemResource(new File("/home/forjin/Documents/ula/trunk/src/RSA/CmdClient/data/add_binary_rsa.xml"));
//	    mvm.add("point.x", 0.0);
//	    mvm.add("point.y", 0.0);
//	    mvm.add("bottomRight.X", 100.0);
//	    mvm.add("bottomRight.Y", 100.0);
//	    mvm.add("startDate", "");
//	    mvm.add("endDate", "");
	    mvm.add("file", file);
	    mvm.add("threads", "1");

		QueryResponse response = restTemplate.postForObject(testURL, mvm, QueryResponse.class);
		System.out.println("Clean up locks:" + response.getTaskId());
	}

	@Test
	public void testPreviewQuery() {
		String testURL = "http://localhost:8080/SpatialCubeService/Data/PreviewQuery.xml";

		String query = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
		"<query xmlns=\"http://www.vpac.org/namespaces/rsaquery-0.1\">" +
		"	<input id=\"infile\" href=\"rsa:small_landsat/100m\" />" +
		"	<output id=\"outfile\">" +
		"		<grid ref=\"#infile\" />" +
		"		<dimension ref=\"#infile/x\" />" +
		"		<dimension ref=\"#infile/y\" />" +
		"		<dimension ref=\"#infile/time\" />" +
		"		<variable name=\"Combined\" ref=\"#infile/B10\" />" +
		"	</output>" +
		"	<filter id=\"Add\" cls=\"org.vpac.ndg.query.AddBinary\">" +
		"		<sampler name=\"inputA\" ref=\"#infile/B10\"  />" +
		"		<sampler name=\"inputB\" ref=\"#infile/B20\"  />" +
		"		<cell name=\"output\" ref=\"#outfile/Combined\" />" +
		"	</filter>" +
		"</query>";

		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
		mvm.add("query", query);
		mvm.add("threads", "1");

		HttpServletResponse response = restTemplate.postForObject(testURL, mvm,
				HttpServletResponse.class);
		assertNotNull(response);
		System.out.println("Done");
	}

	@Test
	public void testExportFloat() {
		String testURL;
		ExportResponse response;
		String datasetId = "ff8081813d244eb7013d244ebc9d0001";
		testURL = "http://localhost:8080/SpatialCubeService/Data/Export.xml?datasetId={datasetId}";
		response = restTemplate.postForObject(testURL, null, ExportResponse.class, datasetId);
		assertNotNull(response.getTaskId());
	}
}



