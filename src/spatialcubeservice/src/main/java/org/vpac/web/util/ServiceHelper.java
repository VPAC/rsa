package org.vpac.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.vpac.ndg.query.Filter;

/**
 * This class is intended to load all filter implementation specified in
 * /META-INF/services/org.vpac.ndg.query.Filter service provider configuration.
 * 
 * @author hsumanto
 * 
 */
public class ServiceHelper {

	private static ServiceLoader<Filter> filterSetLoader = ServiceLoader
			.load(Filter.class);

	public static List<Filter> getFilters() {
		List<Filter> filters = new ArrayList<>();
		for (Filter f : filterSetLoader) {
			filters.add(f);
		}
		return filters;
	}
}
