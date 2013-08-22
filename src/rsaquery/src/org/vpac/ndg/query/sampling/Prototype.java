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

package org.vpac.ndg.query.sampling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.QueryDefinition.AttributeDefinition;
import org.vpac.ndg.query.QueryRuntimeException;
import org.vpac.ndg.query.StringUtils;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.Type;
import org.vpac.ndg.query.math.VectorElement;

/**
 * Metatdata for connections between filters.
 * @author Alex Fraser
 */
public class Prototype {

	// There is one nodata strategy for each prototypical element.
	private Type[] types;
	private NodataStrategy[] nodataStrategies;
	// null until getElement is called
	private Element<?> element;
	// Arbitrary attributes can be passed along with the data type.
	private AttributeDefinition[][] attributes;

	// Dimensions are completely separate from the data type and nodata values
	private String[] dimensions;

	public Prototype(Type[] types, NodataStrategy[] ndss,
			AttributeDefinition[][] attributes, String[] dims) {

		int ncomponents = types.length;
		if (ncomponents != ndss.length) {
			throw new IllegalArgumentException(String.format(
					"Number of nodata strategies (%d) does not match number " +
					"of element components (%d)", ndss.length, ncomponents));
		}

		this.types = types;
		this.attributes = attributes;
		nodataStrategies = ndss;
		dimensions = dims;
	}

	public Prototype copy() {
		NodataStrategy[] ndss = new NodataStrategy[nodataStrategies.length];
		AttributeDefinition[][] attrs = new AttributeDefinition[attributes.length][];
		for (int i = 0; i < nodataStrategies.length; i++) {
			ndss[i] = nodataStrategies[i].copy();
			attrs[i] = new AttributeDefinition[attributes[i].length];
			for (int j = 0; j < attributes[i].length; j++) {
				attrs[i][j] = attributes[i][j].copy();
			}
		}
		return new Prototype(types.clone(), ndss, attributes.clone(), dimensions.clone());
	}

	/**
	 * @see #combine(Prototype[], String[])
	 */
	public static Prototype combine(Collection<? extends HasPrototype> sources,
			String[] dimensions) throws QueryConfigurationException {

		Prototype[] prototypes = new Prototype[sources.size()];
		int i = 0;
		for (HasPrototype source : sources) {
			prototypes[i] = source.getPrototype();
			i++;
		}
		return combine(prototypes, dimensions);
	}

	/**
	 * Create a new prototype that is the combination of a bunch of other
	 * prototypes.
	 *
	 * @param prototypes The prototypes to combine.
	 * @param dimensions The dimensions of the new prototype. If null, the
	 *        dimensions will be inherited from the sources, in which case all
	 *        sources must have the same dimensions.
	 * @return The new prototype.
	 */
	public static Prototype combine(Prototype[] prototypes, String[] dimensions)
			throws QueryConfigurationException {

		List<Type> types = new ArrayList<Type>();
		List<NodataStrategy> ndss = new ArrayList<NodataStrategy>();
		List<AttributeDefinition[]> attributes = new ArrayList<AttributeDefinition[]>();
		String[] dims = dimensions;

		for (Prototype pt : prototypes) {
			if (dimensions != null) {
				// Caller has overridden dimensions; don't need to inherit.
			} else if (dims == null) {
				dims = pt.getDimensions();
			} else if (!Arrays.equals(dims, pt.getDimensions())) {
				throw new QueryConfigurationException(
						"Can't create prototype: components have " +
						"differing dimensionality.");
			}

			types.addAll(Arrays.asList(pt.types));
			for (NodataStrategy nds : pt.getNodataStrategies()) {
				ndss.add(nds.copy());
			}
			for (AttributeDefinition[] ads : pt.attributes) {
				AttributeDefinition[] newAds = new AttributeDefinition[ads.length];
				for (int i = 0; i < newAds.length; i++) {
					newAds[i] = ads[i].copy();
				}
				attributes.add(newAds);
			}
		}

		if (types.size() != ndss.size()) {
			throw new QueryRuntimeException("Number of element components " +
					"does not match number of nodata strategies.");
		}
		if (types.size() != attributes.size()) {
			throw new QueryRuntimeException("Number of element components " +
					"does not match number of attributes.");
		}

		Type[] typeArray = new Type[types.size()];
		typeArray = types.toArray(typeArray);

		NodataStrategy[] ndsArray = new NodataStrategy[ndss.size()];
		ndsArray = ndss.toArray(ndsArray);

		AttributeDefinition[][] attrsArray = new AttributeDefinition[attributes.size()][];
		attrsArray = attributes.toArray(attrsArray);

		return new Prototype(typeArray, ndsArray, attrsArray, dims);
	}

	/**
	 * @return A prototypical element for this object. This can be used by
	 * algorithms that don't need to know what type of data they are dealing
	 * with. Don't modify this element; call {@link Element#copy()} to get a
	 * local copy.
	 */
	public Element<?> getElement() {
		if (element == null) {
			if (types.length == 1)
				element = types[0].getElement().copy();
			else
				element = VectorElement.create(types);
		}
		return element;
	}

	public Type[] getTypes() {
		return types;
	}

	public String[] getDimensions() {
		return dimensions;
	}

	public void setDimensions(String[] dimensions) {
		this.dimensions = dimensions;
	}

	public NodataStrategy[] getNodataStrategies() {
		return nodataStrategies;
	}

	/**
	 * Convert the prototype to a specific numeric type while keeping the same
	 * number of components.
	 * @param type The type to convert to (byte, short, int, ...)
	 */
	public void convert(String type) throws QueryConfigurationException {
		// Convert to specified type - this keeps the same number of components.
		if (type.equals(""))
			return;

		// Replace all types with the one specified.
		Type targetType = Type.get(type);
		for (int i = 0; i < types.length; i++)
			types[i] = targetType;
		element = null;

		for (NodataStrategy nds : nodataStrategies)
			nds.convert(targetType);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String dim : dimensions) {
			if (sb.length() > 0)
				sb.append(' ');
			sb.append(dim);
		}
		String ts = StringUtils.join(types, " ");
		return String.format("Prototype(%s, %s)", ts, sb);
	}

	public AttributeDefinition[][] getAttributes() {
		return attributes;
	}

}
