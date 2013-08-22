/*
 * This file is part of SpatialCube.
 *
 * SpatialCube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SpatialCube is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SpatialCube.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.vpac.ndg.AppContext;
import org.vpac.ndg.InfrastructureException;
import org.vpac.ndg.Utils;
import org.vpac.ndg.cli.smadaptor.BandConnector;
import org.vpac.ndg.cli.smadaptor.DataCleanup;
import org.vpac.ndg.cli.smadaptor.DataDownloader;
import org.vpac.ndg.cli.smadaptor.DataExport;
import org.vpac.ndg.cli.smadaptor.DataImport;
import org.vpac.ndg.cli.smadaptor.DataQuery;
import org.vpac.ndg.cli.smadaptor.DataUpload;
import org.vpac.ndg.cli.smadaptor.DatasetConnector;
import org.vpac.ndg.cli.smadaptor.Factory;
import org.vpac.ndg.cli.smadaptor.StorageManager;
import org.vpac.ndg.cli.smadaptor.TimesliceConnector;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.BoxInt;
import org.vpac.ndg.geometry.Point;
import org.vpac.ndg.geometry.Tile;
import org.vpac.ndg.geometry.TileManager;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.QueryRuntimeException;
import org.vpac.ndg.query.io.DatasetProvider;
import org.vpac.ndg.query.io.ProviderRegistry;
import org.vpac.ndg.rasterdetails.RasterDetails;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.JobProgress;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.TimeSliceLock;
import org.vpac.ndg.storage.util.TimeSliceUtil;

import com.martiansoftware.nailgun.NGContext;

public class Client {

	final Logger log = LoggerFactory.getLogger(Client.class);

	public final static String USAGE = 
			"rsa <category> <action> [options] [arguments...]\n" +
			"rsa dataset list [options] [KEYWORD]\n" +
			"rsa dataset create [options] [-a] <NAME> <RESOLUTION>\n" +
			"rsa dataset search [NAME/RESOLUTION]\n" +
			"rsa dataset show [--tiles] [--time-extents] [-e] [--cdl] [--ncml] [IDENTIFIER]\n" +
			"rsa dataset update <DATASET_ID> [--name|abstract]\n" +
			"rsa dataset delete <IDENTIFIER>\n" +
			"rsa band list [options] <DATASET_ID>\n" +
			"rsa band create [options] [-cm] [--nodata] [--type] <DATASET_ID> <BAND_NAME>\n" +
			"rsa band update <BAND_ID> [--name] [--continuous] [--metadata] [--type]\n" +
			"rsa band delete <BAND_ID>\n" +
			"rsa timeslice list [options] <DATASET_ID>\n" +
			"rsa timeslice show [--tiles] [-e] [options] <TIMESLICE_ID>\n" +
			"rsa timeslice create [options] [-a] <DATASET_ID> <DATE>\n" +
			"rsa timeslice update <TIMESLICE_ID> [--xmax|xmin|ymax|ymin] [--abstract] [--acquisitiontime] \n" +
			"rsa timeslice delete <TIMESLICE_ID/DATASET_ID> [--time-extents]\n" +
			//"rsa data upload [options] [UPLOAD ID] [SEQUENCE NUM] <FILE...>\n" +
			"rsa data import [options] [-r] [--srcnodata] [--async] <TIMESLICE_ID> <BAND_ID> <PRIMARY FILE> [FILE...]\n" +
			"rsa data export [options] [-r] [-obet] [--async] <DATASET_ID>\n" +
			"rsa data query [options] [-obet] [--of] [--async] <QUERY_DEF_FILE>\n" +
			"rsa data download [options] [o] <TASK_ID>\n" +
			"rsa data task [options] [--monitor] [TASK_ID]\n" +
			"rsa data cleanup [options] [--purge]";
//			"rsa help <CATEGORY>\n";

	public final static String HEADER =
			"Command line client for the Raster Storage Archive (RSA).";

	CommandLine cmd;
	StorageManager sm;
	Path workingDirectory;

	TimeSliceUtil timeSliceUtil;
	TileManager tileManager;

	
	public Client(Path workingDirectory) {
		log.debug("client starting");
		this.workingDirectory = workingDirectory.toAbsolutePath();
	}

	/**
	 * The application context should only be initialised once EVER - otherwise
	 * you get resource leaks (e.g. extra open sockets) when using something
	 * like Nailgun. The use of the enum here ensures this. The context acquired
	 * here is passed automatically to {@link AppContext} in the Storage
	 * Manager for use by other parts of the RSA.
	 */
	private static enum AppContextSingleton {
		INSTANCE;

		public ApplicationContext appContext;

		private AppContextSingleton() {
			appContext = new ClassPathXmlApplicationContext(
					new String[] {"spring/beans/CmdClientBean.xml"});
		}
	}

	public void initBeans() {
		log.debug("initialising Spring and storage manager connectors");
		ApplicationContext appContext = AppContextSingleton.INSTANCE.appContext;
		log.trace("retrieved app context");

		if(cmd.hasOption("remote"))
			sm = Factory.create(cmd.getOptionValue("remote"), appContext);
		else
			sm = Factory.create("", appContext);
		log.trace("connector initialised");


		timeSliceUtil = (TimeSliceUtil)appContext.getBean("timeSliceUtil");
		tileManager = (TileManager)appContext.getBean("tileManager");

		DatasetProvider dataProvider = (DatasetProvider)appContext.getBean("dataProvider");
		ProviderRegistry.getInstance().clearProivders();
		ProviderRegistry.getInstance().addProivder(dataProvider);
		log.debug("initialisation complete");
	}

	public void execute(String... args) {
		processArgs(args);
		log.trace("aguments processed");
		run();
		log.debug("client finished");
	}

	/**
	 * Cause Java to exit. This is provided here so it can be overridden in a
	 * subclass.
	 * @param errcode The error code: 0 for success, non-zero for error.
	 */
	protected void exit(int errcode) {
		log.debug("client exiting");
		System.exit(errcode);
	}

	/**
	 * Standard Java entry point. When running under Nailgun,
	 * {@link #nailMain(NGContext)} will be called instead.
	 */
	public static void main(String... args) {
		Path workingDirectory = Paths.get(".");
		Client cli = new Client(workingDirectory);
		cli.execute(args);
	}

	/**
	 * This method is called by the Nailgun server, instead of
	 * {@link #main(String...)}, when launching via Nailgun. This is needed to
	 * get the right working directory. If this method wasn't provided,
	 * {@link #main(String...)} would be called instead.
	 */
	public static void nailMain(NGContext context) {
		Path workingDirectory = Paths.get(context.getWorkingDirectory());
		Client cli = new Client(workingDirectory);
		cli.execute(context.getArgs());
	}

	/**
	 * List the datasets in the RSA.
	 */
	public void listDatasets() {
		log.info("Listing datasets.");

		DatasetConnector dsc = sm.getDatasetConnector();
		List<Dataset> list = dsc.list();
		for (Dataset ds : list) {
			printDataset(ds);
		}
	}

	public void searchDataset(List<String> remainingArgs) {
		log.info("Searching dataset.");

		String name = null;
		String res = null;
		CellSize resolution = null;
		// Name
		try {
			name = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
		}

		// Resolution
		try {
			// Check if the second keyword is resolution
			res = remainingArgs.get(1);
			resolution = CellSize.fromHumanString(res.trim());
		} catch (IndexOutOfBoundsException e) {
		} catch (IllegalArgumentException e) {
			throw e;
		}

		// If no keyword to search, then list all datasets
		if(name == null && resolution == null) {
			listDatasets();
		}

		if(name != null && resolution == null) {
			try {
				// Check if the first keyword is resolution
				resolution = CellSize.fromHumanString(name.trim());
				name = null;
			} catch (IllegalArgumentException e) {
			}
		}

		DatasetConnector dsc = sm.getDatasetConnector();
		log.debug("fetching metadata");
		List<Dataset> list = dsc.searchDataset(name, resolution);
		log.trace("printing");
		for (Dataset ds : list) {
			printDataset(ds);
		}
	}

	public void showDataset(List<String> remainingArgs) throws IOException {
		log.info("Showing dataset details.");

		String id;
		try {
			id = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Dataset ID not specified.");
		}

		// Spatial extents specified by user
		Box extents = null;
		try {
			if (cmd.hasOption("e")) {
				String[] extentsArr = cmd.getOptionValues("e");
				extents = new Box(Double.parseDouble(extentsArr[0]),
								Double.parseDouble(extentsArr[1]),
								Double.parseDouble(extentsArr[2]),
								Double.parseDouble(extentsArr[3]));
			}
		} catch (IndexOutOfBoundsException | NumberFormatException e) {
			throw new IllegalArgumentException("Spatial extents invalid.");
		}
		
		// Spatial extents specified by user
		Date startDate = null;
		Date endDate = null;
		try {
			if (cmd.hasOption("time-extents")) {
				String[] extentsArr = cmd.getOptionValues("time-extents");
				startDate = Utils.parseDate(extentsArr[0]);
				endDate = Utils.parseDate(extentsArr[1]);
			}
		} catch (IndexOutOfBoundsException | IllegalArgumentException e) {
			throw new IllegalArgumentException("Time date extents are invalid.");
		}
		
		if (cmd.hasOption("tiles")) {
			printTileInfo(id, extents, startDate, endDate);
			return;
		}
		
		DatasetConnector dsc = sm.getDatasetConnector();
		if (cmd.hasOption("cdl")) {
			// Write CDL output and return.
			System.out.println(dsc.getCdl(id, extents, startDate, endDate));
			return;
		} else if (cmd.hasOption("ncml")) {
			System.out.println(dsc.getNcml(id, extents, startDate, endDate));
			return;
		}


		TimesliceConnector tsc = sm.getTimesliceConnector();
		BandConnector bsc = sm.getBandConnector();

		log.debug("fetching metadata");
		Dataset ds = dsc.getDataset(id);
		if (ds == null) {
			throw new IllegalArgumentException("Dataset not found.");
		}

		List<Band> bs = bsc.list(ds.getId());
		List<TimeSlice> tss = tsc.list(ds.getId());
		log.trace("printing");

		// Overview
		printDataset(ds);
		System.out.format("    This dataset contains %d bands and %d time slices.\n",
				bs.size(), tss.size());
		System.out.format("    Location: %s\n", dsc.getLocation(id));

		// Spatial extents
		Box bounds = timeSliceUtil.aggregateBounds(tss);
		double xres = 0.0;
		double yres = 0.0;
		if (bounds != null) {
			System.out.format("    Spatial bounds (x1 y1 x2 y2): %.8g %.8g %.8g %.8g\n",
					bounds.getXMin(), bounds.getYMin(),
					bounds.getXMax(), bounds.getYMax());
			xres = bounds.getWidth() / ds.getResolution().toDouble();
			yres = bounds.getHeight() / ds.getResolution().toDouble();
		} else {
			System.out.println("    Null spatial extents; this dataset contains no data.");
		}
		
		System.out.format("    Image shape (x y):            %d %d\n",
				(int)xres, (int)yres);

		// Temporal extents
		TimeSlice first = null, last = null;
		for (TimeSlice ts : tss) {
			if (first == null) {
				first = ts;
				last = ts;
				continue;
			}
			if (ts.getCreated().before(first.getCreated()))
				first = ts;
			if (ts.getCreated().after(last.getCreated()))
				last = ts;
		}
		if (first != null) {
			DateFormat formatter = Utils.getTimestampFormatter();
			System.out.format("    Temporal extents (min max):   %s %s\n",
					formatter.format(first.getCreated()),
					formatter.format(last.getCreated()));
		}
		
		if (extents != null) {
			boolean bIntersect = bounds.intersects(extents); 

			System.out.format("\n    Given bounds (x1 y1 x2 y2):   %.8g %.8g %.8g %.8g\n",
					extents.getXMin(), extents.getYMin(),
					extents.getXMax(), extents.getYMax());
			System.out.format("    Given bounds intersect this dataset spatial bounds? %s\n", bIntersect ? "yes" : "no");
		}
	}

	private void printTileInfo(String identifier, Box extents, Date startDate, Date endDate) {
		DatasetConnector dsc = sm.getDatasetConnector();
		Dataset ds = dsc.get(identifier);
		if (ds == null) {
			throw new IllegalArgumentException("Dataset not found.");
		}

		TimesliceConnector tsc = sm.getTimesliceConnector();
		List<TimeSlice> slices = tsc.list(ds.getId());
		Map<Point<Integer>, Tile> list = new HashMap<Point<Integer>, Tile>();

		SimpleDateFormat format = new SimpleDateFormat();
		format.setTimeZone(TimeZone.getTimeZone("UTC"));

		for (TimeSlice ts : slices) {
			if (filterByTimeExtents(ts, startDate, endDate)) {
				BoxInt globalTileBounds = tileManager.mapToTile(ts.getBounds(),
						ds.getResolution());
				List<Tile> tiles = tileManager.getTiles(globalTileBounds);
				for (Tile tile : tiles) {
					if (!list.containsKey(tile.getIndex())) {
						if (filterByExtents(tile, extents, ds.getResolution()))
							list.put(tile.getIndex(), tile);
					}
				}
			}
		}

		Comparator<Point<Integer>> comparator = new Comparator<Point<Integer>>() {
			@Override
			public int compare(Point<Integer> o1, Point<Integer> o2) {
				if (o1.getX() > o2.getX()) {
					return 1;
				} else if (o1.getX() == o2.getX()) {
					if(o1.getY() < o2.getY())
						return 1;
					else
						return -1;
				}
				return  -1;
			}
		};

		List<Point<Integer>> sortedPoint = new ArrayList<Point<Integer>>();
		for (Point<Integer> index : list.keySet())
			sortedPoint.add(index);
		Collections.sort(sortedPoint, comparator);

		System.out.println("** Tile extents **");
		System.out.println("index\txmin\tymin\txmax\tymax");
		for (Point<Integer> index : sortedPoint) {
			Tile tile = list.get(index);
			Box bound = tileManager.getNngGrid().getBounds(tile.getIndex(), ds.getResolution());
			System.out.println(String.format("%d,%d\t%7.2f\t%7.2f\t%7.2f\t%7.2f", tile.getX(), tile.getY(), bound.getUlCorner().getX(), bound.getLrCorner().getY(), bound.getLrCorner().getX(), bound.getUlCorner().getY()) );
		}
	}

	private void printTileBandInfo(Dataset ds, TimeSlice ts, Box extents) {
		Map<Point<Integer>, List<TileBand>> list = new HashMap<Point<Integer>, List<TileBand>>();

		SimpleDateFormat format = new SimpleDateFormat();
		format.setTimeZone(TimeZone.getTimeZone("UTC"));

		BoxInt globalTileBounds = tileManager.mapToTile(ts.getBounds(),
				ds.getResolution());
		if(extents != null) {
			BoxInt requestedTileBound = tileManager.mapToTile(extents, ds.getResolution());
			globalTileBounds.intersect(requestedTileBound);
		}
		List<Tile> tiles = tileManager.getTiles(globalTileBounds);
		List<Band> bands = sm.getBandConnector().list(ds.getId());

		for(Tile tile : tiles) {
			List<TileBand> tileBands = new ArrayList<TileBand>();
			for(Band band : bands) {
				TileBand tb = new TileBand(tile, band, ts);
				if (tb.existsInStoragepool()) {
					tileBands.add(tb);
				}
			}
			if(tileBands.size() > 0)
				list.put(tile.getIndex(), tileBands);
		}

		Comparator<Point<Integer>> comparator = new Comparator<Point<Integer>>() {
			@Override
			public int compare(Point<Integer> o1, Point<Integer> o2) {
				if (o1.getX() > o2.getX()) {
					return 1;
				} else if (o1.getX() == o2.getX()) {
					if (o1.getY() < o2.getY())
						return 1;
					else
						return -1;
				}
				return  -1;
			}
		};

		List<Point<Integer>> sortedPoint = new ArrayList<Point<Integer>>();
		for (Point<Integer> index : list.keySet())
			sortedPoint.add(index);
		Collections.sort(sortedPoint, comparator);

		System.out.println("index\txmin\tymin\txmax\tymax\tbands");
		for (Point<Integer> index : sortedPoint) {
			List<TileBand> tileBands = list.get(index);
			Tile tile = tileBands.get(0).getTile();
			Box bound = tileManager.getNngGrid().getBounds(tile.getIndex(), ds.getResolution());
			System.out.print(String.format("%d,%d\t%7.2f\t%7.2f\t%7.2f\t%7.2f", tile.getX(), tile.getY(), bound.getUlCorner().getX(), bound.getLrCorner().getY(), bound.getLrCorner().getX(), bound.getUlCorner().getY()) );
			List<String> bandNames = new ArrayList<String>();
			for (TileBand tb : tileBands) {
				bandNames.add(tb.getBand().getName());
			}
			System.out.print(String.format("\t%s",org.springframework.util.StringUtils.arrayToCommaDelimitedString(bandNames.toArray())));
			System.out.println();
		}
	}


	public boolean filterByExtents(Tile tile, Box extents, CellSize resoulution) {
		if (extents == null)
			return true;
		else if (extents.intersects(tileManager.getNngGrid().getBounds(tile.getIndex(), resoulution)))
			return true;
		return false;
	}

	public boolean filterByTimeExtents(TimeSlice ts, Date startDate, Date endDate) {
		if (startDate == null && endDate == null)
			return true;
		else if (ts.getCreated().compareTo(startDate) >= 0 && ts.getCreated().compareTo(endDate) <= 0)
			return true;
		return false;
	}

	/**
	 * Create a new dataset.
	 * @param remainingArgs Name, abstract, resoltion, domain type.
	 */
	public void createDataset(List<String> remainingArgs) {
		log.info("Creating a dataset.");

		String name;
		String abs;
		String resolution;
		String precision;
		// Name
		try {
			name = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Dataset name not specified.");
		}
		// Resolution
		try {
			resolution = remainingArgs.get(1);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Resolution not specified.");
		}

		if(name.trim().isEmpty()) {
			// Capture if dataset name not specified
			throw new IllegalArgumentException("Dataset name not specified.");			
		}

		if(resolution.trim().isEmpty()) {
			// Capture if resolution not specified
			throw new IllegalArgumentException("Resolution not specified.");			
		}

		// Abstract
		if (cmd.hasOption('a')) {
			abs = cmd.getOptionValue('a');
		} else {
			abs = "";
		}
		// Precision
		if (cmd.hasOption('p')) {
			precision = cmd.getOptionValue('p');
		} else {
			precision = "1 day";
		}

		DatasetConnector dsc = sm.getDatasetConnector();
		log.debug("creating dataset");
		String id = dsc.create(name, abs, resolution, precision);
		log.trace("printing");
		System.out.println(id);
	}

	/**
	 * Delete dataset from database and all dataset tiles from storagepool.
	 * @param remainingArgs dataset ID.
	 * @throws IOException Error when deleting dataset.
	 */
	public void deleteDataset(List<String> remainingArgs) throws IOException {
		log.info("Deleting a dataset.");

		String id;
		try {
			id = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Dataset ID not specified.");
		}

		DatasetConnector dsc = sm.getDatasetConnector();
		log.debug("deleting dataset");
		try {
			dsc.delete(id);
		} catch (IOException e) {
			log.error("Could not delete dataset: {}", e.getMessage());
			throw e;
		}
		System.out.println(String.format("Dataset: %s has been deleted.", id));
	}

	protected void printDataset(Dataset ds) {
		System.out.println(String.format("%s %s/%s",
				ds.getId(), ds.getName(),
				ds.getResolution().toHumanString()));
	}

	/**
	 * List time slices.
	 * @param remainingArgs dataset ID.
	 */
	public void listTimeSlices(List<String> remainingArgs) {
		log.info("Listing time slices.");

		String id;
		try {
			id = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Dataset ID not specified.");
		}

		DatasetConnector dsc = sm.getDatasetConnector();
		log.debug("fetching metadata");
		Dataset ds = dsc.getDataset(id);
		if (ds == null) {
			throw new IllegalArgumentException("Dataset not found.");
		}

		TimesliceConnector tsc = sm.getTimesliceConnector();
		List<TimeSlice> tslist = tsc.list(ds.getId());

		log.trace("printing");
		for (TimeSlice ts : tslist) {
			printTimeSlice(ts);
		}
	}

	/**
	 * List time slices.
	 * @param remainingArgs dataset ID.
	 */
	public void showTimeSlice(List<String> remainingArgs) {
		log.info("Showing time slice.");

		String id;
		try {
			id = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Time slice ID not specified.");
		}

		// Spatial extents specified by user
		Box extents = null;
		try {
			if (cmd.hasOption("e")) {
				String[] extentsArr = cmd.getOptionValues("e");
				extents = new Box(Double.parseDouble(extentsArr[0]),
								Double.parseDouble(extentsArr[1]),
								Double.parseDouble(extentsArr[2]),
								Double.parseDouble(extentsArr[3]));
			}
		} catch (IndexOutOfBoundsException | NumberFormatException e) {
			throw new IllegalArgumentException("Spatial extents invalid.");
		}
				
		TimesliceConnector tsc = sm.getTimesliceConnector();
		log.debug("fetching metadata");
		TimeSlice ts = tsc.get(id);
		Dataset ds = tsc.getDataset(id);

		
		if (ts == null) {
			throw new IllegalArgumentException("Dataset not found.");
		}
		
		if (cmd.hasOption("tiles")) {
			printTileBandInfo(ds, ts, extents);
			return;
		}
		
		List<TimeSliceLock> tsls = tsc.listLocks(id);

		log.trace("printing");
		printTimeSlice(ts);

		System.out.format("    Child of: %s/%s\n", ds.getName(),
				ds.getResolution().toHumanString());
		System.out.format("    Location: %s\n", tsc.getLocation(id));
		System.out.format("    Spatial bounds (x1 y1 x2 y2): %.8g %.8g %.8g %.8g\n",
				ts.getXmin(), ts.getYmin(),
				ts.getXmax(), ts.getYmax());

		if (ts.getLockCount() > 0) {
			if (ts.getLockMode() == 'w')
				System.out.println("    1 write lock");
			else if (ts.getLockCount() == 1)
				System.out.println("    1 read lock");
			else
				System.out.format("    %d readlocks\n", ts.getLockCount());

			for (TimeSliceLock tsl : tsls) {
				System.out.format("        %s by %s\n", tsl.getOperation(),
						tsl.getUser());
			}
		} else {
			System.out.println("    0 locks");
		}
	}

	/**
	 * Create a new time slice.
	 * @param remainingArgs dataset ID, date.
	 */
	public void createTimeSlice(List<String> remainingArgs) {
		log.info("Creating a time slice.");

		String dsid;
		String date;
		String abs;
		try {
			dsid = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Dataset ID not specified.");
		}
		try {
			date = remainingArgs.get(1);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Date not specified.");
		}

		if(dsid.trim().isEmpty()) {
			// Capture if dataset ID not specified
			throw new IllegalArgumentException("Dataset ID not specified.");			
		}

		if(date.trim().isEmpty()) {
			// Capture if dataset ID not specified
			throw new IllegalArgumentException("Creation date not specified.");			
		}

		// Abstract
		if (cmd.hasOption('a')) {
			abs = cmd.getOptionValue('a');
		} else {
			abs = "";
		}

		DatasetConnector dsc = sm.getDatasetConnector();
		log.debug("fetching metadata");
		Dataset ds = dsc.getDataset(dsid);
		if (ds == null) {
			throw new IllegalArgumentException("Dataset not found.");
		}

		TimesliceConnector tsc = sm.getTimesliceConnector();
		TimeSlice ts = tsc.create(ds.getId(), date, abs);

		log.trace("printing");
		printTimeSlice(ts);
	}

	public void printTimeSlice(TimeSlice ts) {
		DateFormat formatter = Utils.getTimestampFormatter();
		String acquisitionTime = formatter.format(ts.getCreated());
		String lockInfo;
		if (ts.getLockCount() == null || ts.getLockCount() == 0)
			lockInfo = "unlocked";
		else
			lockInfo = "locked";
		System.out.println(String.format("%s %s %s", ts.getId(),
				acquisitionTime, lockInfo));
	}

	/**
	 * List time slices.
	 * @param remainingArgs dataset ID.
	 */
	public void listBands(List<String> remainingArgs) {
		log.info("Listing bands.");

		String id;
		try {
			id = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Dataset ID not specified.");
		}

		DatasetConnector dsc = sm.getDatasetConnector();
		log.debug("fetching metadata");
		Dataset ds = dsc.getDataset(id);
		if (ds == null) {
			throw new IllegalArgumentException("Dataset not found.");
		}

		BandConnector bc = sm.getBandConnector();
		List<Band> bandlist = bc.list(ds.getId());

		log.trace("printing");
		for (Band band : bandlist) {
			printBand(band);
		}
	}

	/**
	 * Create a new time slice.
	 * @param remainingArgs dataset ID, date.
	 */
	public void createBand(List<String> remainingArgs) {
		log.info("Creating a band.");

		String dsid;
		String name;
		String isContinuous = "false";
		String isMetadata = "false";
		try {
			dsid = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Dataset ID not specified.");
		}
		try {
			name = remainingArgs.get(1);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Band name not specified.");
		}

		if(dsid.trim().isEmpty()) {
			// Capture if dataset ID not specified
			throw new IllegalArgumentException("Dataset ID not specified.");			
		}

		if(name.trim().isEmpty()) {
			// Capture if band name not specified
			throw new IllegalArgumentException("Band name not specified.");						
		}

		/** Whether the domain is continuous (e.g. float) not categorical.
		  * This will affect how the data is interpolated.*/
		if (cmd.hasOption('c')) {
			isContinuous = "true";
		}

		/** Whether the data is metadata band or non-metadata band. */		
		if (cmd.hasOption('m')) {
			isMetadata = "true";
		}

		/** Whether the nodata is specified. */
		String nodata = null;
		if (cmd.hasOption("nodata")) {
			nodata = cmd.getOptionValue("nodata");
		}

		String datatype = null;
		if (cmd.hasOption("type")) {
			datatype = cmd.getOptionValue("type");
		}

		DatasetConnector dsc = sm.getDatasetConnector();
		log.debug("fetching metadata");
		Dataset ds = dsc.getDataset(dsid);
		if (ds == null) {
			throw new IllegalArgumentException("Dataset not found.");
		}

		BandConnector bc = sm.getBandConnector();
		log.debug("creating band");
		Band band = bc.createBand(ds.getId(), name, datatype, nodata,
				isContinuous, isMetadata);

		log.trace("printing");
		printBand(band);
	}

	public void printBand(Band band) {
		String meta;
		if (band.isMetadata())
			meta = "metadata";
		else
			meta = "data";

		String domain;
		if (band.isContinuous())
			domain = "continuous";
		else
			domain = "discrete";

		if (band.getNodata() == null || band.getType() == null) {
			System.out.println(String.format("%s %s %s %s (no data imported yet)",
					band.getId(), band.getName(), domain, meta));
		} else {
			System.out.println(String.format("%s %s %s %s %s nodata:%s",
					band.getId(), band.getName(), domain, meta, band.getType().toString(), band.getNodata()));
		}
	}

	/**
	 * Upload files, then run the import tool.
	 * 
	 * @param remainingArgs
	 *            The files to import. The first file is the primary file; the
	 *            others are supporting files (optional).
	 * @throws TaskException 
	 * @throws TaskInitialisationException 
	 */
	public void importData(List<String> remainingArgs)
			throws TaskInitialisationException, TaskException {

		log.info("Importing data.");

		validateAsync();

		String srcnodata = null;
		String timeSliceId;
		String bandId;
		List<String> files;
		try {
			timeSliceId = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Timeslice ID not specified.");
		}

		try {
			bandId = remainingArgs.get(1);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Band not specified.");
		}

		if(timeSliceId.trim().isEmpty()) {
			// Capture if timeslice ID not specified
			throw new IllegalArgumentException("Timeslice ID not specified.");			
		}

		if(bandId.trim().isEmpty()) {
			// Capture if band ID not specified
			throw new IllegalArgumentException("Band ID not specified.");			
		}

		if (cmd.hasOption("srcnodata")) {
			srcnodata = cmd.getOptionValue("srcnodata");
		}

		Boolean bilinear = getUseBilinear();

		files = remainingArgs.subList(2, remainingArgs.size());

		// First, upload to the staging area.
		DataUpload uploader = sm.getDataUploader();
		uploader.setTimeSlice(timeSliceId);

		for (String f : files) {
			if (f.trim().isEmpty())
				continue;
			Path path = Paths.get(f);
			path = workingDirectory.resolve(path);
			uploader.addInput(path);
		}

		String uploadId;
		try {
			log.debug("uploading");
			uploadId = uploader.upload();
		} catch (IOException | IllegalArgumentException e) {
			log.error("Could not upload data: {}", e.getMessage());
			exit(1);
			return;
		}

		// Now run the import.
		DataImport importer = sm.getDataImporter();
		importer.setUploadId(uploadId);
		importer.setBand(bandId);
		importer.setUseBilinearInterpolation(bilinear);
		if (srcnodata != null && !srcnodata.isEmpty()) {
			importer.setSrcnodata(srcnodata);
		}

		importer.setRemainingArgs(remainingArgs);
		log.debug("importing");
		String taskId = importer.start();
		if (cmd.hasOption("async")) {
			printTask(taskId);
		} else {
			printTaskInline(taskId);
		}
	}

	private void validateAsync() {
		if (cmd.hasOption("async") && !cmd.hasOption("remote")) {
			throw new IllegalArgumentException(
					"--async can only be specified with --remote");
		}
	}

	private void printTaskInline(String taskId)
			throws TaskInitialisationException, TaskException {
		JobProgress task = null;
		try {
			do {
				task = sm.getTaskConnector().get(taskId);

				if (task.getState() == TaskState.INITIALISATION_ERROR) {
					throw new TaskInitialisationException(String.format(
							"Task failed to start: %s", task.getErrorMessage()));
				} else if (task.getState() == TaskState.EXECUTION_ERROR) {
					throw new TaskException(String.format(
							"Task failed to complete: %s", task.getErrorMessage()));
				}

				System.out.print(String.format("\r%s %d/%d %5.0f%% %s",
						task.getId(), task.getCurrentStep(),
						task.getNumberOfSteps(), task.getCurrentStepProgress(),
						task.getStepDescription()));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					log.warn("Interrupted while waiting for next progress update.");
				}
			} while (task != null && task.getCurrentStepProgress() < 100);
		} finally {
			// Move to new line (print statement above does not).
			System.out.println();
		}
	}

	private Boolean getUseBilinear() {
		Boolean bilinear = null;
		if (cmd.hasOption("r")) {
			String value = cmd.getOptionValue("r");
			if (value.equals("near"))
				bilinear = false;
			else if (value.equals("bilinear"))
				bilinear = true;
			else
				throw new IllegalArgumentException(String.format("Unrecognised interpolation type %s", value));
		}
		return bilinear;
	}

	/**
	 * Export a section of data from the storage pool.
	 * @param remainingArgs dataset ID
	 * @throws TaskInitialisationException
	 * @throws TaskException
	 * @throws IOException 
	 */
	public void exportData(List<String> remainingArgs)
			throws TaskInitialisationException, TaskException, IOException {

		log.info("Exporting data.");

		DataExport exporter = sm.getDataExporter();

		validateAsync();

		// Dataset ID
		String dsid;
		try {
			dsid = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Dataset ID not specified.");
		}

		if(dsid.trim().isEmpty()) {
			// Capture if dataset ID not specified
			throw new IllegalArgumentException("Dataset ID not specified.");			
		}

		DatasetConnector dsc = sm.getDatasetConnector();
		Dataset ds = dsc.getDataset(dsid);
		if (ds == null) {
			throw new IllegalArgumentException("Dataset not found.");
		}

		exporter.setDatasetId(ds.getId());

		// Spatial extents
		try {
			if (cmd.hasOption("e")) {
				String[] extents = cmd.getOptionValues("e");
				exporter.setExtents(extents[0], extents[1], extents[2], extents[3]);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Spatial extents invalid.");
		}

		// Temporal extents
		try {
			if (cmd.hasOption("t")) {
				String[] extents = cmd.getOptionValues("t");
				exporter.setTimeRange(extents[0], extents[1]);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Temporal extents invalid.");
		}

		// Band selection
		if (cmd.hasOption("b")) {
			String bandnames = cmd.getOptionValue("b");
			String[] bandnamesArr = bandnames.split(","); 
			List<String> bandNamesFilter = new ArrayList<String>();
			for(String bandname: bandnamesArr) {
				if(bandname.trim().isEmpty()) {
					continue;
				}
				bandNamesFilter.add(bandname);
			}
			if(bandNamesFilter.size() == 0) {
				throw new IllegalArgumentException("Band names invalid.");					
			}
			exporter.setBandNamesFilter(bandNamesFilter);
		}

		exporter.setUseBilinearInterpolation(getUseBilinear());

		String defaultFileName = String.format("%s_%s.zip", ds.getName(), ds.getResolution().toString());
		Path outputFilePath = getFileLocation(defaultFileName);

		log.debug("starting export");
		String taskId = exporter.start();
		if (cmd.hasOption("async")) {
			printTask(taskId);
		} else {
			printTaskInline(taskId);
			JobProgress task = sm.getTaskConnector().get(taskId);
			if(task.getState() == TaskState.FINISHED)
				sm.getDataDownloader().Download(taskId, outputFilePath);
		}
	}

	private Path getFileLocation(String defaultFileName) {
		Path outputDir;
		String outputFile;
		if (cmd.hasOption("o")) {
			Path f = Paths.get(cmd.getOptionValue("o")).toAbsolutePath();
			if (Files.isDirectory(f)) {
				outputDir = f;
				outputFile = null;
			} else {
				outputDir = f.getParent();
				outputFile = f.getFileName().toString();
			}
		} else {
			outputDir = workingDirectory;
			outputFile = null;
		}

		Path outputFilePath;
		if (outputFile == null) {
			outputFilePath = outputDir.resolve(defaultFileName);
		} else {
			outputFilePath = outputDir.resolve(outputFile);
		}
		return outputFilePath;
	}

	/**
	 * Querying data from the storage pool.
	 * @param remainingArgs query file name
	 * @throws TaskInitialisationException
	 * @throws TaskException
	 * @throws IOException 
	 * @throws QueryConfigurationException 
	 */
	public void queryData(List<String> remainingArgs) throws IOException,
			TaskInitialisationException, TaskException {

		log.info("Querying data.");

		DataQuery queryRunner = sm.getDataQuery();

		validateAsync();

		// Dataset ID
		Path queryPath;
		try {
			queryPath = workingDirectory.resolve(remainingArgs.get(0));
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Dataset ID not specified.");
		}
		if (!Files.isRegularFile(queryPath) || !Files.isReadable(queryPath)) {
			log.warn("Query definition \"{}\" does not appear to be readable.",
					queryPath);
		}
		queryRunner.setQueryPath(queryPath);

		// Spatial extents
		try {
			if (cmd.hasOption("e")) {
				String[] extents = cmd.getOptionValues("e");
				queryRunner.setExtents(extents[0], extents[1], extents[2], extents[3]);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Spatial extents invalid.");
		}

		// Temporal extents
		try {
			if (cmd.hasOption("t")) {
				String[] extents = cmd.getOptionValues("t");
				queryRunner.setTimeRange(extents[0], extents[1]);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Temporal extents invalid.");
		}

		// Multithreading
		if (cmd.hasOption("threads")) {
			String threads = cmd.getOptionValue("threads");
			queryRunner.setThreads(threads);
		}

		// Output format
		if (cmd.hasOption("of"))
			queryRunner.setNetcdfVersion(cmd.getOptionValue("of"));

		String defaultFileName = queryPath.getFileName().toString();
		defaultFileName += ".nc";
		Path outputFile = getFileLocation(defaultFileName);

		queryRunner.setOutputPath(outputFile);
		log.debug("running query");	
		String taskId = queryRunner.start();	
		if (cmd.hasOption("async")) {
			printTask(taskId);
		} else {
			printTaskInline(taskId);
			JobProgress task = sm.getTaskConnector().get(taskId);
			if (task.getState() == TaskState.FINISHED) {
				sm.getDataDownloader().Download(taskId, outputFile);
			}
		}
	}

	public void downloadData(List<String> remainingArgs) {
		log.info("Downloading data.");
		DataDownloader dc = sm.getDataDownloader();
		String taskId = remainingArgs.get(0);
		Path output = null;
		if(cmd.hasOption("o"))
			output = Paths.get(cmd.getOptionValue("o"));
		else
			output = Paths.get(".");
		dc.Download(taskId, output);
	}

	public void cleanup(List<String> remainingArgs) {
		log.info("Cleaning up old tasks.");

		boolean force = cmd.hasOption("purge");

		DataCleanup dc = sm.getDataCleanup();
		log.debug("cleaning up");
		int numCleaned = dc.cleanLocks(force);
		System.out.format("Cleaned up %d stale (previously locked) tasks.\n",
				numCleaned);
	}

	public void listTasks(List<String> remainingArgs)
			throws TaskInitialisationException, TaskException {
		log.info("Listing tasks.");

		if (remainingArgs.size() > 0) {
			// Just get the specified task
			String taskId = remainingArgs.get(0);
			if(cmd.hasOption("monitor")) {
				printTaskInline(taskId);
			} else {
				printTask(taskId);
			}

		} else {
			// Print all tasks that match the search criteria
			String type;
			String status;

			if (cmd.hasOption("y"))
				type = cmd.getOptionValue("y");
			else
				type = null;

			if (cmd.hasOption("s"))
				status = cmd.getOptionValue("s");
			else
				status = TaskState.RUNNING.toString();

			List<JobProgress> tasks = sm.getTaskConnector().list(type, status);
			for (JobProgress task : tasks) {
				printTask(task.getId());
			}
		}
	}

	public void printTask(String taskId) {
		JobProgress task = sm.getTaskConnector().get(taskId);
		System.out.print(String.format("%s %d/%d %5.0f%% %s",
				task.getId(), task.getCurrentStep(), task.getNumberOfSteps(), task.getCurrentStepProgress(),
				task.getStepDescription()));

		boolean error = false;
		if (task.getState() == TaskState.INITIALISATION_ERROR)
			error = true;
		else if (task.getState() == TaskState.EXECUTION_ERROR)
			error = true;

		if (error) {
			System.out.println(String.format("\t%s: %s", task.getState(),
					task.getErrorMessage()));
		} else {
			System.out.println(String.format("\t%s", task.getState()));
		}
	}
	
	@SuppressWarnings("static-access")
	public void processArgs(String... args) {
		Options options;

		options = new Options();
		//options.addOption("p", "page", true, "List page number (default 1)");
		//options.addOption("e", "express", false, "Export and download synchronously (may fail for large extents)");
		options.addOption("a", "abstract", true, "Abstract (free-form text).");
		options.addOption("c", "continuous", false, "Indicates that the " +
				"domain is continuous, i.e. not discrete.");
		options.addOption("r", "resample", true, "Resampling method to use " +
				"(near or bilinear). If not specified, bilinear will be used " +
				"for continuous data and near will be used for discrete; see " +
				"-c.");
		options.addOption(null, "purge", false,
				"Destroys all locks, even if they haven't expired.");
		options.addOption("m", "metadata", false,
				"Indicates that a band contains metadata.");
		options.addOption("d", "datatype", true,
				"The type of data (default: BYTE).");
		options.addOption("x", "express", false,
				"Wait for export to finish, and download data immediately.");
		options.addOption("b", "band", true,
				"Set the bands to export (comma-separated list of band names).");
		options.addOption(OptionBuilder.withLongOpt("extents").hasArgs(4).
				withDescription("Spatial extents (xmin ymin xmax ymax).").
				create("e"));
		options.addOption(OptionBuilder.withLongOpt("time-extents").hasArgs(2).
				withDescription("Temporal extents (tmin tmax).").
				create("t"));
		options.addOption("p", "precision", true,
				"The temporal precision (default: \"1 day\").");
		options.addOption("o", "output", true, "The output file to write to.");
		options.addOption(null, "threads", true,
				"The number of threads to use (default: 1).");
		options.addOption("y", "task-type", true,
				"The task type to filter by (default: no filter).");
		options.addOption("s", "status", true,
				"The task status to filter by (default: RUNNING).");
		options.addOption(null, "nodata", true, "The band nodata value.");
		options.addOption(null, "type", true, "The band data type.");
		options.addOption(null, "acquisitiontime", true, "The timeslice created time.");
		
		options.addOption(null, "xmin", true, "The timeslice xmin value for updating.");
		options.addOption(null, "xmax", true, "The timeslice xmax value for updating.");
		options.addOption(null, "ymin", true, "The timeslice ymin value for updating.");
		options.addOption(null, "ymax", true, "The timeslice ymax value for updating.");
		options.addOption(null, "tiles", false, "Display tile informations of dataset or timeslice.");

		options.addOption(null, "monitor", false, "Continuously monitoring jobprogress.");
		options.addOption(null, "srcnodata", true,
				"The input file nodata value.");
		options.addOption(null, "name", true,
				"The name of the object which can be dataset or band.");
		options.addOption(null, "cdl", false,
				"Output information in CDL format.");
		options.addOption(null, "ncml", false,
				"Output information in NCML format.");
		options.addOption(null, "of", true,
				"Output format for query: nc3 or nc4 (default: nc4).");
		options.addOption("h", "help", false, "Show this help text.");
		options.addOption(null, "remote", true,
				"The remote option used for remote storage source task.");
		options.addOption(null, "async", true,
				"The async option used for asynchronous data task.");

		
		try {
			CommandLineParser parser = new GnuParser();
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			log.error(e.getMessage());
			System.err.print(USAGE);
			exit(1);
			return;
		}

		if (cmd.hasOption('h')) {
			// Just print help and exit.
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(USAGE, HEADER, options, getHelpFooter());
			exit(0);
			return;
		}
		else {
			initBeans();
		}
	}

	public void run() {
		try {
			workingDirectory = workingDirectory.toRealPath();
		} catch (IOException e) {
			log.error("Could not determine working directory from {}.",
					workingDirectory);
			exit(1);
			return;
		}

		String category;
		String action;
		try {
			category = cmd.getArgs()[0];
		} catch (IndexOutOfBoundsException e) {
			log.error("Missing category (e.g. \"dataset\").");
			System.err.print(USAGE);
			exit(1);
			return;
		}
		try {
			action = cmd.getArgs()[1];
		} catch (IndexOutOfBoundsException e) {
			log.error("Missing action (e.g. \"list\").");
			System.err.print(USAGE);
			exit(1);
			return;
		}

		category = category.toLowerCase();
		action = action.toLowerCase();

		@SuppressWarnings("unchecked")
		List<String> args = cmd.getArgList();
		List<String> remainingArgs = args.subList(2, args.size());

		try {
			// Datasets
			if (category.equals("dataset")) {
				if (action.equals("list")) {
					listDatasets();
				} else if (action.equals("search")) {
					searchDataset(remainingArgs);
				} else if (action.equals("show")) {
					showDataset(remainingArgs);
				} else if (action.equals("create")) {
					createDataset(remainingArgs);
				} else if (action.equals("update")) {
					updateDataset(remainingArgs);
				} else if (action.equals("delete")) {
					deleteDataset(remainingArgs);
				} else {
					throw new IllegalArgumentException(String.format("%s is not a recognised action.", action));
				}

			// Time slices
			} else if (category.equals("timeslice")) {
				if (action.equals("list")) {
					listTimeSlices(remainingArgs);
				} else if (action.equals("show")) {
					showTimeSlice(remainingArgs);
				} else if (action.equals("create")) {
					createTimeSlice(remainingArgs);
				} else if (action.equals("delete")) {
					deleteTimeSlice(remainingArgs);
				} else if (action.equals("update")) {
					updateTimeSlice(remainingArgs);
				} else {
					throw new IllegalArgumentException(String.format("%s is not a recognised action.", action));
				}

			// Bands
			} else if (category.equals("band")) {
				if (action.equals("list")) {
					listBands(remainingArgs);
				} else if (action.equals("create")) {
					createBand(remainingArgs);
				} else if (action.equals("delete")) {
					deleteBand(remainingArgs);
				} else if (action.equals("update")) {
					updateBand(remainingArgs);
				} else {
					throw new IllegalArgumentException(String.format("%s is not a recognised action.", action));
				}

			} else if (category.equals("data")) {
//				case "upload":
//					System.out.println("Uploading data.");
//					break;
				if (action.equals("import")) {
					importData(remainingArgs);
				} else if (action.equals("export")) {
					exportData(remainingArgs);
				} else if (action.equals("download")) {
					downloadData(remainingArgs);
				} else if (action.equals("query")) {
					queryData(remainingArgs);
				} else if (action.equals("task")) {
					listTasks(remainingArgs);
				} else if (action.equals("cleanup")) {
					cleanup(remainingArgs);
				} else {
					throw new IllegalArgumentException(String.format("%s is not a recognised action.", action));
				}

//			} else if (category.equals("help")) {
//				if (action.equals("export")) {
//					helpExport(remainingArgs);
//				}

			} else {
				throw new IllegalArgumentException(String.format("%s is not a recognised category.", category));
			}

		} catch (IllegalArgumentException e) {
			log.error(e.getMessage());
			if (e.getCause() != null)
				log.error("Cause: {}", e.getCause().getMessage());
			exit(1);
			return;

		} catch (UnsupportedOperationException e) {
			log.error("Chosen operation not implemented: {}", e.getMessage());
			if (e.getCause() != null)
				log.error("Cause: {}", e.getCause().getMessage());
			exit(1);
			return;

		} catch (InfrastructureException e) {
			log.error("Failed to communicate with database: {}", e.getMessage());
			if (e.getCause() != null)
				log.error("Cause: {}", e.getCause().getMessage());
			exit(2);
			return;

		} catch (TaskInitialisationException | TaskException | IOException
				| QueryRuntimeException e) {
			log.error("Execution failed: {}", e.getMessage());
			if (e.getCause() != null)
				log.error("Cause: {}", e.getCause().getMessage());
			exit(2);
			return;
		}
	}

	private void updateDataset(List<String> remainingArgs) {
		log.info("Updating a dataset.");

		DatasetConnector bc = sm.getDatasetConnector();
		try {
			Dataset newDataset = parseDataset(remainingArgs, remainingArgs.get(0));
			log.debug("updating dataset");
			if(cmd.hasOption("name"))
				bc.update(newDataset);
			else
				bc.updateInfo(newDataset);
		} catch (Exception e) {
			log.error("Could not update dataset: {}", e.getMessage());
		}
		System.out.println(String.format("Dataset has been updated."));

	}

	
	
	private void deleteBand(List<String> remainingArgs) throws IOException {
		log.info("Deleting a band.");

		String id;
		try {
			id = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("Band ID not specified.");
		}

		BandConnector bc = sm.getBandConnector();
		try {
			log.debug("deleting band");
			bc.delete(id);
		} catch (IOException e) {
			log.error("Could not delete band: {}", e.getMessage());
			throw e;
		}
		System.out.println(String.format("Band: %s has been deleted.", id));
	}

	private void updateBand(List<String> remainingArgs) {
		log.info("Updating a band.");

		BandConnector bc = sm.getBandConnector();
		try {
			Band newBand = parseBand(remainingArgs, remainingArgs.get(0));
			log.debug("updating band");
			if(cmd.hasOption("name"))
				bc.update(newBand);
			else
				bc.updateInfo(newBand);
		} catch (Exception e) {
			log.error("Could not update band: {}", e.getMessage());
		}
		System.out.println(String.format("Banb has been updated."));

	}
	
	public Band parseBand(List<String> properties, String oldBandId) {
		Band band = sm.getBandConnector().retrieve(oldBandId);
		for(Option opt : cmd.getOptions()) {

			switch(opt.getLongOpt()){
				case "name":
					band.setName(opt.getValue());
					break;
				case "continuous":
					band.setContinuous(Boolean.parseBoolean(opt.getValue()));
					break;
				case "metadata":
					band.setMetadata(Boolean.parseBoolean(opt.getValue()));
					break;
				case "type":
					band.setType(RasterDetails.valueOf(opt.getValue()));
					break;
			}
		}
		return band;
	}

	public Dataset parseDataset(List<String> properties, String datasetId) throws java.text.ParseException {
		Dataset ds = sm.getDatasetConnector().get(datasetId);
		for(Option opt : cmd.getOptions()) {

			switch(opt.getLongOpt()){
				case "abstract":
					ds.setAbst(opt.getValue());
					break;
				case "name":
					ds.setName(opt.getValue());
					break;
			}
		}
		return ds;
	}

	
	public TimeSlice parseTimeslice(List<String> properties, String timesliceId) throws java.text.ParseException {
		TimeSlice ts = sm.getTimesliceConnector().get(timesliceId);
		for(Option opt : cmd.getOptions()) {

			switch(opt.getLongOpt()){
				case "acquisitiontime":
					DateFormat formatter = Utils.getTimestampFormatter();
					ts.setCreated(formatter.parse(opt.getValue()));
					break;
				case "abstract":
					ts.setDataAbstract(opt.getValue());
					break;
				case "xmin":
					ts.setXmin(Double.parseDouble(opt.getValue()));
					break;
				case "xmax":
					ts.setXmax(Double.parseDouble(opt.getValue()));
					break;
				case "ymin":
					ts.setYmin(Double.parseDouble(opt.getValue()));
					break;
				case "ymax":
					ts.setYmax(Double.parseDouble(opt.getValue()));
					break;
			}
		}
		return ts;
	}
	
	private void deleteTimeSlice(List<String> remainingArgs) throws IOException {
		log.info("Deleting a timeslice.");

		String id;
		try {
			id = remainingArgs.get(0);
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("TimeSlice ID not specified.");
		}

		TimesliceConnector tsc = sm.getTimesliceConnector();
		try {
			if (cmd.hasOption("time-extents")) {
				String[] extentsArr = cmd.getOptionValues("time-extents");
				Date startDate = Utils.parseDate(extentsArr[0]);
				Date endDate = Utils.parseDate(extentsArr[1]);
				List<TimeSlice> tsList = tsc.list(id);
				for(TimeSlice ts : tsList) {
					if(ts.getCreated().compareTo(startDate) >= 0 && ts.getCreated().compareTo(endDate) <= 0) {
						tsc.delete(ts.getId());
						System.out.println(String.format("TimeSlice: %s has been deleted.", ts.getId()));
					}
				}
			} else {
				log.debug("deleting time slice");
				tsc.delete(id);
				System.out.println(String.format("TimeSlice: %s has been deleted.", id));
			}
			
		} catch (IndexOutOfBoundsException | IllegalArgumentException | IOException e) {
			throw new IllegalArgumentException("Time date extents are invalid.");
		}
	}

	private void updateTimeSlice(List<String> remainingArgs) {
		log.info("Updating a timeslice.");
		TimesliceConnector tc = sm.getTimesliceConnector();
		
		try {
			TimeSlice newTs = parseTimeslice(remainingArgs, remainingArgs.get(0));
			log.debug("updating time slice");
			if(cmd.hasOption("acquisitionTime"))
				tc.update(newTs);
			else
				tc.updateInfo(newTs);
		} catch (Exception e) {
			log.error("Could not update timeslice: {}", e.getMessage());
			System.out.println(String.format("Could not update timeslice."));
		}
		System.out.println(String.format("Timeslice has been updated."));

		
	}
	
	/**
	 * @return Text that describes the enum values that one can enter on the
	 *         command line.
	 */
	public static String getHelpFooter() {
		StringBuilder sb = new StringBuilder();

		sb.append("SPATIAL_EXTENTS should be specified in the target " +
				"projection, in the form <XMIN> <YMIN> <XMAX> <YMAX> (as " +
				"separate arguments).\n");
		sb.append("TEMPORAL_EXTENTS are expressed as <START> <END>. Dates " +
				"are inclusive.\n");

		sb.append("Resolutions: ");
		boolean first = true;
		for (CellSize res : CellSize.values()) {
			if (!first)
				sb.append(", ");
			else
				first = false;
			sb.append(String.format("%s", res.toHumanString()));
		}
		sb.append("\n");

		sb.append("Data types: ");
		first = true;
		for (RasterDetails type : RasterDetails.values()) {
			if (!first)
				sb.append(", ");
			else
				first = false;
			sb.append(String.format("%s", type.toString()));
		}
		sb.append("\n");

		sb.append("Task types: ");
		first = true;
		for (TaskType type : TaskType.values()) {
			if (!first)
				sb.append(", ");
			else
				first = false;
			sb.append(String.format("%s", type.toString()));
		}
		sb.append("\n");

		sb.append("Task states: ");
		first = true;
		for (TaskState type : TaskState.values()) {
			if (!first)
				sb.append(", ");
			else
				first = false;
			sb.append(String.format("%s", type.toString()));
		}
		sb.append("\n");

		return sb.toString();
	}
}