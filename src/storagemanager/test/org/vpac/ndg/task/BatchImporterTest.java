package org.vpac.ndg.task;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.TestUtil;
import org.vpac.ndg.Utils;
import org.vpac.ndg.common.datamodel.CellSize;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

/**
 * This unit test class is for testing batch import process.
 * 
 * @author hsumanto
 * 
 */
@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/config/TestBeanLocations.xml" })
@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
public class BatchImporterTest extends AbstractJUnit4SpringContextTests {
	@Rule
	public MethodRule benchmarkRun = new BenchmarkRule();

	@Autowired
	TestUtil testUtil;

	/**
	 * Perform batch import on small scale timeseries dataset.
	 * 
	 * @throws Exception
	 */
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	public void testBatchImport() throws Exception {
		String datasetName = "small_landsat";
		Path datasetSrcDir = Paths.get("../../data/" + datasetName);

		boolean continuous = true;
		CellSize targetResolution = CellSize.m25;
		long precision = Utils.parseTemporalPrecision("1 day");
		String srcnodata = "-999";

		testUtil.batchImport(datasetName, targetResolution, precision,
				continuous, datasetSrcDir, srcnodata);
	}
}
