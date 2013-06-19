package org.vpac.ndg.query.sampling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.QueryDefinition.AttributeDefinition;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.Type;

/**
 * This strategy is used for bands that have no nodata metadata, i.e. every
 * number is considered to be valid.
 */
public class NodataNullStrategy implements NodataStrategy {

	final Logger log = LoggerFactory.getLogger(NodataNullStrategy.class);

	private Type type;
	protected ScalarElement defaultNodata;
	protected boolean warned;

	public NodataNullStrategy(Type type) {
		this.type = type;
		defaultNodata = type.getElement().copy();
		defaultNodata.set(0);
		warned = false;
	}

	@Override
	public boolean isNoData(ScalarElement value) {
		return false;
	}

	@Override
	public ScalarElement getNodataValue() {
		if (!warned) {
			log.warn("nodata value being used for a variable without " +
					"fill metadata. Defaulting to {} for fill value.",
					defaultNodata);
			warned = true;
		}
		return defaultNodata;
	}

	@Override
	public NodataStrategy copy() {
		return new NodataNullStrategy(type);
	}

	@Override
	public void convert(Type type) {
		this.type = type;
		defaultNodata = type.convert(defaultNodata);
	}

	@Override
	public AttributeDefinition getAttributeDefinition() {
		// No nodata representation possible!
		return null;
	}

	@Override
	public String toString() {
		return String.format("NDS(null)");
	}
}
