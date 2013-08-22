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
