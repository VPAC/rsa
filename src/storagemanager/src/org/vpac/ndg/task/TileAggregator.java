/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.task;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.FileUtils;
import org.vpac.ndg.Utils;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.rasterservices.FileInformation;
import org.vpac.ndg.rasterservices.NcmlCreator;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TileBand;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.ndg.storagemanager.GraphicsFile;

public class TileAggregator extends Task {

	final private Logger log = LoggerFactory.getLogger(TileAggregator.class);

	private List<TileBand> source;
	private ScalarReceiver<GraphicsFile> target;
	private GraphicsFile vrtFile;
	private TimeSlice timeSlice;
	private Band band;
	private List<Path> tmpFileList; // Store the tmp ncml files in tmp directory
	private Path temporaryLocation;
	TimeSliceDao timeSliceDao;
	TimeSliceUtil timeSliceUtil;
	NdgConfigManager ndgConfigManager;
	
	public TileAggregator() {
		this(Constant.TASK_DESCRIPTION_TILEAGGREGATOR);
	}

	public TileAggregator(String description) {
		super(description);
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		timeSliceDao = (TimeSliceDao) appContext.getBean("timeSliceDao");
		ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");
		timeSliceUtil = (TimeSliceUtil) appContext.getBean("timeSliceUtil");
	}

	@Override
	public void initialise() throws TaskInitialisationException {
		if (getSource() == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}

		if (getTarget() == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_TARGET_DATASET_NOT_SPECIFIED);
		}

		// If temporaryLocation is null, create temporary location
		if(temporaryLocation == null) {			
			try {
				temporaryLocation = FileUtils.createTmpLocation();
			} catch (IOException e) {
				log.error("Could not create temporary directory: {}", e);
				throw new TaskInitialisationException(String.format("Error encountered when create temporary directory: %s", temporaryLocation));
			}
			log.info("Temporary Location: {}", temporaryLocation);
		}

		tmpFileList = new ArrayList<Path>();
	}

	public void revalidateBeforeExecution() throws TaskException {
		if(getSource().isEmpty()) {
			throw new TaskException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}

		for (TileBand tileband : source) {
			if (!tileband.getBand().equals(band)) {
				throw new TaskException(String.format(
						"Band \"%s\" not found in dataset.",
						tileband.getBand().getName()));
			}
		}
	}

	@Override
	public void execute() throws TaskException {
		revalidateBeforeExecution();

		List<Path> tileList = new ArrayList<Path>();
		for (TileBand tileband : source) {
			tileList.add(tileband.getFileLocation());
		}

		Path vrtFileLocation = vrtFile.getFileLocation();
		FileInformation vrtInfo = FileInformation.read(vrtFileLocation);

		String ncmlFilename = band.getName() + "_agg" + Constant.EXT_NCML;			
		Path ncmlFileLocation = temporaryLocation.resolve(ncmlFilename);
		log.debug("ncml location: {}", ncmlFileLocation);		

		GraphicsFile ncmlFile = new GraphicsFile(ncmlFileLocation);
		target.set(ncmlFile);

		try {
			log.debug("{}", ncmlFile.getFileLocation());
			NcmlCreator.createNcml(vrtInfo, tileList, ncmlFile.getFileLocation());
		} catch (IOException e) {
			String msg = String.format(Constant.ERR_TASK_EXCEPTION, getDescription(), e.getMessage());
			throw new TaskException(msg);
		}

		String ncWmsAdminUsername = ndgConfigManager.getConfig().getNcWmsAdminUsername();
		String ncWmsAdminPassword = ndgConfigManager.getConfig().getNcWmsAdminPassword();
		if (Utils.sendNcwmsRequest()) {
			if(ncWmsAdminUsername == null || ncWmsAdminUsername.isEmpty()) {
				throw new TaskException("ncWmsAdminUsername is not specified.");
			}

			if(ncWmsAdminPassword == null || ncWmsAdminPassword.isEmpty()) {
				throw new TaskException("ncWmsAdminPassword is not specified.");
			}
		}

		// Copy file to storage pool.
		// TODO: This should be done in the Committer!!
		DateFormat formatter = Utils.getTimestampFormatter();
		Path from = ncmlFile.getFileLocation();
		Path to = timeSliceUtil.getFileLocation(timeSlice).resolve(from.getFileName());
		try {
			// Store the tmp ncml file for removal later
			tmpFileList.add(from);
			// Copy into ncml file into storagepool
			FileUtils.copy(from, to);
			// Set the new location in storage pool
			ncmlFile.setFileLocation(to);
		} catch (IOException e) {
			throw new TaskException(String.format(Constant.ERR_COPY_FILE_FAILED, from, to));
		}

		if (Utils.sendNcwmsRequest()) {
			// At the moment use the same info for dataset id and dataset title
			String creationTime = formatter.format(timeSlice.getCreated());
			Dataset ds = timeSliceDao.getParentDataset(timeSlice.getId());
			String ncmlId = String.format("%s:%s:%s", 
					ds.getName(),
					ds.getResolution(),
					creationTime);

			updateNcWmsConfig(ncWmsAdminUsername, ncWmsAdminPassword, ncmlId, ncmlId, ncmlFile.getFileLocation());
		}
	}

	/**
	 * Make a HTTP POST request to ncWMS to update config.xml with the new
	 * dataset.
	 * 
	 * @param ncWmsAdminUsername
	 *            The ncWMS admin username
	 * @param ncWmsAdminPassword
	 *            The ncWMS admin password
	 * @param datasetId
	 *            The dataset to be added
	 * @param datasetTitle
	 *            The dataset title to be added
	 * @param to
	 *            The dataset location
	 * @throws Exception
	 */
	private void updateNcWmsConfig(String ncWmsAdminUsername,
			String ncWmsAdminPassword, String datasetId, String datasetTitle,
			Path to) throws TaskException {

		log.debug("ncWMS dataset: {}", datasetId);
		log.debug("ncWMS file location: {}", to);

		// String url =
		// "http://uladev.in.vpac.org:8080/ncWMS/admin/addDatasetIntoConfig";
		String host = "localhost";
		int port = 8080;
		String url = "http://" + host + ":" + port
				+ "/ncWMS/admin/addDatasetIntoConfig";

		PostMethod method = new PostMethod(url);
		NameValuePair[] data = {
				new NameValuePair("dataset.new.id", datasetId),
				new NameValuePair("dataset.new.title", datasetId),
				new NameValuePair("dataset.new.location", to.toString()) };
		// Set the dataset to be added into ncWMS config.xml.
		method.setRequestBody(data);

		// // Provide custom retry handler is necessary
		// method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		// new DefaultHttpMethodRetryHandler(1, false));

		HttpClient client = new HttpClient();
		try {
			client.getParams().setAuthenticationPreemptive(true);
			Credentials defaultcreds = new UsernamePasswordCredentials(
					ncWmsAdminUsername, ncWmsAdminPassword);
			client.getState().setCredentials(
					new AuthScope(host, port, AuthScope.ANY_REALM),
					defaultcreds);

			// // To be avoided unless in debug mode
			// Credentials defaultcreds = new
			// UsernamePasswordCredentials(ncWmsAdminUsername,
			// ncWmsAdminPassword);
			// client.getState().setCredentials(AuthScope.ANY, defaultcreds);
			log.debug("Sent request to ncWMS: {}", url);

			// Execute the method.
			int statusCode = client.executeMethod(method);

			// Check if redirect to index.jsp
			if (statusCode != HttpStatus.SC_MOVED_TEMPORARILY) {
				String error = "Failed sending request to ncWMS, request status code "
						+ statusCode;
				log.error("Method failed: {}", method.getStatusLine());
				log.error(error);
				throw new TaskException(error);
			}
		} catch (HttpException e) {
			String error = "Fatal protocol violation";
			String msg = String.format(Constant.ERR_GENERIC_EXCEPTION, error, e.getMessage());
			throw new TaskException(msg);
		} catch (IOException e) {
			String error = "Fatal I/O error";
			String msg = String.format(Constant.ERR_GENERIC_EXCEPTION, error, e.getMessage());
			throw new TaskException(msg);
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
	}

	@Override
	public void rollback() {
		// Delete ncml file
		if(target != null && target.get().deleteIfExists()) {
			log.debug("CLEANUP: File deletion = {}", target.get().getFileLocation());
		}
	}

	@Override
	public void finalise() {
		for(Path tmpNcml: tmpFileList) {
			// Delete vrt file in temporary storage
			if(FileUtils.deleteIfExists(tmpNcml)) {
				log.debug("CLEANUP: File deletion = {}", tmpNcml);
			}						
		}		
	}

	public void setSource(List<TileBand> source) {
		this.source = source;
	}

	public List<TileBand> getSource() {
		return source;
	}

	public void setTarget(ScalarReceiver<GraphicsFile> target) {
		this.target = target;
	}

	public ScalarReceiver<GraphicsFile> getTarget() {
		return target;
	}

	public void setVrt(GraphicsFile vrtFile2) {
		this.vrtFile = vrtFile2;
	}

	public GraphicsFile getVrtList() {
		return vrtFile;
	}

	public void setTimeSlice(TimeSlice timeSlice) {
		this.timeSlice = timeSlice;
	}

	public TimeSlice getTimeSlice() {
		return timeSlice;
	}

	public Band getBand() {
		return band;
	}

	public void setBand(Band band) {
		this.band = band;
	}

	public List<Path> getTmpFileList() {
		return tmpFileList;
	}	

	/**
	 * Set the temporary location for storing temporary .ncml file.
	 * @param temporaryLocation The specified temporary location.
	 */
	public void setTemporaryLocation(Path temporaryLocation) {
		this.temporaryLocation = temporaryLocation;
	}
}
