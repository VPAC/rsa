package org.vpac.ndg.cli.smadaptor;

import java.util.List;

import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.storagemanager.HasDescription;

public interface DataImport extends HasDescription {

	public void setRemainingArgs(List<String> remainingArgs);

	public void setUploadId(String uploadId);

	public String start() throws TaskInitialisationException, TaskException;

	public void setBand(String bandId);

	public void setSrcnodata(String srcnodata);

	/**
	 * @param value Whether to use bilinear interpolation. If null, the decision
	 *        will be made based on the domain type of the band.
	 */
	public void setUseBilinearInterpolation(Boolean value);

}
