package org.vpac.ndg.storage.model;

import java.util.Date;

import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Point;

/**
 * Represents a tile in time. 
 * For example: "Survey based on satellite imagery, May 2010".
 * @author jpark
 * @see Dataset
 */
public class TimeSlice implements java.io.Serializable, Comparable<TimeSlice> {
	private static final long serialVersionUID = 1L;
	private String id;
	private String relativeLocation;
	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;
	private String vrtFile;
	private Date created;
	private String dataAbstract;

	// Locking fields. Non-primitive types are used to allow NULL fields in the
	// database (for old databases in which these fields weren't present).
	private Integer lockCount;
	private Character lockMode;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRelativeLocation() {
		return relativeLocation;
	}
	public void setRelativeLocation(String relativeLocation) {
		this.relativeLocation = relativeLocation;
	}
	public double getXmin() {
		return xmin;
	}
	public void setXmin(double xmin) {
		this.xmin = xmin;
	}
	public double getXmax() {
		return xmax;
	}
	public void setXmax(double xmax) {
		this.xmax = xmax;
	}
	public double getYmin() {
		return ymin;
	}
	public void setYmin(double ymin) {
		this.ymin = ymin;
	}
	public double getYmax() {
		return ymax;
	}
	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	
	/**
	 * The spatial extents of the time slice.
	 * 
	 * The initial bounds are infinitesimally small; thus, to check whether a
	 * time slice contains data, one may test:
	 * 
	 * <pre>
	 * {@link TimeSlice#getBounds() getBounds()}.{@link Box#getArea() getArea()} != 0.0
	 * </pre>
	 * 
	 * Otherwise, the bounds should be treated as invalid; i.e. if the area is
	 * zero then it is an error to use the bounds for any other purpose.
	 * 
	 * @return The spatial extents of this time slice in the internal projection
	 *         of the RSA.
	 */
	public Box getBounds() {
		Point<Double> p1 = new Point<Double>(getXmin(), getYmin());
		Point<Double> p2 = new Point<Double>(getXmax(), getYmax());
		Box bounds = new Box(p1, p2);
		return bounds;
	}

	/**
	 * @param bounds The bounds of the time slice in the internal projection.
	 */
	public void setBounds(Box bounds) {
		setXmin(bounds.getXMin());
		setXmax(bounds.getXMax());
		setYmin(bounds.getYMin());
		setYmax(bounds.getYMax());
	}

	public String getVrtFile() {
		return vrtFile;
	}
	public void setVrtFile(String vrtFile) {
		this.vrtFile = vrtFile;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getDataAbstract() {
		return dataAbstract;
	}
	public void setDataAbstract(String dataAbstract) {
		this.dataAbstract = dataAbstract;
	}
	public Integer getLockCount() {
		return lockCount;
	}
	public void setLockCount(Integer lockCount) {
		if (lockCount == null)
			this.lockCount = 0;
		else
			this.lockCount = lockCount;
	}

	public TimeSlice() {
		xmin = 0.0;
		xmax = 0.0;
		ymin = 0.0;
		ymax = 0.0;
	}
	
	public TimeSlice(Date created) {
		this();
		this.created = created;
	}

	@Override
	public String toString() {
		return String.format("TS(%s)", getCreated());
	}

	@Override
	public int compareTo(TimeSlice other) {
		return this.getCreated().compareTo(other.getCreated());
	}

	public Character getLockMode() {
		return lockMode;
	}
	public void setLockMode(Character lockMode) {
		if (lockMode == null)
			this.lockMode = 'r';
		else
			this.lockMode = lockMode;
	}
}