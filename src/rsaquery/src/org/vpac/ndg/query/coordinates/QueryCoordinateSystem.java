package org.vpac.ndg.query.coordinates;

import org.vpac.ndg.query.math.Swizzle;
import org.vpac.ndg.query.math.SwizzleFactory;
import org.vpac.ndg.query.math.VectorInt;

public class QueryCoordinateSystem implements HasShape, HasGridProjected {

	private GridProjected grid;
	private TimeAxis timeAxis;
	private VectorInt shape;

	public QueryCoordinateSystem(GridProjected grid, TimeAxis timeAxis) {
		this.grid = grid;
		this.timeAxis = timeAxis;

		// Expand shape to include time dimension if appropriate.
		if (timeAxis == null) {
			shape = grid.getShape();
		} else {
			shape = VectorInt.createEmpty(grid.getRank() + 1);
			Swizzle resize = SwizzleFactory.resize(grid.getRank(),
					shape.size());
			resize.swizzle(grid.getShape(), shape);
			shape.setT(timeAxis.getValues().size());
		}
	}

	@Override
	public GridProjected getGrid() {
		return grid;
	}

	@Override
	public VectorInt getShape() {
		return shape;
	}

	@Override
	public int getRank() {
		return shape.size();
	}

	public TimeAxis getTimeAxis() {
		return timeAxis;
	}

}
