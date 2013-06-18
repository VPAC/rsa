package org.vpac.ndg.query.sampling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.VariableAdapter;
import org.vpac.ndg.query.math.ScalarElement;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

import ucar.ma2.Index;

public class VariableBufferScalar extends VariableBuffer {

	private ArrayList<PixelSourceScalar> sources;

	public VariableBufferScalar(List<VariableAdapter> variables)
			throws QueryConfigurationException {

		super(variables);
		sources = new ArrayList<PixelSourceScalar>();
	}

	public void addSource(PixelSourceScalar source) {
		sources.add(source);
	}

	@Override
	public void transfer(VectorReal coFrom, VectorInt coTo, int sourceIndex) throws IOException {
		ScalarElement value = sources.get(sourceIndex).getScalarPixel(coFrom);
		Index ima = indices.get(sourceIndex);
		coTo.fillIndex(ima);
		for (int i = 0; i < buffers.size(); i++)
			buffers.get(i).set(ima, value);
	}

	@Override
	List<? extends PixelSource> getSources() {
		return sources;
	}

}
