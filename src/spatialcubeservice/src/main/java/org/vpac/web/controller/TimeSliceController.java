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

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

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
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.dao.TimeSliceDao;
import org.vpac.ndg.storage.dao.TimeSliceLockDao;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.TimeSliceLock;
import org.vpac.ndg.storage.util.TimeSliceUtil;
import org.vpac.web.exception.ResourceNotFoundException;
import org.vpac.web.model.request.PagingRequest;
import org.vpac.web.model.request.TimeSliceRequest;
import org.vpac.web.model.request.TimeSliceSearchRequest;
import org.vpac.web.model.response.DatasetResponse;
import org.vpac.web.model.response.TimeSliceCollectionResponse;
import org.vpac.web.model.response.TimeSliceLockCollectionResponse;
import org.vpac.web.model.response.TimeSliceResponse;
import org.vpac.web.util.ControllerHelper;
import org.vpac.web.util.Pager;

@Controller
@RequestMapping("/TimeSlice")
public class TimeSliceController {

	final private Logger log = LoggerFactory.getLogger(TimeSliceController.class);

	@Autowired
	private ControllerHelper helper;
	@Autowired
	private DatasetDao datasetDao;
	@Autowired
	private TimeSliceDao timeSliceDao;
	@Autowired
	private TimeSliceUtil timeSliceUtil;
	@Autowired
	TimeSliceLockDao timeSliceLockDao;
	
	private Pager<TimeSlice> pager = new Pager<TimeSlice>();
	
	@InitBinder
	public void binder(WebDataBinder binder) {
		helper.BindDateTimeFormatter(binder);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getAll(@Valid TimeSliceSearchRequest request, @Valid PagingRequest page, ModelMap model ) {

		log.info("Get All TimeSlices");
		log.debug("Dataset ID: {}", request.getDatasetId());
		log.debug("Creation Date: {}", request.getCreationDate());
		log.debug("Begin Date: {}", request.getSearchBeginDate());
		log.debug("End Date: {}", request.getSearchEndDate());

		List<TimeSlice> list = getAllTimeSlice(request.getDatasetId());
		if(request.getCreationDate() != null)
			list = select(list, having(on(TimeSlice.class).getCreated(), equalTo(request.getCreationDate())));

		if(request.getSearchBeginDate() != null)
			list = select(list, having(on(TimeSlice.class).getCreated(), greaterThanOrEqualTo(request.getSearchBeginDate())));

		if(request.getSearchEndDate() != null)
			list = select(list, having(on(TimeSlice.class).getCreated(), lessThanOrEqualTo(request.getSearchEndDate())));

		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new TimeSliceCollectionResponse(pager.page(list, page)));
		return "List";
	}

	private List<TimeSlice> getAllTimeSlice(final String datasetId) {
		return datasetDao.getTimeSlices(datasetId);
	}

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public String getTimeSliceById(@PathVariable String id, ModelMap model ) throws ResourceNotFoundException {
		TimeSlice ts = timeSliceDao.retrieve(id);
		if(ts == null) {
			// Capture if timeslice not exist
			throw new ResourceNotFoundException(String.format("TimeSlice with ID = \"%s\" not found.", id));			
		}
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new TimeSliceResponse(ts));
		return "TimeSlice";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createOrUpdateTimeSlice(@Valid TimeSliceRequest request, ModelMap model ) throws ResourceNotFoundException, IOException {

		log.info("Create / Update TimeSlice");
		log.debug("Timeslice ID: {}", request.getTimesliceId());
		log.debug("Dataset ID: {}", request.getDatasetId());
		log.debug("Creation Date: {}", request.getCreationDate());
		log.debug("Abstract: {}", request.getAbs());
		log.debug("Xmin: {}", request.getXmin());
		log.debug("Xmax: {}", request.getXmax());
		log.debug("Ymin: {}", request.getYmin());
		log.debug("Ymax: {}", request.getYmax());
		
		if(request.getTimesliceId().isEmpty()) {
			// create timeslice
			TimeSlice ts = new TimeSlice(request.getCreationDate());
			datasetDao.addTimeSlice(request.getDatasetId(), ts);
			model.addAttribute(ControllerHelper.RESPONSE_ROOT, new TimeSliceResponse(ts));
			model.remove(request);
			return "Success";
		} else {
			// update timeslice
			TimeSlice ts = timeSliceDao.retrieve(request.getTimesliceId());
			if(ts == null)
				throw new ResourceNotFoundException(String.format("TimeSlice with ID = \"%s\" not found.", request.getTimesliceId()));
			ts.setXmin(request.getXmin());
			ts.setXmax(request.getXmax());
			ts.setYmin(request.getYmin());
			ts.setYmax(request.getYmax());
			ts.setDataAbstract(request.getAbs());
			if(ts.getCreated().equals(request.getCreationDate()))
				timeSliceDao.update(ts);
			else {
				ts.setCreated(request.getCreationDate());
				timeSliceUtil.update(ts);
			}
			return "Success";
		}
	}
	
	@RequestMapping(value="/Delete/{id}", method = RequestMethod.POST)
	public String deleteTimeSlice(@PathVariable String id, ModelMap model ) throws ResourceNotFoundException, IOException {
		TimeSlice ts = timeSliceDao.retrieve(id);
		if(ts == null) {
			// Capture if dataset not exist
			throw new ResourceNotFoundException(String.format("TimeSlice with ID = \"%s\" not found.", id));			
		}
		timeSliceUtil.delete(ts);
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new TimeSliceResponse(ts));
		return "Success";
	}


	@RequestMapping(value="/Form", method = RequestMethod.GET)
	public String createTestForm() {
		return "TimeSliceForm";
	}
	
	@RequestMapping(value="/Parent/{id}", method = RequestMethod.GET)
	public String getDataset(@PathVariable String id, ModelMap model ) throws ResourceNotFoundException, IOException {
		Dataset ds = timeSliceDao.getParentDataset(id);
		if(ds == null) {
			// Capture if dataset not exist
			throw new ResourceNotFoundException(String.format("TimeSlice with ID = \"%s\" not found.", id));			
		}
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new DatasetResponse(ds));
		return "Success";
	}

	@RequestMapping(value="/Lock/{id}", method = RequestMethod.GET)
	public String getTimeSliceLocks(@PathVariable String id, ModelMap model ) throws ResourceNotFoundException {
		log.debug("get all lock for timesliceId");
		log.debug("timesliceId: {}", id);

		
		List<TimeSliceLock> list = timeSliceLockDao.findByTimeSlice(id);
		if(list == null) {
			// Capture if timeslice not exist
			throw new ResourceNotFoundException(String.format("TimeSliceLock with ID = \"%s\" not found.", id));			
		}
		model.addAttribute(ControllerHelper.RESPONSE_ROOT, new TimeSliceLockCollectionResponse(list));
		return "List";
	}
}
