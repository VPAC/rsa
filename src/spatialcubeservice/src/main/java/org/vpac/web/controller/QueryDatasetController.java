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
