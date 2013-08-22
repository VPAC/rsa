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

package org.vpac.ndg.rasterservices;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.FieldDefn;
import org.gdal.osr.SpatialReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.GdalInterface;
import org.vpac.ndg.OgrInterface;
import org.vpac.ndg.common.datamodel.JsonFields.ProjectionCategory;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Point;
import org.vpac.ndg.geometry.Projection;
import org.vpac.ndg.rasterdetails.RasterDetails;
import org.vpac.ndg.storage.model.JobProgress;

/**
 * Parses a geographic file to get basic information about the domain. Note:
 * this class uses the gdal java bindings and not an "out of process" approach.
 * 
 * <ul>
 * <li>Input: A dataset (vector or raster). See setInputDataset.</li>
 * <li>Output: A JSON object containing details about the dataset. See
 * getJsonObject.</li>
 * </ul>
 *  
 * @author glennf
 *
 */
// TODO: This class should be split in two: one for raster datasets, and one for
// vectors.
public class FileInformation {

	final private Logger log = LoggerFactory.getLogger(FileInformation.class);

	private static final String TITLE = "Getting File Information";
	private Path inputDataset = null;
	private Path outputDataset = null;
	private String title = "";
	private String cmd = "";
	private int exitValue = 0;
	private String output = new String();
	private JobProgress progress = null;

	GdalInterface gdalInterface;
	OgrInterface ogrInterface;

	/** boundingbox in the coordinate system of the input file	 */
	private Box bbox;
	
	/** the number of columns in the raster  */
	private int sizeX = 0;
	
	/** the number of rows in the raster */
	private int sizeY = 0;		
	
	/** the size of the pixels in the latitude direction */
	private double pixelSizeX = 0.0;
	
	/** the size of the pixels in the longitude direction  */
	private double pixelSizeY = 0.0;
	
	/** the linear unit name */
	private String linearUnit = "";
	
	/** the linear unit value */
	private double linearUnitValue = 0f;
	
	/** true if file is in geographic coordinates */
	private boolean isGeographic = true;
	
	/** true if the file is in a projected SRS */
	private boolean isProjected = false;

	/** The EPSG ID for the attached dataset. If -1, the system will attempt
	 * to determine the srcCRS automatically. */
	private int epsgId = -1;
	
	/** string for the soure CRS */
	private String srcCRS = "";
	
	/** the angular unit name */
	private String angleUnit = "";
	
	/** the angular unit value */
	private double angleUnitValue = 0f;
	
	private String noData = null;
	
	/** is file a vector or raster file */
	private Type type = Type.UNKNOWN;
	
	/** the driver short name */
	private String driverShortName = "";
	
	public enum Type {
		/** indidcates the file is raster based */ 
		RASTER, 
		/** indidcates the file is vector based */
		VECTOR, 
		/** indidcates the file is not known */
		UNKNOWN
	}
	
	private RasterDetails dataType = RasterDetails.FLOAT32;
	
	/** (Vector only) layer count */
	private int layerCount = 0;	
	
	/** layer names  */
	private ArrayList<LayerInformation> layers = null;
	
	/** How the projection has been determined. */
	protected ProjectionCategory projectionCategory =
		ProjectionCategory.DEFAULT;
	
	/**
	 * class to hold information pertaining to a vector type input fields
	 * @author glennf
	 *
	 */
	public class FieldInfo {
		
		private String name;
		private String type;
		private ArrayList<String> values;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public ArrayList<String> getValues() {
			if( values == null ) {
				values = new ArrayList<String>();
			}
			return values;
		}

		public void setValues(ArrayList<String> values) {
			this.values = values;
		}		
	}
	
	
	/** class for representing layer information  */
	public class LayerInformation {
		private String name;
		private int featureCount;
		private String geomColumn;
		private String srcSRS;
		private double[] extent;		
		private ArrayList<FieldInfo> fields = new ArrayList<FieldInfo>();
		
		public LayerInformation(String name, int fc, String gc, String srs, double[] ext, ArrayList<FieldInfo> fields) {
			this.name = name;
			this.fields = fields;
			featureCount = fc;
			geomColumn = gc;
			srcSRS = srs;
			extent = ext;
		}		
		
		public int getFeatureCount() {
			return featureCount;
		}
		public void setFeatureCount(int featureCount) {
			this.featureCount = featureCount;
		}
		public String getGeomColumn() {
			return geomColumn;
		}
		public void setGeomColumn(String geomColumn) {
			this.geomColumn = geomColumn;
		}
		public String getSrcSRS() {
			return srcSRS;
		}
		public void setSrcSRS(String srcSRS) {
			this.srcSRS = srcSRS;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double[] getExtent() {
			return extent;
		}

		public void setExtent(double[] extent) {
			this.extent = extent;
		}

		public ArrayList<FieldInfo> getFields() {
			return fields;
		}

		public void setFields(ArrayList<FieldInfo> fields) {
			this.fields = fields;
		}
		
	}
	
	public FileInformation() {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		gdalInterface = (GdalInterface) appContext.getBean("gdalInterface");
		ogrInterface = (OgrInterface) appContext.getBean("ogrInterface");
		setTitle(TITLE);
	}
	
	/**
	 * run the process of reading the file and determining the relevant 
	 * value for each member
	 */
	public void runInline() {
		Path fileName = this.getInputDataset();
		
		Dataset ds = null;
		
		try {
			ds = gdalInterface.open(fileName);
		} catch( Exception e) {
			// perfectly possible as this could be the wrong type of input
			// no nothing
		}
		
		DataSource dv = null;
		try {
			dv = ogrInterface.open(fileName);
		} catch ( Exception e ) {
			// perfectly possible as this could be the wrong type of input
			// no nothing
		}
		
		if( ds != null ) {
			extractDataFromRaster(ds);
			ds.delete();
		} 
		if( dv != null ) {
			extractDataFromVector(dv);
			dv.delete();
		}
				
	}
	
	/**
	 * extracts the necessary data from a vector based file format for use in 
	 * selecting the data that is required to be converted into raster format
	 * @param ds
	 */
	private void extractDataFromVector(DataSource ds) {

		setType(Type.VECTOR);
		if(ds.GetDriver() != null && ds.GetDriver().getName() != null) {
			setDriverShortName(ds.GetDriver().getName());			
		}
		log.trace("Driver = {}", getDriverShortName());
		
		log.trace("= Vector File Information =");
		setLayerCount(ds.GetLayerCount());
		log.trace("Number of layers = {}", getLayerCount());
		for (int i = 0; i < getLayerCount(); ++i) {

			log.trace("Layer {} fields:", i);
			ArrayList<FieldInfo> fields = new ArrayList<FieldInfo>();
			int fat = ds.GetLayer(i).GetLayerDefn().GetFieldCount();
			for( int j = 0; j < fat; j++) {
				FieldDefn fielddef = ds.GetLayer(i).GetLayerDefn().GetFieldDefn(j);
				log.trace(" - {}", fielddef.GetName());
				
				FieldInfo fi = new FieldInfo();
				fi.setName(fielddef.GetName());
				fi.setType(fielddef.GetTypeName());

				if (fielddef.GetTypeName().compareTo("String") == 0) {
					// we also need a list of features for the string category
					// items
					int num = ds.GetLayer(i).GetFeatureCount();
					HashSet<String> set = new HashSet<String>();

					String s = fielddef.GetName();
					for (int k = 0; k < num; ++k) {
						Feature f = ds.GetLayer(i).GetFeature(k);
						String n = f.GetFieldAsString(s);
						if (n != null) {
							set.add(n);
						}
					}

					for (String t : set) {
						fi.getValues().add(t);
					}
				}
				fields.add(fi);
			}
			
			LayerInformation info = new LayerInformation(ds.GetLayer(i)
					.GetName(), ds.GetLayer(i).GetFeatureCount(), ds
					.GetLayer(i).GetGeometryColumn(), ds.GetLayer(i)
					.GetSpatialRef().ExportToWkt(), ds.GetLayer(i).GetExtent(),
					fields);

			getLayers().add(info);

			SpatialReference sr = new SpatialReference();
			if (getEpsgId() != -1) {
				log.trace("Using explicit projection.");
				sr.ImportFromEPSG(getEpsgId());
				srcCRS = sr.ExportToWkt();
				projectionCategory = ProjectionCategory.EXTRINSIC;
			} else {
				try {
					srcCRS = info.getSrcSRS();
					sr.ImportFromWkt(srcCRS);
					projectionCategory = ProjectionCategory.INTRINSIC;					
					/* -------------------------------------------------------------------- */
					/*      Write the projection epsg, if possible.                         */
					/* -------------------------------------------------------------------- */
					if( (sr.IsProjected() > 0) && 
						(sr.GetAuthorityName("PROJCS") != null) &&
						sr.GetAuthorityName("PROJCS").equalsIgnoreCase("EPSG") ) {
						epsgId = Integer.parseInt(sr.GetAuthorityCode("PROJCS"));
					} else if( (sr.IsGeographic() > 0) && 
						(sr.GetAuthorityName("GEOGCS") != null) &&
						sr.GetAuthorityName("GEOGCS").equalsIgnoreCase("EPSG") ) {
						epsgId = Integer.parseInt(sr.GetAuthorityCode("GEOGCS"));
					}	
					log.trace("Using derived projection (EPSG:{})", epsgId);
				}
				catch (RuntimeException e) {
					log.debug("Dataset has no projection information. Using default EPSG:{}",
							sr.ImportFromEPSG(Projection.getDefaultGeoEpsg()));
					sr.ImportFromEPSG(Projection.getDefaultGeoEpsg());
					srcCRS = sr.ExportToWkt();
					projectionCategory = ProjectionCategory.DEFAULT;
				}
			}

			setGeographic(sr.IsGeographic() > 0);
			log.trace("Is Geographic: {}", this.isGeographic());

			setProjected(sr.IsProjected() > 0);
			log.trace("Is Projected: {}", this.isProjected());

			// Calculate the extents
			bbox = new Box(new Point<Double>(info.getExtent()[0], info.getExtent()[3]),
						   new Point<Double>(info.getExtent()[1], info.getExtent()[2]));
			log.trace("Bounds: {}", bbox);
		}
	}

	/**
	 * Extracts metadata from a raster based input file for use in display and
	 * conversion of the data. Only information about the first band is
	 * gathered.
	 */
	private void extractDataFromRaster(Dataset ds) {		
		setType(Type.RASTER);
		if(ds.GetDriver() != null && ds.GetDriver().getShortName() != null) {
			setDriverShortName(ds.GetDriver().getShortName());			
		}

		Band rb = ds.GetRasterBand(1);

		Double[] ndval = new Double[1];
		rb.GetNoDataValue(ndval);

		log.trace("= Raster File Information =");
		log.trace("File = {} (Type = {})", getInputDataset(), getDriverShortName());
		for (Double d: ndval) {
			log.trace("NoData value = {}", d);
		}

		this.setDataType(RasterDetails.valueOf(rb.getDataType()));
		Double[] nodata = new Double[1];
		rb.GetNoDataValue(nodata);
		if (nodata[0] != null) {
			this.setNoData(nodata[0].toString());
		} else {
			this.setNoData(null);
		}

		// Determine projection. See:
		// http://www.gdal.org/ogr/classOGRSpatialReference.html#8a5b8c9a205eedc6b88a14aa0c219969
		SpatialReference sr = new SpatialReference();
		if (getEpsgId() != -1) {
			log.trace("Using explicit projection.");
			sr.ImportFromEPSG(getEpsgId());
			srcCRS = sr.ExportToWkt();
			projectionCategory = ProjectionCategory.EXTRINSIC;
		} else {
			try {
				srcCRS = ds.GetProjection();
				sr.ImportFromWkt(srcCRS);
				projectionCategory = ProjectionCategory.INTRINSIC;
				/* -------------------------------------------------------------------- */
				/*      Write the projection epsg, if possible.                         */
				/* -------------------------------------------------------------------- */
				if( (sr.IsProjected() > 0) && 
					(sr.GetAuthorityName("PROJCS") != null) &&
					sr.GetAuthorityName("PROJCS").equalsIgnoreCase("EPSG") ) {
					epsgId = Integer.parseInt(sr.GetAuthorityCode("PROJCS"));
				} else if( (sr.IsGeographic() > 0) && 
					(sr.GetAuthorityName("GEOGCS") != null) &&
					sr.GetAuthorityName("GEOGCS").equalsIgnoreCase("EPSG") ) {
					epsgId = Integer.parseInt(sr.GetAuthorityCode("GEOGCS"));
				}				
				log.trace("Using derived projection (EPSG:{})", epsgId);
			}
			catch (RuntimeException e) {
				log.debug("Dataset has no projection information. Using default EPSG:" +
						sr.ImportFromEPSG(Projection.getDefaultGeoEpsg()));
				sr.ImportFromEPSG(Projection.getDefaultGeoEpsg());
				srcCRS = sr.ExportToWkt();
				projectionCategory = ProjectionCategory.DEFAULT;
			}
		}
		log.trace("Projection = {}", srcCRS);
		setGeographic(sr.IsGeographic() > 0);
		log.trace("Is Geographic: {}", this.isGeographic());

		setProjected(sr.IsProjected() > 0);
		log.trace("Is Projected: {}", this.isProjected());

		setLinearUnit(sr.GetLinearUnitsName());
		setLinearUnitValue(sr.GetLinearUnits());
		setAngleUnitValue(sr.GetAngularUnits());

		setSizeX(ds.getRasterXSize());
		setSizeY(ds.getRasterYSize());

		// Get extents. See http://www.gdal.org/classGDALDataset.html
		double[] geoTransform = ds.GetGeoTransform();
		setPixelSizeX(geoTransform[1]);
		setPixelSizeY(geoTransform[5]);

		// Calculate the extents
		bbox = new Box(
				new Point<Double>(geoTransform[0], geoTransform[3]),
				new Point<Double>(
						(geoTransform[0] + geoTransform[1] * ds.getRasterXSize() + geoTransform[2] * ds.getRasterYSize()),
						(geoTransform[3] + geoTransform[4] * ds.getRasterXSize() + geoTransform[5] * ds.getRasterYSize())));
		log.trace("Bounds: {}", bbox);
	}

	/**
	 * @return The width of the current dataset in pixles.
	 */
	public int getSizeX() {
		return sizeX;
	}

	/**
	 * Set the width of the raster for the current dataset.
	 * @param sizeX The width, in pixels.
	 */
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	/**
	 * @return The height of the current dataset in pixles.
	 */
	public int getSizeY() {
		return sizeY;
	}

	/**
	 * Set the height of the raster for the current dataset.
	 * @param sizeY The height, in pixels.
	 */
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public int getEpsgId() {
		return epsgId;
	}
	
	public void setEpsgId(int epsgId) {
		this.epsgId = epsgId;
	}

	public String getSrcCRS() {
		return srcCRS;
	}

	public void setSrcCRS(String srcCRS) {
		this.srcCRS = srcCRS;
	}

	/** 
	 * print the details of the input file set
	 * @param fi file information object (GIS toolkit based)
	 */
	public static void printFileDetails(FileInformation fi) {
		System.out.format("Size X = %d", fi.getSizeX());
		System.out.format("Size Y = %d", fi.getSizeY());
//		Utils.debug("ULC " + fi.getGeographicExtents().getUlCorner().getX() + " , "
//				+ fi.getGeographicExtents().getUlCorner().getY());
//		Utils.debug("LLC " + fi.getGeographicExtents().getLlCorner().getX() + " , "
//				+ fi.getGeographicExtents().getLlCorner().getY());
//		Utils.debug("LRC " + fi.getGeographicExtents().getLrCorner().getX() + " , "
//				+ fi.getGeographicExtents().getLrCorner().getY());
//		Utils.debug("URC " + fi.getGeographicExtents().getUrCorner().getX() + " , "
//				+ fi.getGeographicExtents().getUrCorner().getY());
//		Utils.debug("Pizel Size " + fi.getPixelSizeX() + " , "
//				+ fi.getPixelSizeY());
//		Utils.debug("bbox = " + fi.getGeographicExtents().getBBoxAsPostGisString(Projection.getDefaultGeoEpsg()));
		
		System.out.format("Is geographic: %s", fi.isGeographic()?"Yes":"No" );
		System.out.format("Is projected: %s", fi.isProjected()?"Yes":"No" );
	}

	public boolean isGeographic() {
		return isGeographic;
	}

	public void setGeographic(boolean isGeographic) {
		this.isGeographic = isGeographic;
	}

	public double getPixelSizeX() {
		return pixelSizeX;
	}

	public void setPixelSizeX(double pixelSizeX) {
		this.pixelSizeX = pixelSizeX;
	}

	public double getPixelSizeY() {
		return pixelSizeY;
	}

	public void setPixelSizeY(double pixelSizeY) {
		this.pixelSizeY = pixelSizeY;
	}

	public String getLinearUnit() {
		return linearUnit;
	}

	public void setLinearUnit(String linearUnit) {
		this.linearUnit = linearUnit;
	}

	public double getLinearUnitValue() {
		return linearUnitValue;
	}

	public void setLinearUnitValue(double d) {
		this.linearUnitValue = d;
	}

	public String getAngleUnit() {
		return angleUnit;
	}

	public void setAngleUnit(String angleUnit) {
		this.angleUnit = angleUnit;
	}

	public double getAngleUnitValue() {
		return angleUnitValue;
	}

	public void setAngleUnitValue(double angleunitValue) {
		this.angleUnitValue = angleunitValue;
	}

	public boolean isProjected() {
		return isProjected;
	}

	public void setProjected(boolean isProjected) {
		this.isProjected = isProjected;
	}

	/**
	 * @return The bounding box of the file in its native projection.
	 */
	public Box getBbox() {
		return bbox;
	}

	public int getLayerCount() {
		return layerCount;
	}

	public void setLayerCount(int layerCount) {
		this.layerCount = layerCount;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public ArrayList<LayerInformation> getLayers() {
		if( layers == null ) {
			layers = new ArrayList<LayerInformation>();
		}
		return layers;
	}

	public void setLayerNames(ArrayList<LayerInformation> layerNames) {
		this.layers = layerNames;
	}

	public RasterDetails getDataType() {
		return dataType;
	}
	
	public void setDataType(RasterDetails dt) {
		dataType = dt;
	}

	public void setNoData(String noData) {
		this.noData = noData;
	}

	/**
	 * @return The nodata value for this dataset, or null if it is not defined.
	 */
	public String getNoData() {
		return noData;
	}

	/**
	 * Get file information for the specified dataset.
	 * @param fileLocation The specified dataset file location.
	 * @return Returns the file information for the specified dataset using GDAL.
	 */
	public static FileInformation read(Path fileLocation) {
		
		// get initial file and find out what it is
		FileInformation fi = new FileInformation();
		fi.setInputDataset(fileLocation);

		fi.runInline();

		return fi;
	}

	
	public String getDriverShortName() {
		return driverShortName;
	}

	public void setDriverShortName(String driverShortName) {
		this.driverShortName = driverShortName;
	}

	/**
	 * @return The location of the primary file to operate on.
	 */
	public Path getInputDataset() {
		return inputDataset;
	}

	/**
	 * @param file The location of the primary file to operate on.
	 */
	public void setInputDataset(Path file) {
		this.inputDataset = file;
	}

	/**
	 * @return The location of the resultant file, if any.
	 */
	public Path getOutputDataset() {
		return outputDataset;
	}

	/**
	 * @param file The location of the resultant file, if any.
	 */
	public void setOutputDataset(Path file) {
		this.outputDataset = file;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public JobProgress getProgress() {
		return progress;
	}

	public void setProgress(JobProgress progress) {
		this.progress = progress;
	}

	public String getStdout() {
		return output;
	}

	public int getExitValue() {
		return exitValue;
	}

	public void setExitValue(int exitValue) {
		this.exitValue = exitValue;
	}

	/**
	 * Determines whether a process completed successfully. Override this if
	 * the command you are running uses non-zero return codes to indicate
	 * success.
	 * @param exitValue The code returned by the command.
	 * @return true if the command was successful.
	 */
	protected boolean isSuccess(int exitValue) {
		return exitValue == 0;
	}

	/**
	 * Initialise the task with required arguments.
	 */
	public void initialise() {
		
	}

}
