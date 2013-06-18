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
