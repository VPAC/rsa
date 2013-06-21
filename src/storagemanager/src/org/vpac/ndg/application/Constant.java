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

package org.vpac.ndg.application;

/**
 * This class stores all string constants used.
 * @author hsumanto
 *
 */
public class Constant {
	public static final String TOOL_IMPORT = "rsa data import";
	public static final String TOOL_EXPORT = "rsa data export";

	public static final String TASK_DESCRIPTION_TRANSLATOR = "Translating to different format";
	public static final String TASK_DESCRIPTION_TRANSFORMER = "Transforming to internal projection";
	public static final String TASK_DESCRIPTION_TILERASTERISER = "Determing tile extents";
	public static final String TASK_DESCRIPTION_IMAGETILEBANDCREATOR = "Making tilebands out of image and available bands";
	public static final String TASK_DESCRIPTION_TILEBANDCREATOR = "Making tilebands out of tile extents and available bands";
	public static final String TASK_DESCRIPTION_TILETRANSFORMER = "Cutting each tile based on extents and band";
	public static final String TASK_DESCRIPTION_VRTBUILDER = "Making a VRT file on newly created tiles";
	public static final String TASK_DESCRIPTION_TILEAGGREGATOR = "Making a spatial aggregation on newly created tiles";
	public static final String TASK_DESCRIPTION_NCMLBUILDER = "Aggregating into NCML";
	public static final String TASK_DESCRIPTION_TILEUPDATER = "Updating/Inserting tiles into dataset";
	public static final String TASK_DESCRIPTION_COMMITTER = "Committing tiles into storagepool";
	public static final String TASK_DESCRIPTION_COMPRESSOR = "Packaging output into a zip file";
	public static final String TASK_DESCRIPTION_NOOP = "No operation";
	
	public static final String GDAL_CACHEMAX_IN_MB = "1000";
	public static final String UNKNOWN = "unknown";
	public static final String EXT_VRT = ".vrt";
	public static final String EXT_NCML = ".ncml";
	public static final String SUFFIX_COMPOSITE = "_composite";
	public static final String SUFFIX_TMP = "_tmp";
	public static final String SUFFIX_OLD = "_old";
	public static final String TRANSFORM_NC = "transform.nc";	
	
	public static final String PREFIX_TMPDIR = "tmpDir";
	public static final String EMPTY = "";
	
	// Command-line arguments 
	public static final String CMD_LINE_OPTION_VERBOSE = "v";
	public static final String CMD_LINE_OPTION_NAME = "name";
	public static final String CMD_LINE_OPTION_RESOLUTION = "tr";
	public static final String CMD_LINE_OPTION_TARGET_SRS = "t_srs";
	public static final String CMD_LINE_OPTION_SOURCE_SRS = "s_srs";
	public static final String CMD_LINE_OPTION_TARGET_DSTNODATA = "dstnodata";
	public static final String CMD_LINE_OPTION_DOMAIN = "domain";
	public static final String CMD_LINE_OPTION_INPUT = "i";
	public static final String CMD_LINE_OPTION_TIME = "time";
	public static final String CMD_LINE_OPTION_LAYER = "l";
	public static final String CMD_LINE_OPTION_ATTRIBUTE = "a";
	public static final String CMD_LINE_OPTION_ABSTRACT = "ab";
	public static final String CMD_LINE_OPTION_TIMESLICE_ABSTRACT = "pfab";	
	
	public static final String CMD_LINE_OPTION_DESCRIPTION_VERBOSE = "verbose description";
	public static final String CMD_LINE_OPTION_DESCRIPTION_NAME = "input dataset name";
	public static final String CMD_LINE_OPTION_DESCRIPTION_INPUT = "input dataset";
	public static final String CMD_LINE_OPTION_DESCRIPTION_SOURCE_SRS = "source spatial reference system (e.g. \"EPSG:3112\")";
	public static final String CMD_LINE_OPTION_DESCRIPTION_TARGET_SRS = "target spatial reference system (e.g. \"EPSG:3112\")";
	public static final String CMD_LINE_OPTION_DESCRIPTION_DSTNODATA = "target nodata";
	public static final String CMD_LINE_OPTION_DESCRIPTION_TIME = "acquisition time";	
	public static final String CMD_LINE_OPTION_DESCRIPTION_LAYER = "layer id ( vector only )";	
	public static final String CMD_LINE_OPTION_DESCRIPTION_ATTRIBUTE = "attribute ( vector only )";
	public static final String CMD_LINE_OPTION_DESCRIPTION_RESOLUTION = "resolution";
	public static final String CMD_LINE_OPTION_DESCRIPTION_DOMAIN = "domain type";
	public static final String CMD_LINE_OPTION_DESCRIPTION_ABSTRACT = "input dataset abstract";	
	public static final String CMD_LINE_OPTION_DESCRIPTION_TIMESLICE_ABSTRACT = "time slice abstract for this import";	

	public static final String ERR_PARAMETER_NOT_SPECIFIED = "Required [-%s %s] parameter not specified.";
	public static final String ERR_SOURCE_DATASET_NOT_SPECIFIED = "Source dataset not specified.";
	public static final String ERR_TARGET_DATASET_NOT_SPECIFIED = "Target dataset not specified.";
	public static final String ERR_TIMESLICE_NOT_SPECIFIED = "TimeSlice not specified.";
	public static final String ERR_TIMESLICE_PARENT_NOT_SPECIFIED = "TimeSlice parent dataset not specified.";
	public static final String ERR_DATASET_BANDS_NOT_SPECIFIED = "Dataset bands not specified.";
	public static final String ERR_NO_INPUT_IMAGES = "The set of input images is empty.";
	public static final String ERR_TILE_NOT_EXIST = "Tile [%s] doesn't exist.";
	public static final String ERR_PARAMETER_NOT_VALID = "Invalid [-%s %s] parameter = %s specified.";
	public static final String ERR_INTERNAL_STORAGE_EPSG_ID_NOT_VALID = "Invalid internal storage 'EPSG ID' = %s specified.";	
	public static final String ERR_FILEPATH_INVALID = "Invalid filepath = %s/%s specified.";	
	public static final String ERR_COPY_FILE_FAILED = "Failed to copy source file = [%s] into target file = [%s].";
	public static final String ERR_MOVE_FILE_FAILED = "Failed to rename source file = [%s] into target file = [%s].";
	public static final String ERR_TASK_INITIALISATION_FAILED = "Failed to initialise task";
	public static final String ERR_INNER_TASK_INITIALISATION_FAILED = "Failed to initialise inner pipeline task";
	public static final String ERR_TASK_EXECUTION_FAILED = "Failed to execute task.";
	public static final String ERR_ILLEGAL_ARGUMENT = "Illegal argument specified.";
	public static final String ERR_READ_CONFIGURATION = " READ CONFIG [%s] failed.\nDETAILS: %s";
	public static final String ERR_TASK_INITIALISATION_EXCEPTION = "TASK INIT [%s] failed.\nDETAILS: %s";
	public static final String ERR_TASK_EXCEPTION = "TASK [%s] failed.\nDETAILS: %s";
	public static final String ERR_GENERIC_EXCEPTION = "Error encountered: %s.\nDETAILS: %s";

	public static final String INFO_JOB_PROGRESS = "Job Progress";

	public static final String DEBUG_DIRECTORY_DELETION = "CLEANUP: Directory deletion = %s";
	public static final String DEBUG_INNER_PIPELINE = "-->";	
	public static final String DEBUG_SINGLE_LINE = "--------------------------------------------------------------------------------";	
	public static final String DEBUG_DOUBLE_LINE = "================================================================================";	

	/**
	 * Get a string notifying that the given parameter is not specified.
	 * @param parameterName The given parameter name.
	 * @param parameterDesc The given parameter description.
	 * @return Returns a string notifying that the given parameter is not specified.
	 */
	public static String getParameterNotSpecifiedString(String parameterName, String parameterDesc) {
		return String.format(Constant.ERR_PARAMETER_NOT_SPECIFIED, parameterName, parameterDesc);
	}

	/**
	 * Get a string notifying that a specified parameter is invalid.
	 * @param parameterName The given parameter name.
	 * @param parameterDesc The given parameter description.
	 * @param parameterValue The given parameter invalid value.
	 * @return Returns a string notifying that a specified parameter is invalid.
	 */
	public static String getInvalidParameterSpecifiedString(String parameterName, String parameterDesc, String parameterValue) {
		return String.format(Constant.ERR_PARAMETER_NOT_VALID, parameterName, parameterDesc, parameterValue);
	}	
}
