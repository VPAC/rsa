package org.vpac.ndg.datamodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.TestUtil;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.CoordinateAxis;
import ucar.nc2.dataset.CoordinateSystem;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.ncml.NcMLReader;
import ucar.unidata.geoloc.ProjectionImpl;
import ucar.unidata.geoloc.ProjectionPoint;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
public class AggregateViewTest {

	final private Logger log = LoggerFactory.getLogger(AggregateViewTest.class);

	private Path tempDir;
	private RsaAggregationFactory factory;
	private AggregationOpener opener;
	private List<NetcdfFile> resources;

	@Autowired
	TestUtil testUtil;
	@Autowired
	DatasetDao datasetDao;

	private static final String SECTION = "0:1, 2498:2502, 5998:6002";
	private static final int[] B30_DATA = new int[] {
		-999, -999, -999, -999, -999, // time[0] y[2498]
		-999, -999, -999, -999, -999, // time[0] y[2499]
		-999, -999, -999, -999, -999, // time[0] y[2500]
		-999, -999, -999, -999, -999, // time[0] y[2501]
		-999, -999, -999, -999, -999, // time[0] y[2502]

		2068, 2085, 2102, 2136, 2186, // time[1] y[2498]
		2068, 2102, 2152, 2169, 2136, // time[1] y[2499]
		2017, 2051, 2085, 2102, 2051, // time[1] y[2500]
		1967, 1984, 2017, 2051, 2051, // time[1] y[2501]
		1967, 1950, 2000, 2000, 2068  // time[1] y[2502]
	};

	private static final int[] B40_DATA = new int[] {
		-999, -999, -999, -999, -999, // time[0] y[2498]
		-999, -999, -999, -999, -999, // time[0] y[2499]
		-999, -999, -999, -999, -999, // time[0] y[2500]
		-999, -999, -999, -999, -999, // time[0] y[2501]
		-999, -999, -999, -999, -999, // time[0] y[2502]
	      
		2679, 2597, 2597, 2679, 2720, // time[1] y[2498]
		2597, 2597, 2720, 2679, 2720, // time[1] y[2499]
		2557, 2557, 2597, 2597, 2638, // time[1] y[2500]
		2516, 2557, 2557, 2557, 2638, // time[1] y[2501]
		2475, 2516, 2557, 2557, 2557  // time[1] y[2502]
	};

	private static final String SECTION2 = "0:1, 2500:2504, 4995:4999";
	private static final int[] B30_DATA2 = new int[] {
		-999, -999, -999, -999, -999, // time[0] y[2500]
		-999, -999, -999, -999, -999, // time[0] y[2501]
		-999, -999, -999, -999, -999, // time[0] y[2502]
		2764, 2732, 2764, 2717, 2591, // time[0] y[2503]
		2827, 2874, 2889, 2764, 2575, // time[0] y[2504]

		-999, -999, -999, -999, -999, // time[0] y[2500]
		-999, -999, -999, -999, -999, // time[0] y[2501]
		-999, -999, -999, -999, -999, // time[0] y[2502]
		-999, -999, -999, -999, -999, // time[0] y[2503]
		-999, -999, -999, -999, -999  // time[0] y[2504]
	};

	private static final int[] B40_DATA2 = new int[] {
		-999, -999, -999, -999, -999, // time[0] y[2500]
		-999, -999, -999, -999, -999, // time[0] y[2501]
		-999, -999, -999, -999, -999, // time[0] y[2502]
		3588, 3588, 3552, 3552, 3409, // time[0] y[2503]
		3660, 3660, 3660, 3588, 3481, // time[0] y[2504]

		-999, -999, -999, -999, -999, // time[0] y[2500]
		-999, -999, -999, -999, -999, // time[0] y[2501]
		-999, -999, -999, -999, -999, // time[0] y[2502]
		-999, -999, -999, -999, -999, // time[0] y[2503]
		-999, -999, -999, -999, -999  // time[0] y[2504]
	};

	@Before
	public void setUp() throws Exception {
		testUtil.initialiseDataForExport();
		tempDir = FileUtils.createTmpLocation();
		factory = new RsaAggregationFactory();
		opener = new AggregationOpener();
		resources = new ArrayList<NetcdfFile>();
	}

	/**
	 * Constructs a virtual datacube from the RSA, and serialises it to NCML,
	 * as a file on disk. Then, the NetCDF-Java library is used to read the NCML
	 * and compare a verify a small set of known values.
	 */
	@Test
	public void testWriteNcml() throws Exception {
		Dataset dataset = datasetDao.findDatasetByName(
				testUtil.getExportDatasetName(), testUtil.getExportDatasetRes());

		AggregationDefinition cubeMemento = factory.create(dataset);

		String fileName = String.format("%s.ncml",
				testUtil.getExportDatasetName());
		Path output = tempDir.resolve(fileName);
		cubeMemento.serialise(output);

		NetcdfDataset cube = NcMLReader.readNcML(output.toUri().toString(), null);
		resources.add(cube);

		checkKnownSection(cube);
	}

	/**
	 * Constructs a virtual datacube from the RSA, and compares the contents to
	 * known values (as obtained using ToolsUI). Note that the cube is never
	 * serialised to disk; only read operations are used.
	 */
	@Test
	public void testOpenVirtualDatacube() throws Exception {
		Dataset dataset = datasetDao.findDatasetByName(
				testUtil.getExportDatasetName(), testUtil.getExportDatasetRes());

		AggregationDefinition cubeMemento = factory.create(dataset);

		String location = String.format("%s/%s",
				testUtil.getExportDatasetName(),
				testUtil.getExportDatasetRes().toHumanString());
		NetcdfDataset cube = opener.open(cubeMemento, location);
		resources.add(cube);

		// Debugging info, like ncdump -h
		log.debug("{}", cube);
		checkKnownSection(cube);
	}

	@Test
	public void testOpenVirtualDatacubeOnExportWithMissingTiles() throws Exception {
		String dsName = "missing";
		Dataset ds = testUtil.initialiseDataForExport(dsName, testUtil.getExportDatasetRes(), testUtil.getExportDatasetPath());
		// Make sure one tile is absent.
		Box extents = testUtil.removeOneTile(ds);		

		List<String> bands = new ArrayList<String>();
		bands.add("B30");
		bands.add("B40");
		AggregationDefinition cubeMemento = factory.create(ds, extents,
				null, null, bands);

		String location = String.format("%s/%s", dsName, ds.getResolution().toHumanString());
		NetcdfDataset cube = opener.open(cubeMemento, location);
		resources.add(cube);

		// Debugging info, like ncdump -h
		log.debug("{}", cube);
		checkKnownSectionOnCubeWithMissingTiles(cube);
	}

	@Test
	public void testCoordinateSystem() throws Exception {
		Dataset dataset = datasetDao.findDatasetByName(
				testUtil.getExportDatasetName(), testUtil.getExportDatasetRes());

		AggregationDefinition cubeMemento = factory.create(dataset);
		String location = String.format("%s/%s",
				testUtil.getExportDatasetName(),
				testUtil.getExportDatasetRes().toHumanString());
		NetcdfDataset cube = opener.open(cubeMemento, location);
		resources.add(cube);

		cube.enhance();

		assertEquals("ucar.nc2.dataset.conv.CF1Convention", cube.getConventionUsed());

		// Systems
		assertEquals(1, cube.getCoordinateSystems().size());
		CoordinateSystem cs = cube.getCoordinateSystems().get(0);
		assertEquals("time y x", cs.toString());
		assertTrue(cs.isGeoXY());

		// Axes
		List<CoordinateAxis> cas = cube.getCoordinateAxes();
		assertEquals(3, cas.size());

		// Transforms
		assertEquals(1, cube.getCoordinateTransforms().size());

		// Projection
		ProjectionImpl proj = cs.getProjection();
		log.debug("Projection: {}", proj);
		ProjectionPoint p;
		p = proj.latLonToProj(-10.0, 134.0);
		log.debug("-10.0, 134.0: {}", p);
		p = proj.latLonToProj(-10.0, 155.0);
		log.debug("-10.0, 155.0: {}", p);
	}

	/**
	 * Constructs several virtual datacubes with differing extents.
	 */
	@Test
	public void testExtents() throws Exception {
		Dataset dataset = datasetDao.findDatasetByName(
				testUtil.getExportDatasetName(), testUtil.getExportDatasetRes());
		List<TimeSlice> tss = datasetDao.getTimeSlices(dataset.getId());
		Collections.sort(tss);
		List<Band> bs = datasetDao.getBands(dataset.getId());


		// First cube has natural extends of dataset.
		AggregationDefinition cubeMemento0 = factory.create(dataset);

		// Second cube has extents of upper tile.
		Box extents1 = new Box(810000, -3625000, 935000, -3500000);
		AggregationDefinition cubeMemento1 = factory.create(dataset, tss, bs, extents1);

		// Second cube has extents of lower tile.
		Box extents2 = new Box(935000, -3625000, 1060000, -3500000);
		AggregationDefinition cubeMemento2 = factory.create(dataset, tss, bs, extents2);

		String location = String.format("%s/%s",
				testUtil.getExportDatasetName(),
				testUtil.getExportDatasetRes().toHumanString());

		NetcdfDataset cube0 = opener.open(cubeMemento0, location + "0");
		resources.add(cube0);
		log.debug("cube0 (aggregate): {}", cube0);
		NetcdfDataset cube1 = opener.open(cubeMemento1, location + "1");
		resources.add(cube1);
		// Debugging info, like ncdump -h
		log.debug("cube1 (upper tile): {}", cube1);
		NetcdfDataset cube2 = opener.open(cubeMemento2, location + "2");
		resources.add(cube2);
		// Debugging info, like ncdump -h
		log.debug("cube2 (lower tile): {}", cube2);

		// Make sure Y values are the same for the tiles. There is only one tile
		// in the Y dimension, so there is no need to align data.
		Variable vy0 = cube0.findVariable("y");
		Variable vy1 = cube1.findVariable("y");
		Variable vy2 = cube2.findVariable("y");

		Array ay0 = vy0.read();
		Array ay1 = vy1.read();
		assertArray(ay0, ay1);

		ay0 = vy0.read();
		Array ay2 = vy2.read();
		assertArray(ay0, ay2);

		// Make sure X values are the same for the tiles, taking tile alignment
		// into account.
		Variable vx0 = cube0.findVariable("x");
		Variable vx1 = cube1.findVariable("x");
		Variable vx2 = cube2.findVariable("x");

		Array ax0 = vx0.read("0:4999");
		Array ax1 = vx1.read();
		assertArray(ax0, ax1);

		ax0 = vx0.read("5000:9999");
		Array ax2 = vx2.read();
		assertArray(ax0, ax2);
	}

	@Test
	public void testOpenVirtualDatacubeOutOfBounds() throws Exception {
		Dataset dataset = datasetDao.findDatasetByName(
				testUtil.getExportDatasetName(), testUtil.getExportDatasetRes());
		List<TimeSlice> tss = datasetDao.getTimeSlices(dataset.getId());
		Collections.sort(tss);
		List<Band> bs = datasetDao.getBands(dataset.getId());

		String location = String.format("%s/%s",
				testUtil.getExportDatasetName(),
				testUtil.getExportDatasetRes().toHumanString());

		// Use extents that *begin* outside the bounds of the data (xmin is one
		// cell to the left of the natural extents).
		Box extents = new Box(809975.0, -3625000, 935000, -3500000);
		AggregationDefinition cubeMemento = factory.create(dataset, tss, bs, extents);
		NetcdfDataset cube0 = opener.open(cubeMemento, location + "0");
		resources.add(cube0);
		log.debug("cube0 (aggregate): {}", cube0);

		cube0.enhance();
		try {
			// Expected min value is tile lower-bound plus half a grid cell.
			final double expected = (810000.0 - 5000.0 * 25.0) + 12.5;
			double minXValue = cube0.findCoordinateAxis("x").getMinValue();
			assertEquals(expected, minXValue, 0.01);
		} catch (ArrayIndexOutOfBoundsException e) {
			// Reading data from the cube will fail if it has not been specified
			// properly.
			log.error("Could not read data from datacube; NCML is probably " +
					"poorly defined.");
			throw e;
		}
	}

	protected void checkKnownSection(NetcdfFile dataset) throws IOException,
			InvalidRangeException {

		// Read 10 values from the centre of the data, and check that it is as
		// expected. Note that the section specification is dependent on the
		// tile size. Therefore if the RSA is configured to use a different tile
		// size, this section spec will need to change.
		Variable var;
		Array arr;
		var = dataset.findVariable("B30");
		assertNotNull("Couldn't find variable B30", var);
		arr = var.read(SECTION);
		assertArray(B30_DATA, arr);

		var = dataset.findVariable("B40");
		assertNotNull("Couldn't find variable B40", var);
		arr = var.read(SECTION);
		assertArray(B40_DATA, arr);
	}

	protected void checkKnownSectionOnCubeWithMissingTiles(NetcdfFile dataset) throws IOException,
			InvalidRangeException {

		// Read 10 values from the centre of the data, and check that it is as
		// expected. Note that the section specification is dependent on the
		// tile size. Therefore if the RSA is configured to use a different tile
		// size, this section spec will need to change.
		Variable var;
		Array arr;
		var = dataset.findVariable("B30");
		assertNotNull("Couldn't find variable B30", var);
		arr = var.read(SECTION2);
		assertArray(B30_DATA2, arr);

		var = dataset.findVariable("B40");
		assertNotNull("Couldn't find variable B40", var);
		arr = var.read(SECTION2);
		assertArray(B40_DATA2, arr);
	}
	
	
	/**
	 * Confirm that the contents of two arrays are the same.
	 */
	protected void assertArray(int[] expected, Array actual) {
		assertEquals(expected.length, actual.getSize());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual.getInt(i));
		}
	}
	protected void assertArray(Array expected, Array actual) {
		assertEquals(expected.getSize(), actual.getSize());
		for (int i = 0; i < expected.getSize(); i++) {
			assertEquals(expected.getInt(i), actual.getInt(i));
		}
	}

	@After
	public void tearDown() {
		try {
			FileUtils.removeDirectory(tempDir);
		} catch (IOException e) {
			log.error("Could not delete temporary directory.", e);	
		}
		for (NetcdfFile f : resources) {
			try {
				f.close();
			} catch (IOException e) {
				log.error("Could not close dataset.", e);
			}
		}
	}

}
