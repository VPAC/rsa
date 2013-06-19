package org.vpac.ndg.datamodel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.ncml.NcMLReader;

/**
 * Constructs virtual aggregations from NCML mementos.
 * @author Alex Fraser
 */
public class AggregationOpener {

	Logger log = LoggerFactory.getLogger(AggregationOpener.class);

	/**
	 * Converts a deserialised NCML file into an open (read-only) NetcdfDataset.
	 * 
	 * @throws IOException
	 *             if any of the referenced files can't be open.
	 * @throws IllegalArgumentException
	 *             if the aggregation definition is malformed.
	 */
	public NetcdfDataset open(AggregationDefinition ncmlDataset, String locationHint)
			throws IOException, IllegalArgumentException {

		StringWriter writer = new StringWriter();
		ncmlDataset.serialise(writer);
		log.debug("Opening {}", writer);
		InputStream istream = new ByteArrayInputStream(writer.toString().getBytes());
		NetcdfDataset netcdfFile = NcMLReader.readNcML(istream, null);
		return netcdfFile;

	}

}
