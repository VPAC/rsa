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

package org.vpac.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.util.DatasetUtil;
import org.vpac.web.model.request.PagingRequest;
import org.vpac.web.model.response.QueryNodeCollectionResponse;
import org.vpac.web.model.response.QueryDatasetResponse;
import org.vpac.web.util.ControllerHelper;

/**
 * This class is intended as controller for QueryDataset related web services.
 * 
 * @author hsumanto
 * 
 */
@Controller
@RequestMapping("/QueryDataset")
public class QueryDatasetController {

	@Autowired
	ControllerHelper helper;

	@Autowired
	DatasetDao datasetDao;

	@Autowired
	DatasetUtil datasetUtil;

	@InitBinder
	public void binder(WebDataBinder binder) {
		helper.BindDateTimeFormatter(binder);
		helper.BindCellSizeFormatter(binder);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getAllQueryDataset(
			@RequestParam(required = false) String name,
			@Valid PagingRequest page, ModelMap model) {
		List<Dataset> list = datasetDao.search(name, page.getPage(),
				page.getPageSize());
		List<QueryDatasetResponse> dsrs = new ArrayList<>();
		for (Dataset ds : list) {
			List<Band> bands = datasetDao.getBands(ds.getId());
			dsrs.add(new QueryDatasetResponse(ds, bands));
		}
		model.addAttribute(ControllerHelper.RESPONSE_ROOT,
				new QueryNodeCollectionResponse("Inputs", dsrs));
		return "List";
	}
}
