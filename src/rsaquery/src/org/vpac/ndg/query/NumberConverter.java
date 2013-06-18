package org.vpac.ndg.query;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberConverter {

	private Pattern[] patterns;
	private Method[] converters;

	public NumberConverter() throws QueryRuntimeException {
		try {
			patterns = new Pattern[] {
					Pattern.compile("^([-+0-9]+)b$"), // byte
					Pattern.compile("^([-+0-9]+)s$"), // short
					Pattern.compile("^([-+0-9]+)$"), // int/long (synonymous)
					Pattern.compile("^([-+0-9.eE]+)f$"), // float
					Pattern.compile("^([-+0-9.eE]+)$"), // double
			};
			converters = new Method[] {
					Byte.class.getMethod("parseByte", String.class),
					Short.class.getMethod("parseShort", String.class),
					Integer.class.getMethod("parseInt", String.class),
					Float.class.getMethod("parseFloat", String.class),
					Double.class.getMethod("parseDouble", String.class),
					// Not in patterns; fallback.
					Long.class.getMethod("parseLong", String.class),
			};
		} catch (Exception e) {
			throw new QueryRuntimeException("Could not create number parsers.", e);
		}
	}

	public Object fromString(String value) {
		if (value == null)
			return null;

		try {
			for (int i = 0; i < patterns.length; i++) {
				Matcher matcher = patterns[i].matcher(value);
				if (matcher.matches())
					return converters[i].invoke(null, matcher.group(1));
			}
		} catch (Exception e) {
			throw new QueryRuntimeException(e);
		}

		for (Method m : converters) {
			try {
				return m.invoke(null, value);
			} catch (Exception e) {
				// Try next type.
			}
		}
		return value;
	}

}
