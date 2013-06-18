package org.vpac.ndg.query;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.QueryDefinition.AttributeDefinition;
import org.vpac.ndg.query.coordinates.HasBounds;
import org.vpac.ndg.query.coordinates.HasRank;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Swizzle;
import org.vpac.ndg.query.math.SwizzleFactory;
import org.vpac.ndg.query.math.Type;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.CellFactory;
import org.vpac.ndg.query.sampling.CellType;
import org.vpac.ndg.query.sampling.HasPrototype;
import org.vpac.ndg.query.sampling.NodataNullStrategy;
import org.vpac.ndg.query.sampling.NodataStrategy;
import org.vpac.ndg.query.sampling.PixelSource;
import org.vpac.ndg.query.sampling.PixelSourceFactory;
import org.vpac.ndg.query.sampling.Prototype;

/**
 * A node in a query graph. This class wraps {@link Filter Filters}, providing
 * the extra functionality needed by the query engine.
 * 
 * @author Alex Fraser
 */
public class FilterAdapter implements HasBounds, HasRank, Diagnostics {

	private final Logger log = LoggerFactory.getLogger(FilterAdapter.class);

	private BoxReal bounds;
	private String[] dimensions;
	private String name;
	private Filter innerFilter;
	private VectorReal internalCo;
	private VectorReal lastCo;
	private Map<String, PixelSource> outputSockets;

	CellFactory cellFactory;
	PixelSourceFactory pixelSourceFactory;


	public FilterAdapter(String name, Filter innerFilter) throws
			QueryConfigurationException {

		this.name = name;
		this.innerFilter = innerFilter;
		outputSockets = new HashMap<String, PixelSource>();
		cellFactory = new CellFactory();
		pixelSourceFactory = new PixelSourceFactory();
	}

	public void initialise() throws QueryConfigurationException {
		innerFilter.initialise(bounds);
	}

	Cell createCell(String name) throws QueryConfigurationException {
		Field f;
		try {
			f = innerFilter.getClass().getField(name);
		} catch (NoSuchFieldException e) {
			throw new QueryConfigurationException(String.format(
					"Could not create output socket %s in filter %s",
					name, innerFilter.getClass()), e);
		}

		CellType cellType = f.getAnnotation(CellType.class);
		if (cellType == null) {
			throw new QueryConfigurationException(String.format(
					"Could not create output socket %s in filter %s: no type " +
					"defined (missing @CellType annotation).",
					name, innerFilter.getClass()));
		}

		Prototype prototype;
		try {
			prototype = getPrototype(cellType.value()).copy();
		} catch (QueryConfigurationException e) {
			throw new QueryConfigurationException(String.format(
					"Could not create output socket %s in filter %s: %s", name,
					innerFilter.getClass(), e.getMessage()), e);
		}

		String convertTo = cellType.as();
		prototype.convert(convertTo);

		log.debug("Creating cell {} with prototype {}", name, prototype);
		return cellFactory.create(name, prototype);
	}

	Prototype getPrototype(String inheritDecl)
			throws QueryConfigurationException {

		if (inheritDecl.contains(",")) {
			// Vector
			String[] componentDecls = inheritDecl.split(",");
			Prototype[] ps = new Prototype[componentDecls.length];
			for (int i = 0; i < ps.length; i++) {
				ps[i] = getPrototypeSingle(componentDecls[i]);
			}
			return Prototype.combine(ps, dimensions);
		} else {
			Prototype pt = getPrototypeSingle(inheritDecl).copy();
			pt.setDimensions(dimensions);
			return pt;
		}
	}

	Prototype getPrototypeSingle(String typeDecl)
			throws QueryConfigurationException {

		Type type;
		try {
			type = Type.get(typeDecl);
		} catch (IllegalArgumentException e) {
			type = null;
		}

		if (type != null) {
			// Explicit type.
			// TODO: allow nodata strategy to be specified in type declaration
			Type[] types = new Type[] { type };
			NodataStrategy nds = new NodataNullStrategy(type);
			NodataStrategy[] ndss = new NodataStrategy[] { nds };
			AttributeDefinition[][] attrs = new AttributeDefinition[][] { new AttributeDefinition[0] };
			return new Prototype(types, ndss, attrs, dimensions);
		}

		// Inherited type; may be scalar or vector
		Field tf;
		try {
			tf = innerFilter.getClass().getField(typeDecl);
		} catch (NoSuchFieldException e) {
			throw new QueryConfigurationException(String.format(
					"Could not find field %s to inherit type from.", typeDecl),
					e);
		}
		if (!HasPrototype.class.isAssignableFrom(tf.getType())) {
			throw new QueryConfigurationException(String.format(
					"Inherited field %s has no prototype.", typeDecl));
		}

		HasPrototype typeSource;
		try {
			if (tf.get(innerFilter) == null) {
				throw new QueryConfigurationException(String.format(
						"Inherited field %s accessed before assignment.",
						typeDecl));
			}
			typeSource = (HasPrototype) tf.get(innerFilter);
		} catch (IllegalAccessException e) {
			throw new QueryConfigurationException(String.format(
					"Inherited field %s is not accessible.", typeDecl));
		}

		return typeSource.getPrototype();
	}

	/**
	 * Get an output socket. Note that this must be called after the shape of
	 * the filter has been set.
	 *
	 * @param name The name of the output socket.
	 * @return A sampler that fetches filtered values from this filter.
	 * @throws QueryConfigurationException if the field does not exist, or if it
	 *             is inaccessible.
	 */
	public PixelSource getOutputSocket(String name)
			throws QueryConfigurationException {

		PixelSource socket = outputSockets.get(name);

		if (socket != null)
			return socket;

		Cell cell = createCell(name);
		socket = pixelSourceFactory.create(this, cell, bounds);
		log.debug("Created output socket {}", socket);

		Field f;
		try {
			f = innerFilter.getClass().getField(name);
		} catch (NoSuchFieldException e) {
			throw new QueryConfigurationException(String.format(
					"Could not create output socket %s in filter %s",
					name, innerFilter.getClass()), e);
		}
		try {
			f.set(innerFilter, cell);
		} catch (IllegalAccessException e) {
			throw new QueryConfigurationException(String.format(
					"Could not assign output socket %s in filter %s",
					name, innerFilter.getClass()), e);
		}

		outputSockets.put(name, socket);

		return socket;
	}

	/**
	 * Evaluate the filter; i.e. ask the filter to populate its outputs for a
	 * given coordinate. If the filter has already been run for those
	 * coordinates, cached values may be used for the outputs, in which case the
	 * kernel will not be called.
	 * @param co The coordinates in the output image being written to.
	 * @throws IOException If the filter's source fields could not be read from.
	 */
	public void invoke(VectorReal co) throws IOException {
		try {
			if (co.equals(lastCo))
				return;
			internalCo.set(co);
		} catch (IndexOutOfBoundsException e) {
			throw new QueryRuntimeException(String.format(
					"Failed to set coordinates of filter \"%s\". Check " +
					"dimensionality.", name), e);
		}
		innerFilter.kernel(internalCo);
		lastCo.set(co);
	}

	@Override
	public BoxReal getBounds() {
		return bounds;
	}

	/**
	 * Finds the effective shape of this filter by applying a reduction to one
	 * of the inputs.
	 */
	void inferShapeFromInputs() throws QueryConfigurationException {
		InheritDimensions reduces = innerFilter.getClass().getAnnotation(InheritDimensions.class);
		if (reduces == null)
			return;

		Field field;
		try {
			field = innerFilter.getClass().getField(reduces.from());
		} catch (NoSuchFieldException e) {
			throw new QueryConfigurationException(String.format(
					"Could not find reduction field %s in filter %s.",
					reduces.from(), innerFilter.getClass().getName()));
		}
		Object value;
		try {
			value = field.get(innerFilter);
		} catch (IllegalAccessException e) {
			throw new QueryConfigurationException(String.format(
					"Could not access reduction field %s in filter %s.",
					reduces.from(), innerFilter.getClass().getName()));
		}
		if (value == null) {
			throw new QueryConfigurationException(String.format(
					"Reduction field %s in filter %s is null.",
					reduces.from(), innerFilter.getClass().getName()));
		}
		if (!HasBounds.class.isAssignableFrom(value.getClass())) {
			throw new QueryConfigurationException(String.format(
					"Reduction field %s in filter %s has no bounds.",
					reduces.from(), innerFilter.getClass().getName()));
		}
		if (!HasPrototype.class.isAssignableFrom(value.getClass())) {
			throw new QueryConfigurationException(String.format(
					"Reduction field %s in filter %s has no prototype.",
					reduces.from(), innerFilter.getClass().getName()));
		}

		BoxReal inputBounds = ((HasBounds) value).getBounds();
		Prototype pt = ((HasPrototype) value).getPrototype();

		// Inherit dimensionality
		int inputDims = inputBounds.getRank();
		int outputDims = inputDims - reduces.reduceBy();
		Swizzle resizer = SwizzleFactory.resize(inputDims, outputDims);
		BoxReal bounds = new BoxReal(outputDims);
		resizer.swizzle(inputBounds, bounds);
		dimensions = Arrays.copyOfRange(pt.getDimensions(), reduces.reduceBy(),
				pt.getDimensions().length);
		log.debug("Dimensions of filter {} are \"{}\"", name, dimensions);
		log.debug("Bounds of filter {} inferred to be {}", name, bounds);
		setBounds(bounds);
	}

	void setBounds(BoxReal bounds) {
		this.bounds = bounds;
		internalCo = VectorReal.createEmpty(bounds.getRank());
		lastCo = VectorReal.createEmpty(bounds.getRank(), Double.NaN);
	}

	public String getName() {
		return name;
	}

	@Override
	public int getRank() {
		return bounds.getRank();
	}

	Class<?> getFieldType(String name) throws QueryConfigurationException {
		try {
			return innerFilter.getClass().getField(name).getType();
		} catch (NoSuchFieldException e) {
			throw new QueryConfigurationException(String.format(
					"Could not access field \"%s\" of filter \"%s\"", name,
					this.name));
		}
	}

	/**
	 * Assign a uniform field by name.
	 *
	 * @param name The name of the field.
	 * @param value The value to set it to.
	 * @throws QueryConfigurationException If the value is the wrong type, if it
	 *         doesn't meet its constraints, or if the field can not be set to
	 *         the given value.
	 */
	// Package scope: allows Query to access.
	void setParameter(String name, Object value)
			throws QueryConfigurationException {

		Field f;

		try {
			f = innerFilter.getClass().getField(name);
		} catch (NoSuchFieldException e) {
			throw new QueryConfigurationException(String.format(
				"Parameter %s#%s is not defined.", innerFilter.getClass(),
				name), e);
		}

		if (value == null) {
			throw new QueryConfigurationException(String.format(
					"Parameter %s#%s should not be null.", innerFilter.getClass(),
					name));
		}

		Constraint con = f.getAnnotation(Constraint.class);
		checkConstraints(con, name, value);

		// Assign.
		try {
			f.set(innerFilter, value);
		} catch (IllegalAccessException e) {
			throw new QueryConfigurationException(String.format(
					"Parameter %s#%s can not be assigned.", innerFilter.getClass(),
					name), e);
		}
		log.info("Bound {} to {}", value,
				String.format("%s.%s", this.name, f.getName()));
	}

	void checkConstraints(Constraint con, String name, Object value)
			throws QueryConfigurationException {

		if (con == null)
			return;

		if (con.dimensions() >= 0) {
			if (!HasRank.class.isAssignableFrom(value.getClass()))
				throw new QueryConfigurationException(String.format(
						"Parameter %s.%s is has no dimensions; should be %dD.",
						getName(), name, con.dimensions()));
			HasRank shaped = (HasRank) value;
			if (shaped.getRank() != con.dimensions()) {
				throw new QueryConfigurationException(String.format(
						"Parameter %s.%s is %dD; should be %dD.",
						getName(), name, shaped.getRank(),
						con.dimensions()));
			}
		}
	}

	/**
	 * @throws QueryConfigurationException
	 *             If any of the uniform or vaying fields are null or can't be
	 *             accessed.
	 */
	// Package scope: allows Query to access.
	void verifyConfiguration() throws QueryConfigurationException {

		for (Field f : innerFilter.getClass().getFields()) {
			if ((f.getModifiers() | Modifier.PUBLIC) == 0)
				continue;

			Object value;

			try {
				value = f.get(innerFilter);
			} catch (IllegalAccessException e) {
				throw new QueryConfigurationException(String.format(
						"Parameter \"%s\" is not accessible.", f.getName()), e);
			}

			if (value == null) {
				if (Cell.class.isAssignableFrom(f.getType())) {
					try {
						f.set(innerFilter, createCell(f.getName()));
					} catch (IllegalAccessException e) {
						throw new QueryConfigurationException(String.format(
								"Could not initialise unbound cell %s.",
								f.getName()));
					}
				} else {
					log.warn("Public filter field {}.{} is unbound.",
							getName(), f.getName());
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Filter(%s) {\n",
				innerFilter.getClass().getName()));

		for (Field f : innerFilter.getClass().getFields()) {
			if ((f.getModifiers() | Modifier.PUBLIC) == 0)
				continue;
			if (!PixelSource.class.isAssignableFrom(f.getType()) &&
					!Cell.class.isAssignableFrom(f.getType())) {
				continue;
			}
			try {
				sb.append(String.format("\t%s: %s\n", f.getName(),
						f.get(innerFilter)));
			} catch (Exception e) {
				continue;
			}
		}

		sb.append("}");
		return sb.toString();
	}

	@Override
	public void diagnostics() {
		if (!log.isInfoEnabled())
			return;

		try {
			for (Field f : innerFilter.getClass().getFields()) {
				Object ob = f.get(innerFilter);
				if (ob == null)
					continue;
				if (!Diagnostics.class.isAssignableFrom(ob.getClass()))
					continue;
				Diagnostics child = (Diagnostics) ob;
				child.diagnostics();
			}
		} catch (Exception e) {
			log.debug("Failed to list diagnostics information: {}",
					e.getMessage());
		}
	}

	public Filter getInnerFilter() {
		return innerFilter;
	}

	public void setInnerFilter(Filter innerFilter) {
		this.innerFilter = innerFilter;
	}

}
