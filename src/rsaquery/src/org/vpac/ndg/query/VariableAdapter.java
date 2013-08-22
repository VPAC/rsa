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
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.QueryDefinition.AttributeDefinition;
import org.vpac.ndg.query.coordinates.HasRank;
import org.vpac.ndg.query.coordinates.HasShape;
import org.vpac.ndg.query.coordinates.QueryCoordinateSystem;
import org.vpac.ndg.query.coordinates.Warp;
import org.vpac.ndg.query.coordinates.WarpDual;
import org.vpac.ndg.query.coordinates.WarpFactory;
import org.vpac.ndg.query.coordinates.WarpIdentity;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Type;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.ArrayAdapter;
import org.vpac.ndg.query.sampling.ArrayAdapterImpl;
import org.vpac.ndg.query.sampling.HasPrototype;
import org.vpac.ndg.query.sampling.NodataNullStrategy;
import org.vpac.ndg.query.sampling.NodataStrategy;
import org.vpac.ndg.query.sampling.NodataStrategyFactory;
import org.vpac.ndg.query.sampling.PageCache;
import org.vpac.ndg.query.sampling.Prototype;

import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Section;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;

/**
 * An Adapter for {@link Variable}, to make it look like an IVariable. This type
 * of variable has a backing store on disk; hence "persistent".
 * @author Alex Fraser
 */
public class VariableAdapter implements HasShape, HasRank, HasPrototype {

	final Logger log = LoggerFactory.getLogger(VariableAdapter.class);

	private Variable variable;
	private String name;
	private VectorInt shape;
	private NodataStrategy nodataStrategy;
	private DatasetMeta owner;
	private PageCache cache;
	private Type type;
	private Prototype prototype;

	WarpFactory warpFactory;

	public VariableAdapter(Variable variable, String name, DatasetMeta owner)
			throws QueryConfigurationException {

		log.debug("Wrapping variable {}", variable);

		this.variable = variable;
		this.name = name;
		this.shape = VectorInt.fromInt(variable.getShape());

		String[] dims = variable.getDimensionsString().split(" ");

		this.owner = owner;

		warpFactory = new WarpFactory();

		type = resolveType(variable);
		NodataStrategyFactory nodataStrategyFactory = new NodataStrategyFactory();
		nodataStrategy = nodataStrategyFactory.create(variable, type);
		AttributeDefinition[] adArray = getAttributeMementos();
		log.debug("Nodata strategy is {}", nodataStrategy);

		prototype = new Prototype(new Type[] {type},
				new NodataStrategy[] { nodataStrategy },
				new AttributeDefinition[][] { adArray }, dims);
		log.debug("Prototype is {}", prototype);
		cache = new PageCache(this);
	}

	public static Type resolveType(Variable variable) {
		String dataType = variable.getDataType().toString();
		if (variable.isUnsigned())
			dataType = "u" + dataType;
		return Type.get(dataType);
	}

	public AttributeDefinition[] getAttributeMementos()
			throws QueryConfigurationException {
		List<Attribute> attributes = this.variable.getAttributes();
		List<AttributeDefinition> ads = new ArrayList<AttributeDefinition>();
		for (Attribute attr : attributes)
			ads.add(attributeToMemento(attr));
		AttributeDefinition[] adArray = new AttributeDefinition[ads.size()];
		adArray = ads.toArray(adArray);
		return adArray;
	}

	/**
	 * "serialise" an attribute so that it can be constructed again later.
	 */
	private AttributeDefinition attributeToMemento(Attribute ain)
			throws QueryConfigurationException {
		AttributeDefinition ad = new AttributeDefinition();
		ad.name = ain.getFullName();
		ad._type = ain.getDataType();
		ad._unsigned = ain.isUnsigned();

		if (ain.getDataType().isNumeric()) {
			Type type = Type.get(ad._type, ad._unsigned);
			Array array = ain.getValues();
			ArrayAdapter adapter = ArrayAdapterImpl.createAndPromote(array, ad._type, new NodataNullStrategy(type));
			for (int i = 0; i < ain.getLength(); i++) {
				ad.getProcessedValues().add(adapter.get(i).getValue());
			}
		} else {
			for (int i = 0; i < ain.getLength(); i++) {
				ad.getProcessedValues().add(ain.getValue(i));
			}
		}
		return ad;
	}

	public Variable getVariable() {
		return variable;
	}

	@Override
	public VectorInt getShape() {
		return shape;
	}

	public BoxReal calculateBounds(QueryCoordinateSystem context)
			throws QueryConfigurationException {
		Warp warp = createWarpTo(context);
		VectorReal start = VectorReal.createEmpty(getRank());
		VectorReal end = getShape().toReal();
		BoxReal bounds = new BoxReal(start, end);
		warp.warp(bounds);
//		bounds.getMax().setT(bounds.getMax().getT() + 1.0);
		return bounds;
	}

	public Warp createWarpTo(QueryCoordinateSystem context) throws
			QueryConfigurationException {
		return createWarp(context, true);
	}

	public Warp createWarpFrom(QueryCoordinateSystem context) throws
			QueryConfigurationException {
		return createWarp(context, false);
	}

	private Warp createWarp(QueryCoordinateSystem context, boolean invert)
			throws QueryConfigurationException {

		List<String> dimensions = Arrays.asList(
				variable.getDimensionsString().split(" "));

		QueryCoordinateSystem from;
		QueryCoordinateSystem to;

		if (!invert) {
			from = context;
			to = owner.getCoordinateSystem();
		} else {
			from = owner.getCoordinateSystem();
			to = context;
		}

		// Create spatial warp for variables that have spatial dimensions.
		Warp spatialWarp = null;
		if (dimensions.contains("x") && dimensions.contains("y")) {
			spatialWarp = warpFactory.createGridWarp(from.getGrid(),
					to.getGrid(), dimensions.size());
		} else if (dimensions.contains("lat") && dimensions.contains("lon")) {
			spatialWarp = warpFactory.createGridWarp(from.getGrid(),
					to.getGrid(), dimensions.size());
		}

		// Create temporal warp for variables that have a time dimension.
		Warp temporalWarp = null;
		if (dimensions.contains("time")) {
			temporalWarp = warpFactory.createTemporalWarp(from.getTimeAxis(),
					to.getTimeAxis());
		}

		// Combine warps together.
		if (spatialWarp != null && temporalWarp != null)
			return new WarpDual(spatialWarp, temporalWarp);
		else if (spatialWarp != null)
			return spatialWarp;
		else if (temporalWarp != null)
			return temporalWarp;
		else
			return new WarpIdentity();
	}

	public PageCache getPageCache() {
		return cache;
	}

	public Array read() throws IOException {
		synchronized(owner.getDataset()) {
			return variable.read();
		}
	}

	public Array read(Section section) throws IOException,
			InvalidRangeException {
		synchronized(owner.getDataset()) {
			return variable.read(section);
		}
	}

	public VariableAdapter slice(int dim, int value)
			throws InvalidRangeException, QueryConfigurationException {
		return new VariableAdapter(variable.slice(dim, value), name, owner);
	}

	public int findDimensionIndex(String dimname) {
		return variable.findDimensionIndex(dimname);
	}

	public String getDimensionsString() {
		return variable.getDimensionsString();
	}

	public DataType getDataType() {
		return variable.getDataType();
	}

	@Override
	public String toString() {
		String dimstring = variable.getDimensionsString();
		return String.format("%s(%s)", name, dimstring);
	}

	public List<Attribute> getAttributes() {
		return variable.getAttributes();
	}

	public String getName() {
		return name;
	}

	@Override
	public int getRank() {
		return getShape().size();
	}

	@Override
	public Prototype getPrototype() {
		return prototype;
	}

	public NodataStrategy getNodataStrategy() {
		return nodataStrategy;
	}

}
