package org.vpac.ndg.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class StringUtils {

	public static String join(Object[] array, String delimiter) {
		return join(Arrays.asList(array), delimiter);
	}

	public static String join(Collection<?> array, String delimiter) {
		if (array.size() == 0)
			return "";

		Iterator<?> iter = array.iterator();
		StringBuilder sb = new StringBuilder();
		sb.append(iter.next().toString());
		while (iter.hasNext()) {
			sb.append(delimiter);
			sb.append(iter.next().toString());
		}
		return sb.toString();
	}

}
