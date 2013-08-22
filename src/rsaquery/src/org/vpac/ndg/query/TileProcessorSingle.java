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

package org.vpac.ndg.query;

import java.io.IOException;
import java.util.Collection;

import org.vpac.ndg.query.iteration.CoordinatePair;
import org.vpac.ndg.query.iteration.Rectangle;
import org.vpac.ndg.query.math.BoxInt;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;
import org.vpac.ndg.query.sampling.Binding;

public class TileProcessorSingle implements TileProcessor {

	BoxInt bounds;
	Collection<Binding> bindings;

	public TileProcessorSingle() {
	}

	@Override
	public void processTile() throws IOException {
		VectorInt currentTileShape = bounds.getSize();

		Rectangle rect = new Rectangle(currentTileShape);
		VectorReal offset = bounds.getMin().toReal().add(0.5);
		rect.setOrigin(offset);
		for (CoordinatePair coords : rect) {
			// The loops are nested like this, instead of the other way around,
			// in case the bindings want to access different outputs in the same
			// filter - in which case, accessing them without changing the
			// coordinates should avoid reevaluation.
			for (Binding b : bindings)
				b.transfer(coords.coordinates, coords.imageIndex, 0);
		}
	}

	@Override
	public void setBounds(BoxInt bounds) {
		this.bounds = bounds;
	}

	@Override
	public void setBindings(Collection<Binding> bindings) {
		this.bindings = bindings;
	}

	@Override
	public void shutDown() {
		// Nothing to do.
	}
}