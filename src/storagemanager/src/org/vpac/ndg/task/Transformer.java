package org.vpac.ndg.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.CommandUtil;
import org.vpac.ndg.application.Constant;
import org.vpac.ndg.common.StringUtils;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.rasterdetails.RasterDetails;
import org.vpac.ndg.rasterservices.ProcessException;
import org.vpac.ndg.storagemanager.GraphicsFile;

/**
 * The task of this class is to transform GraphicsFiles into the intended GraphicsFile.
 * In the target GraphicsFile, user could specify the intended projection, resolution, extents 
 * and format to transform to.  
 * 
 * @author hsumanto
 *
 */
public class Transformer extends Task {

	final private Logger log = LoggerFactory.getLogger(Transformer.class);

	private List<GraphicsFile> source;
	private GraphicsFile target;
	private Box extents;
	private boolean useBilinearInterpolation;
	private List<String> command;
	private String srcnodata;
	private String dstnodata;
	private RasterDetails datatype;

	private CommandUtil commandUtil;

	public Transformer() {
		super(Constant.TASK_DESCRIPTION_TRANSFORMER);
		commandUtil = new CommandUtil();
	}

	public Transformer(String description) {
		super(description);
		commandUtil = new CommandUtil();
	}
	
	@Override
	public void initialise() throws TaskInitialisationException {
		// Perform validation on source and target fields;
		if(getSource() == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
		}
		
		if(getTarget() == null) {
			throw new TaskInitialisationException(getDescription(), Constant.ERR_TARGET_DATASET_NOT_SPECIFIED);
		}	
		
	}
	
	@Override
	public void execute() throws TaskException {
		if(isCheckSource()) {
			if(getSource().isEmpty()) {
				// During import if no input images then throws exception
				throw new TaskException(getDescription(), Constant.ERR_NO_INPUT_IMAGES);
			}			
		}
		else {
			if(getSource().isEmpty()) {
				// During export if no input images, still allow transformation task to finish.
				// As there is no input images, gdalwarp won't be performed and
				// therefore the target image won't exist.
				// An example: when timeslice contains no tiles during export.
				log.debug("Source is empty; will not transform.");
				return;
			}
		}		

		// Initialize command parameter list
		command = new ArrayList<String>();

		command.add("gdalwarp");

		// Set the target projection (SRS) when applicable
		if(target.getEpsgId() > -1) {
			command.add("-t_srs");
			command.add("EPSG:" + target.getEpsgId());
		} else {
			if(target.getSrs() != null && !target.getSrs().isEmpty()) {
				command.add("-t_srs");
				command.add(target.getSrs());							
			}
		}

		// Override the target extents when applicable
		if(getExtents() != null) {
			command.add("-te");
			command.add(Double.toString(getExtents().getXMin()));
			command.add(Double.toString(getExtents().getYMin()));
			command.add(Double.toString(getExtents().getXMax()));
			command.add(Double.toString(getExtents().getYMax()));
		}
		else {
			// Target extents
			if (getTarget().getBounds() != null) {			
				command.add("-te");
				command.add(Double.toString(getTarget().getBounds().getXMin()));
				command.add(Double.toString(getTarget().getBounds().getYMin()));
				command.add(Double.toString(getTarget().getBounds().getXMax()));
				command.add(Double.toString(getTarget().getBounds().getYMax()));
			}
		}

		// Set the target resolution when applicable
		if (getTarget().getResolution() != null) {
			command.add("-tr");
			command.add(Double.toString(getTarget().getResolution().toDouble()));
			command.add(Double.toString(getTarget().getResolution().toDouble()));
		}

		// Sampling method
		command.add("-r");
		if (useBilinearInterpolation)
			command.add("bilinear");
		else
			command.add("near");

		// Output format (e.g. netCDF, GTiff)
		command.add("-of");
		command.add(target.getFormat().toGdalString());

		if (datatype != null) {
			command.add("-ot");
			command.add(datatype.getGdalDataType());
		}

		// Override srcnodata metadata (or lack thereof)
		if (srcnodata != null && !srcnodata.isEmpty()) {
			command.add("-srcnodata");
			command.add(srcnodata);
		}

		// Override dstnodata metadata (or lack thereof)
		if (dstnodata != null && !dstnodata.isEmpty()) {
			command.add("-dstnodata");
			command.add(dstnodata);
		}

		// Add extra options such as Mosaicking options, Memory management options
		addExtraOptions(command);

		// Creation options (e.g. compression)
		for (String co : target.getFormat().getCreationOptions()) {
			command.add("-co");
			command.add(co);
		}

		// Input files
		boolean sourceExist = false;
		for(GraphicsFile gf : source) {
			// If graphic file not found, don't add it into gdalwarp sourcefiles
			if(!gf.exists()) {
				continue;
			}
			sourceExist = true;
			command.add(gf.getFileLocation().toString());
		}

		// Extra check here to capture missing parameter earlier
		if(!sourceExist) {
			String strSource = StringUtils.join(source, "\n");
			throw new TaskException(getDescription(), "Source file not exist:\n" + strSource);
		}

		// Output file
		command.add(target.getFileLocation().toString());	

		try {
			// If source is raster dataset							
			// If source is vector dataset
			commandUtil.start(command);			
		} catch (ProcessException | InterruptedException | IOException e) {
			throw new TaskException(getDescription(), e);
		} 
	}

	@Override
	public void rollback() {
		// Remove the transformed image from temporary storage		
		if(target.deleteIfExists()) {
			log.trace("Deleted {}", target);
		}		
	}

	@Override
	public void finalise() {
		if(isCleanupSource()) {
			// Remove the source images from upload or temporary storage
			for(GraphicsFile sourceImage: source) {
				// Delete source image
				if(sourceImage.deleteIfExists()) {
					log.trace("Deleted {}", sourceImage);
				}
			}
		}

		if(isCleanupTarget()) {
			// Remove the transformed image from temporary storage
			if(target.deleteIfExists()) {
				log.trace("Deleted {}", target);
			}
		}
	}

	public boolean isUseBilinearInterpolation() {
		return useBilinearInterpolation;
	}

	public void setUseBilinearInterpolation(boolean useBilinearInterpolation) {
		this.useBilinearInterpolation = useBilinearInterpolation;
	}

	public void setSource(List<GraphicsFile> source) {
		this.source = source;
	}
	
	public List<GraphicsFile> getSource() {
		return source;
	}

	public void setTarget(GraphicsFile target) {
		this.target = target;
	}

	public GraphicsFile getTarget() {
		return target;
	}	
	
	public Box getExtents() {
		return extents;
	}

	public void setExtents(Box extents) {
		this.extents = extents;
	}

	public void addExtraOptions(List<String> args) {
		// SKIP_NOSOURCE=YES/NO: Skip all processing for chunks for which there is no corresponding input data. 
		// This will disable initializing the destination (INIT_DEST) and all other processing, and so should be used careful. 
		// Mostly useful to short circuit a lot of extra work in mosaicing situations.		

		// NOTE: This needs to be disable for netCDF format, as on some cases this option create the below error.
		// ERROR 1: netCDF scanline fetch failed: NetCDF: Operation not allowed in define mode
		// Mosaicing options (when dealing with multiple files)
		args.add("-wo");
		args.add("SKIP_NOSOURCE=YES");

		// Memory management options
		args.add("--config");
		args.add("GDAL_CACHEMAX");
		args.add(Constant.GDAL_CACHEMAX_IN_MB);
		args.add("-wm");
		args.add(Constant.GDAL_CACHEMAX_IN_MB);
	}

	public String getSrcnodata() {
		return srcnodata;
	}

	public void setSrcnodata(String srcnodata) {
		this.srcnodata = srcnodata;
	}

	public String getDstnodata() {
		return dstnodata;
	}

	public void setDstnodata(String dstnodata) {
		this.dstnodata = dstnodata;
	}

	public RasterDetails getDatatype() {
		return datatype;
	}

	public void setDatatype(RasterDetails datatype) {
		this.datatype = datatype;
	}	
}
