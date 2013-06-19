package org.vpac.web.model.request;

import org.hibernate.validator.constraints.Range;

import com.sun.istack.Nullable;

public class PagingRequest {

	@Range(min=0)
	@Nullable
	private int page;
	
	@Range(min=0, max=100)
	@Nullable
	private int pageSize;
	
	public PagingRequest(int page, int pageSize)	{
		this.setPage(page);
		this.setPageSize(pageSize);
	}
	
	public PagingRequest()	{
		this.setPage(0); 
		this.setPageSize(50);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
