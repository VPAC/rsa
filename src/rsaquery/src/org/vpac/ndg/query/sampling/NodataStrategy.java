package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.QueryDefinition.AttributeDefinition;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.Type;

/**
 * Determines whether a value is valid data.
 * @author Alex Fraser
 */
public interface NodataStrategy {
	boolean isNoData(ScalarElement value);
	ScalarElement getNodataValue();
	NodataStrategy copy();
	void convert(Type type);
	AttributeDefinition getAttributeDefinition();
}
