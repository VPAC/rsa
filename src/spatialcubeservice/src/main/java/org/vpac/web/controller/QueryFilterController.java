package org.vpac.web.controller;

import java.io.IOException;
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
import org.vpac.ndg.query.Filter;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.util.DatasetUtil;
import org.vpac.web.model.request.PagingRequest;
import org.vpac.web.model.response.QueryFilterResponse;
import org.vpac.web.model.response.QueryNodeCollectionResponse;
import org.vpac.web.util.ControllerHelper;
import org.vpac.web.util.ServiceHelper;

/**
 * This class is intended as controller for QueryFilter related web services.
 * 
 * @author hsumanto
 * 
 */
@Controller
@RequestMapping("/QueryFilter")
public class QueryFilterController {

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
	public String getAllQueryFilter(
			@RequestParam(required = false) String name,
			@Valid PagingRequest page, ModelMap model) throws IOException {

		List<Filter> filters = ServiceHelper.getFilters();
		List<QueryFilterResponse> items = new ArrayList<>();
		for (Filter f : filters) {
			items.add(new QueryFilterResponse(f));
		}
		model.addAttribute(ControllerHelper.RESPONSE_ROOT,
				new QueryNodeCollectionResponse("Filters", items));
		return "List";
	}
}
