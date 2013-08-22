/*
 * This file is part of SpatialCube.
 *
 * SpatialCube is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SpatialCube is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * SpatialCube.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2013 CRCSI - Cooperative Research Centre for Spatial Information
 * http://www.crcsi.com.au/
 */

package org.vpac.ndg.common.datamodel;

import java.util.EnumSet;
import java.util.Iterator;

/** class for all calculations relating to resolution */
public enum CellSize implements UiOption<CellSize> {
	// For Projected Coordinate System: unit in meter	
	/** indicates an unknown or unset resolution */
	unknown(0, "unknown", 100.0),
	/** indicates a cell size of 10km */
	km10(1, "10km", 10000.0),
	/** indicates a cell size of 1km */
	km1(2, "1km", 1000.0), 
	/** indicates a cell size of 500m */
	m500(3, "500m", 500.0), 
	/** indicates a cell size of 250m */
	m250(4, "250m", 250.0), 
	/** indicates a cell size of 100m */
	m100(5, "100m", 100.0), 
	/** indicates a cell size of 50km */
	m50(6, "50m", 50.0), 
	/** indicates a cell size of 25km */
	m25(7, "25m", 25.0), 
	/** indicates a cell size of 20km */
	m20(8, "20m", 20.0), 
	/** indicates a cell size of 10m */
	m10(9, "10m", 10.0),
	/** indicates a cell size of 5m */
	m5(10, "5m", 5.0), 
	/** indicates a cell size of 2.5m */
	m2p5(11, "2.5m", 2.5),
	/** indicates a cell size of 1m */
	m1(12, "1m", 1.0),
	/** indicates a cell size of 10cm */
	mm100(13, "10cm", 0.1),
	
	// For Geographic Coordinate System: unit in decimal degree
	// FIXME: finalize the degree unit to be used
	/** indicates a cell size of 10 decimal degree */
	d10(21, "10deg", 10.0),
	/** indicates a cell size of 5 decimal degree */	
	d5(22, "5deg", 5.0),
	/** indicates a cell size of 1 decimal degree */	
	d1(23, "1deg", 1.0),
	/** indicates a cell size of 0.5 decimal degree */	
	d0p5(24, "0.5deg", 0.5),
	/** indicates a cell size of 0.1 decimal degree */	
	d0p1(25, "0.1deg", 0.1),
	/** indicates a cell size of 0.05 decimal degree */
	d0p05(26, "0.05deg", 0.05),
	/** indicates a cell size of 0.01 decimal degree */
	d0p01(27, "0.01deg", 0.01),
	/** indicates a cell size of 0.005 decimal degree */
	d0p005(28, "0.005deg", 0.005),
	/** indicates a cell size of 0.001 decimal degree */
	d0p001(29, "0.001deg", 0.001),
	/** indicates a cell size of 0.0005 decimal degree */
	d0p0005(30, "0.0005deg", 0.0005),
	/** indicates a cell size of 0.0001 decimal degree */
	d0p0001(31, "0.0001deg", 0.0001),
	/** indicates a cell size of 0.00005 decimal degree */
	d0p00005(32, "0.00005deg", 0.00005),
	/** indicates a cell size of 0.00001 decimal degree */
	d0p00001(33, "0.00001deg", 0.00001)
	;

	public static final EnumSet<CellSize> PROJCS_CELLSIZES = EnumSet.range(unknown, mm100);
	public static final EnumSet<CellSize> GEOGCS_CELLSIZES = EnumSet.range(d10, d0p00001);
	
	private int value;
	private String humanReadableValue;
	private double valueInUnits;

	private CellSize(int value, String humanReadableValue, double valueInUnits) {
		this.value = value;
		this.humanReadableValue = humanReadableValue;
		this.valueInUnits = valueInUnits;
	}
	
	// the identifierMethod
	public int toInt() {
		return value;
	}

	// the valueOfMethod
	public static CellSize fromInt(int value) {
		switch (value) {
		case 0:
			return unknown;
		case 1:
			return km10;
		case 2:
			return km1;
		case 3:
			return m500;
		case 4:
			return m250;
		case 5:
			return m100;
		case 6:
			return m50;
		case 7:
			return m25;
		case 8:
			return m20;
		case 9:
			return m10;
		case 10:
			return m5;
		case 11:
			return m2p5;
		case 12:
			return m1;
		case 13:
			return mm100;
			
		case 21:
			return d10;
		case 22:
			return d5;
		case 23:
			return d1;
		case 24:
			return d0p5;
		case 25:
			return d0p1;
		case 26:
			return d0p05;
		case 27:
			return d0p01;
		case 28:
			return d0p005;
		case 29:
			return d0p001;
		case 30:
			return d0p0005;
		case 31:
			return d0p0001;			
		case 32:
			return d0p00005;
		case 33:
			return d0p00001;
			
		default:
			return unknown;
		}
	}
	
	public double toDouble() {
		return valueInUnits;
	}
	
	/**
	 * @return A human-readable string representation of the enum value.
	 */
	@Override
	public String toHumanString() {
		return humanReadableValue;
	}
	
	@Override
	public CellSize[] getValues() {
		return CellSize.values();
	}
	

	@Override
	public CellSize[] getEnumSetValues(EnumSet<CellSize> enumSet) {
		CellSize[] result = enumSet.toArray(new CellSize[0]);
		
		return result;
	}	
	
	public static EnumSet<CellSize> getEnumSet(String resolutionList, boolean isGeographic) {
		EnumSet<CellSize> result = EnumSet.noneOf(CellSize.class);
		resolutionList = resolutionList.trim();
		
		EnumSet<CellSize> resolutionSet = CellSize.PROJCS_CELLSIZES;
		if(isGeographic) {
			resolutionSet = CellSize.GEOGCS_CELLSIZES;
		}		
		
		String[] resolutionArr = resolutionList.split(" ");
		for(String resolutionStr : resolutionArr) {
			Iterator<CellSize> it = resolutionSet.iterator();		
			while(it.hasNext()) {
				CellSize resolution = it.next();
				if(resolution.toString().equalsIgnoreCase(resolutionStr)) {
					result.add(resolution);
					break;
				}
			}
		}			
		
		return result;
	}
	
	@Override
	public CellSize getValueOf(String identifier) {
		return CellSize.valueOf(identifier);
	}

	public static CellSize fromHumanString(String value) {
		for (CellSize cs : CellSize.values()) {
			if (cs.toHumanString().equalsIgnoreCase(value))
				return cs;
		}
		throw new IllegalArgumentException(String.format(
				"No matching resolution for %s", value));
	}

	public boolean isUnitInMeter() {
		return PROJCS_CELLSIZES.contains(this);
	}
	
	public static void main(String[] args) {
		System.out.println(CellSize.m500.toHumanString() + " in meter? " + CellSize.m500.isUnitInMeter());
		System.out.println(CellSize.d0p001.toHumanString() + " in meter? " + CellSize.d0p001.isUnitInMeter());
	}

}
