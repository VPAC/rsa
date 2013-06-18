package org.vpac.ndg.query.sampling;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.vpac.ndg.query.QueryConfigurationException;
import org.vpac.ndg.query.VariableAdapter;
import org.vpac.ndg.query.math.VectorElement;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

import ucar.ma2.Index;

public class VariableBufferVector extends VariableBuffer {

	private ArrayList<PixelSourceVector> sources;

	public VariableBufferVector(List<VariableAdapter> variables)
			throws QueryConfigurationException {

		super(variables);
		this.sources = new ArrayList<PixelSourceVector>();
	}

	public void addSource(PixelSourceVector source) {
		sources.add(source);
	}

	@Override
	public void transfer(VectorReal coFrom, VectorInt coTo, int sourceIndex) throws IOException {
		VectorElement value = sources.get(sourceIndex).getVectorPixel(coFrom);
		Index ima = indices.get(sourceIndex);
		coTo.fillIndex(ima);
		for (int i = 0; i < buffers.size(); i++)
			buffers.get(i).set(ima, value.get(i));
	}

	@Override
	List<? extends PixelSource> getSources() {
		return sources;
	}

}
