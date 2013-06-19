package org.vpac.ndg.query;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.Index;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

@RunWith(BlockJUnit4ClassRunner.class)
public class QueryTest extends TestCase {

	//final private Logger log = LoggerFactory.getLogger(QueryTest.class);
	final int DEFAULT_PIXEL_NUMBER = 64;

	@Test(expected=IOException.class)
	public void test_invalid_href() throws Exception {
		File config = new File("data/config/invalid_href.xml");
		File outputFile = new File("data/output/invalid1.nc");

		QueryRunner.run(config, outputFile);
	}

/*	
	// TODO: To be moved to SpatialCubeService test cases.
	@Test
	public void testMultiPreview() throws Exception {
		File outputFile = new File("data/output/hypercube.nc");
		NetcdfFile dataset = null;
		NetcdfFileWriter outputDataset = null;
		try {
			dataset = NetcdfFile.open(outputFile.getPath());

			List<Variable> variables = dataset.getVariables();
			int numOfBand = 0;
			int numOfTimeslice = 0;
			for(Variable v: variables) {
				if(v.getRank() < 2) {
					// Only interested with >= 2D variables
					continue;
				}
				else if(v.getRank() == 2) {
					if(numOfTimeslice < 1) {
						numOfTimeslice = 1;
					}
				}
				else if(v.getRank() >= 3) {
					if(numOfTimeslice < 3) {
						int currNumOfTimeslice = v.getShape()[0];
						if(currNumOfTimeslice < 3) {
							numOfTimeslice = currNumOfTimeslice;
						} else {
							numOfTimeslice = 3;
						}
					}
				}

				log.debug(v.getNameAndDimensions());
				log.debug("variable rank: {}", v.getRank());
				log.debug("variable dimension size: {}", v.getDimensions().size());
				numOfBand++;
			}
			outputDataset = NetcdfFileWriter.createNew(Version.netcdf4,
					"data/output/watermark_multipreview.nc");
			outputDataset.addDimension(null, "x", 64 * numOfTimeslice);
			outputDataset.addDimension(null, "y", 64 * numOfBand);
			outputDataset.addVariable(null, "x", DataType.INT, "x");
			outputDataset.addVariable(null, "y", DataType.INT, "y");
			outputDataset.addVariable(null, "multipreview", DataType.FLOAT,
					"y x");
			outputDataset.create();
			log.debug("number of preview allowable bands: " + numOfBand);
			log.debug("number of preview allowable timeslice: "
					+ numOfTimeslice);

			Variable multiPreviewVar = outputDataset.findVariable("multipreview");
			numOfBand = 0;
			for(Variable v: variables) {
				if(v.getRank() < 2) {
					// Only interested with 2D or more than 2D variables
					continue;
				}
				else if(v.getRank() == 2) {
					int[] origin = new int[] { 0, 0 };
					int[] shape = new int[] { DEFAULT_PIXEL_NUMBER,
							DEFAULT_PIXEL_NUMBER };
					Array otherArray = v.read(origin, shape);
					Array array = Array.factory(DataType.FLOAT, new int[] {
							DEFAULT_PIXEL_NUMBER, DEFAULT_PIXEL_NUMBER });
					float min = Float.MAX_VALUE;
					float max = Float.MIN_VALUE;
					for (int i = 0; i < DEFAULT_PIXEL_NUMBER
							* DEFAULT_PIXEL_NUMBER; i++) {
						float val = otherArray.getFloat(i);
						if (val < min) {
							min = val;
						}
						if (val > max) {
							max = val;
						}
					}
					for (int i = 0; i < DEFAULT_PIXEL_NUMBER
							* DEFAULT_PIXEL_NUMBER; i++) {
						float val = otherArray.getFloat(i);
						float scaled = (val - min) / (max - min);
						array.setFloat(i, scaled);
					}
					int[] offset = new int[] {
							numOfBand * DEFAULT_PIXEL_NUMBER, 0 };
					outputDataset.write(multiPreviewVar, offset, array);
				}
				else if(v.getRank() >= 3) {
					float min = Float.MAX_VALUE;
					float max = Float.MIN_VALUE;
					for (int t = 0; t < numOfTimeslice; t++) {
						int[] origin = new int[] { t, 0, 0 };
						// The shape for multipreview we are interested is always [1, 64, 64]
						int[] shape = new int[] { 1, DEFAULT_PIXEL_NUMBER,
								DEFAULT_PIXEL_NUMBER };
						Array otherArray = v.read(origin, shape);
						Array array = Array.factory(DataType.FLOAT, new int[] {
								DEFAULT_PIXEL_NUMBER, DEFAULT_PIXEL_NUMBER });
						for (int i = 0; i < DEFAULT_PIXEL_NUMBER
								* DEFAULT_PIXEL_NUMBER; i++) {
							float val = otherArray.getFloat(i);
							if (val < min) {
								min = val;
							}
							if (val > max) {
								max = val;
							}
						}
						for (int i = 0; i < DEFAULT_PIXEL_NUMBER
								* DEFAULT_PIXEL_NUMBER; i++) {
							float val = otherArray.getFloat(i);
							float scaled = (val - min) / (max - min);
							array.setFloat(i, scaled);
						}
						int[] offset = new int[] {
								numOfBand * DEFAULT_PIXEL_NUMBER,
								t * DEFAULT_PIXEL_NUMBER };
						outputDataset.write(multiPreviewVar, offset, array);
					}
				}
				log.debug(v.getNameAndDimensions());
				log.debug("variable rank: {}", v.getRank());
				log.debug("variable dimension size: {}", v.getDimensions()
						.size());
				numOfBand++;
			}
		} finally {
			if (dataset != null)
				dataset.close();
			try {
				outputDataset.close();
			} catch (Exception e) {
				log.warn("Failed to close output file", e);
			}
		}
	}
*/

	@Test
	public void test_1_wetting() throws Exception {
		File config = new File("data/config/wettingextents.xml");
		File outputFile = new File("data/output/watermark.nc");
		File expectedFile = new File("data/expected/watermark.nc");

		QueryRunner.run(config, outputFile);

		NetcdfFile dataset = null;
		NetcdfFile expected = null;
		try {
			dataset = NetcdfFile.open(outputFile.getPath());
			Variable var = dataset.findVariable("wet");
			assertEquals("y x", var.getDimensionsString());
			assertEquals(64, var.getShape()[0]);
			assertEquals(64, var.getShape()[1]);

			Array output = var.read();
			Index ima = output.getIndex();
			Array outtime = dataset.findVariable("time").read();

			// Input is 3 linear gradients:
			//  - time=50:  east-west (right is low, top is high)
			//  - time=100: south-north (bottom is low, top is high)
			//  - time=150: west-east (left is low, right is high)
			// Output is a box, with its right (west) edge made of pixels from
			// time=50; the bottom edge from time=100, and the left edge from
			// time=150. There is no top edge.
			// Note that y increases going north.

			// Northern pixel, just outside the right box edge.
			ima.set(63, 47);
			assertEquals(0, output.getInt(ima));
			assertEquals(0, outtime.getInt(ima));
			// Just inside right box edge; matches t=50.
			ima.set(63, 48);
			assertEquals(1, output.getInt(ima));
			assertEquals(50, outtime.getInt(ima));
			// Level with bottom edge, but still inside right edge.
			ima.set(15, 48);
			assertEquals(1, output.getInt(ima));
			assertEquals(50, outtime.getInt(ima));
			// Just to the left of the right edge; matches t=100.
			ima.set(15, 47);
			assertEquals(1, output.getInt(ima));
			assertEquals(100, outtime.getInt(ima));
			// Level with left edge, but still inside bottom edge.
			ima.set(15, 15);
			assertEquals(1, output.getInt(ima));
			assertEquals(100, outtime.getInt(ima));
			// Just above bottom edge; matches t=150
			ima.set(16, 15);
			assertEquals(1, output.getInt(ima));
			assertEquals(150, outtime.getInt(ima));
			// Back up to the top, still inside left edge.
			ima.set(63, 15);
			assertEquals(1, output.getInt(ima));
			assertEquals(150, outtime.getInt(ima));

			// Semantic tests complete; now test every single cell, just for
			// fun!
			expected = NetcdfFile.open(expectedFile.getPath());
			Variable vex;
			Variable vac;
			vex = expected.findVariable("wet");
			vac = dataset.findVariable("wet");
			assertArray(vex.getDataType(), vex.read(), vac.read());
			vex = expected.findVariable("time");
			vac = dataset.findVariable("time");
			assertArray(vex.getDataType(), vex.read(), vac.read());
		} finally {
			if (dataset != null)
				dataset.close();
			if (expected != null)
				expected.close();
		}
	}

	@Test
	public void test_2_fire() throws Exception {
		File config = new File("data/config/activefire.xml");
		File outputFile = new File("data/output/on_fire.nc");
		File expectedFile = new File("data/expected/on_fire.nc");

		QueryRunner.run(config, outputFile);

		NetcdfFile dataset = null;
		NetcdfFile expected = null;
		try {
			dataset = NetcdfFile.open(outputFile.getPath());
			Variable var = dataset.findVariable("temp");
			assertEquals("y x", var.getDimensionsString());
			assertEquals(64, var.getShape()[0]);
			assertEquals(64, var.getShape()[1]);

			Array output = var.read();
			Index ima = output.getIndex();
			Array outtime = dataset.findVariable("time").read();

			// Input is 3 linear gradients:
			//  - time=50:  east-west (right is low, top is high)
			//  - time=100: south-north (bottom is low, top is high)
			//  - time=150: west-east (left is low, right is high)
			// Output is a box, with its right (west) edge made of pixels from
			// time=150; the top edge from time=100, and the left edge from
			// time=50. There is no bottom edge. Unlike the water test above,
			// this box has an angled interface where two edges meet.
			// Note that y increases going north.

			// Top-left pixel, just inside left edge.
			ima.set(63, 0);
			assertEquals(251, output.getInt(ima));
			assertEquals(50, outtime.getInt(ima));
			// Just outside left edge.
			ima.set(63, 1);
			assertEquals(251, output.getInt(ima));
			assertEquals(100, outtime.getInt(ima));
			// Top-right pixel, just inside top edge.
			ima.set(63, 63);
			assertEquals(251, output.getInt(ima));
			assertEquals(100, outtime.getInt(ima));
			// Just outside top edge.
			ima.set(62, 63);
			assertEquals(251, output.getInt(ima));
			assertEquals(150, outtime.getInt(ima));
			ima.set(49, 50);
			assertEquals(199, output.getInt(ima));
			assertEquals(150, outtime.getInt(ima));
			// Back in top edge - demonstrates angled nature of interface.
			ima.set(50, 49);
			assertEquals(199, output.getInt(ima));
			assertEquals(100, outtime.getInt(ima));
			// Just inside box (not in any edge).
			ima.set(49, 49);
			assertEquals(0, output.getInt(ima));
			assertEquals(0, outtime.getInt(ima));

			// Semantic tests complete; now test every single cell, just for
			// fun!
			expected = NetcdfFile.open(expectedFile.getPath());
			Variable vex;
			Variable vac;
			vex = expected.findVariable("temp");
			vac = dataset.findVariable("temp");
			assertArray(vex.getDataType(), vex.read(), vac.read());
			vex = expected.findVariable("time");
			vac = dataset.findVariable("time");
			assertArray(vex.getDataType(), vex.read(), vac.read());
		} finally {
			if (dataset != null)
				dataset.close();
			if (expected != null)
				expected.close();
		}
	}

	@Test
	public void test_3_quality() throws Exception {
		File config = new File("data/config/qualityselection.xml");
		File outputFile = new File("data/output/quality_colour.nc");
		File expectedFile = new File("data/expected/quality_colour.nc");

		QueryRunner.run(config, outputFile);

		NetcdfFile dataset = null;
		NetcdfFile expected = null;
		try {
			dataset = NetcdfFile.open(outputFile.getPath());
			expected = NetcdfFile.open(expectedFile.getPath());
			Variable vex;
			Variable vac;
			vex = expected.findVariable("Red");
			vac = dataset.findVariable("colour1");
			assertArray(vex.getDataType(), vex.read(), vac.read());
			vex = expected.findVariable("Green");
			vac = dataset.findVariable("colour2");
			assertArray(vex.getDataType(), vex.read(), vac.read());
			vex = expected.findVariable("Blue");
			vac = dataset.findVariable("colour3");
			assertArray(vex.getDataType(), vex.read(), vac.read());
			vex = expected.findVariable("Quality");
			vac = dataset.findVariable("quality");
			assertArray(vex.getDataType(), vex.read(), vac.read());
			vex = expected.findVariable("time");
			vac = dataset.findVariable("time");
			assertArray(vex.getDataType(), vex.read(), vac.read());
		} finally {
			if (dataset != null)
				dataset.close();
			if (expected != null)
				expected.close();
		}
	}

	// There used to be two tests: 5a and 5b, which produced a graphical plot or
	// time series data (1D). However this functionality is now provided by the
	// plot web service in the RSA's Spatial Cube Service.

	@Test
	public void test_6a_minimiseVariance_onepass() throws Exception {
		File config = new File("data/config/minimisevariance.xml");
		File outputFile = new File("data/output/minvariance.nc");
		File expectedFile = new File("data/expected/minvariance.nc");

		QueryRunner.run(config, outputFile);

		NetcdfFile dataset = null;
		NetcdfFile expected = null;
		try {
			dataset = NetcdfFile.open(outputFile.getPath());
			Variable var = dataset.findVariable("Band1");
			byte[] data = new byte[] {
					-110, -107, -103, -91, -92, -100,
				    -123, -113, -97,  -92, -91,  -84,
				    -106, -105, -91,  -80, -92, -100,
				     -95, -104, -90,  -91, -96, -103,
				     -98,  -95, -101, -95, -98,  -95,
				    -101, -106, -95,  -89, -92, -106};
			assertArray(data, var.read("0:5,0:5"));

			expected = NetcdfFile.open(expectedFile.getPath());
			Variable vex;
			Variable vac;
			vex = expected.findVariable("Band1");
			vac = dataset.findVariable("Band1");
			assertArray(vex.getDataType(), vex.read(), vac.read());
			vex = expected.findVariable("time");
			vac = dataset.findVariable("time");
			assertArray(vex.getDataType(), vex.read(), vac.read());
		} finally {
			if (dataset != null)
				dataset.close();
			if (expected != null)
				expected.close();
		}
	}

	@Test
	public void test_6b_minimiseVariance_twopass() throws Exception {
		File config = new File("data/config/minimisevariance_twopass.xml");
		File outputFile = new File("data/output/minvariance_twopass.nc");
		File expectedFile = new File("data/expected/minvariance_twopass.nc");

		QueryRunner.run(config, outputFile);

		NetcdfFile dataset = null;
		NetcdfFile expected = null;
		try {
			dataset = NetcdfFile.open(outputFile.getPath());
			Variable var = dataset.findVariable("Band1");
			byte[] data = new byte[] {
					-110, -107, -103, -91, -92, -100,
				    -123, -113, -97,  -92, -91,  -84,
				    -106, -105, -91,  -80, -92, -100,
				     -95, -104, -90,  -91, -96, -103,
				     -98,  -95, -101, -95, -98,  -95,
				    -101, -106, -95,  -89, -92, -106};
			assertArray(data, var.read("0:5,0:5"));

			expected = NetcdfFile.open(expectedFile.getPath());
			Variable vex;
			Variable vac;
			vex = expected.findVariable("Band1");
			vac = dataset.findVariable("Band1");
			assertArray(vex.getDataType(), vex.read(), vac.read());
			vex = expected.findVariable("time");
			vac = dataset.findVariable("time");
			assertArray(vex.getDataType(), vex.read(), vac.read());
		} finally {
			if (dataset != null)
				dataset.close();
			if (expected != null)
				expected.close();
		}
	}

	@Test
	public void test_7_blur() throws Exception {
		File config = new File("data/config/blur.xml");
		File outputFile = new File("data/output/blur.nc");
		File expectedFile = new File("data/expected/blur.nc");

		QueryRunner.run(config, outputFile);

		NetcdfFile dataset = null;
		NetcdfFile expected = null;
		try {
			dataset = NetcdfFile.open(outputFile.getPath());
			expected = NetcdfFile.open(expectedFile.getPath());
			Variable vex;
			Variable vac;
			vex = expected.findVariable("Band1");
			vac = dataset.findVariable("Band1");
			assertArray(vex.getDataType(), vex.read(), vac.read());
		} finally {
			if (dataset != null)
				dataset.close();
			if (expected != null)
				expected.close();
		}
	}

	@Test
	public void test_hypercube() throws Exception {
		File config = new File("data/config/hypercube.xml");
		File outputFile = new File("data/output/hypercube.nc");
		File expectedFile = new File("data/expected/hypercube.nc");

		QueryRunner.run(config, outputFile);

		NetcdfFile dataset = null;
		NetcdfFile expected = null;
		try {
			dataset = NetcdfFile.open(outputFile.getPath());
			expected = NetcdfFile.open(expectedFile.getPath());
			Variable vex;
			Variable vac;
			vex = expected.findVariable("Band1");
			vac = dataset.findVariable("Band1");
			assertArray(vex.getDataType(), vex.read(), vac.read());
			vex = expected.findVariable("time");
			vac = dataset.findVariable("time");
			assertArray(vex.getDataType(), vex.read(), vac.read());
		} finally {
			if (dataset != null)
				dataset.close();
			if (expected != null)
				expected.close();
		}
	}

	@Test
	public void test_2D() throws Exception {
		File config = new File("data/config/multiply_2_2d.xml");
		File outputFile = new File("data/output/multiply_2_2d.nc");
		File expectedFile = new File("data/expected/multiply_2_2d.nc");

		QueryRunner.run(config, outputFile);

		NetcdfFile dataset = null;
		NetcdfFile expected = null;
		try {
			dataset = NetcdfFile.open(outputFile.getPath());
			expected = NetcdfFile.open(expectedFile.getPath());
			Variable vex;
			Variable vac;
			vex = expected.findVariable("Band1");
			vac = dataset.findVariable("Band1");
			assertArray(vex.getDataType(), vex.read(), vac.read());
		} finally {
			if (dataset != null)
				dataset.close();
			if (expected != null)
				expected.close();
		}
	}


	/**
	 * Confirm that the contents of two arrays are the same.
	 */
	protected static void assertArray(byte[] expected, Array actual) {
		assertEquals(expected.length, actual.getSize());
		for (int i = 0; i < expected.length; i++) {
			byte ex = expected[i];
			byte ac = actual.getByte(i);
			assertEquals(ex, ac);
		}
	}

	/**
	 * Confirm that the contents of two arrays are the same.
	 */
	protected static void assertArray(int[] expected, Array actual) {
		assertEquals(expected.length, actual.getSize());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.getInt(i));
		}
	}

	private void assertArray(DataType type, Array expected, Array actual) {
		assertEquals(expected.getSize(), actual.getSize());
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
