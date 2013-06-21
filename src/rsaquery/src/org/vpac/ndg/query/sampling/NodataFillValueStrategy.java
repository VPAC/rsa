/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.QueryDefinition.AttributeDefinition;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.Type;

/**
 * This strategy is used for bands that have a single nodata value.
 *
 * @see <a href="http://cf-pcmdi.llnl.gov/documents/cf-conventions/1.0/cf-conventions.html">_FillValue</a>
 */
public class NodataFillValueStrategy implements NodataStrategy {

	protected ScalarElement fillValue;

	public NodataFillValueStrategy(ScalarElement fillValue) {
		this.fillValue = fillValue;
	}

	@Override
	public boolean isNoData(ScalarElement value) {
		return value.equals(fillValue);
	}

	@Override
	public ScalarElement getNodataValue() {
		return fillValue;
	}

	@Override
	public NodataStrategy copy() {
		return new NodataFillValueStrategy(fillValue.copy());
	}

	@Override
	public void convert(Type type) {
		fillValue = type.convert(fillValue);
	}

	@Override
	public AttributeDefinition getAttributeDefinition() {
		AttributeDefinition ad = new AttributeDefinition();
		ad.name = "_FillValue";
		ad.getProcessedValues().add(fillValue.getValue());
		return ad;
	}

	@Override
	public String toString() {
		return String.format("NDS(_FillValue = %s)", fillValue);
	}
}
