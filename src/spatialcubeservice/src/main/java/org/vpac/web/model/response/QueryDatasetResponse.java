/*
 * This file is part of the Raster Storage Archive (RSA).
 *
 * The RSA is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * The RSA is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * the RSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

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
