package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorElement;

public class CellFactory {
	public Cell create(String name, Prototype prototype) {
		Element<?> ptElem = prototype.getElement();
		if (ScalarElement.class.isAssignableFrom(ptElem.getClass())) {
			// Scalar
			return new CellScalar(name, prototype);
		} else if (VectorElement.class.isAssignableFrom(ptElem.getClass())) {
			// Vector
			return new CellVector(name, prototype);
		} else {
			throw new UnsupportedOperationException(String.format(
					"Can't create Cell for type %s",
					prototype.getClass().getName()));
		}
	}
}
