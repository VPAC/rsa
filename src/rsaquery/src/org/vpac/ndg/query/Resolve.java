package org.vpac.ndg.query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Resolve {

	private static final Pattern REF_PATTERN = Pattern.compile(
			"#(\\w+)(?:/(.+))?");

	public NodeReference decompose(String ref) throws QueryConfigurationException {
		if (ref == null) {
			throw new QueryConfigurationException(String.format(
					"Invalid reference (null)."));
		}
		Matcher matcher = REF_PATTERN.matcher(ref);
		if (!matcher.matches()) {
			throw new QueryConfigurationException(String.format(
					"Invalid reference \"%s\".", ref));
		}

		NodeReference nodeReference = new NodeReference();
		nodeReference.nodeId = matcher.group(1);
		nodeReference.socketName = matcher.group(2);
		return nodeReference;
	}
}
