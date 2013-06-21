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
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;
import org.vpac.web.model.response.DatasetCollectionResponse;
import org.vpac.web.model.response.DatasetResponse;
import org.vpac.web.util.ControllerHelper;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;

public class AtomFeedView extends AbstractAtomFeedView {
	private String feedId = "tag:http://web.vpac.org/Atom";
	private String title = "http://web.vpac.org/Atom:news";

	// private String newsAbsoluteUrl = "http://web.vpac.org/Atom/news/";

	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Feed feed,
			HttpServletRequest request) {
		feed.setId(feedId);
		feed.setTitle(title);
		setUpdatedIfNeeded(model, feed);
		super.buildFeedMetadata(model, feed, request);
	}

	private void setUpdatedIfNeeded(Map<String, Object> model, Feed feed) {
		Date lastUpdate = (Date) model.get("News");
		if(lastUpdate != null) {
			if (feed.getUpdated() == null || lastUpdate.compareTo(feed.getUpdated()) > 0) {
					feed.setUpdated(lastUpdate);
			}
		}
	}

	@Override
	protected List<Entry> buildFeedEntries(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		DatasetCollectionResponse list = (DatasetCollectionResponse) model
				.get(ControllerHelper.RESPONSE_ROOT);
		List<Entry> items = new ArrayList<Entry>(1);

		for (DatasetResponse dr : list.getItems()) {
			Entry item = new Entry();
			item.setTitle(dr.getName());
			Content content = new Content();
			content.setValue("ID:" + dr.getId() + "\n" + "ABSTACT:"
					+ dr.getDataAbstract() + "\n" + "NAME:" + dr.getName()
					+ "\n");
			ArrayList<Content> contents = new ArrayList<Content>();
			contents.add(content);
			item.setContents(contents);
			items.add(item);
		}

		return items;
	}
}