package org.vpac.ndg.cli;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test case to test if CmdClient is working properly.
 * @author hsumanto
 * @author Alex Fraser
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/beans/CmdClientBean.xml"})
public class InvalidInputTests extends ConsoleTest {

	@Test
	public void testImportEmptyTimeSliceId() {
		client.execute("data", "import", " ", "Band1", "DummyInput.nc");
		assertEquals("Import should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Timeslice ID not specified"));
	}

	@Test
	public void testImportEmptyBandId() {
		client.execute("data", "import", "DummyTimeSliceId", " ", "DummyInput.nc");
		assertEquals("Import should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Band ID not specified"));
	}

	@Test
	public void testImportEmptyFile() {
		client.execute("data", "import", "DummyTimeSliceId", "DummyBandId", " ");
		assertEquals("Import should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("No source file"));
	}

	@Test
	public void testImportInvalidTimeSliceId() {
		client.execute("data", "import", "DummyTimeSliceId", "DummyBandId", "../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc");
		assertEquals("Import should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("TimeSlice \"DummyTimeSliceId\" not found"));
	}

	@Test
	public void testExportEmptyDatasetId() {
		client.execute("data", "export", " ");
		assertEquals("Export should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset ID not specified"));
	}

	@Test
	public void testExportInvalidDatasetId() {
		client.execute("data", "export", "-x", "DummyDatasetId");
		assertEquals("Export should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset not found"));
	}

	@Test
	public void testListTimeSliceEmptyDatasetId() {
		client.execute("timeslice", "list", " ");
		assertEquals("List should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset not found"));
	}

	@Test
	public void testListTimeSliceInvalidDatasetId() {
		client.execute("timeslice", "list", "DummyDatasetId");
		assertEquals("List should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset not found"));
	}

	@Test
	public void testListBandEmptyDatasetId() {
		client.execute("band", "list", " ");
		assertEquals("List should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset not found"));
	}	

	@Test
	public void testListBandInvalidDatasetId() {
		client.execute("band", "list", "DummyDatasetId");
		assertEquals("List should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset not found"));
	}

	@Test
	public void testcreateDatasetEmptyDatasetId() {
		client.execute("dataset", "create", " ", "100m");
		assertEquals("Create should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset name not specified"));
	}	

	@Test
	public void testcreateDatasetInvalidResolution() {
		client.execute("dataset", "create", "DummyDatasetName", "Invalid_100m_resolution");
		assertEquals("Create should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("No matching resolution"));
	}

	@Test
	public void testCreateTimeSliceEmptyDatasetId() {
		client.execute("timeslice", "create", " ", "20120903");
		assertEquals("Create should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset ID not specified"));
	}

	@Test
	public void testCreateTimeSliceEmptyCreationDate() {
		client.execute("timeslice", "create", "DummyDatasetId", " ");
		assertEquals("Create should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Creation date not specified"));
	}

	@Test
	public void testCreateTimeSliceInvalidCreationDate() {
		client.execute("timeslice", "create", "DummyDatasetId", "DummyDate");
		assertEquals("Create should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset not found"));
	}

	@Test
	public void testCreateBandEmptyDatasetId() {
		client.execute("band", "create", " ", "DummyBand", "BYTE");
		assertEquals("Create should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset ID not specified"));
	}

	@Test
	public void testCreateBandInvalidDatasetId() {
		client.execute("band", "create", "DummyDatasetId", "DummyBand", "BYTE");
		assertEquals("Create should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset not found"));
	}

	@Test
	public void testCreateBandEmptyBandName() {
		client.execute("band", "create", "DummyDatasetId", " ", "BYTE");
		assertEquals("Create should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Band name not specified"));
	}

	@Test
	public void testCreateBandEmptyDataType() {
		client.execute("band", "create", "DummyDatasetId", "Band1", "--type", " ");
		assertEquals("Create should have failed.", 1, errcode);
		assertTrue(errput.toString().contains("Dataset not found."));
	}
}
