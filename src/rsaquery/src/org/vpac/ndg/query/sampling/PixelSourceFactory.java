package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.FilterAdapter;
import org.vpac.ndg.query.math.BoxReal;

public class PixelSourceFactory {
	public PixelSource create(FilterAdapter filter, Cell cell, BoxReal bounds) {

		if (CellScalar.class.isAssignableFrom(cell.getClass())) {
			// Scalar
			FilteredPixelScalar fp = new FilteredPixelScalar(filter,
					(CellScalar)cell, bounds);
			return fp;

		}else if (CellVector.class.isAssignableFrom(cell.getClass())) {
			// Vector
			FilteredPixelVector fp = new FilteredPixelVector(filter,
					(CellVector)cell, bounds);
			return fp;

		} else {
			throw new UnsupportedOperationException(String.format(
					"Can't create PixelSource wrapper for type %s",
					cell.getClass().getName()));
		}
	}
}
