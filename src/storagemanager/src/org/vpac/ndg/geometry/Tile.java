package org.vpac.ndg.geometry;

import org.vpac.ndg.Utils;

/**
 * This class represents a single tile spatially.
 * @author glennf
 * @author hsumanto
 *
 */
public class Tile implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;	
	private int x;
	private int y;

	public Tile() {
	}

	public Tile(int x, int y) {
		this.x = x;
		this.y = y;
	}

	
	public Tile(Point<Integer> index) {
		setX(index.getX());
		setY(index.getY());
	}	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public Point<Integer> getIndex() {
		return new Point<Integer>(getX(), getY());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) 
			return true;

		if (!(obj instanceof Tile))
			return false;

		Tile other = (Tile) obj;

		if(x != other.getX() || y != other.getY()) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Utils.hashCode(x) ^ Utils.hashCode(y);
	}

	@Override
	public String toString() {
		return String.format("Tile(%d, %d)", x, y);
	}
}
