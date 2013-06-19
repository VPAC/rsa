package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.math.VectorInt;

public abstract class TilingStrategyBase implements TilingStrategy {

	@Override
	public VectorInt getGridShape(VectorInt baseShape) {

		VectorInt tileShape = getTileShape(baseShape);

		VectorInt tileGridShape = baseShape.divNew(tileShape);
		VectorInt tileGridRemainder = baseShape.modNew(tileShape);
 		for (int i = 0; i < tileShape.size(); i++) {
			if (tileGridRemainder.get(i) > 0)
				tileGridShape.set(i, tileGridShape.get(i) + 1);
 		}
 
 		return tileGridShape;
	}

}
