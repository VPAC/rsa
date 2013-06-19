package org.vpac.ndg.query;

import java.util.HashMap;
import java.util.Map;

import org.vpac.ndg.query.sampling.PixelSource;

/**
 * A searchable store of datasets.
 * @author Alex Fraser
 */
public class FilterStore {

	protected Map<String, FilterAdapter> filters;

	Resolve resolve;

	public FilterStore() {
		filters = new HashMap<String, FilterAdapter>();
		resolve = new Resolve();
	}

	public void add(FilterAdapter filter) {
		filters.put(filter.getName(), filter);
	}

	/**
	 * @param ref A reference, e.g. <em>#blur</em>.
	 * @return The matching filter
	 * @throws QueryConfigurationException If the filter can't be found, or if
	 *         the reference is invalid.
	 */
	public FilterAdapter findFilter(String ref)
			throws QueryConfigurationException {
		return getFilter(resolve.decompose(ref).nodeId);
	}

	/**
	 * @param id The name of the dataset, e.g. <em>infile</em>.
	 * @return The matching dataset.
	 * @throws QueryConfigurationException If the dataset can't be found.
	 */
	public FilterAdapter getFilter(String id) throws
			QueryConfigurationException {

		FilterAdapter filter = filters.get(id);
		if (filter == null) {
			throw new QueryConfigurationException(String.format(
					"Filter \"%s\" is not defined (yet).", id));
		}
		return filter;
	}

	public PixelSource findOutputSocket(String ref)
			throws QueryConfigurationException {

		NodeReference nr = resolve.decompose(ref);

		if (nr.socketName == null)
			throw new QueryConfigurationException(String.format(
					"Socket name not specified in \"%s\".", ref));

		FilterAdapter filter = getFilter(nr.nodeId);
		return filter.getOutputSocket(nr.socketName);
	}

}
