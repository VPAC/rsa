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

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.vpac.ndg.storage.dao.BandDao;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Band;
import org.vpac.web.exception.ResourceNotFoundException;
import org.vpac.web.model.request.BandRequest;
import org.vpac.web.model.request.BandSearchRequest;
import org.vpac.web.model.request.PagingRequest;
import org.vpac.web.model.response.BandCollectionResponse;
import org.vpac.web.model.response.BandResponse;
import org.vpac.web.util.ControllerHelper;
import org.vpac.web.util.Pager;

@Controller
@RequestMapping("/Band")
public class BandController {

	final private Logger log = LoggerFactory.getLogger(BandController.class);

	private Pager<Band> pager = new Pager<Band>();
	@Autowired
	DatasetDao datasetDao; 
	@Autowired
	BandDao bandDao; 

	@RequestMapping(method = RequestMethod.GET)
	public String searchBand(@Valid BandSearchRequest request, @Valid PagingRequest page, ModelMap model) {

		log.info("Search Band");
		log.debug("Dataset ID: {}", request.getDatasetId());

		List<Band> list = datasetDao.getBands(request.getDatasetId());
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new BandCollectionResponse(pager.page(list, page)));
		return "List";
	}

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String getBandById(@PathVariable String id, ModelMap model) throws ResourceNotFoundException {
		Band b = bandDao.retrieve(id);
		if(b == null) {
			// Capture if band not exist
			throw new ResourceNotFoundException(String.format("Band with ID = \"%s\" not found.", id));			
		}
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new BandResponse(b));
		return "Band";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createBand(@Valid final BandRequest br, ModelMap model) {

		log.info("Create Band");
		log.debug("Dataset ID: {}", br.getDatasetId());
		log.debug("Name: {}", br.getName());
		log.debug("Type: {}", br.getType());
		log.debug("IsMetadata: {}", br.isMetadata());
		log.debug("IsContinuous: {}", br.isContinuous());

		Band band = new Band(br.getName(), br.isContinuous(), br.isMetadata());
		if (br.getType() != null)
			band.setType(br.getType());
		datasetDao.addBand(br.getDatasetId(), band);
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new BandResponse(band));
		return "Success";
	}

	@RequestMapping(value="/Form", method = RequestMethod.GET)
	public String createTestForm() {
		return "BandForm";
	}
}
