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
