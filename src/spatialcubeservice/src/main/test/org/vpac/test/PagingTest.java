/*
 * This file is part of SpatialCube.
 *
 * SpatialCube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SpatialCube is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SpatialCube.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.test;

import java.util.ArrayList;
import java.util.List;
import org.vpac.web.model.request.PagingRequest;
import org.vpac.web.util.Pager;

import junit.framework.TestCase;

public class PagingTest extends TestCase {

	public PagingTest() {
	}
	
	public void testDefault() {
		PagingRequest pr = new PagingRequest();
		assertEquals(0, pr.getPage());
		assertEquals(50, pr.getPageSize());
		
		pr.setPageSize(2);
		List<String> testList = new ArrayList<String>();
		// here are zero page
		testList.add("one");
		testList.add("two");
		// here are one page
		testList.add("three");
		testList.add("four");
		// here are two page
		testList.add("five");
		testList.add("six");
		// here are three page
		testList.add("seven");
		testList.add("eight");
		// here are four page
		testList.add("nine");
		
		Pager<String> pager = new Pager<String>();
		pr.setPage(2);
		List<String> resultList = pager.page(testList, pr);
		assertEquals(2, resultList.size());
		assertEquals("five", resultList.get(0));
		pr.setPage(4);
		resultList = pager.page(testList, pr);
		assertEquals(1, resultList.size());
		assertEquals("nine", resultList.get(0));
		
		pr.setPage(-1);
		resultList = pager.page(testList, pr);
		assertEquals(9, resultList.size());
	}
}

