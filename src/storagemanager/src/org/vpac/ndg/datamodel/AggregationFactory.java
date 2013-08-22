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

package org.vpac.ndg.datamodel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.datamodel.AggregationDefinition.AggDef;
import org.vpac.ndg.datamodel.AggregationDefinition.VarAggDef;
import org.vpac.ndg.datamodel.AggregationDefinition.VarDef;

/**
 * Convenience methods for creating nested NCML objects.
 * @author Alex Fraser
 */
public class AggregationFactory {

	final static Logger log = LoggerFactory.getLogger(AggregationFactory.class);

	/**
	 * Create a dataset that references a literal resource.
	 * 
	 * <pre>
	 * &lt;netcdf location="foo.nc"/>
	 * </pre>
	 * 
	 * @param location The path to the file on disk.
	 * @return The NCML fragment that refers to the file, or null if the file
	 *         does not exist.
	 */
	public AggregationDefinition raw(Path location) {
		if (!Files.exists(location))
			return null;

		return new AggregationDefinition(location);
	}

	/**
	 * Create a union of several datasets. This is useful for combining several
	 * single-band datasets into one multi-band dataset.
	 * 
	 * <pre>
	 * &lt;netcdf>
	 *      &lt;aggregation type="union">
	 *          &lt;netcdf>...&lt;/netcdf>
	 *          &lt;netcdf>...&lt;/netcdf>
	 *          &lt;netcdf>...&lt;/netcdf>
	 *      &lt;/aggregation>
	 * &lt;/netcdf>
	 * </pre>
	 * 
	 * @see <a href="http://www.unidata.ucar.edu/software/netcdf/ncml/v2.2/Aggregation.html#union">The definition in the unidata docs</a>
	 * 
	 * @param children
	 *            The child datasets to combine. If the bands need to be
	 *            re-mapped (e.g. if all the source datasets have the same band
	 *            names, which would conflict), then
	 *            {@link VarDef#remap(String, String) add a remapping
	 *            declaration} to the datasets before calling this function.
	 * @return Returns null if child datasets are empty, otherwise returns
	 * 		   the new dataset with extra variables (bands).
	 */
	public AggregationDefinition union(List<AggregationDefinition> children) {

		if (children.size() == 0)
			return null;

		AggregationDefinition dataset = new AggregationDefinition();

		AggDef agg = new AggDef();
		agg.setType(AggregationType.UNION.getValue());
		agg.getChildren().addAll(children);
		dataset.setAggregation(agg);

		return dataset;
	}

	/**
	 * Like {@link AggregationFactory#union(List) union(List)}, but remaps the
	 * bands of the children.
	 *
	 * @return The new union aggregation, or null if the list of children is
	 *         empty.
	 */
	public AggregationDefinition union(List<AggregationDefinition> children,
			List<String> bandNames) {

		if (children.size() == 0)
			return null;

		// Remap bands. Assume that the bands map 1:1 with the children.
		if (bandNames != null) {
			for (int i = 0; i < children.size(); i++) {
				AggregationDefinition child = children.get(i);
				String bandName = bandNames.get(i);
				child.getVariables().add(VarDef.remap("Band1", bandName));
			}
		}

		return union(children);
	}

	/**
	 * Create a new NcmlDataset that joins an existing set of datasets. This is
	 * useful for aggregating in time.
	 * 
	 * <pre>
	 * &lt;netcdf>
	 *     &lt;variable name="time" shape="time" type="int">
	 *         &lt;attribute name="units" value="days since 2011-01-01"/>
	 *         &lt;attribute name="_CoordinateAxisType" value="Time" />
	 *     &lt;/variable>
	 *     &lt;aggregation dimName="time" type="joinNew">
	 *         &lt;variableAgg name="Band10"/>
	 *         &lt;variableAgg name="Band20"/>
	 *         &lt;netcdf coordValue="0">...&lt;/netcdf>
	 *         &lt;netcdf coordValue="30">...&lt;/netcdf>
	 *     &lt;/aggregation>
	 * &lt;/netcdf>
	 * </pre>
	 * 
	 * @see <a href="http://www.unidata.ucar.edu/software/netcdf/ncml/v2.2/Aggregation.html#joinNew">The
	 *      definition in the unidata docs</a>
	 *
	 * @param aggVars The variables that will take part in the new aggregation
	 *        (e.g. Band1, ...).
	 * @param newVariable The new variable (e.g. time).
	 * @param children The child datasets. These may point to physical files, or
	 *        nested aggregations. Important: each dataset must have the
	 *        {@link AggregationDefinition#coordValue coordvalue} field set,
	 *        with an offset from the origin of <em>newVariable</em>.
	 * @return Returns null if child datasets are empty, otherwise returns the
	 *         new dataset with an extra dimension (compared to its children).
	 */
	public AggregationDefinition joinNew(List<VarAggDef> aggVars,
			VarDef newVariable, List<AggregationDefinition> children) {

		if (children.size() == 0)
			return null;

		for (int i = 0; i < children.size(); i++) {
			AggregationDefinition d = children.get(i);
			if (d.getCoordValue() == null) {
				throw new IllegalArgumentException("All child datasets of a " +
						"joinNew aggregation must have the coordValue field " +
						"set.");
			}
		}
		if (newVariable.getShape() == null) {
			throw new IllegalArgumentException("Variable shape not specified.");
		}

		AggregationDefinition dataset = new AggregationDefinition();

		AggDef agg = new AggDef(newVariable.getShape(),
				AggregationType.JOIN_NEW.getValue());
		agg.getAggVars().addAll(aggVars);
		agg.getChildren().addAll(children);

		dataset.setAggregation(agg);

		dataset.getVariables().add(newVariable);

		return dataset;
	}

	/**
	 * Like joinNew(List<VarAggDef> aggVars, VarDef newVariable, List<AggregationDefinition> children), but assigns coordinate values to the datasets.
	 *
	 * @return The joinNew aggregation, or null if there are no children.
	 */
	public AggregationDefinition joinNew(List<AggregationDefinition> children,
			List<String> bandNames, VarDef newDimension,
			List<String> coordinateValues) {

		if (children.size() == 0)
			return null;

		for (int i = 0; i < children.size(); i++) {
			AggregationDefinition child = children.get(i);
			String coordValue = coordinateValues.get(i);
			child.setCoordValue(coordValue);
		}

		// List which variables to union (all the bands).
		List<VarAggDef> aggVars = new ArrayList<>();
		for (String bandName : bandNames) {
			aggVars.add(new VarAggDef(bandName));
		}

		return joinNew(aggVars, newDimension, children);
	}

	/**
	 * Create a tiled arrangement of several datasets. This is useful for
	 * combining several neighbouring datasets into one dataset.
	 * 
	 * <pre>
	 * &lt;netcdf>
	 *     &lt;aggregation type="tiled" dimName="x y">
	 *         &lt;netcdf section="0:4719,0:4219">...&lt;/netcdf>
	 *         &lt;netcdf section="0:4719,4220:8440"/>...&lt;/netcdf>
	 *         &lt;netcdf section="4720:9440,0:4219"/>...&lt;/netcdf>
	 *         &lt;netcdf section="4720:9440,4220:8440"/>...&lt;/netcdf>
	 *     &lt;/aggregation>
	 * &lt;/netcdf>
	 * </pre>
	 * 
	 * @param children
	 *            The child datasets. These may point to physical files, or
	 *            nested aggregations. Important: each dataset must have the
	 *            {@link AggregationDefinition#section section} field set, with
	 *            offsets from the origin of the tile grouping.
	 * 
	 * @return Returns null if child datasets are empty, otherwise returns
	 * 		   a new dataset containing the specified tiles.
	 */
	public AggregationDefinition tile(List<AggregationDefinition> children,
			String dimensions) {

		if(children.size() == 0) {
			// When all child datasets are empty, returns null
			return null;
		}

		for (int i = 0; i < children.size(); i++) {
			AggregationDefinition d = children.get(i);
			if (d.getSection() == null) {
				throw new IllegalArgumentException("All child datasets of a " +
						"tiled aggregation must have the section field set.");
			}
		}

		AggregationDefinition dataset = new AggregationDefinition();

		AggDef agg = new AggDef(dimensions, AggregationType.TILED.getValue());
		agg.getChildren().addAll(children);
		dataset.setAggregation(agg);

		return dataset;
	}

	/**
	 * Like {@link AggregationFactory#tile(List, String) tile(List, String)},
	 * but assigns tile sections to the children.
	 */
	public AggregationDefinition tile(List<AggregationDefinition> children,
			String dimensions, List<String> sections) {

		if(children.size() == 0) {
			// When all child aggregation are empty, returns null
			return null;
		}

		for (int i = 0; i < children.size(); i++) {
			AggregationDefinition child = children.get(i);
			String section = sections.get(i);
			child.setSection(section);
		}

		return tile(children, dimensions);
	}
}
