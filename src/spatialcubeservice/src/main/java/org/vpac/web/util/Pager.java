package org.vpac.web.util;

import java.util.List;

import org.vpac.web.model.request.PagingRequest;

public class Pager<T> {

	public List<T> page(List<T> list, PagingRequest p)
	{
		if (list == null)
			return null;
		
		List<T> returnList = null; 

		if(p != null) {
			int fromIndex = Math.min(p.getPage() * p.getPageSize(), list.size());
			int toIndex = Math.min((1 + p.getPage()) * p.getPageSize(), list.size());
			try {
				returnList = list.subList(fromIndex, toIndex);
			}
			catch (Exception ex) {
				returnList = new Pager<T>().page(list, new PagingRequest());
			}
		}
		else
			returnList = new Pager<T>().page(list, new PagingRequest());
		
		return returnList;
	}
}
