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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * A dataset in an NCML file. This will contain either:
 * <ul>
 * <li>A <em>location</em>, representing a physical file, or</li>
 * <li>An <em>aggregation</em>, representing a collection of sub-datasets.</li>
 * </ul>
 * <p>
 * Examples:
 * </p>
 * 
 * <pre>
 * &lt;netcdf location="timeslice.nc" coordValue="0"/>
 * </pre>
 * 
 * <pre>
 * &lt;netcdf>
 *   &lt;variable name="time" shape="time" type="int">
 *     &lt;attribute name="units" value="days since 2011-01-01"/>
 *     &lt;attribute name="_CoordinateAxisType" value="Time"/>
 *   &lt;/variable>
 *   &lt;aggregation dimName="time" type="joinNew">...&lt;/aggregation>
 * &lt;/netcdf>
 * </pre>
 * 
 * @author Alex Fraser
 */
@XStreamAlias("netcdf")
public class AggregationDefinition {

	@XStreamOmitField
	protected static final String DOCTYPE =
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	@XStreamOmitField
	protected static final String NAMESPACE =
			"http://www.unidata.ucar.edu/namespaces/netcdf/ncml-2.2";

	@XStreamOmitField
	public int nValidChildren = 0;

	@XStreamAsAttribute
	private String location;
	@XStreamAsAttribute
	private String section;

	@XStreamImplicit
	private List<DimDef> dimensions;
	@XStreamImplicit
	private List<VarDef> variables;
	private AggDef aggregation;

	@XStreamAsAttribute
	private String coordValue;

	// Filthy hack to put namespace in the root element; see serialise().
	@XStreamAsAttribute
	private String xmlns;

	public AggregationDefinition() {
	}

	public AggregationDefinition(Path location) {
		this();
		this.location = location.toString();
	}

	public static AggregationDefinition deserialise(Path path) {
		XStream xstream = getXstream();
		AggregationDefinition q = (AggregationDefinition)xstream.fromXML(
				path.toFile());
		return q;
	}

	public void serialise(Path path) throws IOException {
		XStream xstream = getXstream();
		try (PrintWriter out = new PrintWriter(new FileWriter(path.toFile()))) {
			out.println(DOCTYPE);
			setXmlns(NAMESPACE);
			xstream.toXML(this, out);
		} finally {
			setXmlns(null);
		}
	}

	public void serialise(StringWriter out) throws IOException {
		XStream xstream = getXstream();
		try (PrintWriter outWriter = new PrintWriter(out)) {
			outWriter.println(DOCTYPE);
			setXmlns(NAMESPACE);
			xstream.toXML(this, outWriter);
		} finally {
			setXmlns(null);
		}
	}

	protected static XStream getXstream() {
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(AggregationDefinition.class);
		xstream.processAnnotations(AggDef.class);
		xstream.processAnnotations(AttrDef.class);
		xstream.processAnnotations(VarAggDef.class);
		xstream.processAnnotations(VarDef.class);
		xstream.processAnnotations(ValDef.class);
		xstream.processAnnotations(DimDef.class);
		return xstream;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public AggDef getAggregation() {
		return aggregation;
	}

	public void setAggregation(AggDef aggregation) {
		this.aggregation = aggregation;
	}

	public String getCoordValue() {
		return coordValue;
	}

	public void setCoordValue(String coordValue) {
		this.coordValue = coordValue;
	}

	public List<VarDef> getVariables() {
		if (variables == null) {
			variables = new ArrayList<>();
		}
		return variables;
	}

	protected String getXmlns() {
		return xmlns;
	}

	/**
	 * Sets the namespace attribute. This should only be done for the root
	 * element.
	 */
	protected void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

	public List<DimDef> getDimensions() {
		if (dimensions == null) {
			dimensions = new ArrayList<>();
		}
		return dimensions;
	}

	public void setDimensions(List<DimDef> dimensions) {
		this.dimensions = dimensions;
	}

	/**
	 * Combines a collection of datasets.
	 * <p>Examples:</p>
	 * <pre>&lt;aggregation dimName="time" type="joinNew"></pre>
	 * <pre>&lt;aggregation dimName="x y" type="tiled"></pre>
	 */
	@XStreamAlias("aggregation")
	public static class AggDef {
		@XStreamAsAttribute
		private String dimName;
		@XStreamAsAttribute
		private String type;

		@XStreamImplicit
		private List<VarAggDef> aggVars;
		@XStreamImplicit
		private List<AggregationDefinition> children;

		public AggDef() {
		}

		public AggDef(String dimName, String type) {
			this();
			this.setDimName(dimName);
			this.setType(type);
		}

		public String getDimName() {
			return dimName;
		}

		public void setDimName(String dimName) {
			this.dimName = dimName;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<VarAggDef> getAggVars() {
			if (aggVars == null)
				aggVars = new ArrayList<>();
			return aggVars;
		}

		public List<AggregationDefinition> getChildren() {
			if (children == null)
				children = new ArrayList<>();
			return children;
		}
	}

	@XStreamAlias("variable")
	public static class VarDef {
		@XStreamAsAttribute
		private String name;
		/**
		 * Original name, allows e.g. "Band1" to be remapped to "Band20".
		 * <pre>&lt;variable name="Band20" orgName="Band1"/&gt;</pre>
		 */
		@XStreamAsAttribute
		private String orgName;

		@XStreamAsAttribute
		private String shape;
		@XStreamAsAttribute
		private String type;

		private ValDef values;

		@XStreamImplicit
		private List<AttrDef> attrs;

		public VarDef() {
		}

		/**
		 * Create a variable that can be used to imply a new dimension in a
		 * joinNew aggregation.
		 */
		public static VarDef newDimension(String name, String type,
				String units) {
			VarDef vd = new VarDef();
			vd.setName(name);
			// The shape of this variable will be taken from a dimension of the
			// same name.
			vd.setShape(name);
			vd.setType(type);
			vd.getAttrs().add(new AttrDef("units", units));
			if (name.equals("time"))
					vd.getAttrs().add(new AttrDef("_CoordinateAxisType", "Time"));
			return vd;
		}

		public static VarDef remap(String originalName, String newname) {
			VarDef vd = new VarDef();
			vd.setName(newname);
			vd.setOrgName(originalName);
			return vd;
		}

		public static VarDef regularCoordinateAxis(String name, String start, String increment) {
			ValDef values = new ValDef(start, increment);
			VarDef vd = new VarDef();
			vd.setName(name);
			vd.setValues(values);
			return vd;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getOrgName() {
			return orgName;
		}

		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}

		public String getShape() {
			return shape;
		}

		public void setShape(String shape) {
			this.shape = shape;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<AttrDef> getAttrs() {
			if (attrs == null)
				attrs = new ArrayList<>();
			return attrs;
		}

		public ValDef getValues() {
			return values;
		}

		public void setValues(ValDef values) {
			this.values = values;
		}
	}

	@XStreamAlias("dimension")
	public static class DimDef {
		@XStreamAsAttribute
		private String name;
		@XStreamAsAttribute
		private String length;

		public DimDef() {}

		public DimDef(String name, String length) {
			this.setName(name);
			this.setLength(length);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLength() {
			return length;
		}

		public void setLength(String length) {
			this.length = length;
		}
	}

	/**
	 * Values of a variable.
	 */
	@XStreamAlias("values")
	public static class ValDef {
		@XStreamAsAttribute
		private String start;
		@XStreamAsAttribute
		private String increment;

		// Could also have ctext listing literal values.

		public ValDef() {}

		public ValDef(String start, String increment) {
			this.setStart(start);
			this.setIncrement(increment);
		}

		public String getStart() {
			return start;
		}

		public void setStart(String start) {
			this.start = start;
		}

		public String getIncrement() {
			return increment;
		}

		public void setIncrement(String increment) {
			this.increment = increment;
		}
	}

	@XStreamAlias("attribute")
	public static class AttrDef {
		@XStreamAsAttribute
		private String name;
		@XStreamAsAttribute
		private String value;

		public AttrDef() {}

		public AttrDef(String name, String value) {
			this.setName(name);
			this.setValue(value);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	@XStreamAlias("variableAgg")
	public static class VarAggDef {
		@XStreamAsAttribute
		private String name;

		public VarAggDef() {}

		public VarAggDef(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

}
