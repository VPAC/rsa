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

package org.vpac.ndg;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.configuration.NdgConfigManager;
import org.vpac.ndg.storage.util.DatasetUtil;
import org.vpac.ndg.storage.model.Dataset;

public class FileUtils {

	final static Logger log = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * Copy source file into target file if the target file exists, 
	 * then the target file is replaced.
	 * @param from The source file.
	 * @param to The target file.
	 * @return Return the path to the target file.
	 * @throws IOException
	 */
	public static Path copy(Path from, Path to) throws IOException {
		return Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
	}	

	/**
	 * Move source file into target file if the target file exists, 
	 * then the target file is replaced.
	 * @param from The source file.
	 * @param to The target file.
	 * @return Return the path to the target file.
	 * @throws IOException
	 */
	public static Path move(Path from, Path to) throws IOException {
		return Files.move(from, to, StandardCopyOption.REPLACE_EXISTING);
	}		

	/**
	 * Create a temporary directory in the default temporary storagepool.
	 * 
	 * @return The path to the newly created temporary directory in the default
	 *         temporary storagepool.
	 * @throws IOException If there is IO error when creating the temporary
	 * directory.
	 * @see DatasetUtil#getPath(Dataset)
	 * @see FileUtils#getTargetLocation(String)
	 */
	public static Path createTmpLocation() throws IOException {

		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		NdgConfigManager ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");

		Path defaultTmpPool = FileSystems.getDefault().getPath(ndgConfigManager.getConfig().getDefaultTmpPool());	
		return Files.createTempDirectory(defaultTmpPool, Constant.PREFIX_TMPDIR);
	}

	/**
	 * @param taskId
	 *            The ID of the task that would write to the directory.
	 * @return The path to the directory owned by the task. The path may need to
	 *         be created before it can be used.
	 * @see FileUtils#createTmpLocation()
	 * @see FileUtils#createDirectory(Path)
	 * @see DatasetUtil#getPath(Dataset)
	 */
	public static Path getTargetLocation(String taskId) {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		NdgConfigManager ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");

		return Paths.get(ndgConfigManager.getConfig().getDefaultPickupLocation(), taskId);
	}

	/**
	 * Remove the given file.
	 * @param filepath The given file path.
	 * @return true if the file has been successfully deleted.
	 */
	public static boolean deleteIfExists(Path filepath) {	
		boolean bOK;
		try {
			bOK = Files.deleteIfExists(filepath);
		} catch (IOException e) {
			bOK = false;
		}
		return bOK;
	}

	/**
	 * Create the specified directory.
	 * @param directory The given directory.
	 * @return Returns the created directory.
	 * @throws IOException
	 */
	public static boolean createDirectory(Path directory) throws IOException {
		directory = Files.createDirectory(directory);
		return Files.exists(directory);
	}		
	
	/**
	 * Remove a directory and all of its contents.
	 * 
	 * @param directory
	 *            The directory to be removed.
	 * @throws IOException
	 *             If there was an error deleting the file tree. In this case,
	 *             some of the files may have already been deleted.
	 */
	public static void removeDirectory(Path directory) throws IOException {
		final Path canonicalPath = directory.toAbsolutePath().normalize();
		if (canonicalPath.equals(canonicalPath.getRoot()))
			throw new IOException("Refusing to delete root directory.");

		Files.walkFileTree(canonicalPath, new SimpleFileVisitor<Path>() {
			/**
			 * Delete files.
			 */
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			/**
			 * Delete directory after files have been removed (above).
			 */
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException e)
					throws IOException {
				if (e == null) {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				} else {
					// directory iteration failed
					throw e;
				}
			}
		});
	}
	
	/**
	 * Remove a directory and all of its contents.
	 * 
	 * @param directory
	 *            The directory to be removed.
	 * @throws IOException
	 *             If there was an error deleting the file tree. In this case,
	 *             some of the files may have already been deleted.
	 */
	public static void removeDirectory(File directory) throws IOException {	
		removeDirectory(directory.toPath());
	}

	/**
	 * Remove a directory and all of its contents.
	 * 
	 * @param directory
	 *            The directory to be removed.
	 * @throws IOException
	 *             If there was an error deleting the file tree. In this case,
	 *             some of the files may have already been deleted.
	 */
	public static void removeDirectory(String directory) throws IOException {		
		removeDirectory(new File(directory));
	}

	/**
	 * Remove the contents of a directory, but not the directory itself.
	 * 
	 * @param directory
	 *            The directory whose contents to be removed.
	 * @throws IOException
	 *             If there was an error deleting the file tree. In this case,
	 *             some of the files may have already been deleted.
	 */
	public static void removeDirectoryContents(final Path directory) throws IOException {
		final Path canonicalPath = directory.toAbsolutePath().normalize();
		if (canonicalPath.equals(canonicalPath.getRoot()))
			throw new IOException("Refusing to delete root directory.");

		Files.walkFileTree(canonicalPath, new SimpleFileVisitor<Path>() {
			/**
			 * Delete files.
			 */
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			/**
			 * Delete directory after files have been removed (above) - unless
			 * it's the root directory.
			 */
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException e)
					throws IOException {
				if (canonicalPath.equals(dir)) {
					// don't delete base directory
					return FileVisitResult.TERMINATE;
				}

				if (e == null) {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				} else {
					// directory iteration failed
					throw e;
				}
			}
		});
	}

	/**
	 * Remove the contents of a directory, but not the directory itself.
	 * 
	 * @param directory
	 *            The directory whose contents to be removed.
	 * @throws IOException
	 *             If there was an error deleting the file tree. In this case,
	 *             some of the files may have already been deleted.
	 */
	public static void removeDirectoryContents(File directory)
			throws IOException {
		removeDirectoryContents(directory.toPath());
	}

	/**
	 * Remove the contents of a directory, but not the directory itself.
	 * 
	 * @param directory
	 *            The directory whose contents to be removed.
	 * @throws IOException
	 *             If there was an error deleting the file tree. In this case,
	 *             some of the files may have already been deleted.
	 */
	public static void removeDirectoryContents(String directory)
			throws IOException {
		removeDirectoryContents(new File(directory));
	}

	/**
	 * Get the acquisition time the specified filepath.
	 * @param filePath The given filepath.
	 * @return Returns acquisition time if found otherwise null.
	 */
	public static Date getAcquisitionTime(Path filePath) {
		Date result = null;				
		String datetimeStr = getAcquisitionTimeStr(filePath);		

		result = Utils.parseDate(datetimeStr);		
		return result;
	}	

	// e.g. LS5_TM_OTH_P51_GALPGS01-002_095_082_20100209_B10.nc == 20100209
	protected static Pattern LANDSAT_TIME_PATTERN = Pattern.compile(
			".*_([0-9]{8})_B[0-9]+.[a-zA-Z]+$");

	/**
	 * Get the acquisition time string from the specified filepath.
	 * @param filePath The given filepath.
	 * @return Returns acquisition time string if found otherwise null.
	 */
	public static String getAcquisitionTimeStr(Path filePath) {
		String fileNameStr = filePath.getFileName().toString();

		Matcher m = LANDSAT_TIME_PATTERN.matcher(fileNameStr);
		if (m.matches()) {
			return m.group(1);
		}

		throw new IllegalArgumentException(String.format(
				"Could not find date/time in file name %s.", fileNameStr));
	}	

	// e.g. LS5_TM_OTH_P51_GALPGS01-002_095_082_20100209_B10.nc == B10
	protected static Pattern LANDSAT_BAND_PATTERN = Pattern.compile(
			".*_[0-9]{8}_(B[0-9]+).[a-zA-Z]+$");

	/**
	 * Get the band name string from the specified filepath.
	 * @param filePath The given file path.
	 * @return Returns band name if found otherwise null.
	 */
	public static String getBandNameStr(Path filePath) {
		String fileNameStr = filePath.getFileName().toString();

		Matcher m = LANDSAT_BAND_PATTERN.matcher(fileNameStr);
		if (m.matches()) {
			return m.group(1);
		}

		throw new IllegalArgumentException(String.format(
				"Could not find band name in file name %s.", fileNameStr));
	}
}
