package org.vpac.ndg.query;

import java.io.IOException;
import java.util.Collection;

import org.vpac.ndg.query.math.BoxInt;
import org.vpac.ndg.query.sampling.Binding;

public interface TileProcessor {
	void processTile() throws IOException;

	void setBounds(BoxInt bounds);

	void setBindings(Collection<Binding> bindings);

	void shutDown();
}
