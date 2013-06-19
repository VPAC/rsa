package org.vpac.web.model.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.storage.model.JobProgress;
 
@XmlRootElement(name = "TaskCollection")
public class TaskCollectionResponse {
	private List<TaskResponse> items;

	public List<TaskResponse> getItems() {
		return items;
	}

	@XmlElement(name = "Task")
	public void setItems(List<TaskResponse> items) {
		this.items = items;
	}

	public TaskCollectionResponse(List<JobProgress> list) {
		if(list != null) {
			items = new ArrayList<TaskResponse>();
			for(JobProgress jp: list)
				this.items.add(new TaskResponse(jp));
		}
	}
	
	public TaskCollectionResponse() {
		items = new ArrayList<TaskResponse>();
	}

	public List<JobProgress> toJobProgress() {
		List<JobProgress> list = new ArrayList<JobProgress>();
		for(TaskResponse tr : this.getItems()) {
			list.add(tr.toJobProgress());
		}
		return list;
	}
}
