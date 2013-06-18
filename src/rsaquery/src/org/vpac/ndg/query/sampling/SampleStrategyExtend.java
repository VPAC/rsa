package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Returns the nearest pixel when accessing a location that would otherwise be
 * out of bounds. This causes the edge pixels to "smear" beyond the natural
 * extents.
 * 
 * @author Alex Fraser
 */
public class SampleStrategyExtend implements SampleStrategy {

	private VectorInt minbounds;
	private VectorInt maxbounds;

	public SampleStrategyExtend(VectorInt shape) {
		minbounds = VectorInt.createEmpty(shape.size());
		maxbounds = shape.copy().subNew(1);
	}

	@Override
	public ScalarElement get(SamplerScalar samp, VectorReal co) throws IOException {
		co.min(maxbounds);
		co.max(minbounds);
		return samp.reallyGet(co);
	}

}
