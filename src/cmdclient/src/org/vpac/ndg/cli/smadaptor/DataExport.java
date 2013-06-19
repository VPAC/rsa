package org.vpac.ndg.cli.smadaptor;

import java.io.IOException;
import java.util.List;

import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;

public interface DataExport {

	public void setDatasetId(String id);
	public void setExtents(String x1, String y1, String x2, String y2);
	public void setTimeRange(String start, String end);
	public void setBandNamesFilter(List<String> bandNamesFilter);

	/**
	 * @param value Whether to use bilinear interpolation. If null, the decision
	 *        will be made based on the domain type of the band.
	 */
	public void setUseBilinearInterpolation(Boolean value);


	/**
	 * Export the data.
	 * @return The path of the file that was written to.
	 * @throws TaskInitialisationException
	 *             if the task fails to start.
	 * @throws TaskException
	 *             if the task fails to finish.
	 * @throws IOException
	 *             if the file can't be copied to the output location.
	 */
//	public Path exportSync(Path outputDir, String outputFile)
//			throws TaskInitialisationException, TaskException, IOException;

	public String start() throws TaskInitialisationException, TaskException, IOException;

}
