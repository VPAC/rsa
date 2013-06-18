package org.vpac.ndg.query;

import java.io.IOException;

import org.vpac.ndg.query.coordinates.HasCoordinateSystem;
import org.vpac.ndg.query.coordinates.HasGridProjected;

import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Provides a common way to access various metadata of a dataset.
 * @author Alex Fraser
 */
public interface DatasetMeta extends HasGridProjected, HasCoordinateSystem {

	String getName();

	/**
	 * Close the underlying dataset.
	 */
	void close() throws IOException;

	/**
	 * @return The wrapped dataset.
	 */
	NetcdfDataset getDataset();

	Variable findVariable(String name);

	VariableAdapter getVariableAdapter(String name)
			throws QueryConfigurationException;

}
