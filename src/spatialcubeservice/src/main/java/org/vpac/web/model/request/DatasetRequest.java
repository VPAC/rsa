package org.vpac.web.model.request;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.vpac.ndg.common.datamodel.CellSize;

import com.sun.istack.Nullable;

/**
 * Used for dataset related request.
 * Validate that the dataset name is not null/empty/trailing whitespaces.
 * Validate that the dataset resolution is the accepted resolution.
 */
public class DatasetRequest {

	@Nullable
	private String id;
	@NotBlank
	private String name;
	@NotNull
	private CellSize resolution;
	@NotNull
	private String precision;
	@NotNull
	private String dataAbstract;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CellSize getResolution() {
		return resolution;
	}
	public void setResolution(CellSize resolution) {
		this.resolution = resolution;
	}
	public String getPrecision() {
		return precision;
	}
	public void setPrecision(String precision) {
		this.precision = precision;
	}
	public String getDataAbstract() {
		return dataAbstract;
	}
	public void setDataAbstract(String dataAbstract) {
		this.dataAbstract = dataAbstract;
	}
}
