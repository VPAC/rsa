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

package org.vpac.web.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.vpac.ndg.Utils;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.util.DatasetUtil;
import org.vpac.web.exception.ResourceNotFoundException;
import org.vpac.web.model.request.DatasetRequest;
import org.vpac.web.model.request.PagingRequest;
import org.vpac.web.model.response.DatasetCollectionResponse;
import org.vpac.web.model.response.DatasetResponse;
import org.vpac.web.util.ControllerHelper;

@Controller
@RequestMapping("/Dataset")
public class DatasetController {

	final private Logger log = LoggerFactory.getLogger(DatasetController.class);

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
	public String getAllDataset(@RequestParam(required=false) String name, @Valid PagingRequest page, ModelMap model ) {
		List<Dataset> list = datasetDao.search(name, page.getPage(), page.getPageSize());
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new DatasetCollectionResponse(list));
		return "List";
	}

	@RequestMapping(value="/Search", method = RequestMethod.GET)
	public String searchDataset(@RequestParam(required=false) String name, @RequestParam(required=false) String resolution, ModelMap model ) {
		List<Dataset> list = datasetDao.search(name, CellSize.fromHumanString(resolution));
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new DatasetCollectionResponse(list));
		return "List";
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String getDatasetById(@PathVariable String id, ModelMap model ) throws ResourceNotFoundException {
		Dataset ds = datasetDao.retrieve(id);
		if(ds == null) {
			// Capture if dataset not exist
			throw new ResourceNotFoundException(String.format("Dataset with ID = \"%s\" not found.", id));			
		}
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new DatasetResponse(ds));
		return "List";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createOrUpdateDataset(@Valid DatasetRequest dr, ModelMap model ) throws ResourceNotFoundException, IOException {

		log.info("Create / Update Dataset");
		log.debug("Id: {}", dr.getId());
		log.debug("Name: {}", dr.getName());
		log.debug("Resolution: {}", dr.getResolution());
		log.debug("Precision: {}", dr.getPrecision());
		log.debug("Abstract: {}", dr.getDataAbstract());

		long precision = Utils.parseTemporalPrecision(dr.getPrecision());
		if(dr.getId().isEmpty()) {
			Dataset newDataset = new Dataset(dr.getName(), dr.getDataAbstract(), dr.getResolution(), precision);
			datasetDao.create(newDataset);
			model.addAttribute(ControllerHelper.RESPONSE_ROOT, new DatasetResponse(newDataset));
		} else {
			Dataset ds = datasetDao.retrieve(dr.getId());
			if(ds == null)
				throw new ResourceNotFoundException(String.format("Dataset with ID = \"%s\" not found.", dr.getId()));			
			ds.setAbst(dr.getDataAbstract());
			ds.setResolution(dr.getResolution());
			ds.setPrecision(Long.parseLong(dr.getPrecision()));
			if(ds.getName().equals(dr.getName()))
				datasetDao.update(ds);
			else {
				ds.setName(dr.getName());
				datasetUtil.update(ds);
			}
			model.addAttribute(ControllerHelper.RESPONSE_ROOT, new DatasetResponse(ds));
		}
		

		return "Success";
	}

	@RequestMapping(value="/Delete/{id}", method = RequestMethod.POST)
	public String deleteDataset(@PathVariable String id, ModelMap model ) throws ResourceNotFoundException, IOException {
		Dataset ds = datasetDao.retrieve(id);
		if(ds == null) {
			// Capture if dataset not exist
			throw new ResourceNotFoundException(String.format("Dataset with ID = \"%s\" not found.", id));			
		}
		datasetUtil.deleteDataset(ds);
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new DatasetResponse(ds));
		return "Success";
	}

	
	@RequestMapping(value="/Form", method = RequestMethod.GET)
	public String createTestForm() {
		return "DatasetForm";
	}
}
