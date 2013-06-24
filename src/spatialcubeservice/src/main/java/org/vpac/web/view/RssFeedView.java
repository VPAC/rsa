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