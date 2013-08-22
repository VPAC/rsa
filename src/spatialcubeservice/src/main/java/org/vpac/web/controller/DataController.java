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

package org.vpac.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.vpac.ndg.CommandUtil;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.common.datamodel.GdalFormat;
import org.vpac.ndg.datamodel.AggregationDefinition;
import org.vpac.ndg.datamodel.AggregationOpener;
import org.vpac.ndg.datamodel.RsaAggregationFactory;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.lock.ProcessUpdateTimer;
import org.vpac.ndg.query.Query;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.QueryDefinition;
import org.vpac.ndg.query.filter.FilterUtils;
import org.vpac.ndg.query.io.DatasetProvider;
import org.vpac.ndg.query.io.ProviderRegistry;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.Type;
import org.vpac.ndg.query.sampling.ArrayAdapter;
import org.vpac.ndg.query.sampling.ArrayAdapterImpl;
import org.vpac.ndg.query.sampling.NodataStrategy;
import org.vpac.ndg.query.sampling.NodataStrategyFactory;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.JobProgressDao;
import org.vpac.ndg.storage.dao.UploadDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.JobProgress;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.Upload;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.ndg.storage.util.UploadUtil;
import org.vpac.ndg.task.Exporter;
import org.vpac.ndg.task.ImageTranslator;
import org.vpac.ndg.task.Importer;
import org.vpac.web.exception.ResourceNotFoundException;
import org.vpac.web.model.request.DataExportRequest;
import org.vpac.web.model.request.FileRequest;
import org.vpac.web.model.request.PagingRequest;
import org.vpac.web.model.request.TaskSearchRequest;
import org.vpac.web.model.response.CleanUpResponse;
import org.vpac.web.model.response.DatasetPlotResponse;
import org.vpac.web.model.response.ExportResponse;
import org.vpac.web.model.response.FileInfoResponse;
import org.vpac.web.model.response.ImportResponse;
import org.vpac.web.model.response.QueryResponse;
import org.vpac.web.model.response.TaskCollectionResponse;
import org.vpac.web.model.response.TaskResponse;
import org.vpac.web.util.ControllerHelper;
import org.vpac.web.util.Pager;

import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.NetcdfFileWriter.Version;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

@Controller
@RequestMapping("/Data")
public class DataController {

	final private Logger log = LoggerFactory.getLogger(DataController.class);

	@Autowired
	private ControllerHelper helper;
	@Autowired
	UploadDao uploadDao;
	@Autowired
	DatasetDao datasetDao;
	@Autowired
	UploadUtil uploadUtil;
	@Autowired
	JobProgressDao jobProgressDao;
	@Autowired
	TimeSliceUtil timeSliceUtil;
	@Autowired
	DatasetProvider rsaDatasetProvider;
	@Autowired
	DatasetProvider previewDatasetProvider;

	private Pager<JobProgress> pager = new Pager<JobProgress>();

	@InitBinder
	public void binder(WebDataBinder binder) {
		helper.BindDateTimeFormatter(binder);
	}

	@RequestMapping(value = "/Form", method = RequestMethod.GET)
	public String displayForm() {
		return "FileUploadForm";
	}

	@RequestMapping(value = "/Import/Form", method = RequestMethod.GET)
	public String displayImportForm() {
		return "ImportForm";
	}

	@RequestMapping(value = "/Export/Form", method = RequestMethod.GET)
	public String displayExportForm() {
		return "ExportForm";
	}
	
	@RequestMapping(value="/Upload", method = RequestMethod.POST)
	//public String uploadFile(@RequestParam String timeSliceId, @RequestParam MultipartFile file, @RequestParam(required = false) String fileId, @RequestParam(required = false) String count, ModelMap model) throws IOException {
	public String uploadFile(@Valid FileRequest fileRequest, ModelMap model) throws Exception {	

		log.info("File Upload");
		log.debug("Timeslice ID: {}", fileRequest.getTimeSliceId());
		log.debug("Task ID: {}", fileRequest.getTaskId());

		Upload upload;
		if(fileRequest.getTaskId() == null || fileRequest.getTaskId().isEmpty()) {
			upload = new Upload(fileRequest.getTimeSliceId());
			uploadDao.create(upload);
			log.debug("Create UploadFileId: {}", upload.getFileId());
		}
		else {
			upload = uploadDao.retrieve(fileRequest.getTaskId());
			if(upload == null) {
				// Capture if band not exist
				throw new ResourceNotFoundException(String.format("Upload with task ID = \"%s\" not found.", fileRequest.getTaskId()));			
			}
			log.debug("Retrieve UploadFileId: {}", upload.getFileId());
		}
		Path fileIdDir = uploadUtil.getDirectory(upload).toAbsolutePath();
		uploadUtil.createDirectory(upload);

		for (MultipartFile file : fileRequest.getFiles()) {
			Path target = fileIdDir.resolve(file.getOriginalFilename());
			if (!file.isEmpty())
				file.transferTo(target.toFile());
		}

		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new FileInfoResponse(upload.getFileId()));
		return "FileUploadSuccess";
	}
	
 	@RequestMapping(value="/Import", method = RequestMethod.POST)
	public String importTimeSlice(@RequestParam(required=true) String taskId, @RequestParam(required=true) String bandId, @RequestParam(required=true) String srcnodata, @RequestParam(required=false) Boolean useBilinearInterpolation, ModelMap model ) throws TaskInitialisationException {

		log.info("TimeSlice Import");
		log.debug("Task ID: {}", taskId);
		log.debug("Band ID: {}", bandId);
		log.debug("srcnodata: {}", srcnodata);
		log.debug("useBilinearInterpolation: {}", useBilinearInterpolation);

		Importer importer = new Importer();
		// mandatory
		importer.setUploadId(taskId);
		importer.setBand(bandId);
		importer.setSrcnodata(srcnodata);
		if (useBilinearInterpolation != null)
			importer.setUseBilinearInterpolation(useBilinearInterpolation);
		
		// After calling runInBackground, we can't access the exporter any more.
		// So the model must be updated after configuration, but before the task
		// is started.
		importer.configure();
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new ImportResponse(importer.getTaskId()));
		importer.runInBackground();

		return "Success";
	}

	@RequestMapping(value="/Export", method = RequestMethod.POST)
	public String export(@Valid DataExportRequest request, ModelMap model) throws TaskInitialisationException {

		log.info("TimeSlice Export");
		log.debug("Top left: {}", request.getTopLeft());
		log.debug("Bottom right: {}", request.getBottomRight());
		log.debug("Start date: {}", request.getSearchStartDate());
		log.debug("End date: {}", request.getSearchEndDate());
		log.debug("Projection: {}", request.getProjection());

		Exporter exporter = new Exporter();
		// mandatory
		exporter.setDatasetId(request.getDatasetId());
		// optional
		exporter.setBandIds(request.getBandId());
		exporter.setStart(request.getSearchStartDate());
		exporter.setEnd(request.getSearchEndDate());
		exporter.setTargetProjection(request.getProjection());
		if(request.getTopLeft() != null && request.getBottomRight() != null) {
			Box b = new Box(request.getTopLeft(), request.getBottomRight());
			exporter.setExtents(b);
		}
		else
			exporter.setExtents(null);

		// After calling runInBackground, we can't access the exporter any more.
		// So the model must be updated after configuration, but before the task
		// is started.
		exporter.configure();
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new ExportResponse(exporter.getTaskId()));
		exporter.runInBackground();

		return "Success";
	}
	
 	@RequestMapping(value="/Task", method = RequestMethod.GET)
	public String getTasks(@Valid TaskSearchRequest request, @Valid PagingRequest page, ModelMap model ) {

		log.info("Data getTasks");
		log.debug("Task Type being searched: {}", request.getSearchType());
		log.debug("Task State being searched: {}", request.getSearchState());

		List<JobProgress> list = jobProgressDao.search(request.getSearchType(), request.getSearchState(), page.getPage(), page.getPageSize());
		if(list.size() > 0)
			model.addAttribute(ControllerHelper.RESPONSE_ROOT, new TaskCollectionResponse(pager.page(list, page)));
		else
			model.addAttribute(ControllerHelper.RESPONSE_ROOT, new TaskCollectionResponse());
		return "List";
	}

	@RequestMapping(value="/Task/{id}", method = RequestMethod.GET)
	public String getTaskById(@PathVariable String id, ModelMap model ) throws ResourceNotFoundException {

		log.info("Data getTaskById");
		log.debug("Task ID: {}", id);

		JobProgress j = jobProgressDao.retrieve(id);
		if(j != null)
			model.addAttribute(ControllerHelper.RESPONSE_ROOT, new TaskResponse(j));
		else
			throw new ResourceNotFoundException("This task id not found.");
		return "List";
	}

	
	@RequestMapping(value = "/Download/{taskId}", method = RequestMethod.GET)
	public void downloadFile(@PathVariable("taskId") String taskId, HttpServletResponse response) throws TaskException, FileNotFoundException {
		// get your file as InputStream
		Path p = Exporter.findOutputPath(taskId);
		FileInputStream is = new FileInputStream(p.toFile());
	    try {
			// Send headers, such as file name and length. The length is
			// required for streaming to work properly; see
			// http://stackoverflow.com/questions/10552555/response-flushbuffer-is-not-working
			// Not using response.setContentLength(int), because the file may be
			// larger than 2GB, in which case an int would overflow.
			response.setContentType("application-xdownload");
			response.setHeader("Content-Disposition", "attachment; filename=" + p.getFileName().toString());
			response.setHeader("Content-Length", String.format("%d", Files.size(p)));
			response.flushBuffer();
			// Copy file to response's OutputStream (send it to the client)
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			log.warn("IO error writing file to output stream. User may " +
					"have cancelled.");
		}
	}

	@RequestMapping(value = "/Convert/{filename}", method = RequestMethod.GET)
	public void convert(@PathVariable("filename") String filename, HttpServletResponse response) throws TaskInitialisationException, TaskException, ResourceNotFoundException {
		if(filename == null) {
			throw new ResourceNotFoundException("Filename not specified.");
		}

		File srcFile = new File("/home/hsumanto/tmp/" + filename + ".nc");
		File dstFile = new File("/home/hsumanto/tmp/" + filename + ".png");
		ImageTranslator converter = new ImageTranslator();
		// mandatory
		converter.setFormat(GdalFormat.PNG);
		converter.setLayerIndex(1);
		converter.setSrcFile(srcFile.toPath());
		converter.setDstFile(dstFile.toPath());
		converter.initialise();
		converter.execute();
		
		try {
			// get your file as InputStream
			InputStream is = new FileInputStream(dstFile);
			// copy it to response's OutputStream
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}

	@RequestMapping(value = "/Plot", method = RequestMethod.GET)
	public ModelAndView plot(@RequestParam(required = true) String datasetId,
			@RequestParam(required = false) String bandName,
			@RequestParam(required = true) double x,
			@RequestParam(required = true) double y,
			@RequestParam(required = false) Date start,
			@RequestParam(required = false) Date end,
			@RequestParam(required = false) boolean omitNodata,
			Model model)
			throws IOException, InvalidRangeException, QueryConfigurationException {
		Dataset ds = null;
		if(datasetId.contains("/")) {
			String[] splittedDatasetId = datasetId.split("/");
			String name = splittedDatasetId[0];
			CellSize resolution = CellSize.fromHumanString(splittedDatasetId[1]);
			ds = datasetDao.findDatasetByName(name, resolution);
		} else {
			ds = datasetDao.retrieve(datasetId);
		}
		if (ds == null)
			return null;

		List<TimeSlice> tsList = datasetDao.findTimeSlices(ds.getId(), start, end);
		if (tsList == null)
			return null;

		List<Band> bands = null;
		if (bandName == null || bandName == "") {
			bands = datasetDao.getBands(ds.getId());
		} else {
			List<String> bandNames = new ArrayList<String>();
			bandNames.add(bandName);
			bands = datasetDao.findBandsByName(ds.getId(), bandNames);
		}
		if (bands == null)
			return null;

		RsaAggregationFactory factory = new RsaAggregationFactory();
		ModelAndView modelNView = new ModelAndView("DisplayPlot");

		try {
			AggregationDefinition def = factory.create(ds, tsList, bands, new Box(x, y, x, y));
			log.info("TS0:" + tsList.get(0).getCreated());
			log.info("TSN:" + tsList.get(tsList.size() - 1).getCreated());
			AggregationOpener opener = new AggregationOpener();
			NetcdfDataset dataset = opener.open(def, "");
			Variable varx = dataset.findVariable("x");
			Variable vary = dataset.findVariable("y");
			int xIndex = getArrayIndex(varx, x);
			int yIndex = getArrayIndex(vary, y);
			
			int[] origin = new int[] {0, yIndex, xIndex };
			int[] shape = new int[] {tsList.size(), 1, 1 };
			
			Map<String, List<Pair>> bandPlotPair = new HashMap<String, List<Pair>>();
			Array ar = null;
			NodataStrategyFactory ndsfac = new NodataStrategyFactory();
			for(Band b : bands) {
				Variable var = dataset.findVariable(b.getName());
				Type type = Type.get(var.getDataType(), var.isUnsigned());
				NodataStrategy nds = ndsfac.create(var, type);
				ar = var.read(origin, shape);
				ArrayAdapter arradapt = ArrayAdapterImpl.createAndPromote(ar,
						var.getDataType(), nds);
				List<Pair> plotValues = new ArrayList<Pair>();
				for(int i = 0; i < ar.getIndex().getSize(); i++) {
					ScalarElement elem = arradapt.get(i);
					Date timeval = tsList.get(i).getCreated();
					if (elem.isValid())
						plotValues.add(new Pair(timeval, elem.doubleValue()));
					else if (!omitNodata)
						plotValues.add(new Pair(timeval, null));
				}
				bandPlotPair.put(b.getName(), plotValues);
			}
			modelNView.addObject("bandPlotValues", bandPlotPair);
			modelNView.addObject("datasetName", ds.getName());
			modelNView.addObject("pointX", x);
			modelNView.addObject("pointY", y);

			model.addAttribute(ControllerHelper.RESPONSE_ROOT, new DatasetPlotResponse(tsList, ar));
			
		} catch (IOException e) {
			log.error(e.getStackTrace().toString());
			throw e;
		}
		
		
		return modelNView;
	}
	
//	private String toCSV(List<TimeSlice> tsList) {
//		StringBuilder builder = new StringBuilder();
//		for(TimeSlice ts : tsList) {
//			if(builder.length() > 0)
//				builder.append(',');
//			builder.append(ts.getCreated());
//		}
//		return builder.toString();
//	}
//	
//	private String toCSV(Array ar) {
//		StringBuilder builder = new StringBuilder();
//		long arSize = ar.getIndex().getSize();
//		for(int i = 0; i < arSize; i++) {
//			if(builder.length() > 0) 
//				builder.append(',');
//			builder.append(ar.getDouble(i));
//		}
//		return builder.toString();
//	}

	private int getArrayIndex(Variable var, double value) throws IOException {
		int index = -1;
		Array ar = var.read();
		for(int i = 0; i < ar.getIndex().getSize(); i++) {
			if(ar.getDouble(i) <= value)
				index = i;
		}
		return index;
	}
	
	
	/*
	@RequestMapping(value = "/Download/{file_name}", method = RequestMethod.GET)
	public void getFile(@PathVariable("file_name") String fileName,
			HttpServletResponse response) {
		try {
			// get your file as InputStream
			File f = new File("c:/temp/" + fileName + ".docx");
			InputStream is = new FileInputStream(f);
			// copy it to response's OutputStream
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
	*/

	@RequestMapping(value = "/CleanUp", method = RequestMethod.POST)
	public String cleanUp(@RequestParam(required=false) Boolean force, ModelMap model) {
		String process = ProcessUpdateTimer.INSTANCE.acquire();
		try {
			int returnCount = timeSliceUtil.cleanOthers(process, force);
			model.addAttribute(ControllerHelper.RESPONSE_ROOT, new CleanUpResponse(returnCount));
			return "Success";
		} finally {
			ProcessUpdateTimer.INSTANCE.release();
		}
	}

	@RequestMapping(value = "/Query", method = RequestMethod.POST)
	public String query(@RequestParam(required = false) MultipartFile file,
			@RequestParam(required = false) String threads,
			@RequestParam(required = false) Double minX,
			@RequestParam(required = false) Double minY,
			@RequestParam(required = false) Double maxX,
			@RequestParam(required = false) Double maxY,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String netcdfVersion,
			ModelMap model)
			throws IOException, QueryConfigurationException {

		// FIXME: this should only be done once!
		ProviderRegistry.getInstance().clearProivders();
		ProviderRegistry.getInstance().addProivder(rsaDatasetProvider);
		ProviderRegistry.getInstance().addProivder(previewDatasetProvider);

		final QueryDefinition qd = QueryDefinition.fromXML(file.getInputStream());
		if(minX != null)
			qd.output.grid.bounds = String.format("%f %f %f %f", minX, minY, maxX, maxY);
		
		if(startDate != null) {
			qd.output.grid.timeMin = startDate;
			qd.output.grid.timeMax = endDate;
		}

		Version version;
		if (netcdfVersion != null) {
			if (netcdfVersion.equals("nc3")) {
				version = Version.netcdf3;
			} else if (netcdfVersion.equals("nc4")) {
				version = Version.netcdf4;
			} else {
				throw new IllegalArgumentException(String.format(
						"Unrecognised NetCDF version %s", netcdfVersion));
			}
		} else {
			version = Version.netcdf4;
		}
		final Version ver = version;

		final QueryProgress qp = new QueryProgress(jobProgressDao);
		String taskId = qp.getTaskId();
		final Integer t = threads == null ? null : Integer.parseInt(threads);
		Path outputDir = FileUtils.getTargetLocation(taskId);
		final Path queryPath = outputDir.resolve(file.getOriginalFilename());
		if (!Files.exists(outputDir))
			Files.createDirectories(outputDir);

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					executeQuery(qd, qp, t, queryPath, ver);
				} catch (Exception e) {
					qp.setErrorMessage(e.getMessage());
					log.error("Task exited abnormally: ", e);
				}
			}
		});

		thread.start();
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new QueryResponse(taskId));
		return "Success";
	}

	@RequestMapping(value = "/PreviewQuery", method = RequestMethod.POST)
	public void previewQuery(@RequestParam(required = false) String query,
			@RequestParam(required = false) String threads,
			@RequestParam(required = false) Double minX,
			@RequestParam(required = false) Double minY,
			@RequestParam(required = false) Double maxX,
			@RequestParam(required = false) Double maxY,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String netcdfVersion,
			ModelMap model, HttpServletResponse response)
			throws Exception {

		// FIXME: this should only be done once!
		ProviderRegistry.getInstance().clearProivders();
		ProviderRegistry.getInstance().addProivder(rsaDatasetProvider);
		ProviderRegistry.getInstance().addProivder(previewDatasetProvider);

		final QueryDefinition qd = QueryDefinition.fromString(query);
		if(minX != null)
			qd.output.grid.bounds = String.format("%f %f %f %f", minX, minY, maxX, maxY);
		
		if(startDate != null) {
			qd.output.grid.timeMin = startDate;
			qd.output.grid.timeMax = endDate;
		}

		Version version;
		if (netcdfVersion != null) {
			if (netcdfVersion.equals("nc3")) {
				version = Version.netcdf3;
			} else if (netcdfVersion.equals("nc4")) {
				version = Version.netcdf4;
			} else {
				throw new IllegalArgumentException(String.format(
						"Unrecognised NetCDF version %s", netcdfVersion));
			}
		} else {
			version = Version.netcdf4;
		}
		final Version ver = version;

		final QueryProgress qp = new QueryProgress(jobProgressDao);
		String taskId = qp.getTaskId();
		final Integer t = threads == null ? null : Integer.parseInt(threads);
		Path outputDir = FileUtils.getTargetLocation(taskId);
		final Path queryPath = outputDir.resolve("query_output.nc");
		if (!Files.exists(outputDir))
			Files.createDirectories(outputDir);

		try {
			executeQuery(qd, qp, t, queryPath, ver);

			if (!Files.exists(queryPath)) {
				throw new ResourceNotFoundException("Query output file not found: " + queryPath);
			}

			Path previewPath = queryPath.getParent().resolve("preview.png");

			ImageTranslator converter = new ImageTranslator();
			// mandatory
			converter.setFormat(GdalFormat.PNG);
			converter.setLayerIndex(1);
			converter.setSrcFile(queryPath);
			converter.setDstFile(previewPath);
			converter.initialise();
			converter.execute();

			try {
				// get your file as InputStream
				InputStream is = new FileInputStream(previewPath.toString());
				String result = DatatypeConverter.printBase64Binary(IOUtils
						.toByteArray(is));
				response.setContentType("application/json");
				response.setContentLength(result.getBytes().length);
				response.getOutputStream().write(result.getBytes());
				response.flushBuffer();
			} catch (IOException ex) {
				throw new RuntimeException(
						"IOError writing file to output stream: " + ex);
			}
		} catch (Exception e) {
			qp.setErrorMessage(e.getMessage());
			log.error("Task exited abnormally: ", e);
			throw e;
		}
	}


	@RequestMapping(value = "/MultiPreviewQuery", method = RequestMethod.POST)
	public void multiPreviewQuery(@RequestParam(required = false) String query,
			@RequestParam(required = false) String threads,
			@RequestParam(required = false) Double minX,
			@RequestParam(required = false) Double minY,
			@RequestParam(required = false) Double maxX,
			@RequestParam(required = false) Double maxY,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate,
			@RequestParam(required = false) String netcdfVersion,
			ModelMap model, HttpServletResponse response)
			throws Exception {

		// FIXME: this should only be done once!
		ProviderRegistry.getInstance().clearProivders();
		ProviderRegistry.getInstance().addProivder(rsaDatasetProvider);
		ProviderRegistry.getInstance().addProivder(previewDatasetProvider);

		final QueryDefinition qd = QueryDefinition.fromString(query);
		if(minX != null)
			qd.output.grid.bounds = String.format("%f %f %f %f", minX, minY, maxX, maxY);
		
		if(startDate != null) {
			qd.output.grid.timeMin = startDate;
			qd.output.grid.timeMax = endDate;
		}

		Version version;
		if (netcdfVersion != null) {
			if (netcdfVersion.equals("nc3")) {
				version = Version.netcdf3;
			} else if (netcdfVersion.equals("nc4")) {
				version = Version.netcdf4;
			} else {
				throw new IllegalArgumentException(String.format(
						"Unrecognised NetCDF version %s", netcdfVersion));
			}
		} else {
			version = Version.netcdf4;
		}
		final Version ver = version;

		final QueryProgress qp = new QueryProgress(jobProgressDao);
		String taskId = qp.getTaskId();
		final Integer t = threads == null ? null : Integer.parseInt(threads);
		Path outputDir = FileUtils.getTargetLocation(taskId);
		final Path queryPath = outputDir.resolve("query_output.nc");
		if (!Files.exists(outputDir))
			Files.createDirectories(outputDir);

		try {
			executeQuery(qd, qp, t, queryPath, ver);

			if (!Files.exists(queryPath)) {
				throw new ResourceNotFoundException("Query output file not found: " + queryPath);
			}

			Path ncPreviewPath = queryPath.getParent().resolve("multipreview.nc");

			// generate multiple 2D preview out of query output
			boolean hasNoData = FilterUtils.multiPreview(queryPath, ncPreviewPath);

			Path pngPreviewPath = queryPath.getParent().resolve("multipreview.png");
			Path tifPreviewPath = queryPath.getParent().resolve("multipreview.tif");

			CommandUtil commandUtil = new CommandUtil();

			// Warp to tiff first - this sets an alpha flag.
			List<String> command = new ArrayList<String>();
			command.add("gdalwarp");
			command.add("-of");
			command.add("GTiff");
			if (hasNoData) {
				command.add("-srcnodata");
				command.add("-999");
				command.add("-dstnodata");
				command.add("-999");
				command.add("-dstalpha");
			}
			command.add(ncPreviewPath.toString());
			command.add(tifPreviewPath.toString());
			commandUtil.start(command);

			// Now translate to png.
			command = new ArrayList<String>();
			command.add("gdal_translate");
			command.add("-of");
			command.add("PNG");
			command.add("-scale");
			command.add("-ot");
			command.add("Byte");
			command.add(tifPreviewPath.toString());
			command.add(pngPreviewPath.toString());
			commandUtil.start(command);

			try {
				// get your file as InputStream
				InputStream is = new FileInputStream(pngPreviewPath.toString());
				String result = DatatypeConverter.printBase64Binary(IOUtils
						.toByteArray(is));
				response.setContentType("application/json");
				response.setContentLength(result.getBytes().length);
				response.getOutputStream().write(result.getBytes());
				response.flushBuffer();
			} catch (IOException ex) {
				throw new RuntimeException(
						"IOError writing file to output stream");
			}
		} catch (Exception e) {
			qp.setErrorMessage(e.getMessage());
			log.error("Task exited abnormally: ", e);
			throw e;
		}
	}

	private void executeQuery(QueryDefinition qd, QueryProgress qp,
			Integer threads, Path outputPath, Version netcdfVersion)
			throws IOException, QueryConfigurationException {
		NetcdfFileWriter outputDataset = NetcdfFileWriter.createNew(
				netcdfVersion, outputPath.toString());

		try {
			Query q = new Query(outputDataset);
			if (threads != null)
				q.setNumThreads(threads);
			q.setMemento(qd, "preview:");
			try {
				q.setProgress(qp);
				q.run();
			} finally {
				q.close();
			}
		} finally {
			try {
				outputDataset.close();
			} catch (Exception e) {
				log.warn("Failed to close output file", e);
			}
		}
	}
	
	public class Pair {
		private Date date;
		private Double value;
		
		public Pair(Date d, Double v) {
			this.date = d;
			this.value = v;
		}
		
		@Override
		public String toString() {
			return String.format("[%s, %f]", date.getTime(), value);
		}
	}
}