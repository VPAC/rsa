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