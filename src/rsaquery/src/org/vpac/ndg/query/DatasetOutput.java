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

package org.vpac.ndg.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.QueryDefinition.AttributeDefinition;
import org.vpac.ndg.query.QueryDefinition.DatasetOutputDefinition;
import org.vpac.ndg.query.QueryDefinition.GridDefinition;
import org.vpac.ndg.query.QueryDefinition.VariableDefinition;
import org.vpac.ndg.query.coordinates.CoordinateUtils;
import org.vpac.ndg.query.coordinates.GridProjected;
import org.vpac.ndg.query.coordinates.QueryCoordinateSystem;
import org.vpac.ndg.query.coordinates.TimeAxis;
import org.vpac.ndg.query.math.Type;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.NodataStrategy;
import org.vpac.ndg.query.sampling.PixelSource;
import org.vpac.ndg.query.sampling.Prototype;

import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Attribute;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFileWriter;
import ucar.nc2.Variable;
import ucar.nc2.dataset.CoordinateTransform;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.NetcdfDataset.Enhance;
import ucar.nc2.time.CalendarDate;
import ucar.nc2.time.CalendarPeriod;

/**
 * Allows construction of the output dataset.
 * 
 * @author Alex Fraser
 */
public class DatasetOutput implements DatasetMeta {

	private static final String VAR_NAME_FORMAT = "%s%d";

	final Logger log = LoggerFactory.getLogger(DatasetOutput.class);

	private static Set<Enhance> CSYS_ENHANCEMENTS = EnumSet
			.of(Enhance.CoordSystems);

	private DatasetOutputDefinition def;
	private NetcdfFileWriter target;
	private NetcdfDataset targetDs;
	protected DatasetStore datasetStore;
	protected List<String> conventions;
	private QueryCoordinateSystem coordinateSystem;
	private Map<String, VariableAdapter> adapters;

	CoordinateUtils coordinateUtils;

	public DatasetOutput(DatasetOutputDefinition def, NetcdfFileWriter target,
			DatasetStore datasetStore) {

		this.def = def;
		this.target = target;
		this.datasetStore = datasetStore;
		conventions = new ArrayList<String>();
		coordinateUtils = new CoordinateUtils(datasetStore);
		adapters = new HashMap<String, VariableAdapter>();
	}

	/**
	 * Populate the output dataset with dimensions and variables as described in
	 * the {@link DatasetOutputDefinition}. This assumes that the coordinate
	 * system metadata has been initialised.
	 * 
	 * @param csys The coordinate system that the query will operate in. Note
	 *        that this may have different dimensions that the output dataset.
	 * @param filterStore A representative filter store that contains one copy
	 *        of each filter. When using multiple threads, there may be several
	 *        filter stores. This one is just used for creating output variables
	 *        and such, so any filter store will do. Binding happens later.
	 */
	public List<VariableBindingDefinition> configure(
			QueryCoordinateSystem csys, QueryDefinition qdef,
			FilterStore filterStore) throws IOException,
			QueryConfigurationException {

		// These operations are quite tightly coupled. Be careful when changing
		// the order. For example, variable definitions should be dereferenced
		// before they are declared as actual variables - but it runs deeper
		// even than that.

		createCoordinateSystemMetadata(def.grid, csys);

		String[] dimensions = gatherDimensions(def.variables, filterStore);
		List<VariableDefinition> dimensionVars = createDimensions(def.grid,
				dimensions, csys);
		declareVariables(dimensionVars);

		List<VariableBindingDefinition> vbds = new ArrayList<DatasetOutput.VariableBindingDefinition>();
		List<VariableDefinition> bandVars = dereferenceVariables(def.variables,
				filterStore, vbds);
		declareVariables(bandVars);
		finaliseConventions();
		recordHistory(qdef);

		// Leave define mode. This lets data be written to the variables.
		target.create();

		adoptCoordinateSystem(dimensions, csys);

		populateDimensions(dimensionVars, csys);
		targetDs = NetcdfDataset
				.wrap(target.getNetcdfFile(), CSYS_ENHANCEMENTS);
		return vbds;
	}

	private String[] gatherDimensions(List<VariableDefinition> vds,
			FilterStore filterStore) throws QueryConfigurationException {

		Set<String> dims = new HashSet<String>();
		for (VariableDefinition vd : vds) {
			Prototype pt = findPrototype(vd.ref, datasetStore, filterStore);
			for (String dim : pt.getDimensions())
				dims.add(dim);
		}
		return dims.toArray(new String[dims.size()]);
	}

	private Prototype findPrototype(String ref, DatasetStore ds, FilterStore fs)
			throws QueryConfigurationException {

		try {
			return fs.findOutputSocket(ref).getPrototype();
		} catch (QueryConfigurationException e) {
			throw new QueryConfigurationException(String.format(
					"Could not find prototype for output socket %s", ref), e);
		}
	}

	final int MAX_HISTORY_ITEMS = 3;

	private void recordHistory(QueryDefinition qdef) {

		StringBuffer sb = new StringBuffer();

		// Copy old history
		for (DatasetInput di : datasetStore.getInputDatasets()) {
			Attribute history = di.getDataset().findGlobalAttribute("history");
			sb.append(history.getStringValue());
			sb.append("\n");
		}

		// Add time stamp
		Date timestamp = new Date();
		sb.append(timestamp.toString());
		sb.append(": rsaquery");

		// List filters
		sb.append(" filters(");
		for (int i = 0; i < qdef.filters.size(); i++) {
			if (i > 0)
				sb.append(", ");
			if (i < MAX_HISTORY_ITEMS) {
				String[] classComps = qdef.filters.get(i).classname
						.split("\\.");
				sb.append(classComps[classComps.length - 1]);
			} else {
				sb.append("...");
				break;
			}
		}
		sb.append(")");

		// List inputs
		sb.append(" inputs(");
		for (int i = 0; i < qdef.inputs.size(); i++) {
			if (i > 0)
				sb.append(", ");
			if (i < MAX_HISTORY_ITEMS) {
				sb.append(qdef.inputs.get(i).href);
			} else {
				sb.append("...");
				break;
			}
		}
		sb.append(")");

		Attribute attr = new Attribute("history", sb.toString());
		target.addGroupAttribute(null, attr);
	}

	@Override
	public void close() throws IOException {
		if (target != null)
			target.close();
		target = null;
		targetDs = null;
	}

	@Override
	public GridProjected getGrid() {
		return coordinateSystem.getGrid();
	}

	@Override
	public QueryCoordinateSystem getCoordinateSystem() {
		return coordinateSystem;
	}

	@Override
	public NetcdfDataset getDataset() {
		return targetDs;
	}

	@Override
	public String getName() {
		return def.id;
	}

	@Override
	public Variable findVariable(String name) {
		return target.findVariable(name);
	}

	class VariableBindingDefinition {
		String fromRef;
		List<String> toRefs;

		public VariableBindingDefinition() {
			toRefs = new ArrayList<String>();
		}
	}

	/**
	 * Apply inheritance and overrides to variables - but don't create them yet.
	 */
	protected List<VariableDefinition> dereferenceVariables(
			List<VariableDefinition> vds, FilterStore filterStore,
			List<VariableBindingDefinition> bindingDefs)
			throws QueryConfigurationException {

		if (vds == null) {
			throw new QueryConfigurationException("No output variables declared.");
		}

		List<VariableDefinition> newVds = new ArrayList<VariableDefinition>();
		for (VariableDefinition vd : vds) {

			String name = vd.name;
			String dimensions = null;

			if (vd.ref == null) {
				log.warn("Ignoring unlinked variable \"%s\"", name);
				continue;
			}

			PixelSource ps = filterStore.findOutputSocket(vd.ref);
			Prototype pt = ps.getPrototype();
			dimensions = StringUtils.join(pt.getDimensions(), " ");

			// Store information about this variable's linkage for binding later.
			VariableBindingDefinition vbd = new VariableBindingDefinition();
			vbd.fromRef = vd.ref;

			Type[] types = pt.getTypes();
			for (int i = 0; i < types.length; i++) {
				VariableDefinition newVd = new VariableDefinition();
				if (types.length == 1)
					newVd.name = name;
				else
					newVd.name = String.format(VAR_NAME_FORMAT, name, i + 1);
				newVd.dimensions = dimensions;
				newVd.type = types[i].getStorageType().toString();
				newVd.attributes = new ArrayList<AttributeDefinition>();
				vbd.toRefs.add(String.format("#%s/%s", def.id, newVd.name));

				// Process user-defined attributes.
				Map<String, AttributeDefinition> procAttr = newVd
						.getProcessedAttributes();
				boolean hasNodataAttr = false;
				if (vd.attributes != null) {
					for (AttributeDefinition ad : vd.attributes) {
						AttributeDefinition adProc = prepareAttribute(ad);
						if (ad.name.equals("_FillValue") || ad.name.equals("valid_range")) {
							hasNodataAttr = true;
						}
						procAttr.put(adProc.name, adProc);
					}
				}

				// Add automatic nodata attribute, if user didn't specify.
				if (!hasNodataAttr) {
					NodataStrategy nds = pt.getNodataStrategies()[i];
					AttributeDefinition ad = nds.getAttributeDefinition();
					if (ad != null) {
						hasNodataAttr = true;
						procAttr.put(ad.name, ad);
					}
				}

				// Complete definition of data type.
				if (!procAttr.containsKey("_Unsigned")) {
					AttributeDefinition ad = new AttributeDefinition();
					ad.name = "_Unsigned";
					if (types[i].isUnsigned())
						ad.getProcessedValues().add("true");
					else
						ad.getProcessedValues().add("false");
					procAttr.put(ad.name, ad);
				}

				// Add inherited attributes, but don't overwrite the ones
				// created above.
				for (AttributeDefinition ad : pt.getAttributes()[i]) {
					if (procAttr.containsKey(ad.name))
						continue;
					if (ad.name.equals("_FillValue")
							|| ad.name.equals("valid_range")
							|| ad.name.equals("valid_min")
							|| ad.name.equals("valid_max")) {
						if (hasNodataAttr)
							continue;
					}
					procAttr.put(ad.name, ad);
				}

				// Fill value has to be the same type as the variable; maybe not
				// so for valid_range. See:
				// http://stackoverflow.com/questions/17139391/netcdf-java-how-to-write-unsigned-attribute
				if (procAttr.containsKey("_FillValue")) {
					AttributeDefinition ad = procAttr.get("_FillValue");
					ad._type = types[i].getStorageType();
					ad._unsigned = types[i].isUnsigned();
				}

				newVds.add(newVd);
			}
			bindingDefs.add(vbd);
		}

		return newVds;
	}

	AttributeDefinition prepareAttribute(AttributeDefinition ad) {
		AttributeDefinition adproc = new AttributeDefinition();
		adproc.name = ad.name;

		// Try to convert to numbers
		String[] values;
		values = ad.value.split(" ");
		NumberConverter conv = new NumberConverter();
		List<Object> numbers = new ArrayList<Object>();
		for (String s : values) {
			numbers.add(conv.fromString(s));
		}
		adproc.getProcessedValues().addAll(numbers);

		return adproc;
	}

	/**
	 * Create variables in the output dataset.
	 * 
	 * @param vds The definitions of variables to declare. References will not
	 *        be resolved at this point (except for attributes), so the
	 *        definitions should be fully-defined.
	 */
	protected void declareVariables(List<VariableDefinition> vds)
			throws QueryConfigurationException {

		if (vds == null)
			return;

		for (VariableDefinition vd : vds) {

			if (vd.name == null) {
				throw new QueryConfigurationException(
						"Variable is under-defined.");
			}
			if (vd.dimensions == null || vd.type == null) {
				throw new QueryConfigurationException(String.format(
						"Variable \"%s\" is under-defined.", vd.name));
			}

			DataType type = DataType.getType(vd.type);

			Variable varnew = target.addVariable(null, vd.name, type,
					vd.dimensions);
			log.info("Created variable {} with shape {}", vd.name,
					varnew.getShape());

			// Create attributes.
			for (AttributeDefinition ad : vd.getProcessedAttributes().values()) {
				Attribute aout = createAttribute(ad);
				varnew.addAttribute(aout);
			}
		}
	}

	/**
	 * @param ain An attribute to copy.
	 * @return The new duplicate.
	 */
	private Attribute createAttribute(AttributeDefinition ad) {
		if (ad._type != null) {
			List<Object> values = ad.getProcessedValues();
			Array array = Array.factory(ad._type, new int[] { values.size() });
			if (ad._unsigned) {
				log.debug("Setting {} to unsigned.", ad.name);
				array.setUnsigned(true);
			}
			for (int i = 0; i < values.size(); i++) {
				array.setObject(i, values.get(i));
			}
			Attribute attr = new Attribute(ad.name, array);
			log.debug("{}", attr);
			return attr;
		} else {
			return new Attribute(ad.name, ad.getProcessedValues());
		}
	}

	/**
	 * Create dimensions in the output dataset.
	 * 
	 * @param dimensionVars
	 * @param csys
	 * @return 
	 */
	protected List<VariableDefinition> createDimensions(GridDefinition gd,
			String[] dimensions, QueryCoordinateSystem csys)
			throws QueryConfigurationException {

		List<VariableDefinition> varDefs = new ArrayList<VariableDefinition>();
		GridProjected grid = csys.getGrid();
		VectorReal range = grid.getBounds().getSize();

		// Get referent to grid source dataset so coordinate axes can inherit
		// attributes.
		DatasetMeta sd = datasetStore.findDataset(gd.ref);

		for (String name : dimensions) {
			Integer length = null;
			boolean isUnlimited = false;
			String type;

			// Infer from the grid. One day, this could be overridden manually
			// if need be.
			if (name.equals("time")) {
				log.debug("Inferring length from collated time axis");
				length = csys.getTimeAxis().getValues().size();
				isUnlimited = true;
				// For now, assume time fits in an integer. Should probably
				// detect appropriate type based on range.
				type = "int";
			} else {
				int i = getVectorIndex(range, name);
				log.debug(
						"Inferring length from range {} and resolution {}",
						range.get(i), grid.getResolution().get(i));
				length = (int) (range.get(i) / grid.getResolution().get(i));

				// For now, assume grid coordinates are double.
				type = "double";
			}
			log.debug("Inferred length of {} dimension is {}", name, length);

			if (length.compareTo(0) <= 0) {
				throw new QueryConfigurationException(String.format(
						"Dimension \"%s\" has invalid length (%d). Check " +
						"bounding box.", name, length));
			}

			target.addDimension(null, name, length, true, isUnlimited, false);

			// Synthesise dimension variable.

			VariableDefinition vardef = new VariableDefinition();
			vardef.name = name;
			vardef.dimensions = name;
			vardef.type = type;

			try {
				VariableAdapter sourceVar = sd.getVariableAdapter(name);
				Map<String, AttributeDefinition> procAttrs = vardef.getProcessedAttributes();
				for (AttributeDefinition ad : sourceVar.getAttributeMementos()) {
					procAttrs.put(ad.name, ad);
				}
			} catch (QueryConfigurationException e) {
				log.warn("Could not find coordinate axis \"{}\" in grid " +
						"source dataset. Some metadata may be missing from " +
						"output dataset, so it may not be recognised as " +
						"gridded.", name);
			}

			varDefs.add(vardef);
		}

		return varDefs;
	}

	private int getVectorIndex(VectorReal range, String name)
			throws QueryConfigurationException {
		String dim;
		if (name.equals("lon"))
			dim = "x";
		else if (name.equals("lat"))
			dim = "y";
		else
			dim = name;

		int i = range.indexOf(dim);
		if (i < 0) {
			throw new QueryConfigurationException(String.format(
					"Can not determine length of dimension \"%s\": " +
					"Can not find dimension in specified grid.", name));
		}
		return i;
	}

	/**
	 * Create the coordinate system variables in the target dataset. These are
	 * metadata that will be used by other programs to display the gridded data.
	 */
	protected void createCoordinateSystemMetadata(GridDefinition gd,
			QueryCoordinateSystem csys) throws QueryConfigurationException,
			IOException {

		if (gd == null)
			return;

		if (gd.ref == null) {
			throw new QueryConfigurationException("Grid definition lacks a "
					+ "reference to a coordinate system. Defintion of new "
					+ "coordinate systems is not implemented.");
		}

		DatasetMeta sd = datasetStore.findDataset(gd.ref);
		NetcdfDataset sourceDataset = sd.getDataset();
		if (sourceDataset.enhanceNeeded(CSYS_ENHANCEMENTS))
			sourceDataset.enhance(CSYS_ENHANCEMENTS);

		GridProjected grid = csys.getGrid();
		for (CoordinateTransform ct : grid.getSrs().getCoordinateTransforms()) {
			String name = ct.getName();
			Variable sourceVar = sourceDataset.findVariable(name);
			Variable varnew = target
					.addVariable(null, sourceVar.getFullNameEscaped(),
							sourceVar.getDataType(), "");

			for (Attribute ain : sourceVar.getAttributes()) {
				if (ain.getFullName().equals("GeoTransform")) {
					// Use own transform as determined in intialiseGrid

					// TODO: will this work if the image is oriented south-up?
					// See http://www.gdal.org/gdal_datamodel.html
					VectorReal min = grid.getBounds().getMin();
					VectorReal max = grid.getBounds().getMax();
					String affineTransform = String.format("%f %f %f %f %f %f",
							// xmin, xres, 0
							min.getX(), grid.getResolution().getX(), 0f,
							// ymax, 0, -yres
							max.getY(), 0f, -grid.getResolution().getY());

					varnew.addAttribute(new Attribute("GeoTransform",
							affineTransform));

				} else {
					// Pass-through
					varnew.addAttribute(copyAttribute(ain));
				}
			}
		}

		Attribute conventionsAttr = sourceDataset
				.findGlobalAttribute("Conventions");
		if (conventionsAttr != null) {
			String conv = conventionsAttr.getStringValue();
			String[] conventions;
			if (conv.contains(","))
				conventions = conv.split(",");
			else
				conventions = conv.split(" ");
			for (String convention : conventions) {
				if (convention.startsWith("CF-1"))
					addConvention(convention);
			}
		}

	}

	protected void addConvention(String convention) {
		conventions.add(convention);
	}

	/**
	 * @param ain An attribute to copy.
	 * @return The new duplicate.
	 */
	private Attribute copyAttribute(Attribute ain) {
		if (ain.isArray())
			return new Attribute(ain.getFullName(), ain.getValues());
		else if (ain.isString())
			return new Attribute(ain.getFullName(), ain.getStringValue());
		else
			return new Attribute(ain.getFullName(), ain.getNumericValue());
	}

	/**
	 * Write conventions (e.g. CF-1.5) to the target dataset.
	 */
	protected void finaliseConventions() {
		if (conventions.size() == 0)
			return;

		String delimiter = " ";
		for (String conv : conventions) {
			if (conv.contains(" "))
				delimiter = ",";
		}
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (String conv : conventions) {
			if (!first)
				sb.append(delimiter);
			sb.append(conv);
		}
		target.addGroupAttribute(null,
				new Attribute("Conventions", sb.toString()));
	}

	protected boolean isGridDimension(String name) {
		return name.equals("x") || name.equals("lon") || name.equals("y")
				|| name.equals("lat");
	}

	/**
	 * Copy data from variables in existing datasets into the output.
	 * 
	 * @param csys
	 */
	protected void populateDimensions(List<VariableDefinition> vs,
			QueryCoordinateSystem csys) throws IOException,
			QueryConfigurationException {

		if (vs == null)
			return;

		for (VariableDefinition vardef : vs) {
			String dim = vardef.dimensions;
			Array array;
			if (!dim.equals("time")) {
				GridProjected grid = csys.getGrid();
				int di = getVectorIndex(grid.getResolution(), dim);

				// Note that the values are centered on the cells, while the
				// bounds touch the edge. Therefore they must be offset by half
				// a cell.
				double incr = grid.getResolution().get(di);
				double min = grid.getBounds().getMin().get(di) + (incr / 2.0);

				Dimension dimension = target.getNetcdfFile().findDimension(dim);
				int numElems = dimension.getLength();
				int[] shape = new int[] { numElems };

				array = Array.factory(DataType.DOUBLE, shape);
				for (int i = 0; i < numElems; i++) {
					array.setDouble(i, min + (incr * i));
				}

			} else {
				// Time slices tend to be irregular, so the values need to be
				// copied. Use the global query coordinate system.
				Dimension dimension = target.getNetcdfFile().findDimension(dim);
				TimeAxis timeAxis = csys.getTimeAxis();
				assert (dimension.getLength() == timeAxis.getValues().size());
				int[] shape = new int[] { timeAxis.getValues().size() };

				array = Array.factory(DataType.INT, shape);
				int i = 0;
				CalendarDate epoch = timeAxis.getUnits().getBaseCalendarDate();
				CalendarPeriod period = timeAxis.getUnits().getTimeUnit();
				for (CalendarDate value : timeAxis.getValues()) {
					array.setInt(i, period.subtract(epoch, value));
					i++;
				}

			}

			try {
				target.write(target.findVariable(vardef.name), array);
			} catch (InvalidRangeException e) {
				throw new QueryConfigurationException(String.format(
						"Variable \"%s\" can not be copied from \"%s\": "
								+ "shape mismatch.", vardef.name, vardef.ref),
						e);
			}
		}
	}

	private void adoptCoordinateSystem(String[] dimensions,
			QueryCoordinateSystem csys) throws QueryConfigurationException {

		boolean hasTime = false;
		for (String dim : dimensions) {
			if (dim.equals("time")) {
				hasTime = true;
				break;
			}
		}

		GridProjected grid = coordinateUtils.adaptGrid(csys.getGrid(), dimensions);

		TimeAxis timeAxis;
		if (hasTime)
			timeAxis = csys.getTimeAxis();
		else
			timeAxis = null;

		coordinateSystem = new QueryCoordinateSystem(grid, timeAxis);
	}

	@Override
	public String toString() {
		return String.format("DatasetOutput(%s)", def.id);
	}

	@Override
	public VariableAdapter getVariableAdapter(String name)
			throws QueryConfigurationException {

		VariableAdapter var = adapters.get(name);

		if (var != null) {
			log.debug("Reusing existing variable adapter for {}", name);
			return var;
		}

		Variable innerVar = findVariable(name);
		if (innerVar == null) {
			throw new QueryConfigurationException(String.format(
					"Variable \"%s\" can't be found in dataset \"%s\".", name,
					def.id));
		}

		VariableAdapter adapter = new VariableAdapter(innerVar, name, this);
		adapters.put(name, adapter);
		return adapter;
	}
}
