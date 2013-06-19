package org.vpac.ndg.query.sampling;

import org.vpac.ndg.query.coordinates.HasShape;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorInt;

import ucar.ma2.Array;
import ucar.ma2.Index;

/**
 * Unifies array access across different data types.
 * @author Alex Fraser
 */
public interface ArrayAdapter extends HasShape {

	ScalarElement get(Index ima);
	ScalarElement get(int i);
	void set(Index ima, ScalarElement value);
	void set(int i, ScalarElement value);
	void unset(Index ima);
	void unset(int i);
	Array getArray();
	void resize(VectorInt shape);

}
