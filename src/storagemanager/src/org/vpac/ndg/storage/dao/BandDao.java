package org.vpac.ndg.storage.dao;

import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
public interface BandDao extends BasicDao<Band> {
	Band find(String datasetId, String bandName);
	Dataset getParentDataset(String bandId);
}
