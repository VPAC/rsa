package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorReal;

/**
 * Provides a way to modify the results of sampling.
 * @author Alex Fraser
 *
 */
public interface SampleStrategy {

	public ScalarElement get(SamplerScalar samp, VectorReal co) throws IOException;

}
