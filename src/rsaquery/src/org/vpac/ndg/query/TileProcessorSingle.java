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