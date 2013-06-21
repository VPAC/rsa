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

package org.vpac.ndg.cli.smadaptor.local;

import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.cli.smadaptor.DataQuery;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.query.Query;
import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.QueryDefinition;
import org.vpac.ndg.storage.dao.JobProgressDao;

import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.NetcdfFileWriter.Version;

public class LocalDataQuery implements DataQuery {

	static final Logger log = LoggerFactory.getLogger(LocalDataQuery.class);

	Path queryPath;
	Integer threads;
	Path outputPath;
	private Box extent;
	private String startDate;
	private String endDate;
	private String netcdfVersion;

	@Autowired
	JobProgressDao jobProgressDao;

	@Override
	public void setQueryPath(Path definition) {
		this.queryPath = definition;
	}

	@Override
	public void setThreads(String threads) {
		this.threads = Integer.parseInt(threads);
	}

	@Override
	public void setExtents(String x1, String y1, String x2, String y2) {
		this.extent = new Box(Double.parseDouble(x1), Double.parseDouble(y1), Double.parseDouble(x2), Double.parseDouble(y2));
	}

	@Override
	public void setTimeRange(String start, String end) {
		this.startDate = start;
		this.endDate = end;
	}

	@Override
	public String start() throws IOException {
		final QueryDefinition qd = QueryDefinition.fromXML(queryPath.toFile());
		if (extent != null) {
			qd.output.grid.bounds = String.format("%f %f %f %f",
					extent.getXMin(), extent.getYMin(),
					extent.getXMax(), extent.getYMax());
		}

		if (startDate != null) {
			qd.output.grid.timeMin = startDate;
			qd.output.grid.timeMax = endDate;
		}

		final QueryProgress qp = new QueryProgress(jobProgressDao);
		String taskId = qp.getTaskId();

		log.info("--of {}", netcdfVersion);
		Version version;
		if (netcdfVersion != null){
			if (netcdfVersion.equals("nc3")) {
				version = Version.netcdf3;
			} else if (netcdfVersion.equals("nc4")) {
				version = Version.netcdf4;
			} else {
				throw new IllegalArgumentException(
						String.format("Unrecognised format %s", netcdfVersion));
			}
		} else {
			version = Version.netcdf4;
		}
		final Version ver = version;
		log.info("Netcdf version: {}", ver);

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					execute(qd, qp, ver);
				} catch (Exception e) {
					qp.setErrorMessage(e.getMessage());
					log.error("Task exited abnormally: ", e);
				}
			}
		});

		thread.start();
		return taskId;
	}

	private void execute(QueryDefinition qd, QueryProgress qp, Version version)
			throws IOException, QueryConfigurationException {
		NetcdfFileWriter outputDataset = NetcdfFileWriter.createNew(
				version, outputPath.toString());

		try {
			Query q = new Query(outputDataset);
			if (threads != null)
				q.setNumThreads(threads);
			q.setMemento(qd, "rsa:");
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

	@Override
	public void setOutputPath(Path outputFile) {
		this.outputPath = outputFile;
	}

	public String getNetcdfVersion() {
		return netcdfVersion;
	}

	public void setNetcdfVersion(String netcdfVersion) {
		this.netcdfVersion = netcdfVersion;
	}

}
