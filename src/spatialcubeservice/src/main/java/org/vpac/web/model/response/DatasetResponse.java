package org.vpac.web.model.response;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.storage.model.Dataset;

 
@XmlRootElement(name = "Dataset")
public class DatasetResponse {
	private String id;
	private String name;
	private CellSize resolution;
	private long precision;
	private String dataAbstract;
	private Date created;

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

	public CellSize getResolution() {
		return resolution;
	}

	@XmlAttribute
	public void setResolution(CellSize resolution) {
		this.resolution = resolution;
	}
	
	public long getPrecision() {
		return precision;
	}

	@XmlAttribute
	public void setPrecision(long precision) {
		this.precision = precision;
	}

	public String getDataAbstract() {
		return dataAbstract;
	}

	@XmlElement
	public void setDataAbstract(String dataAbstract) {
		this.dataAbstract = dataAbstract;
	}

	public DatasetResponse(Dataset d) {
		if(d != null){
			this.setId(d.getId());
			this.setName(d.getName());
			this.setDataAbstract(d.getAbst());
			this.setResolution(d.getResolution());
			this.setPrecision(d.getPrecision());
			this.setCreated(d.getCreated());
		}
	}
	
	public DatasetResponse() {
	}

	public Date getCreated() {
		return created;
	}

	@XmlAttribute
	public void setCreated(Date created) {
		this.created = created;
	}
}
