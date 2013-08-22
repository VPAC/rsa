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

package org.vpac.ndg.task;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.common.datamodel.GdalFormat;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.storagemanager.GraphicsFile;

/**
 * This class responsible to translate an image from one format to another
 * format.
 * 
 * @author hsumanto
 * 
 */
public class ImageTranslator extends Translator {

	final private Logger log = LoggerFactory.getLogger(ImageTranslator.class);

	private Path srcFile;
	private Path dstFile;
	private GdalFormat format;
	private int layerIndex = -1;

	@Override
	public void prepareCommand() {
		if (layerIndex != -1) {
			getCommand().add("-b");
			getCommand().add("" + layerIndex);
		}

		// Re-scale the input pixels values from the range of the source data
		getCommand().add("-scale");

		super.prepareCommand();
		log.debug("{}", getCommand());
	}

	@Override
	public void initialise() throws TaskInitialisationException {
		setSource(new GraphicsFile(srcFile));
		setTarget(new GraphicsFile(dstFile));
		// Disallow -a_srs option to be set during translation
		getTarget().setEpsgId(-1);
		getTarget().setFormat(getFormat());
		setCleanupSource(false);
		setCleanupTarget(false);
		super.initialise();
	}

	@Override
	public void execute() throws TaskException {
		if (getLayerIndex() < 1) {
			// If invalid layer index specified then throws exception
			throw new TaskException(getDescription(),
					"Input layer/band should starts from 1 not from "
							+ getLayerIndex());
		}

		if (!getSource().exists()) {
			// If no input image then throws exception
			throw new TaskException(getDescription(),
					"Source file not exist:\n" + getSource().getFileLocation());
		}

		initialiseCommand();
		prepareCommand();
		executeCommand();
	}

	public Path getSrcFile() {
		return srcFile;
	}

	public void setSrcFile(Path srcFile) {
		this.srcFile = srcFile;
	}

	public Path getDstFile() {
		return dstFile;
	}

	public void setDstFile(Path dstFile) {
		this.dstFile = dstFile;
	}

	public GdalFormat getFormat() {
		return format;
	}

	public void setFormat(GdalFormat format) {
		this.format = format;
	}

	public int getLayerIndex() {
		return layerIndex;
	}

	public void setLayerIndex(int layerIndex) {
		this.layerIndex = layerIndex;
	}

}
