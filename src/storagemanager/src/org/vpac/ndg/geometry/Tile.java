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
