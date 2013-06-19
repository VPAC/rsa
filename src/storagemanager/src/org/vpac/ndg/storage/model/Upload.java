/**
 * 
 */
package org.vpac.ndg.storage.model;
import java.util.Date;

public class Upload implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getTimeSliceId() {
		return timeSliceId;
	}
	public void setTimeSliceId(String timeSliceId) {
		this.timeSliceId = timeSliceId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	private String fileId;
	private String timeSliceId;
	private Date created;
	
	public Upload(String timeSliceId) {
		this.timeSliceId = timeSliceId;
		this.created = new Date();
	}
	public Upload() {
	}
}