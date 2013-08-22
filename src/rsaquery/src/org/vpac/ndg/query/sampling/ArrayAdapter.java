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
