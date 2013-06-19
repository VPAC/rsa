package org.vpac.ndg.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Section;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:resources/spring/beans/CmdClientBean.xml" })
@BenchmarkOptions(benchmarkRounds = 4, warmupRounds = 1)
public class QueryTest extends ConsoleTest {

	final Logger log = LoggerFactory.getLogger(QueryTest.class);

	@Rule
	public MethodRule benchmarkRun = new BenchmarkRule();

	@Test
	public void testThreshold() throws IOException {
		String queryFile = "../../data/query/threshold_rsa.xml";
		String outputFile = String.format("%s.nc", queryFile);
		Files.deleteIfExists(Paths.get(outputFile));
		client.execute("data", "query", queryFile, "-o", outputFile);
		assertEquals(0, errcode);
	}

	@Test
	public void testWetNdviReduced() throws IOException, InvalidRangeException {
		String queryFile = "../../data/query/wetting_ndvi_reduced.xml";
		String outputFile = String.format("%s.nc", queryFile);
		String expectedFile = "../../data/query/wetting_ndvi_reduced_expected.nc";
		Files.deleteIfExists(Paths.get(outputFile));
		client.execute("data", "query", queryFile, "-o", outputFile);
		assertEquals(0, errcode);

		// Use small excerpt from dataset to compare with known values.
		NetcdfFile expectedds = null;
		NetcdfFile actualds = null;
		try {
			// Expected file generated from same query, but with smaller extents
			// "977500.00 -3565000.0 982500.00 -3560000.0"
			// This is to keep it small so it can be checked in to version
			// control.
			expectedds = NetcdfFile.open(expectedFile);
			actualds = NetcdfFile.open(outputFile);

			Section section = new Section("400:599,1100:1299");
			Variable varExp;
			Variable varAct;
			Array arrExp;
			Array arrAct;
			varExp = expectedds.findVariable("wet");
			arrExp = varExp.read();
			varAct = actualds.findVariable("wet");
			arrAct = varAct.read(section);
			assertArray(varAct.getDataType(), arrExp, arrAct);

			varExp = expectedds.findVariable("time");
			arrExp = varExp.read();
			varAct = actualds.findVariable("time");
			arrAct = varAct.read(section);
			assertArray(varAct.getDataType(), arrExp, arrAct);

		} finally {
			if (expectedds != null) {
				try {
					expectedds.close();
				} catch (Exception e) {
					log.error("Failed to close expected dataset", e);
				}
			}
			if (actualds != null) {
				try {
					actualds.close();
				} catch (Exception e) {
					log.error("Failed to close actual dataset", e);
				}
			}
		}
	}

	@Test
	public void testWetNdvi() throws IOException {
		String queryFile = "../../data/query/wetting_ndvi.xml";
		String outputFile = String.format("%s.nc", queryFile);
		Files.deleteIfExists(Paths.get(outputFile));
		client.execute("data", "query", queryFile, "-o", outputFile);
		assertEquals(0, errcode);
	}

	@Test
	public void testAddUnary() throws IOException {
		String queryFile = "../../data/query/add_unary_rsa.xml";
		String outputFile = String.format("%s.nc", queryFile);
		Files.deleteIfExists(Paths.get(outputFile));
		client.execute("data", "query", queryFile, "-o", outputFile);
		assertEquals(0, errcode);
	}

	@Test
	public void testAddBinary() throws IOException {
		String queryFile = "../../data/query/add_binary_rsa.xml";
		String outputFile = String.format("%s.nc", queryFile);
		Files.deleteIfExists(Paths.get(outputFile));
		client.execute("data", "query", queryFile, "-o", outputFile);
		assertEquals(0, errcode);
	}

	private void assertArray(DataType type, Array expected, Array actual) {
		assertArrayEquals(expected.getShape(), actual.getShape());
		switch (type) {
		case BYTE:
		case SHORT:
		case INT:
		case LONG:
			for (int i = 0; i < expected.getSize(); i++) {
				long ex = expected.getLong(i);
				long ac = actual.getLong(i);
				assertEquals(ex, ac);
			}
			break;
		case FLOAT:
		case DOUBLE:
			for (int i = 0; i < expected.getSize(); i++) {
				double ex = expected.getDouble(i);
				double ac = actual.getDouble(i);
				assertEquals(ex, ac, 0.01);
			}
			break;
		default:
			throw new IllegalArgumentException(String.format(
					"Unsupported data type for comparison: %s", type));
		}
	}
}
