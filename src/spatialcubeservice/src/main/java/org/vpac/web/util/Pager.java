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
