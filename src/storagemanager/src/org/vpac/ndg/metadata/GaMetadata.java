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

package org.vpac.ndg.metadata;

import java.io.File;
//import java.util.Date;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
//import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;

/**
 * Nested JavaBeans for interfacing with XML metadata from Geoscience Australia.
 * @author adfries
 */
@XStreamAlias("METADATA")
public class GaMetadata {

	/*
	 * Note: Most of the fields in this metadata format have uppercase names. To
	 * conform with standard Java coding style, these are remapped to camel case
	 * using the @XStreamAlias attribute.
	 */

    @XStreamAlias("DATASET")
    public Dataset dataset;
    @XStreamAlias("CONTACT")
    public Contact contact;
    @XStreamAlias("CITATION")
    public Citation citation;
    @XStreamAlias("EXTENT")
    public Extent extent;
    @XStreamAlias("IMAGE_DESCRIPTION")
    public ImageDescription imageDescription;
    @XStreamAlias("QUICKLOOK")
    public Quicklook quicklook;
    @XStreamAlias("QUALITY")
    public Quality quality;
    @XStreamAlias("PROCESSING")
    public Processing processing;
    @XStreamAlias("PROCESSING_PARAMETERS")
    public ProcessingParameters processingParameters;

    public class Dataset {
    	@XStreamAsAttribute @XStreamAlias("ABSTRACT")
    	public String abstract_;
        @XStreamAsAttribute @XStreamAlias("CHARACTER_SET")
        public String characterSet;
        @XStreamAsAttribute @XStreamAlias("CONSTRAINT_ID")
        public int constraintId;
        @XStreamAsAttribute @XStreamAlias("DATASET_ID")
        public String datasetId;
        @XStreamAsAttribute @XStreamAlias("DATA_LANGUAGE")
        public String dataLanguage;
        @XStreamAsAttribute @XStreamAlias("DS_VERS")
        public String dsVers;
        @XStreamAsAttribute @XStreamAlias("HEIRARCHY_LEVEL")
        public String heirarchyLevel;
        @XStreamAsAttribute @XStreamAlias("HEIRARCHY_LEVEL_NAME")
        public String heirarchyLevelName;
        @XStreamAsAttribute @XStreamAlias("METADATA_STANDARD_NAME")
        public String metadataStandardName;
        @XStreamAsAttribute @XStreamAlias("METADATA_STANDARD_VERSION")
        public String metadataStandardVersion;
        @XStreamAsAttribute @XStreamAlias("PARENT_ID")
        public String parentId;
        @XStreamAsAttribute @XStreamAlias("PRODUCT_FORMAT")
        public String productFormat;
        @XStreamAsAttribute @XStreamAlias("PURPOSE")
        public String purpose;
        @XStreamAsAttribute @XStreamAlias("RESOURCE_STATUS")
        public String resourceStatus;
        @XStreamAsAttribute @XStreamAlias("RESOURCE_TYPE")
        public String resourceType;
        @XStreamAsAttribute @XStreamAlias("SIZE_MB")
        public int sizeMb;
        @XStreamAsAttribute @XStreamAlias("TOPIC_CATEGORY")
        public String topicCategory;
    }

    public class Contact {
        @XStreamAsAttribute @XStreamAlias("INDIVIDUAL_NAME")
        public String individualName;
        @XStreamAsAttribute @XStreamAlias("ORGANISATION_NAME")
        public String organisationName;
        @XStreamAsAttribute @XStreamAlias("POSITION_NAME")
        public String positionName;
        @XStreamAsAttribute @XStreamAlias("ROLE")
        public String role;
    }

    public class Citation {
        @XStreamAsAttribute @XStreamAlias("ALTERNATE_TITLE")
        public String alternateTitle;
        @XStreamAsAttribute @XStreamAlias("CITATION_DATE")
        public String citationDate;        
//        @XStreamConverter(DateConverter.class)
//        public Date citationDate;        
		@XStreamAsAttribute @XStreamAlias("CITATION_TITLE")
		public String citationTitle;
		@XStreamAsAttribute @XStreamAlias("CIT_INDIVIDUAL_NAME")
		public String citIndividualName;
		@XStreamAsAttribute @XStreamAlias("CIT_ORGANISATION_NAME")
		public String citOrganisationName;
		@XStreamAsAttribute @XStreamAlias("CIT_POSITION_NAME")
		public String citPositionName;
		@XStreamAsAttribute @XStreamAlias("CIT_ROLE")
		public String citRole;
		@XStreamAsAttribute @XStreamAlias("DATE_TYPE")
		public String dateType;
		@XStreamAsAttribute @XStreamAlias("EDITION_DATE")
		public String editionDate;
//		@XStreamConverter(DateConverter.class)
//		public Date editionDate;
		@XStreamAsAttribute @XStreamAlias("EDITION_NUMBER")
		public String editionNumber;
		@XStreamAsAttribute @XStreamAlias("RESOURCE_CREATED_BY")
		public String resourceCreatedBy;
    }

    public class Extent {
		@XStreamAsAttribute @XStreamAlias("ACQUISITION_END_DT")
		public String acquisitionEndDt;
//		@XStreamConverter(DateConverter.class)
//		public Date acquisitionEndDt;
		@XStreamAsAttribute @XStreamAlias("ACQUISITION_FROM_DT")
		public String acquisitionFromDt;
//		@XStreamConverter(DateConverter.class)
//		public Date acquisitionFromDt;
		@XStreamAsAttribute @XStreamAlias("EAST_BLONG")
		public Float eastBlong;
		@XStreamAsAttribute @XStreamAlias("EXTENT_EPSG")
		public int extentEpsg;
		@XStreamAsAttribute @XStreamAlias("EXTENT_POLYGON")
		public String extentPolygon;
		@XStreamAsAttribute @XStreamAlias("LL_LAT")
		public Float llLat;
		@XStreamAsAttribute @XStreamAlias("LL_LONG")
		public Float llLong;
		@XStreamAsAttribute @XStreamAlias("LR_LAT")
		public Float lrLat;
		@XStreamAsAttribute @XStreamAlias("LR_LONG")
		public Float lrLong;
		@XStreamAsAttribute @XStreamAlias("NORTH_BLAT")
		public Float northBlat;
		@XStreamAsAttribute @XStreamAlias("SC_LAT")
		public Float scLat;
		@XStreamAsAttribute @XStreamAlias("SC_LONG")
		public Float scLong;
		@XStreamAsAttribute @XStreamAlias("SOUTH_BLAT")
		public Float southBlat;
		@XStreamAsAttribute @XStreamAlias("UL_LAT")
		public Float ulLat;
		@XStreamAsAttribute @XStreamAlias("UL_LONG")
		public Float ulLong;
		@XStreamAsAttribute @XStreamAlias("UR_LAT")
		public Float urLat;
		@XStreamAsAttribute @XStreamAlias("UR_LONG")
		public Float urLong;
		@XStreamAsAttribute @XStreamAlias("WEST_BLONG")
		public Float westBlong;
    }

    public class ImageDescription {
		@XStreamAsAttribute @XStreamAlias("ACQUISITION_REQUIREMENT")
		public String acquisitionRequirement;
		@XStreamAsAttribute @XStreamAlias("ASSOCIATED_MISSION")
		public String associatedMission;
		@XStreamAsAttribute @XStreamAlias("AVAILABLE_BANDS")
		public String availableBands;
		@XStreamAsAttribute @XStreamAlias("BAND_GAINS")
		public String bandGains;
		@XStreamAsAttribute @XStreamAlias("COLLECTION_SITE")
		public String collectionSite;
		@XStreamAsAttribute @XStreamAlias("CYCLE_NUMBER")
		public String cycleNumber;
		@XStreamAsAttribute @XStreamAlias("EVENT_CONTEXT")
		public String eventContext;
		@XStreamAsAttribute @XStreamAlias("EVENT_SEQUENCE")
		public String eventSequence;
		@XStreamAsAttribute @XStreamAlias("EVENT_TIME")
		public String eventTime;
		@XStreamAsAttribute @XStreamAlias("EVENT_TRIGGER")
		public String eventTrigger;
		@XStreamAsAttribute @XStreamAlias("GTP_BEARING")
		public String gtpBearing;
		@XStreamAsAttribute @XStreamAlias("HEADING")
		public String heading;
		@XStreamAsAttribute @XStreamAlias("IMAGERY_TYPE")
		public String imageryType;
		@XStreamAsAttribute @XStreamAlias("IMAGE_CONDITION")
		public String imageCondition;
		@XStreamAsAttribute @XStreamAlias("INSTRUMENT")
		public String instrument;
		@XStreamAsAttribute @XStreamAlias("OPERATION_MODE")
		public String operationMode;
		@XStreamAsAttribute @XStreamAlias("ORBIT")
		public String orbit;
		@XStreamAsAttribute @XStreamAlias("PASS_AOS")
		public String passAos;
		@XStreamAsAttribute @XStreamAlias("PASS_LOS")
		public String passLos;
		@XStreamAsAttribute @XStreamAlias("PASS_SCENE_COUNT")
		public String passSceneCount;
		@XStreamAsAttribute @XStreamAlias("PASS_STATUS")
		public String passStatus;
		@XStreamAsAttribute @XStreamAlias("PLATFORM")
		public String platform;
		@XStreamAsAttribute @XStreamAlias("RECEIVING_ANTENNA")
		public String receivingAntenna;
		@XStreamAsAttribute @XStreamAlias("RESOLUTION_METRES")
		public Float resolutionMetres;
		@XStreamAsAttribute @XStreamAlias("SATELLITE_REFSYS_X")
		public String satelliteRefsysX;
		@XStreamAsAttribute @XStreamAlias("SATELLITE_REFSYS_Y")
		public String satelliteRefsysY;
		@XStreamAsAttribute @XStreamAlias("SC_LTRK_ANGLE")
		public int scLtrkAngle;
		@XStreamAsAttribute @XStreamAlias("SC_XTRK_ANGLE")
		public int scXtrkAngle;
		@XStreamAsAttribute @XStreamAlias("SUN_AZIMUTH")
		public Float sunAzimuth;
		@XStreamAsAttribute @XStreamAlias("SUN_ELEVATION")
		public Float sunElevation;
    }

    public class Quicklook {
		@XStreamAsAttribute @XStreamAlias("QL_BLUE")
		public int qlBlue;
		@XStreamAsAttribute @XStreamAlias("QL_DESCRIPTION")
		public String qlDescription;
		@XStreamAsAttribute @XStreamAlias("QL_FILENAME")
		public String qlFilename;
		@XStreamAsAttribute @XStreamAlias("QL_GREEN")
		public int qlGreen;
		@XStreamAsAttribute @XStreamAlias("QL_GREY")
		public String qlGrey;
		@XStreamAsAttribute @XStreamAlias("QL_RED")
		public int qlRed;
		@XStreamAsAttribute @XStreamAlias("QL_RES")
		public String qlRes;
		@XStreamAsAttribute @XStreamAlias("QL_TYPE")
		public String qlType;
		@XStreamAsAttribute @XStreamAlias("QL_X_PIXELS")
		public int qlXPixels;
		@XStreamAsAttribute @XStreamAlias("QL_Y_PIXELS")
		public int qlYPixels;
    }

    public class Quality {
		@XStreamAsAttribute @XStreamAlias("CLOUD_COVER")
		public String cloudCover;
		@XStreamAsAttribute @XStreamAlias("CLOUD_COVER_PCT")
		public String cloudCoverPct;
		@XStreamAsAttribute @XStreamAlias("MEASNAME")
		public String measname;
		@XStreamAsAttribute @XStreamAlias("POS_ACCURACY_VAL")
		public String posAccuracyVal;
		@XStreamAsAttribute @XStreamAlias("QUALITY_REPORT_FILENAME")
		public String qualityReportFilename;
		@XStreamAsAttribute @XStreamAlias("QUANVAL")
		public int quanval;
		@XStreamAsAttribute @XStreamAlias("QUANVALUNIT")
		public String quanvalunit;
    }

    public class Processing {
		@XStreamAsAttribute @XStreamAlias("DATA_SOURCE")
		public String dataSource;
		@XStreamAsAttribute @XStreamAlias("LINEAGE_STATEMENT")
		public String lineageStatement;
		@XStreamAsAttribute @XStreamAlias("ORIENTATION")
		public String orientation;
		@XStreamAsAttribute @XStreamAlias("PROCESSING_REPORT_FILENAME")
		public String processingReportFilename;
		@XStreamAsAttribute @XStreamAlias("PROCESSOR")
		public String processor;
		@XStreamAsAttribute @XStreamAlias("PROCESSOR_LEVEL")
		public String processorLevel;
		@XStreamAsAttribute @XStreamAlias("PROCESSOR_VERSION")
		public String processorVersion;
		@XStreamAsAttribute @XStreamAlias("PRODUCTION_DATE")
		public String productionDate;
//		@XStreamConverter(DateConverter.class)
//		public Date productionDate;
		@XStreamAsAttribute @XStreamAlias("PRODUCT_FILESIZE")
		public int productFilesize;
		@XStreamAsAttribute @XStreamAlias("PRODUCT_STATUS")
		public String productStatus;
		@XStreamAsAttribute @XStreamAlias("PROD_CODE")
		public String prodCode;
		@XStreamAsAttribute @XStreamAlias("SCOPE_LEVEL")
		public String scopeLevel;
    }

    public class ProcessingParameters {
		@XStreamAsAttribute @XStreamAlias("RESAMPLING_KERNEL")
		public String resamplingKernel;
		@XStreamAsAttribute @XStreamAlias("SCAN_GAP_CORRECTION")
		public String scanGapCorrection;
		@XStreamAsAttribute @XStreamAlias("SCENES_TO_INGEST")
		public String scenesToIngest;
		@XStreamAsAttribute @XStreamAlias("SCENE_SHIFT")
		public String sceneShift;
		@XStreamAsAttribute @XStreamAlias("TERRAIN_MODEL")
		public String terrainModel;
    }

    public static GaMetadata fromXML(File metadataFile) {
    	String defaultDateFormat = "yyyy-MM-dd HH:mm:ss";
        String[] acceptableFormats ={defaultDateFormat}; 
        // xstream by default don't handle customed date format
		DateConverter dateConverter = new DateConverter(defaultDateFormat, acceptableFormats, true);
		XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("ddd", "_");
		XStream xstream = new XStream(new DomDriver("UTF-8", replacer));
        xstream.registerConverter(dateConverter); 
        xstream.processAnnotations(Dataset.class);
        xstream.processAnnotations(Contact.class);
        xstream.processAnnotations(Citation.class);        
        xstream.processAnnotations(Extent.class);
        xstream.processAnnotations(ImageDescription.class);
        xstream.processAnnotations(Quicklook.class);
        xstream.processAnnotations(Quality.class);
        xstream.processAnnotations(Processing.class);
        xstream.processAnnotations(ProcessingParameters.class);
        return (GaMetadata) xstream.fromXML(metadataFile);
    }

}