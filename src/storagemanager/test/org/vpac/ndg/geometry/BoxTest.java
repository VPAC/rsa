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

package org.vpac.ndg.geometry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.gdal.gdal.gdal;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.vpac.ndg.common.datamodel.CellSize;

/**
 * Test gdal geometry intesection method.
 * 
 * @author hsumanto
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration({"file:resources/spring/config/TestBeanLocations.xml"})
public class BoxTest {

	final private Logger log = LoggerFactory.getLogger(BoxTest.class);

	@Autowired
	TileManager tileManager;

	@Before
	public void setUp() throws Exception {
		gdal.AllRegister();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBoxIntersects() {
		Box box1;
		Box box2;

		box1 = new Box(new Point<Double>(0.0, 0.0),
				new Point<Double>(1.0, 1.0));

		// TOUCHING (i.e. not intersecting)
		// left, touching
		box2 = new Box(new Point<Double>(-1.0, 0.2),
				new Point<Double>(0.0, 0.8));
		assertFalse(box1.intersects(box2));
		// right, touching
		box2 = new Box(new Point<Double>(1.0, 0.2),
				new Point<Double>(2.0, 0.8));
		assertFalse(box1.intersects(box2));
		// over, touching
		box2 = new Box(new Point<Double>(0.2, 1.0),
				new Point<Double>(0.8, 2.0));
		assertFalse(box1.intersects(box2));
		// under, touching
		box2 = new Box(new Point<Double>(0.2, -1.0),
				new Point<Double>(0.8, 0.0));
		assertFalse(box1.intersects(box2));

		// INTERSECTING
		// upper-right
		box2 = new Box(new Point<Double>(0.5, 0.5),
				new Point<Double>(1.5, 1.5));
		assertTrue(box1.intersects(box2));
		// lower-left
		box2 = new Box(new Point<Double>(-0.5, -0.5),
				new Point<Double>(0.5, 0.5));
		assertTrue(box1.intersects(box2));

		// ENCLOSED (i.e. intersecting)
		// centre, smaller
		box2 = new Box(new Point<Double>(0.2, 0.2),
				new Point<Double>(0.8, 0.8));
		assertTrue(box1.intersects(box2));
		// centre, larger
		box2 = new Box(new Point<Double>(-0.2, -0.2),
				new Point<Double>(1.2, 1.2));
		assertTrue(box1.intersects(box2));
	}

	@Test
	public void testTileIntersects() {
		Box box1 = tileManager.getNngGrid()
				.getBounds(new Point<Integer>(1, 1), CellSize.m500);
		Box box1Copy = tileManager.getNngGrid()
				.getBounds(new Point<Integer>(1, 1), CellSize.m500);
		log.debug("Tile(1,1) intersects Tile(1,1) = " + box1.intersects(box1Copy));
		assertTrue(box1.intersects(box1Copy));

		Box box2 = tileManager.getNngGrid()
				.getBounds(new Point<Integer>(1, 2), CellSize.m500);
		log.debug("Tile(1,1) intersects Tile(1,2) = " + box1.intersects(box2));
		assertFalse(box2.intersects(box1));
		assertFalse(box1.intersects(box2));

		Box box3 = tileManager.getNngGrid()
				.getBounds(new Point<Integer>(2, 2), CellSize.m500);
		log.debug("Tile(1,1) intersects Tile(2,2) = " + box1.intersects(box3));
		assertFalse(box3.intersects(box1));
		assertFalse(box1.intersects(box3));

		// Make a big box that encompasses box2.
		Box box4 = tileManager.getNngGrid()
				.getBounds(new Point<Integer>(3, 3), CellSize.m500);
		box4.union(box1);
		assertTrue(box1.intersects(box4));
		log.debug("Tile(1,1) intersects Tile(1,1)-Tile(3,3) = " + box1.intersects(box4));
	}
}
