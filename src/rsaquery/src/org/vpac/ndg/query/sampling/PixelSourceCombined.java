package org.vpac.ndg.query.sampling;

import java.io.IOException;
import java.util.List;

import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.math.BoxReal;
import org.vpac.ndg.query.math.Element;
import org.vpac.ndg.query.math.VectorElement;
import org.vpac.ndg.query.math.VectorReal;

public class PixelSourceCombined implements PixelSourceVector {

	private PixelSource[] children;
	private BoxReal bounds;
	private Prototype prototype;
	private VectorElement elem;

	public PixelSourceCombined(List<PixelSource> children)
			throws QueryConfigurationException {
		this.children = children.toArray(new PixelSource[children.size()]);

		prototype = Prototype.combine(children, null);
		elem = (VectorElement) prototype.getElement().copy();

		for (PixelSource child : children) {
			if (bounds == null)
				bounds = child.getBounds().copy();
			else
				bounds.union(child.getBounds());
		}
	}

	@Override
	public BoxReal getBounds() {
		return bounds;
	}

	@Override
	public int getRank() {
		return bounds.getRank();
	}

	@Override
	public Prototype getPrototype() {
		return prototype;
	}

	@Override
	public VectorElement getVectorPixel(VectorReal co) throws IOException {
		int i = 0;
		for (PixelSource child : children) {
			if (PixelSourceVector.class.isAssignableFrom(child.getClass())) {
				PixelSourceVector childV = (PixelSourceVector) child;
				VectorElement celem = childV.getVectorPixel(co);
				for (int j = 0; j < celem.size(); j++) {
					elem.set(i, celem.get(j));
					i++;
				}
			} else {
				PixelSourceScalar childS = (PixelSourceScalar) child;
				elem.set(i, childS.getScalarPixel(co));
				i++;
			}
		}
		return elem;
	}

	@Override
	public Element<?> getPixel(VectorReal co) throws IOException {
		return getVectorPixel(co);
	}

	@Override
	public String toString() {
		if (children.length > 0)
			return String.format("PixelSourceCombined(%s...)", children[0]);
		else
			return String.format("PixelSourceCombined()");
	}

}
