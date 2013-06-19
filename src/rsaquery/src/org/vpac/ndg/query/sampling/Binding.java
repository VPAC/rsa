package org.vpac.ndg.query.sampling;

import java.io.IOException;

import org.vpac.ndg.query.math.BoxInt;
import org.vpac.ndg.query.math.VectorInt;
import org.vpac.ndg.query.math.VectorReal;

import ucar.nc2.NetcdfFileWriter;

public interface Binding {

	/**
	 * Read a value from the pixel source into the buffer.
	 */
	void transfer(VectorReal coFrom, VectorInt coTo, int sourceIndex) throws IOException;

	/**
	 * Write the buffer to the target variable.
	 */
	void commit(NetcdfFileWriter output) throws IOException;

	/**
	 * @param bounds The bounds of the current tile.
	 */
	void setBounds(BoxInt bounds);
}
