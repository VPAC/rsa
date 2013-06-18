package org.vpac.ndg.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

/**
 * Test case to test if CmdClient is working properly.
 * @author hsumanto
 * @author Alex Fraser
 */
@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/beans/CmdClientBean.xml"})
public class ImportTest extends ConsoleTest {
	@Rule
	public MethodRule benchmarkRun = new BenchmarkRule();

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	public void testImportTimeSlices() {
		DecimalFormat df = new DecimalFormat( "00" );
		for(int dateCount=1; dateCount<=3; dateCount++) {
			String dfDay = df.format(dateCount);
			String acquisitionTime = "2010-01-" + dfDay + "T01-50-55.000";
			String datasetName = "DummyDataset123";
			String bandName = "DummyBand123";
			String datasetResolution = "100m";
			String type = "BYTE";
			String primaryFileLocation = "../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc";

			// Assure data structures are in place.
			String datasetId = ensureDatasetExists(datasetName, datasetResolution);
			String tsId = ensureTimeSliceExists(datasetId, acquisitionTime);
			String bandId = ensureBandExists(datasetId, bandName, type);

			// Now the actual test!
			client.execute("data", "import", tsId, bandId, primaryFileLocation);
			assertEquals("Failed to perform import.", 0, errcode);
		}
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	public void testImportTimeSlice() throws TaskInitialisationException, TaskException {
		String acquisitionTime = "2010-01-24T01-50-55.000";
		String datasetName = "DummyDataset123";
		String bandName = "DummyBand123";
		String datasetResolution = "100m";
		String type = "BYTE";
		String primaryFileLocation = "../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc";

		// Assure data structures are in place.
		String datasetId = ensureDatasetExists(datasetName, datasetResolution);
		String tsId = ensureTimeSliceExists(datasetId, acquisitionTime);
		String bandId = ensureBandExists(datasetId, bandName, type);

		// Now the actual test!
		client.execute("data", "import", tsId, bandId, primaryFileLocation);
		assertEquals("Failed to perform import.", 0, errcode);
	}

	protected String findId(String clientOutput, String value) {
		Pattern pattern = Pattern.compile("^([^ ]+) ([^ ]+).*");
		String id = null;
		for (String line : clientOutput.split("\n")) {
			Matcher matcher = pattern.matcher(line);
			if (!matcher.matches())
				continue;
			if (!matcher.group(2).equals(value))
				continue;
			id = matcher.group(1);
			break;
		}
		return id;
	}

	protected String ensureDatasetExists(String name, String resolution) {
		// Check whether the time slice exists.
		String path = name + "/" + resolution;
		client.execute("dataset", "show", path);
		String dsId = findId(output.toString(), path);
		output.reset();
		errput.reset();

		// Re-initialize errcode in case this is the first time the dataset is being created thus 
		// the above dataset show should fail and set errcode to -1
		errcode = 0;
		if (dsId == null) {
			client.execute("dataset", "create", name, resolution);
			assertEquals("Failed to create dataset.", 0, errcode);
			dsId = findId(output.toString(), path);
			output.reset();
			errput.reset();
		}
		assertNotNull("Failed to create dataset.", dsId);
		return dsId;
	}

	protected String ensureTimeSliceExists(String datasetId, String acquisitionTime) {
		// Check whether the time slice exists.
		client.execute("timeslice", "list", datasetId);
		String tsId = findId(output.toString(), acquisitionTime);
		output.reset();
		errput.reset();

		if (tsId == null) {
			client.execute("timeslice", "create", datasetId, acquisitionTime);
			assertEquals("Failed to create time slice.", 0, errcode);
			tsId = findId(output.toString(), acquisitionTime);
			output.reset();
			errput.reset();
		}
		assertNotNull("Failed to create time slice.", tsId);
		return tsId;
	}

	protected String ensureBandExists(String datasetId, String bandName, String type) {
		// Check whether the band already exists.
		client.execute("band", "list", datasetId);
		String bandId = findId(output.toString(), bandName);
		output.reset();
		errput.reset();

		if (bandId == null) {
			client.execute("band", "create", datasetId, bandName, type);
			assertEquals("Failed to create band.", 0, errcode);
			bandId = findId(output.toString(), bandName);
			output.reset();
			errput.reset();
		}
		assertNotNull("Failed to create band.", bandId);
		return bandId;
	}
}
