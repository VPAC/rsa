package org.vpac.web.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Item;
 
public class RssFeedView extends AbstractRssFeedView {
 
	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel feed,
		HttpServletRequest request) {
 
		feed.setTitle("RSS feed for NDG project");
		feed.setDescription("This is rss feed for ndg project");
		feed.setLink("http://www.vpac.org/Temp");
 
		super.buildFeedMetadata(model, feed, request);
	}
 
 
	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
 
//		RSAMap map = (RSAMap) model.get("model");
//		String msg = map.getName() + map.getQuantity();
 
		String msg = "Test";
		List<Item> items = new ArrayList<Item>(1);
		Item item = new Item();
		item.setAuthor("Jin Park");
		item.setLink("http://www.vpac.org");
 
		Content content = new Content();
		content.setValue(msg);
 
		item.setContent(content);
 
		items.add(item);
 
		return items;
	}
}