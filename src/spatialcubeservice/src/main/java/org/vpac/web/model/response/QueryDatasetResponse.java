package org.vpac.web.model.response;

import java.util.ArrayList;
import java.util.List;

import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;

/**
 * This class is intended for single QueryDataset response.
 * 
 * @author hsumanto
 * 
 */
public class QueryDatasetResponse extends QueryNodeResponse {

	public QueryDatasetResponse() {
	}

	public QueryDatasetResponse(Dataset ds, List<Band> bands) {
		if (ds != null) {
			setName(ds.getName());
			setType("input");
			setQualname(ds.getUri());
			setDescription(ds.getAbst());
			setInputs(new ArrayList<QueryInputResponse>());
			setOutputs(new ArrayList<QueryOutputResponse>());
			addDefaultOutputParams();
			addBandParams(bands);
		}
	}

	private void addDefaultOutputParams() {
		getOutputs().add(new QueryOutputResponse("grid", "meta"));
		getOutputs().add(new QueryOutputResponse("time", "scalar,axis"));
		getOutputs().add(new QueryOutputResponse("y", "scalar,axis"));
		getOutputs().add(new QueryOutputResponse("x", "scalar,axis"));
	}

	private void addBandParams(List<Band> bands) {
		if (bands != null) {
			for (Band b : bands) {
				getOutputs()
						.add(new QueryOutputResponse(b.getName(), "scalar"));
			}
		}
	}
}
