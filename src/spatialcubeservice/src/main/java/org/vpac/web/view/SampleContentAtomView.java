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

/*
 * Copyright 2002-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.vpac.web.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.syndication.feed.atom.Content;
import com.sun.syndication.feed.atom.Entry;
import com.sun.syndication.feed.atom.Feed;

import org.springframework.web.servlet.view.feed.AbstractAtomFeedView;

/**
 * A view creating a Atom representation from a list of SampleContent objects. 
 * 
 * @author Alef Arendsen
 * @author Arjen Poutsma
 */
public class SampleContentAtomView extends AbstractAtomFeedView {
	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Feed feed, HttpServletRequest request) {
		feed.setId("tag:springsource.com");
		feed.setTitle("Pet Clinic Visits");
		@SuppressWarnings("unchecked")
		List<SampleContent> contentList = (List<SampleContent>)model.get("sampleContentList");
		for (SampleContent content : contentList) {
			Date date = content.getPublicationDate();
			if (feed.getUpdated() == null || date.compareTo(feed.getUpdated()) > 0) {
				feed.setUpdated(date);
			}
		}
	}

	@Override
	protected List<Entry> buildFeedEntries(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		@SuppressWarnings("unchecked")
		List<SampleContent> contentList = (List<SampleContent>)model.get("sampleContentList");
		List<Entry> entries = new ArrayList<Entry>(contentList.size());

		for (SampleContent content : contentList) {
			Entry entry = new Entry();
			String date = String.format("%1$tY-%1$tm-%1$td", content.getPublicationDate());
			// see http://diveintomark.org/archives/2004/05/28/howto-atom-id#other
			 entry.setId(String.format("tag:springsource.com,%s:%d", date, content.getId()));
			entry.setTitle(String.format("On %s, %s wrote", date, content.getAuthor()));
			entry.setUpdated(content.getPublicationDate());

			Content summary = new Content();
			summary.setValue(content.getText());
			entry.setSummary(summary);
			
			entries.add(entry);
		}

		return entries;

	}
}
