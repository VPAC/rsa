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

package org.vpac.ndg.common.datamodel;

/**
 * lists all the JSON variables used in NDG
 * @author lachlan
 * @author adfries
 */
public class JsonFields
{
	public enum ProjectionCategory {
		DEFAULT,
		EXTRINSIC,
		INTRINSIC;
	}
	
	public static final String PARAM_NAME_USER = "user";
	public static final String PARAM_NAME_TYPE = "type";
	public static final String PARAM_NAME_ID = "id";

	
	public static final String PARAM_NAME_START_INDEX = "StartIndex";
	public static final String PARAM_NAME_COUNT = "Count";
	public static final String PARAM_NAME_PROGRESS_ID = "ProgressId";
	
	public static final String JSON_SESSION_ID = "SessionId";
	
	public static final String JSON_USER_PASSWORD_OLD = "Old Password";
	
	public static final String JSON_LOG_ID = "Log Id";
	public static final String JSON_LOG_TIMESTAMP = "Log Timestamp";
	public static final String JSON_LOG_USER = "Log User";
	public static final String JSON_LOG_ACTION = "Log Action";
	
	public static final String JSON_USER = "User";
	public static final String JSON_USERS = "Users";
	public static final String JSON_USER_ID = "User Id";
	public static final String JSON_USER_FIRST_NAME = "User First Name";
	public static final String JSON_USER_LAST_NAME = "User Last Name";
	public static final String JSON_USER_EMAIL = "User Email Address";
	public static final String JSON_USER_PERMISSION_SET = "User Permission Set Id";
	public static final String JSON_USER_ACTIVE_USER = "Active User";
	public static final String JSON_USER_PASSWORD = "User pw";
	
	public static final String JSON_USER_PERM = "User Permission";
	public static final String JSON_USER_PERM_ID = "User Permission Set Id";
	public static final String JSON_USER_PERM_NAME = "User Permission Set Name";
	public static final String JSON_USER_PERM_DESC = "User Permission Set Desc";
	public static final String JSON_USER_PERM_CAN_UPLOAD = "User Permission Set Can Upload";
	public static final String JSON_USER_PERM_CAN_DOWNLOAD = "User Permission Set Can Download";
	public static final String JSON_USER_PERM_CAN_DELETE = "User Permission Set Can Delete";
	public static final String JSON_USER_PERM_IS_ADMIN = "User Permission Set Is Admin";
	
	public static final String JSON_STORAGE_CONF = "Storage Configuration";

	public static final String JSON_PROGRESS = "ProgressItem";
	public static final String JSON_PROGRESS_ID = "Progress Id";
	public static final String JSON_PROGRESS_TYPE = "Progress Type";
	public static final String JSON_PROGRESS_USER = "Progress User";
	public static final String JSON_PROGRESS_VALUE = "Progress Value" ;
	public static final String JSON_PROGRESS_DESCRIPTION = "Progress Description";
	public static final String JSON_PROGRESS_COMPLETED = "Progress Completed";
	public static final String JSON_PROGRESS_CANCELLED = "progress Cancelled";
	public static final String JSON_PROGRESS_FILE_NAME_SERVER = "Server File Name";

	public static final String JSON_FILE_INFO_ID = "File Info requested";
	public static final String JSON_SERVER_FILE_NAME = "Server File Name";
	
	public static final String JSON_SM_OBJ_ID = "smid";
	public static final String JSON_SM_IMAGE_LOCATION = "smloc";
	public static final String JSON_SM_RESOLUTION = "smres";
	public static final String JSON_SM_DOMAIN_TYPE = "smrsmpl"; // enum DomainType
	public static final String JSON_SM_FORMAT = "smformat"; // enum Format
	public static final String JSON_SM_IMAGES = "images";
	public static final String JSON_SM_PARTIAL_FRAMES = "smpartframe";
	public static final String JSON_SM_MD_NAME = "smmdname";
	public static final String JSON_SM_MD_TYPE = "smmetadata";
	public static final String JSON_SEARCHLIST = "smsearchlist";
	public static final String JSON_SM_MD_PARENT = "smmdparent";
	public static final String JSON_SM_EXT_LEFT = "smleft";
	public static final String JSON_SM_EXT_RIGHT = "smright";
	public static final String JSON_SM_EXT_TOP = "smtop";
	public static final String JSON_SM_EXT_BOTTOM = "smbottom";
	public static final String JSON_SM_RASTERIZE_PROFILE = "smrprofile";
	
	public static final String JSON_SM_LAYER_NAME = "smlayer";
	public static final String JSON_SM_LAYER_FIELD = "smfield";
	public static final String JSON_SM_LAYER_FIELD_TYPE = "smfieldtype";
	public static final String JSON_SM_LAYER_FIELD_VALUES = "smfieldvalues";
	public static final String JSON_SM_DATA_ABSTRACT = "smdataabstract";
	public static final String JSON_SM_CREATED_DATE = "smcreateddate";
	public static final String JSON_SM_OBJ_DESCRIPTION = "description";
	public static final String JSON_FIELD_NAME = "smfieldvaluename";
	public static final String JSON_FIELD_VALUE = "smfieldvalue";
	public static final String JSON_FIELD_INCLUDED = "smfieldvaluestatus";
	public static final String JSON_SM_PARTIAL_FRAME_ABSTRACT = "smpfabstract";
	
	/** for transferring file information */
	public static final String JSON_GEOFILE_INFO_PIXEL_X = "pixelx";
	public static final String JSON_GEOFILE_INFO_PIXEL_Y = "pixely";
	public static final String JSON_GEOFILE_INFO_LINEAR_UNITS = "linearunits";
	public static final String JSON_GEOFILE_INFO_LINEAR_UNITS_TYPE = "linearutilstype";
	public static final String JSON_GEOFILE_INFO_ROTATIONAL_UNITS = "rotationalunits";
	public static final String JSON_GEOFILE_INFO_ROTATIONAL_UNITS_TYPE = "rotationalunitstype";
	public static final String JSON_GEOFILE_INFO_EXTENTS = "extents";
	public static final String JSON_GEOFILE_INFO_ULC = "upperleftcorner";
	public static final String JSON_GEOFILE_INFO_URC = "upperrightcorner";
	public static final String JSON_GEOFILE_INFO_LRC = "lowerrightcorner";
	public static final String JSON_GEOFILE_INFO_LLC = "lowerleftcorner";
	public static final String JSON_GEOFILE_INFO_IS_GEOGRAPHIC = "isgeo";
	public static final String JSON_GEOFILE_INFO_IS_PROJECTED = "isproj";
	public static final String JSON_GEOFILE_INFO_PIXEL_SIZE_X = "pxx";
	public static final String JSON_GEOFILE_INFO_PIXEL_SIZE_Y = "pxy";
	public static final String JSON_GEOFILE_INFO_PROJECTION = "projection";
	public static final String JSON_GEOFILE_INFO_PROJECTION_CATEGORY = "projectioncat"; // enum ProjectionCategory
	public static final String JSON_GEOFILE_INFO_LAYERS = "layers";

	public static final String JSON_INTERNAL_PROJECTION_EPSG = "projectionepsg";	
	public static final String JSON_INTERNAL_PROJECTION_IS_GEOGRAPHIC = "projectionisgeo";	
	public static final String JSON_INTERNAL_RESOLUTION_LIST = "resolutionlist";
	
	public static final String JSON_GEOMETRY_POINT = "JSON_POINT";
	
	public static final String JSON_LAYER_INFO_NAME = "li_name";
	public static final String JSON_LAYER_INFO_FEATURE_COUNT   = "li_fc";
	public static final String JSON_LAYER_INFO_SRS = "li_srs";
	public static final String JSON_LAYER_INFO_EXTENTS = "li_exts";
	public static final String JSON_LAYER_INFO_GEOM_COLUMN = "li_gc";
	public static final String JSON_LAYER_INFO_FIELDS = "li_fields";
	public static final String JSON_LAYER_INFO_FIELD_TYPE = "li_field_type";
	public static final String JSON_LAYER_INFO_FIELD_NAME = "li_field_name";
	public static final String JSON_LAYER_INFO_FIELD_VALUES = "li_fieldvalues";
	
	public static final String JSON_SM_PROGRESS_NUM_STEPS = "prognumsteps";
	public static final String JSON_SM_PROGRESS_CURRENT_STEP = "progcurstep";
	public static final String JSON_SM_PROGRESS_CURRENT_STEP_PROGRESS = "progstepprogress";
	public static final String JSON_SM_PROGRESS_CURRENT_STEP_DESCRIPTION = "progdescription";
	public static final String JSON_SM_PROGRESS_CURRENT_STEP_APPLICATION = "progapplication";
	public static final String JSON_SM_PROGRESS_NAME = "progname";
	public static final String JSON_SM_PROGRESS_STATE = "progstate";
	public static final String JSON_SM_PROGRESS_ERROR_MESSAGE = "progerrmsg";
	public static final String JSON_SM_TASKINFO_LIST = "progaslist";
	
	public static final String JSON_SM_THREAD_ID = "threadid";
	
	public static final String JSON_FIELD_ORDER_ID = "orderid";
	public static final String JSON_FIELD_ORDER_LOCATION = "orderlocation";
	public static final String JSON_FIELD_ORDER_DATE = "orderdate";
	public static final String JSON_FIELD_ORDER_DESCRIPTION = "orderdescription";
	public static final String JSON_ORDER_ITEM = "orderitem";
	
	public static final String JSON_RSA_SUMMARY = "rsasummary";
	public static final String JSON_RSA_SERVER_STATS = "rsastats";
	public static final String JSON_RSA_SERVER_STAT_TOTAL = "rsatotal";
	public static final String JSON_RSA_SERVER_STAT_USABLE = "rsausable";
	public static final String JSON_RSA_SERVER_STAT_FREE = "rsafree";
	
	public static final String JSON_ERROR_MESSAGE = "errormsg"; // String
	public static final String JSON_ERROR_CAUSE = "errorcause"; // String
	public static final String JSON_ERROR_STACK_TRACE = "errorstack"; // List<String>
	
}

