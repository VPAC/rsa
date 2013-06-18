package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.QueryDefinition.AttributeDefinition;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.Type;

/**
 * This strategy is used for bands that have a single nodata value.
 *
 * @see <a href="http://cf-pcmdi.llnl.gov/documents/cf-conventions/1.0/cf-conventions.html">valid_range</a>
 */
public class NodataRangeStrategy implements NodataStrategy {

	protected ScalarElement validMin;
	protected ScalarElement validMax;
	protected ScalarElement fill;

	public NodataRangeStrategy(ScalarElement fill, ScalarElement validMin,
			ScalarElement validMax) {
		this.validMin = validMin;
		this.validMax = validMax;
		this.fill = fill;
	}

	@Override
	public boolean isNoData(ScalarElement value) {
		if (value.compareTo(validMin) < 0)
			return true;
		else if (value.compareTo(validMax) > 0)
			return true;
		else
			return false;
	}

	@Override
	public ScalarElement getNodataValue() {
		return fill;
	}

	@Override
	public NodataStrategy copy() {
		return new NodataRangeStrategy(fill.copy(), validMin.copy(),
				validMax.copy());
	}

	@Override
	public void convert(Type type) {
		validMax = type.convert(validMax);
		validMin = type.convert(validMin);
		fill = type.convert(fill);
	}

	@Override
	public AttributeDefinition getAttributeDefinition() {
		AttributeDefinition ad = new AttributeDefinition();
		ad.name = "valid_range";
		ad.getProcessedValues().add(validMin.getValue());
		ad.getProcessedValues().add(validMax.getValue());
		return ad;
	}

	@Override
	public String toString() {
		return String.format("NDS(range = %s..%s)", validMin, validMax);
	}
}
