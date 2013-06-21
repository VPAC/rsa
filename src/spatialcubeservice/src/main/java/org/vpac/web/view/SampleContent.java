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

import java.util.Date;

public class SampleContent {
	
	private static int idCounter = 0;
	
	private String author;
	
	private Date publicationDate;
	
	private String text;
	
	private int id;
	
	public static SampleContent generateContent(String author, Date date) {
		SampleContent content = new SampleContent();
		content.author = author;
		content.publicationDate = date;
		content.id = idCounter++;
		content.text = "Test";
		
		return content;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public Date getPublicationDate() {
		return publicationDate;
	}
	
	public String getText() {
		return text;
	}

	public int getId() {
		return id;
	}
}
