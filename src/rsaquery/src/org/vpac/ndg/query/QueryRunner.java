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

package org.vpac.ndg.query;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.NetcdfFileWriter.Version;

/**
 * Command line application for running queries.
 * @author Alex Fraser
 */
public class QueryRunner {

	static final Logger log = LoggerFactory.getLogger(QueryRunner.class);

	/**
	 * Run a query from XML.
	 */
	public static void run(File config, File output) throws Exception {
		QueryDefinition qd = QueryDefinition.fromXML(config);
		File projectRoot = config.getParentFile();
		if (projectRoot == null)
			projectRoot = new File(".");

		log.debug("Running query {}", config);
		log.debug("Opening output file {}", output);

		NetcdfFileWriter outputDataset = NetcdfFileWriter.createNew(
				Version.netcdf4, output.getAbsolutePath());

		try {
			Query q = new Query(outputDataset);
			q.setMemento(qd, projectRoot.getAbsolutePath());
			try {
				q.run();
			} finally {
				q.close();
			}
		} finally {
			// FIXME : Need to request to fix NetCdf java library 4.3.16 - currently throws null pointer exception
			if (!outputDataset.isDefineMode())
				outputDataset.close();
		}
	}

	private static final String USAGE =
			"Usage: rsaquery <config file> <output file>";

	/**
	 * This class may be used to execute arbitrary queries from the command
	 * line. Accepts one argument: the file path of the query definition as XML.
	 */
	public static void main(String[] args) {
		File config;
		File output;
		try {
			config = new File(args[0]);
		} catch (IndexOutOfBoundsException e) {
			System.err.println(USAGE);
			System.err.println(
					"Please provide the path to a query defintion file.");
			System.exit(1);
			return;
		}
		try {
			output = new File(args[1]);
		} catch (IndexOutOfBoundsException e) {
			System.err.println(USAGE);
			System.err.println("Please provide the path to an output dataset.");
			System.exit(1);
			return;
		}

		try {
			run(config, output);
			System.out.println("Query finished.");
		} catch (QueryConfigurationException e) {
			System.err.println("Configuration error: " + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Query failed: " + e.getMessage());
			System.exit(1);
		} catch (Exception e) {
			System.err.println("Query failed: " + e.getMessage());
			e.printStackTrace();
			System.exit(2);
		}
	}
}
