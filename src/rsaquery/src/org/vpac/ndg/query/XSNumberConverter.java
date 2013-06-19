package org.vpac.ndg.query;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Converts strings to numbers - for use in XStream deserialiser.
 * @author Alex Fraser
 * @see XStreamConverter
 */
public class XSNumberConverter extends AbstractSingleValueConverter {
	NumberConverter numberConverter;

	public XSNumberConverter() {
		numberConverter = new NumberConverter();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class t) {
		return Number.class.isAssignableFrom(t);
	}

	@Override
	public Object fromString(String value) {
		return numberConverter.fromString(value);
	}
}
