package org.vpac.ndg.storage.model;

import java.util.Date;

/**
 * This class is used to monitor each process such as import, export or query.
 * @author hsumanto
 *
 */
public class Process {
	private String id;
	private String hostname;
	private Date latest;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getLatest() {
		return latest;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public String toString() {
		return String.format("Process(%s)", id);
	}

}
