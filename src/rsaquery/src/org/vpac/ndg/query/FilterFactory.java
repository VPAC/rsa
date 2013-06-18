package org.vpac.ndg.query;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.DatasetOutput.VariableBindingDefinition;
import org.vpac.ndg.query.QueryDefinition.FilterDefinition;
import org.vpac.ndg.query.QueryDefinition.LiteralDefinition;
import org.vpac.ndg.query.QueryDefinition.SamplerDefinition;
import org.vpac.ndg.query.coordinates.QueryCoordinateSystem;
import org.vpac.ndg.query.coordinates.Warp;
import org.vpac.ndg.query.sampling.PixelSource;
import org.vpac.ndg.query.sampling.PixelSourceCombined;
import org.vpac.ndg.query.sampling.PixelSourceScalar;
import org.vpac.ndg.query.sampling.PixelSourceVector;
import org.vpac.ndg.query.sampling.SamplerScalar;

/**
 * Builds filters.
 * @author Alex Fraser
 */
public class FilterFactory {

	final Logger log = LoggerFactory.getLogger(FilterFactory.class);

	private DatasetStore datasetStore;
	private ClassLoader loader;
	private BindingStore bindings;
	private FilterStore filterStore;
	private QueryCoordinateSystem context;

	Resolve resolve;

	public FilterFactory(DatasetStore references, BindingStore bindings,
			QueryCoordinateSystem context) {
		datasetStore = references;
		loader = Thread.currentThread().getContextClassLoader();
		this.bindings = bindings;
		filterStore = new FilterStore();
		this.context = context;
		resolve = new Resolve();
	}

	/**
	 * Create a list of filters based on a list of filter definitions.
	 * @throws IOException 
	 */
	public List<FilterAdapter> createFilters(List<FilterDefinition> fds)
			throws QueryConfigurationException, IOException {

		List<FilterAdapter> filters = new ArrayList<FilterAdapter>();

		if (fds == null)
			return filters;

		for (FilterDefinition fd : fds) {
			FilterAdapter filter = createFilter(fd);
			filters.add(filter);
			filterStore.add(filter);
		}

		return filters;
	}

	public FilterAdapter createFilter(FilterDefinition fd)
			throws QueryConfigurationException, IOException {

		Filter f;
		Class<?> cls;
		try {
			cls = loader.loadClass(fd.classname);
		} catch (ClassNotFoundException e) {
			throw new QueryConfigurationException(String.format(
					"Could not find filter class \"%s\".", fd.classname));
		}

		try {
			f = (Filter) cls.getConstructor().newInstance();
		} catch (Exception e) {
			throw new QueryConfigurationException(String.format(
					"Could not instantiate filter \"%s\": %s", fd.id,
					e.getMessage()));
		}

		FilterAdapter adapter = new FilterAdapter(fd.id, f);

		setLiterals(adapter, fd.literals);
		setSamplers(adapter, fd.samplers);

		// Try to infer the actual shape from the inputs.
		adapter.inferShapeFromInputs();

		return adapter;
	}

	private void setLiterals(FilterAdapter f, List<LiteralDefinition> ls)
			throws QueryConfigurationException {

		if (ls == null)
			return;

		Filter filter = f.getInnerFilter();
		for (LiteralDefinition ld : ls) {
			Field field;
			try {
				field = filter.getClass().getField(ld.name);
			} catch (NoSuchFieldException e) {
				throw new QueryConfigurationException(String.format(
						"Filter %s has no field called %s.",
						filter.getClass().getName(), ld.name));
			}
			Object value = null;
			Class<?> type = field.getType();
			try {
				if (type == Boolean.TYPE || type.isAssignableFrom(Boolean.class))
					value = Boolean.parseBoolean(ld.value);
				else if (type == Byte.TYPE || type.isAssignableFrom(Byte.class))
					value = Byte.parseByte(ld.value);
				else if (type == Short.TYPE || type.isAssignableFrom(Short.class))
					value = Short.parseShort(ld.value);
				else if (type == Integer.TYPE || type.isAssignableFrom(Integer.class))
					value = Integer.parseInt(ld.value);
				else if (type == Long.TYPE || type.isAssignableFrom(Long.class))
					value = Long.parseLong(ld.value);
				else if (type == Float.TYPE || type.isAssignableFrom(Float.class))
					value = Float.parseFloat(ld.value);
				else if (type == Double.TYPE || type.isAssignableFrom(Double.class))
					value = Double.parseDouble(ld.value);
			} catch (NumberFormatException e) {
				throw new QueryConfigurationException(String.format(
						"Type mismatch: field %s.%s should be a %s.",
						filter.getClass().getName(), ld.name, type.getName()), e);
			}

			if (value == null && type.isAssignableFrom(String.class))
				value = ld.value;

			if (value == null) {
				throw new QueryConfigurationException(String.format(
						"Can't assign literal %s.%s: no known conversion to type %s.",
						filter.getClass().getName(), ld.name, type.getName()));
			}

			f.setParameter(ld.name, value);
		}
	}


	// OUTPUTS

	public void bindFilters(List<VariableBindingDefinition> variableBindingDefs)
			throws QueryConfigurationException {
		for (VariableBindingDefinition vbd : variableBindingDefs) {
			bind(vbd);
		}
	}

	public void bind(VariableBindingDefinition vbd)
			throws QueryConfigurationException {

		List<VariableAdapter> variables = collectOutputVariables(vbd.toRefs);
		PixelSource socket = filterStore.findOutputSocket(vbd.fromRef);
		log.info("Binding {} to {}", vbd.fromRef, variables);

		if (PixelSourceScalar.class.isAssignableFrom(socket.getClass())) {
			// Scalar
			PixelSourceScalar sSocket = (PixelSourceScalar) socket;
			bindings.bind(vbd.fromRef, sSocket, variables);

		} else {
			// Vector
			PixelSourceVector vSocket = (PixelSourceVector) socket;
			bindings.bind(vbd.fromRef, vSocket, variables);
		}
	}

	private List<VariableAdapter> collectOutputVariables(List<String> toRefs)
			throws QueryConfigurationException {
		List<VariableAdapter> variables = new ArrayList<VariableAdapter>();
		for (String ref : toRefs) {
			NodeReference nr = resolve.decompose(ref);
			DatasetMeta ds = datasetStore.getDataset(nr.nodeId);
			if (!DatasetOutput.class.isAssignableFrom(ds.getClass())) {
				throw new QueryConfigurationException(String.format(
						"Can't bind output to input variable %s", ref));
			}
			VariableAdapter pvar = ds.getVariableAdapter(nr.socketName);
			variables.add(pvar);
		}
		return variables;
	}


	// INPUTS

	private void setSamplers(FilterAdapter f, List<SamplerDefinition> ss)
			throws QueryConfigurationException, IOException {

		if (ss == null)
			return;

		for (SamplerDefinition sd : ss) {
			List<NodeReference> refs = new ArrayList<NodeReference>();
			for (SamplerDefinition sdChild : sd.children) {
				refs.add(sdChild._nodeRef);
			}

			if (refs.size() == 0) {
				throw new QueryConfigurationException(String.format(
						"Can't configure sampler \"%s\": no sockets match " +
						"references.", sd.name));
			}
			List<PixelSource> sources = gatherPixelSources(refs);

			boolean isScalar = sources.size() == 1 &&
					PixelSourceScalar.class.isAssignableFrom(
							sources.get(0).getClass());
			boolean isSingleVector = sources.size() == 1 &&
					PixelSourceVector.class.isAssignableFrom(
							sources.get(0).getClass());

			PixelSource source;
			Class<?> type = f.getFieldType(sd.name);
			if (PixelSourceScalar.class.isAssignableFrom(type)) {
				// Ensure scalar source for scalar fields.
				if (!isScalar) {
					throw new QueryConfigurationException(String.format(
							"Can't assign vector source to scalar field " +
							"\"%s\"", sd.name));
				}
				log.debug("Assigning scalar sampler to scalar socket {}", sd.name);
				source = sources.get(0);

			} else if (PixelSourceVector.class.isAssignableFrom(type)) {
				// Ensure vector source for vector fields. If there is exactly
				// one vector source in the list, just use that - otherwise,
				// combine them.
				if (isSingleVector) {
					log.debug("Assigning vector sampler to vector socket {}", sd.name);
					source = sources.get(0);
				} else {
					log.debug("Creating vector sampler for vector socket {}", sd.name);
					source = new PixelSourceCombined(sources);
				}

			} else {
				// Unspecified type; both scalar and vector are acceptable. If
				// there is only one pixel source just pass that in unchanged -
				// it may be scalar or vector. Otherwise, combine components
				// into new vector source.
				if (sources.size() == 1) {
					log.debug("Assigning generic sampler to generic socket {}", sd.name);
					source = sources.get(0);
				} else {
					log.debug("Creating vector sampler for generic socket {}", sd.name);
					source = new PixelSourceCombined(sources);
				}
			}

			f.setParameter(sd.name, source);
		}
	}

	private List<PixelSource> gatherPixelSources(List<NodeReference> refs)
			throws QueryConfigurationException {
		List<PixelSource> sources = new ArrayList<PixelSource>(refs.size());
		for (NodeReference nr : refs) {
			sources.add(getPixelSource(nr));
		}
		return sources;
	}

	private PixelSource getPixelSource(NodeReference nr)
			throws QueryConfigurationException {

		DatasetInput inputDataset = null;
		FilterAdapter inputFilter = null;

		// Dataset
		try {
			inputDataset = (DatasetInput) datasetStore.getDataset(nr.nodeId);
		} catch (QueryConfigurationException e) {
			// Will test for null below.
		} catch (ClassCastException e) {
			log.warn("Found dataset \"{}\", but it is not an input dataset.", nr.nodeId);
		}

		// Filter
		try {
			inputFilter = filterStore.getFilter(nr.nodeId);
		} catch (QueryConfigurationException e) {
			// Will test for null below.
		}


		if (inputDataset != null) {
			// IMPORTANT: this sampler creation code can not be moved to the
			// DatasetInput class because then each sampler would be shared by
			// all threads. The VariableAdapter is already shared, which means
			// the samplers will share the cache where possible.
			VariableAdapter var = inputDataset.getVariableAdapter(nr.socketName);
			SamplerScalar sampler = new SamplerScalar(var, context);

			// Configure coordinate transform. Note that this is a transform
			// FROM the output TO the input; a reverse lookup.
			Warp warp = var.createWarpFrom(context);
			sampler.setWarp(warp);
			log.debug("Created sampler {} with warp {}", sampler, warp);
			log.debug("Sampler has bounds {}", sampler.getBounds());
			return sampler;

		} else if (inputFilter != null) {
			return inputFilter.getOutputSocket(nr.socketName);

		} else {
			throw new QueryConfigurationException(String.format(
					"\"%s\" is not a filter or dataset id.", nr.nodeId));
		}
	}

	public FilterStore getFilterStore() {
		return filterStore;
	}

}
