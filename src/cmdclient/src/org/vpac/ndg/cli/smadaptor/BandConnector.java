package org.vpac.ndg.cli.smadaptor;

import java.io.IOException;
import java.util.List;

import org.vpac.ndg.storage.model.Band;


public interface BandConnector {

	public List<Band> list(String id);

	public Band createBand(String dsid, String name, 
			String datatype,
			String nodata,
			String isContinuous,
			String isMetadata);

	public void delete(String id) throws IOException;

	public void update(Band newBand) throws IOException;

	public Band retrieve(String bandId);

	public void updateInfo(Band newBand);
	
}
