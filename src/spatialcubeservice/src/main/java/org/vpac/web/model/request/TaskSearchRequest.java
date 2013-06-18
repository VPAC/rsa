package org.vpac.web.model.request;

import org.vpac.ndg.common.datamodel.TaskState;
import org.vpac.ndg.common.datamodel.TaskType;

import com.sun.istack.Nullable;

public class TaskSearchRequest {

	@Nullable
	private TaskType searchType;
	@Nullable
	private TaskState searchState;
	
	public TaskState getSearchState() {
		return searchState;
	}

	public void setSearchState(TaskState searchState) {
		this.searchState = searchState;
	}

	public TaskSearchRequest() {
	}

	public TaskSearchRequest(TaskType searchType, TaskState state) {
		this.setSearchType(searchType);
		this.setSearchState(state);
	}

	public TaskType getSearchType() {
		return searchType;
	}

	public void setSearchType(TaskType searchType) {
		this.searchType = searchType;
	}
}
