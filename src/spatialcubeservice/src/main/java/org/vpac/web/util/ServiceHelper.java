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
