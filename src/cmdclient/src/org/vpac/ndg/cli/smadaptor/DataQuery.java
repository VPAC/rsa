package org.vpac.ndg.cli.smadaptor;

import java.io.IOException;
import java.nio.file.Path;

import org.vpac.ndg.query.QueryDefinition;

public interface DataQuery {

	public void setExtents(String x1, String y1, String x2, String y2);
	public void setTimeRange(String start, String end);

	/**
	 * @param definition The path to the serialised {@link QueryDefinition query
	 *        definition}.
	 */
	public void setQueryPath(Path definition);

	/**
	 * Run a query on an RSA dataset. Note that this starts execution on a
	 * worker thread; the result and error state should be checked by inspecting
	 * the progress object.
	 * 
	 * @throws IOException If the output can't be written to, or if the
	 *         requested data can't be found or read from.
	 */
	public String start() throws IOException;

	/**
	 * @param threads The number of worker threads to use during operation.
	 */
	public void setThreads(String threads);
	public void setOutputPath(Path outputFile);
	public void setNetcdfVersion(String netcdf4);
}
