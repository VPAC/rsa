package org.vpac.ndg.query.filter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.Filter;
import org.vpac.ndg.query.VariableAdapter;
import org.vpac.ndg.query.math.ElementFloat;
import org.vpac.ndg.query.math.Type;
import org.vpac.ndg.query.sampling.NodataStrategy;
import org.vpac.ndg.query.sampling.NodataStrategyFactory;

import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.NetcdfFileWriter.Version;
import ucar.nc2.Variable;

/**
 * Added some static methods to deal with Filter field.
 * 
 * @author hsumanto
 * 
 */
public class FilterUtils {

	final static Logger log = LoggerFactory.getLogger(FilterUtils.class);
	final static int SECTION_SIZE = 128;
	final static String DEFAULT_PREVIEW_VAR_NAME = "multipreview";
	final static float FILL_VALUE = -999f;

	final static NodataStrategyFactory nodataStrategyFactory = new NodataStrategyFactory();

	public static Field[] getFields(Filter filter) {
		return filter.getClass().getFields();
	}

	/**
	 * Get the filter primitive fields.
	 * 
	 * @param filter
	 *            The specified filter.
	 * @return Returns the filter primitive fields.
	 */
	public static List<Field> getPrimitiveFields(Filter filter) {
		List<Field> result = new ArrayList<Field>();
		for (Field f : getFields(filter)) {
			if (f.getType().isPrimitive()) {
				result.add(f);
			}
		}
		return result;
	}

	/**
	 * Get the filter non-primitive fields.
	 * 
	 * @param filter
	 *            The specified filter.
	 * @return Returns the filter non-primitive fields.
	 */
	public static List<Field> getNonPrimitiveFields(Filter filter) {
		List<Field> result = new ArrayList<Field>();
		for (Field f : getFields(filter)) {
			if (!f.getType().isPrimitive()) {
				result.add(f);
			}
		}
		return result;
	}

	/**
	 * Generate multiple preview for the specified source file on the specified destination.
	 * @param srcFilePath The specified source file to generate the multiple preview from.
	 * @param dstFilePath The specified path of the destination preview file.
	 * @throws IOException
	 * @throws InvalidRangeException
	 */
	public static boolean multiPreview(Path srcFilePath, Path dstFilePath) throws IOException, InvalidRangeException {
		NetcdfFile srcDataset = null;
		NetcdfFileWriter dstDataset = null;
		boolean containsNoData = false;
		try {
			srcDataset = NetcdfFile.open(srcFilePath.toString());
			List<Variable> srcVars = srcDataset.getVariables();
			int numOfBand = 0;
			int numOfTimeslice = 0;
			for (Variable srcVar : srcVars) {
				if (srcVar.getRank() < 2) {
					// Only interested with >= 2D variables
					continue;
				} else if (srcVar.getRank() == 2) {
					if (numOfTimeslice < 1) {
						numOfTimeslice = 1;
					}
				} else if (srcVar.getRank() >= 3) {
					if (numOfTimeslice < 3) {
						int currNumOfTimeslice = srcVar.getShape()[0];
						if (currNumOfTimeslice < 3) {
							numOfTimeslice = currNumOfTimeslice;
						} else {
							numOfTimeslice = 3;
						}
					}
				}

				log.debug(srcVar.getNameAndDimensions());
				log.debug("variable rank: {}", srcVar.getRank());
				log.debug("variable dimension size: {}", srcVar.getDimensions()
						.size());
				numOfBand++;
			}
			dstDataset = NetcdfFileWriter.createNew(Version.netcdf4,
					dstFilePath.toString());
			int xlen = SECTION_SIZE * numOfTimeslice;
			int ylen = SECTION_SIZE * numOfBand;
			dstDataset.addDimension(null, "x", xlen);
			dstDataset.addDimension(null, "y", ylen);
			dstDataset.addVariable(null, "x", DataType.INT, "x");
			dstDataset.addVariable(null, "y", DataType.INT, "y");
			Variable var = dstDataset.addVariable(null, DEFAULT_PREVIEW_VAR_NAME, DataType.FLOAT,
					"y x");
			var.addAttribute(new Attribute("_FillValue", FILL_VALUE));
			dstDataset.create();
			log.debug("number of preview allowable bands: " + numOfBand);
			log.debug("number of preview allowable timeslice: "
					+ numOfTimeslice);

			Variable xvar = dstDataset.findVariable("x");
			Array data = Array.factory(DataType.INT, new int[] {xlen});
			for (int i = 0; i < data.getSize(); i++) {
				data.setInt(i, i);
			}
			dstDataset.write(xvar, data);

			Variable yvar = dstDataset.findVariable("y");
			data = Array.factory(DataType.INT, new int[] {ylen});
			for (int i = 0; i < data.getSize(); i++) {
				data.setInt(i, i);
			}
			dstDataset.write(yvar, data);

			Variable multiPreviewVar = dstDataset
					.findVariable(DEFAULT_PREVIEW_VAR_NAME);
			numOfBand = 0;
			for (Variable v : srcVars) {
				if (v.getRank() < 2) {
					// Only interested with 2D or more than 2D variables
					continue;
				}

				Type type = VariableAdapter.resolveType(v);
				NodataStrategy nds = nodataStrategyFactory.create(v, type);
				if (v.getRank() == 2) {
					int[] origin = new int[] { 0, 0 };
					int[] shape = new int[] { SECTION_SIZE, SECTION_SIZE };
					Array otherArray = v.read(origin, shape);
					Array array = Array.factory(DataType.FLOAT, otherArray.getShape());
					boolean hasNoData = scaleValues(otherArray, array, nds);
					if (hasNoData)
						containsNoData = true;
					int[] offset = new int[] { numOfBand * SECTION_SIZE, 0 };
					dstDataset.write(multiPreviewVar, offset, array);

				} else if (v.getRank() >= 3) {
					int[] origin = new int[] { 0, 0, 0 };
					int[] shape = new int[] { numOfTimeslice, SECTION_SIZE,
							SECTION_SIZE };
					Array otherArray = v.read(origin, shape);
					Array array = Array.factory(DataType.FLOAT, otherArray.getShape());
					boolean hasNoData = scaleValues(otherArray, array, nds);
					if (hasNoData)
						containsNoData = true;
					for (int t = 0; t < numOfTimeslice; t++) {
						int[] origin2 = new int[] { t, 0, 0 };
						int[] shape2 = new int[] { 1, SECTION_SIZE,
								SECTION_SIZE };
						Array array2 = array.section(origin2, shape2).copy();
						int[] offset = new int[] { numOfBand * SECTION_SIZE,
								t * SECTION_SIZE };
						dstDataset.write(multiPreviewVar, offset, array2);
					}
				}
				log.debug(v.getNameAndDimensions());
				log.debug("variable rank: {}", v.getRank());
				log.debug("variable dimension size: {}", v.getDimensions()
						.size());
				numOfBand++;
			}
		} finally {
			if (srcDataset != null) {
				try {
					srcDataset.close();
				} catch (Exception e) {
					log.warn("Failed to close input file", e);
				}
			}
			if (dstDataset != null) {
				try {
					dstDataset.close();
				} catch (Exception e) {
					log.warn("Failed to close output file", e);
				}
			}
		}
		return containsNoData;
	}

	private static boolean scaleValues(Array source, Array target, NodataStrategy nds) {
		float min = Float.MAX_VALUE;
		float max = Float.MIN_VALUE;
		ElementFloat current = new ElementFloat();
		boolean containsNoData = false;
		for (int i = 0; i < target.getSize(); i++) {
			float val = source.getFloat(i);
			current.set(val);
			if (nds.isNoData(current))
				continue;

			if (val < min)
				min = val;
			if (val > max)
				max = val;
		}
		for (int i = 0; i < target.getSize(); i++) {
			float val = source.getFloat(i);
			float scaled;
			current.set(val);
			if (nds.isNoData(current)) {
				scaled = FILL_VALUE;
				containsNoData = true;
			} else {
				scaled = ((val - min) / (max - min)) * 255.0f;
			}
			target.setFloat(i, scaled);
		}
		return containsNoData;
	}
}
