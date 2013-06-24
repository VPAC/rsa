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

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.vpac.web.NewsRepository;
import org.vpac.web.model.News;

@Controller
public class AtomFeedController {
    public static final String LAST_UPDATE_VIEW_KEY = "lastUpdate";
    public static final String NEWS_VIEW_KEY = "news";
    private NewsRepository newsRepository;
    private String viewName;
 
    protected AtomFeedController() {} //required by cglib
 
    public AtomFeedController(NewsRepository newsRepository, String viewName) {
        notNull(newsRepository); 
        hasText(viewName);
        this.newsRepository = newsRepository;
        this.viewName = viewName;
    }
 
	private void hasText(String viewName2) {
		// TODO Auto-generated method stub
		
	}

	private void notNull(NewsRepository newsRepository2) {
		// TODO Auto-generated method stub
		
	}

	@RequestMapping(value = "/feed", method = RequestMethod.GET)       
    public ModelAndView feed() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(viewName);
        List<News> news = newsRepository.fetchPublished();
        modelAndView.addObject(NEWS_VIEW_KEY, news);
        modelAndView.addObject(LAST_UPDATE_VIEW_KEY, getCreationDateOfTheLast(news));
        return modelAndView;
    }
 
    private Date getCreationDateOfTheLast(List<News> news) {
        if(news.size() > 0) {
            return news.get(0).getCreationDate();
        }
        return new Date(0);
    }
}