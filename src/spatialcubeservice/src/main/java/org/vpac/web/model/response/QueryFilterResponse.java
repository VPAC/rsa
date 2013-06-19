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
