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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.Type;

import ucar.nc2.Attribute;
import ucar.nc2.Variable;

public class NodataStrategyFactory {

	final Logger log = LoggerFactory.getLogger(NodataStrategyFactory.class);

	/**
	 * Creates a strategy to deal with out-of-range values in a particular
	 * variable.
	 * @param variable The variable to deal with.
	 * @return The new strategy.
	 */
	public NodataStrategy create(Variable variable, Type type) {
		if (variable.findAttribute("valid_range") != null) {
			return createFromValidRange(variable, type);

		} else if (variable.findAttribute("valid_min") != null ||
				variable.findAttribute("valid_max") != null) {
			return createFromMinMax(variable, type);

		} else {
			ScalarElement fillValue = getFillValue(variable, type);
			if (fillValue != null) {
				return new NodataFillValueStrategy(fillValue);
			} else {
				return new NodataNullStrategy(type);
			}
		}
	}

	/**
	 * @param variable A variable containing the "valid_range" attribute.
	 * @return A suitable NodataStrategy.
	 */
	protected NodataStrategy createFromValidRange(Variable variable, Type type) {
		Attribute rangeAttr = variable.findAttribute("valid_range");
		Number min = rangeAttr.getNumericValue(0);
		Number max = rangeAttr.getNumericValue(1);
		if (min == null || max == null) {
			throw new IllegalArgumentException(String.format(
					"Variable %s specifies a valid_range attribute, but " +
					"the minimum or maximum could not be determined.",
					variable.getFullName()));
		}

		return createFromMinMax(variable, type, min, max);
	}

	/**
	 * @param variable A variable containing the "valid_min" and "valid_max"
	 *        attributes.
	 * @return A suitable NodataStrategy.
	 */
	protected NodataStrategy createFromMinMax(Variable variable, Type type) {

		Attribute minAttr = variable.findAttribute("valid_min");
		Attribute maxAttr = variable.findAttribute("valid_max");
		if (minAttr == null || maxAttr == null) {
			throw new IllegalArgumentException(String.format(
					"Variable %s specifies a valid_min or valid_max " +
					"attribute, but only one could not be determined.",
					variable.getFullName()));
		}
		Number min = minAttr.getNumericValue();
		Number max = maxAttr.getNumericValue();

		return createFromMinMax(variable, type, min, max);
	}

	private NodataStrategy createFromMinMax(Variable variable, Type type,
			Number min, Number max) {
		ScalarElement validMin = type.getElement().copy();
		validMin.set(min);
		ScalarElement validMax = type.getElement().copy();
		validMax.set(max);
		ScalarElement fillValue = getFillValue(variable, type);
		if (fillValue == null) {
			fillValue = deriveFillValue(variable, validMin, validMax);
		}

		return new NodataRangeStrategy(fillValue, validMin, validMax);
	}

	/**
	 * @param variable The variable to query for its fill value.
	 * @return The declared fill value of the variable, or null if no value is
	 *         declared.
	 */
	protected ScalarElement getFillValue(Variable variable, Type type) {
		ScalarElement fillValue = null;
		if (variable.findAttribute("_FillValue") != null) {
			Attribute fillAttr = variable.findAttribute("_FillValue");
			Number fv = fillAttr.getNumericValue();
			fillValue = type.getElement().copy();
			fillValue.set(fv);

		} else if (variable.findAttribute("missing_value") != null) {
			Attribute fillAttr = variable.findAttribute("_FillValue");
			Number fv = fillAttr.getNumericValue();
			fillValue = type.getElement().copy();
			fillValue.set(fv);
		}
		return fillValue;
	}

	protected ScalarElement deriveFillValue(Variable variable, ScalarElement min,
			ScalarElement max) {

		log.debug("Variable {} does not specify fill value; deriving from " +
				"min and max.", variable.getFullName());
		log.debug("min: {}, max: {}", min, max);

		// Pretend type is unsigned to prevent promotion.
		Type type = Type.get(variable.getDataType(), false);
		ScalarElement storageMin = type.getElement().copy();
		ScalarElement storageMax = type.getElement().copy();
		storageMin.set(min);
		storageMax.set(max);
		ScalarElement fillValue = storageMin.subNew(1);
		if (fillValue.compareTo(storageMax) >= 0) {
			// Underflow!
			log.warn("Valid range of variable {} covers entire range of data " +
					"type. Setting fill value to zero.", variable.getFullName());
			fillValue.set(0);
		}
		log.debug("Derived fill value: {}", fillValue);
		return fillValue;
	}
}
