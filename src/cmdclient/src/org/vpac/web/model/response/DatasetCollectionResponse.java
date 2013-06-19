package org.vpac.web.model.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.storage.model.Dataset;
 
@XmlRootElement(name = "DatasetCollection")
public class DatasetCollectionResponse {
	private List<DatasetResponse> items;

	public List<DatasetResponse> getItems() {
		return items;
	}

	@XmlElement(name = "Dataset")
	public void setItems(List<DatasetResponse> items) {
		this.items = items;
	}

	public DatasetCollectionResponse(List<Dataset> list) {
		if(list != null) {
			items = new ArrayList<DatasetResponse>();
			for(Dataset ds: list)
				this.items.add(new DatasetResponse(ds));
		}
	}
	
	public DatasetCollectionResponse() {
		items = new ArrayList<DatasetResponse>();
	}
	
	public List<Dataset> toDatasetList() {
		List<Dataset> list = new ArrayList<Dataset>();
		for(DatasetResponse dsr: items) {
			Dataset ds = new Dataset();
			ds.setId(dsr.getId());
			ds.setAbst(dsr.getDataAbstract());
			ds.setCreated(dsr.getCreated());
			ds.setName(dsr.getName());
			ds.setPrecision(dsr.getPrecision());
			ds.setResolution(dsr.getResolution());
			list.add(ds);
		}
		return list;
	}
}
