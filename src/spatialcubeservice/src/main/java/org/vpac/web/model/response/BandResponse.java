package org.vpac.web.model.response;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.vpac.ndg.rasterdetails.RasterDetails;
import org.vpac.ndg.storage.model.Band;
 
@XmlRootElement(name = "Band")
public class BandResponse {

	private String id;
	private String name;
	private RasterDetails type;
	private boolean metadata;
	private boolean continuous;
	
	public boolean isMetadata() {
		return metadata;
	}

	@XmlElement
	public void setMetadata(boolean metadata) {
		this.metadata = metadata;
	}
	
	public String getId() {
		return id;
	}

	@XmlAttribute
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@XmlElement
	public void setName(String name) {
		this.name = name;
	}

	public RasterDetails getType() {
		return type;
	}

	@XmlAttribute
	public void setType(RasterDetails type) {
		this.type = type;
	}

	public boolean isContinuous() {
		return continuous;
	}

	@XmlAttribute
	public void setContinuous(boolean continuous) {
		this.continuous = continuous;
	}

	public BandResponse(Band b) {
		if(b != null) {
			this.setId(b.getId());
			this.setMetadata(b.isMetadata());
			this.setName(b.getName());
			this.setType(b.getType());
			this.setContinuous(b.isContinuous());
		}
	}
	
	public BandResponse() {
	}

	public Band toBand() {
		Band b = new Band();
		b.setId(this.id);
		b.setMetadata(this.metadata);
		b.setName(this.name);
		b.setType(this.type);
		b.setContinuous(this.continuous);
		return b;
	}
}
