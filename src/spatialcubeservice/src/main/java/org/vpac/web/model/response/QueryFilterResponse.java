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

package org.vpac.web.model.response;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.Description;
import org.vpac.ndg.query.Filter;
import org.vpac.ndg.query.filter.FilterUtils;
import org.vpac.ndg.query.sampling.Cell;
import org.vpac.ndg.query.sampling.PixelSource;

/**
 * This class is intended for a single query filter response.
 * 
 * @author hsumanto
 * 
 */
public class QueryFilterResponse extends QueryNodeResponse {

	final private Logger log = LoggerFactory
			.getLogger(QueryFilterResponse.class);

	public QueryFilterResponse() {
	}

	public QueryFilterResponse(Filter filter) {
		if (filter != null) {
			Description annotation = filter.getClass().getAnnotation(
					Description.class);
			String name;
			String desc;
			if (annotation != null) {
				name = annotation.name();
				desc = annotation.description();
			} else {
				name = filter.getClass().getSimpleName();
				desc = "";
			}

			setName(name);
			setDescription(desc);
			setType("filter");
			setQualname(filter.getClass().getCanonicalName());
			setInputs(new ArrayList<QueryInputResponse>());
			setOutputs(new ArrayList<QueryOutputResponse>());
			for (Field field : FilterUtils.getPrimitiveFields(filter)) {
				getInputs().add(
						new QueryInputResponse(field.getName(), field.getType()
								.getSimpleName()));
			}
			for (Field field : FilterUtils.getNonPrimitiveFields(filter)) {
				if (PixelSource.class.isAssignableFrom(field.getType())) {
					getInputs().add(
							new QueryInputResponse(field.getName(), field
									.getType().getSimpleName()));

				} else if (Cell.class.isAssignableFrom(field.getType())) {
					getOutputs().add(
							new QueryOutputResponse(field.getName(), field
									.getType().getSimpleName()));

				} else {
					log.warn(
							"Non primitive type detected:\n<name>{}</name>\n<type>{}</type>",
							field.getName(), field.getType().getSimpleName());
				}
			}
		}
	}
}
