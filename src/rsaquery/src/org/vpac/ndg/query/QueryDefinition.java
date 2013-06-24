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

package org.vpac.ndg.query;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ucar.ma2.DataType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * <p>Serialisation structure of queries.</p>
 *
 * <img src="doc-files/QueryDefinition_class.png" />
 *
 * <p>For example, this filter graph is serialised as the XML listed below.</p>
 *
 * <img src="doc-files/QueryDefinition_example.png" />
 *
 * <pre>
&lt;?xml version="1.0" encoding="UTF-8"?>
&lt;query xmlns="http://www.vpac.org/namespaces/rsaquery-0.2">
	&lt;input id="infile" file="../input/abstract.nc" />

	&lt;output id="outfile">
		&lt;grid ref="#infile"
			bounds="935175.00 -3675500.0 1050975.0 -3582900.0"
			timeMin="2012-06-22" timeMax="2013-01-01" />

		&lt;variable name="wet" dimensions="y x" ref="#Wet/output" />
		&lt;variable name="time" dimensions="y x" ref="#Wet/outtime" />
	&lt;/output>

	&lt;filter id="Wet" cls="org.vpac.ndg.query.WettingExtents">
		&lt;literal name="dryThreshold" value="64" />
		&lt;sampler name="input" ref="#infile/Band1" />
		&lt;sampler name="intime" ref="#infile/time" />
	&lt;/filter>
&lt;/query>
</pre>
 * @author Alex Fraser
 */
@XStreamAlias("query")
public class QueryDefinition {

	@XStreamImplicit
	public List<DatasetInputDefinition> inputs;
	public CacheDefinition cache;
	public DatasetOutputDefinition output;
	@XStreamImplicit
	public List<FilterDefinition> filters;

	public QueryDefinition copy() {
		QueryDefinition qd = new QueryDefinition();
		if (inputs != null) {
			qd.inputs = new ArrayList<DatasetInputDefinition>();
			for (DatasetInputDefinition did : inputs)
				qd.inputs.add(did.copy());
		}
		if (cache != null)
			qd.cache = cache.copy();
		if (output != null)
			qd.output = output.copy();
		if (filters != null) {
			qd.filters = new ArrayList<FilterDefinition>();
			for (FilterDefinition fd : filters)
				qd.filters.add(fd.copy());
		}
		return qd;
	}

	@XStreamAlias("input")
	public static class DatasetInputDefinition {
		@XStreamAsAttribute
		public String id;
		@XStreamAsAttribute
		public String href;

		@Override
		public String toString() {
			return String.format("<input id=\"%s\">", id);
		}

		public DatasetInputDefinition copy() {
			DatasetInputDefinition did = new DatasetInputDefinition();
			did.id = id;
			did.href = href;
			return did;
		}
	}

	@XStreamAlias("cache")
	public static class CacheDefinition {
		/**
		 * The number of pages to store in the cache per sampler.
		 */
		@XStreamAsAttribute
		@XStreamConverter(IntConverter.class)
		public Integer pages;
		/**
		 * A list of dimensions; the order in which data will be sampled
		 * (fastest-varying is last, like NetCDF).
		 */
		@XStreamAsAttribute
		public String precedence;
		/**
		 * The minimum number of pixels per page.
		 */
		@XStreamAsAttribute
		@XStreamConverter(IntConverter.class)
		public Integer volume;

		/**
		 * The typical sampling window of the kernels, as a string of integers
		 * delimited by spaces. E.g. "7 7".
		 */
		@XStreamAsAttribute
		public String window;
		/**
		 * The dimensions to use for the window. If null, the dimensions of the
		 * band will be used.
		 */
		@XStreamAsAttribute
		public String windowAxes;

		@Override
		public String toString() {
			return String.format("<cache>");
		}

		public CacheDefinition copy() {
			CacheDefinition cd = new CacheDefinition();
			cd.pages = pages;
			cd.precedence = precedence;
			cd.volume = volume;
			cd.window = window;
			cd.windowAxes = windowAxes;
			return cd;
		}
	}

	@XStreamAlias("output")
	public static class DatasetOutputDefinition {
		@XStreamAsAttribute
		public String id;

		public GridDefinition grid;

		@XStreamImplicit
		public List<VariableDefinition> variables;

		public DatasetOutputDefinition copy() {
			DatasetOutputDefinition dod = new DatasetOutputDefinition();
			dod.id = id;
			if (grid != null)
				dod.grid = grid.copy();
			if (variables != null) {
				dod.variables = new ArrayList<VariableDefinition>();
				for (VariableDefinition vd : variables)
					dod.variables.add(vd.copy());
			}
			return dod;
		}

		@Override
		public String toString() {
			return String.format("<output id=\"%s\">", id);
		}
	}

	@XStreamAlias("grid")
	public static class GridDefinition {
		/**
		 * Specifies a source dataset to inherit a projection from, e.g.
		 * <em>#infile</em>.
		 */
		@XStreamAsAttribute
		public String ref;

		/**
		 * The bounding box <em>(xmin, ymin, xmax, ymax)</em>, specified in the
		 * projected coordinate system. If null, a box will be constructed based
		 * on the {@link GridDefinition#autobounds autobounds} field.
		 */
		@XStreamAsAttribute
		public String bounds;

		/**
		 * Temporal lower bound; times before this start date will be omitted.
		 * This is not the <em>actual</em> first date in the output dataset;
		 * that will be inferred from the input.
		 */
		@XStreamAsAttribute
		public String timeMin;
		/**
		 * Temporal upper bound; times after this end date will be omitted.
		 * This is not the <em>actual</em> last date in the output dataset;
		 * that will be inferred from the input.
		 */
		@XStreamAsAttribute
		public String timeMax;

		/**
		 * Automatically create a bounding box based on the input. One of
		 * <em>union</em> or <em>intersection</em>. Defaults to <em>union</em>.
		 * May be overridden by the {@link GridDefinition#bounds bounds} field.
		 */
		@XStreamAsAttribute
		public String autobounds;

		public GridDefinition copy() {
			GridDefinition gd = new GridDefinition();
			gd.ref = ref;
			gd.bounds = bounds;
			gd.autobounds = autobounds;
			gd.timeMax = timeMax;
			gd.timeMin = timeMin;
			return gd;
		}

		@Override
		public String toString() {
			return String.format("<grid ref=\"%s\">", ref);
		}
	}

	@XStreamAlias("variable")
	public static class VariableDefinition {
		@XStreamAsAttribute
		public String name;
		@XStreamAsAttribute
		public String ref;
		@XStreamAsAttribute
		public String dimensions;
		@XStreamAsAttribute
		public String type;

		// Raw attributes
		@XStreamImplicit
		public List<AttributeDefinition> attributes;
		// Processed and inherited attributes
		@XStreamOmitField
		private Map<String, AttributeDefinition> _attributes;

		public VariableDefinition copy() {
			VariableDefinition vd = new VariableDefinition();
			vd.name = name;
			vd.ref = ref;
			vd.dimensions = dimensions;
			vd.type = type;
			if (attributes != null) {
				vd.attributes = new ArrayList<AttributeDefinition>();
				for (AttributeDefinition ad : attributes)
					vd.attributes.add(ad.copy());
			}
			if (_attributes != null) {
				vd._attributes = new HashMap<String, AttributeDefinition>();
				for (Entry<String, AttributeDefinition> ad : _attributes.entrySet())
					vd._attributes.put(ad.getKey(), ad.getValue().copy());
			}
			return vd;
		}

		public Map<String, AttributeDefinition> getProcessedAttributes() {
			if (_attributes == null)
				_attributes = new HashMap<String, AttributeDefinition>();
			return _attributes;
		}

		@Override
		public String toString() {
			if (name != null)
				return String.format("<variable name=\"%s\">", name);
			else
				return String.format("<variable ref=\"%s\">", ref);
		}
	}

	@XStreamAlias("attribute")
	public static class AttributeDefinition {
		@XStreamAsAttribute
		public String name;
		// Raw value(s)
		@XStreamAsAttribute
		public String value;

		// Processed values
		@XStreamOmitField
		private List<Object> _values;
		@XStreamOmitField
		public DataType _type;
		@XStreamOmitField
		public boolean _unsigned;

		public AttributeDefinition copy() {
			AttributeDefinition newAttr = new AttributeDefinition();
			newAttr.name = name;
			newAttr.value = value;
			newAttr._type = _type;
			if (_values != null) {
				newAttr.getProcessedValues().addAll(_values);
			}
			return newAttr;
		}

		public List<Object> getProcessedValues() {
			if (_values == null)
				_values = new ArrayList<Object>();
			return _values;
		}

		@Override
		public String toString() {
			return String.format("<attribute name=\"%s\">", name);
		}
	}

	@XStreamAlias("filter")
	public static class FilterDefinition {
		@XStreamAsAttribute
		public String id;
		@XStreamAsAttribute
		@XStreamAlias("cls")
		public String classname;
		@XStreamImplicit
		public List<LiteralDefinition> literals;
		@XStreamImplicit
		public List<SamplerDefinition> samplers;

		public FilterDefinition copy() {
			FilterDefinition fd = new FilterDefinition();
			fd.id = id;
			fd.classname = classname;
			if (literals != null) {
				fd.literals = new ArrayList<LiteralDefinition>();
				for (LiteralDefinition ld : literals)
					fd.literals.add(ld.copy());
			}
			if (samplers != null) {
				fd.samplers = new ArrayList<SamplerDefinition>();
				for (SamplerDefinition sd : samplers)
					fd.samplers.add(sd.copy());
			}
			return fd;
		}

		@Override
		public String toString() {
			return String.format("<filter name=\"%s\">", id);
		}
	}

	@XStreamAlias("literal")
	public static class LiteralDefinition {
		@XStreamAsAttribute
		public String name;
		@XStreamAsAttribute
		public String value;

		public LiteralDefinition copy() {
			LiteralDefinition sd = new LiteralDefinition();
			sd.name = name;
			sd.value = value;
			return sd;
		}

		@Override
		public String toString() {
			return String.format("<literal name=\"%s\">", name);
		}
	}

	@XStreamAlias("sampler")
	public static class SamplerDefinition {
		@XStreamImplicit
		public List<SamplerDefinition> children;
		@XStreamAsAttribute
		public String name;
		@XStreamAsAttribute
		public String ref;
		@XStreamImplicit
		public List<SliceDefinition> slices;

		@XStreamOmitField
		public NodeReference _nodeRef;

		public SamplerDefinition copy() {
			SamplerDefinition sd = new SamplerDefinition();
			sd.name = name;
			sd.ref = ref;
			if (children != null) {
				sd.children = new ArrayList<SamplerDefinition>();
				for (SamplerDefinition sd2 : children)
					sd.children.add(sd2.copy());
			}
			if (slices != null) {
				sd.slices = new ArrayList<SliceDefinition>();
				for (SliceDefinition sd2 : slices)
					sd.slices.add(sd2.copy());
			}
			if (_nodeRef != null)
				sd._nodeRef = _nodeRef;
			return sd;
		}

		@Override
		public String toString() {
			return String.format("<sampler name=\"%s\">", name);
		}
	}

	@XStreamAlias("slice")
	public static class SliceDefinition {
		@XStreamAsAttribute
		public String dimension;
		@XStreamAsAttribute
		@XStreamConverter(IntConverter.class)
		public Integer value;

		public SliceDefinition copy() {
			SliceDefinition sd = new SliceDefinition();
			sd.dimension = dimension;
			sd.value = value;
			return sd;
		}

		@Override
		public String toString() {
			return String.format("<slice dimension=\"%s\">", dimension);
		}
	}

	private static XStream getMarshaller() {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(QueryDefinition.class);
		xstream.processAnnotations(DatasetInputDefinition.class);
		xstream.processAnnotations(DatasetOutputDefinition.class);
		xstream.processAnnotations(GridDefinition.class);
		xstream.processAnnotations(VariableDefinition.class);
		xstream.processAnnotations(AttributeDefinition.class);
		xstream.processAnnotations(FilterDefinition.class);
		xstream.processAnnotations(LiteralDefinition.class);
		xstream.processAnnotations(SamplerDefinition.class);
		return xstream;
	}

	public static QueryDefinition fromXML(File configFile)
			throws FileNotFoundException {

		InputStream stream = new FileInputStream(configFile);

		try {
			return fromXML(stream);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				System.err.println("Could not close query definition file.");
			}
		}
	}

	public static QueryDefinition fromString(String queryString) {
		XStream xstream = getMarshaller();
		QueryDefinition q = (QueryDefinition)xstream.fromXML(queryString);
		return q;
	}

	public static QueryDefinition fromXML(InputStream stream) {
		XStream xstream = getMarshaller();
		QueryDefinition q = (QueryDefinition)xstream.fromXML(stream);
		return q;
	}

	public String toXML() {
		XStream xstream = getMarshaller();
		String xmlString = xstream.toXML(this);
		return xmlString;
	}

}
