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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.storagemanager.GraphicsFile;

public class Compressor extends Task {

	final Logger log = LoggerFactory.getLogger(Compressor.class);

	private final int BUF_SIZE = 2048;

	private List<Path> sourcePaths;
	private List<List<GraphicsFile>> sourceGraphicsFiles;
	private String zipDirName;
	private Path target;

	public Compressor() {
		super(Constant.TASK_DESCRIPTION_COMPRESSOR);
		sourcePaths = new ArrayList<>();
		sourceGraphicsFiles = new ArrayList<>();
	}

	@Override
	public void initialise() throws TaskInitialisationException {
		if (sourcePaths == null && sourceGraphicsFiles == null) {
			throw new TaskInitialisationException("No source files specified");
		}
	}

	@Override
	public void execute() throws TaskException {
		// Collate all source files.
		List<Path> paths = new ArrayList<>();
		for (List<GraphicsFile> gs : sourceGraphicsFiles) {
			for (GraphicsFile g : gs) {
				// Ignore file if it doesn't exist
				if (!g.exists()) {
					log.info("Non-existent file excluded from compression process:\n{}", g.getFileLocation());
					continue;
				}
				
				paths.add(g.getFileLocation());
			}
		}
		paths.addAll(sourcePaths);
		if (paths.size() == 0) {
			throw new TaskException("No files to compress");
		}

		// Compress them!
		log.debug("Compressing files into {}", target);

		Path zipDirectory = Paths.get(zipDirName);
		ZipEntry entry;
		try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
				new FileOutputStream(target.toFile())))) {

			byte data[] = new byte[BUF_SIZE];

			for (Path p : paths) {
				log.trace("Compressing {}", p);
				try (BufferedInputStream in = new BufferedInputStream(
						new FileInputStream(p.toFile()), BUF_SIZE)) {

					// Create an entry in the zip file. Note that only the file
					// names are used; thus the files are assumed to be all in
					// one directory. This will need to be reworked if
					// subdirectories are required. Note also that a virtual
					// directory is created inside the zip file to avoid
					// creating a zip bomb.
					Path entryPath = zipDirectory.resolve(p.getFileName());
					entry = new ZipEntry(entryPath.toString());
					out.putNextEntry(entry);

					// Stream data to zip file.
					int count;
					while ((count = in.read(data, 0, BUF_SIZE)) != -1) {
						   out.write(data, 0, count);
					}
				} catch (IOException e) {
					throw new TaskException("Could not read from input file " +
							"during compression", e);
				}
			}
		} catch (IOException e) {
			throw new TaskException("Could not write to zip file", e);
		}
	}

	@Override
	public void rollback() {
		try {
			if (target != null)
				Files.deleteIfExists(target);
		} catch (IOException e) {
			log.error("Failed to delete output file: {}", e);
		}
	}

	@Override
	public void finalise() {
		// Nothing to do.
	}

	/**
	 * @param sources A bunch of files to be compressed.
	 */
	public void addSource(List<GraphicsFile> sources) {
		sourceGraphicsFiles.add(sources);
	}

	/**
	 * @param source a literal path to be compressed.
	 */
	public void addSource(Path source) {
		sourcePaths.add(source);
	}

	public Path getTarget() {
		return target;
	}

	public void setTarget(Path target) {
		this.target = target;
	}

	public String getZipdirname() {
		return zipDirName;
	}

	public void setZipDirName(String zipdirname) {
		this.zipDirName = zipdirname;
	}
}
