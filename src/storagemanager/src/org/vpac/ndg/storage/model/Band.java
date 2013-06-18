package org.vpac.ndg.storage.model;

import org.vpac.ndg.rasterdetails.RasterDetails;

public class Band implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private RasterDetails type;
	private String nodata;
	private boolean metadata;
	private boolean continuous;

	public RasterDetails getType() {
		return type;
	}

	/**
	 * Create a new, but undefined, band.
	 */
	public Band() {
	}

	/**
	 * Create a new band and initialise a few basic properties. The remaining
	 * properties will be undefined. In normal operation, bands are created with
	 * a couple of properties unspecified; these are filled in when actual data
	 * is imported.
	 * 
	 * @param name The name of the band.
	 * @param isContinuous Whether the data is continuous. Continuous data is
	 *        eligible for bilinear filtering.
	 * @param isMetadata Whether the band represents metadata.
	 */
	public Band(String name, boolean isContinuous, boolean isMetadata) {
		this.name = name;
		this.type = null;
		this.nodata = null;
		this.continuous = isContinuous; 
		this.metadata = isMetadata;
	}

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

	public boolean isMetadata() {
		return metadata;
	}

	public void setMetadata(boolean metadata) {
		this.metadata = metadata;
	}

	public boolean isContinuous() {
		return continuous;
	}

	public void setContinuous(boolean continuous) {
		this.continuous = continuous;
	}

	public void setType(RasterDetails type) {
		this.type = type;
	}

	/**
	 * @return The nodata value of this band as a numeric string (e.g. "-999"),
	 *         or null if nothing has been imported for this band yet. If the
	 *         band is <em>intended</em> to have no nodata value, the empty
	 *         string ("") will be returned.
	 */
	public String getNodata() {
		return nodata;
	}

	public void setNodata(String nodata) {
		this.nodata = nodata;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Band other = (Band) obj;
		if (continuous != other.continuous)
			return false;
		if (metadata != other.metadata)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nodata == null) {
			if (other.nodata != null)
				return false;
		} else if (!nodata.equals(other.nodata))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (continuous ? 1231 : 1237);
		result = prime * result + (metadata ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((nodata == null) ? 0 : nodata.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return String.format("Band(%s)", getName());
	}
}
