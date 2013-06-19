package org.vpac.ndg.geometry;

import java.util.Map;

import org.vpac.ndg.common.datamodel.CellSize;

/** 
 * Provides the functions to calculate National Nested Grid indices (NNGI).
 * 
 * @author glennf
 * @author adfries
 * @author hsumanto
 */
public class NestedGrid {
	private static double FLOAT_EPSILON = 0.0001;
	protected Point<Double> origin;
	private Map<CellSize, Integer> resolutionList;

	/**
	 * Create National Nested Grid based on the specified projection.
	 * @param mapOrigin The origin of the NNG in map coordinates.
	 * @param resolutionList The resolutions to be supported in NNG.
	 */
	public NestedGrid(Point<Double> mapOrigin, Map<CellSize, Integer> resolutionList) {
		origin = mapOrigin;
		this.resolutionList = resolutionList;
	}

	/**
	 * Get the origin (top left) coordinates as the start point for the grid
	 * cell index.
	 * @return Point containing the top left origin of the grid cell system in
	 * internal map storage projection.
	 */
	public Point<Double> getMapGridOrigin() {
		return new Point<Double>(origin);
	}

	/**
	 * Calculate the coordinate of a point relative to the grid origin. This
	 * effectively applies a false Northing and Easting that correspond to the
	 * grid origin.
	 * @param pnt The point to return a grid cell coordinate for (in map
	 * coordinates).
	 * @return The corresponding coordinate, as an offset from the origin.
	 */
	public Point<Double> mapToOffset(Point<Double> pnt) {
		
		Point<Double> origin = getMapGridOrigin();
		return new Point<Double>(
				pnt.getX() - origin.getX(),
				pnt.getY() - origin.getY());
	}

	public Point<Double> offsetToMap(Point<Double> offset) {
		Point<Double> origin = getMapGridOrigin();
		return new Point<Double>(
				origin.getX() + offset.getX(),
				origin.getY() + offset.getY());
	}

	/**
	 * @param offset The point in map coordinates.
	 * @param cellsize The cell size to calculate the cell for.
	 * @return The corresponding grid cell index. Note: before referencing data
	 * in the grid, check whether either component is negative.
	 */
	public Point<Integer> offsetToCell(Point<Double> offset, CellSize cellsize) {
			
		// The offset represents the number of metres in the map projection from
		// the origin. Divide each by the resolution, taking the floor of the
		// value as the index.
		
		double fv = cellsize.toDouble();

		// Note the asymmetry here: cellToOffset adds 0.5*cellsize to the
		// offset. But here, we accept any offset that is inside the cell - it
		// does not need to be in the centre. So 0.5*cellsize is not used here.
		double ox = offset.getX() / fv;
		// In the map, Y increases as you go north; in the grid, Y increases as
		// you go south.
		double oy = 0d - (offset.getY() / fv);
	
		int oxc = (int) Math.floor(ox);
		int oyc = (int) Math.floor(oy);
		
		Point<Integer> cell = new Point<Integer>(oxc, oyc);
		return cell;
	}

	/**
	 * @param cell The 2D index of the cell.
	 * @param resolution The cell size.
	 * @return return Coordinates of the top left of the grid cell.
	 */
	public Point<Double> cellToOffset(Point<Integer> cell,
			CellSize resolution) {

		double fv = resolution.toDouble();
		// cellToOffset	returns coordinate of the top left of the grid cell	
		Point<Double> offset = new Point<Double>(
				cell.getX() * fv,
				0.0 - cell.getY() * fv);
		return offset;
	}

	/**
	 * Convert a cell into an NNG tile.
	 * @param cell The 2D index of the cell.
	 * @param cellSize The cell size resolution.
	 * @return Returns the 2D index of the tile.
	 */
	public Point<Integer> cellToTile(Point<Integer> cell, CellSize cellSize) {		
		int numOfCellsPerTile = resolutionList.get(cellSize);
		
		int tileX = cell.getX() / numOfCellsPerTile;
		int tileY = cell.getY() / numOfCellsPerTile;
		
		Point<Integer> tile = new Point<Integer>(tileX, tileY);
		return tile;
	}

	/**
	 * Convert a tile into an NNG cell.
	 * @param tile The 2D index of the tile.
	 * @param cellSize The cell size resolution.
	 * @return Returns the 2D index of the cell.
	 */
	public Point<Integer> tileToCell(Point<Integer> tile, CellSize cellSize) {
		int numOfCellsPerTile = resolutionList.get(cellSize);
		
		int cellX = tile.getX() * numOfCellsPerTile;
		int cellY = tile.getY() * numOfCellsPerTile;
		
		Point<Integer> cell = new Point<Integer>(cellX, cellY);
		return cell;
	}

	/**
	 * Convert a point to an NNG cell.
	 * @param map The point in map space.
	 * @param cellSize The grid resolution.
	 * @return The 2D cell index.
	 */
	public Point<Integer> mapToCell(Point<Double> map,
			CellSize cellSize) {

		return offsetToCell(mapToOffset(map), cellSize);
	}

	/**
	 * Convert an NNG cell to a point in map coordinates.
	 * @param cell The 2D cell index.
	 * @param cellSize The grid resolution.
	 * @return The point in map space.
	 */
	public Point<Double> cellToMap(Point<Integer> cell,
			CellSize cellSize) {

		return offsetToMap(cellToOffset(cell, cellSize));
	}

	/**
	 * Convert a point to an NNG tile.
	 * @param map The point in map space.
	 * @param cellSize The grid resolution.
	 * @return Returns the 2D tile index.
	 */
	public Point<Integer> mapToTile(Point<Double> map, CellSize cellSize) {
		return cellToTile(mapToCell(map, cellSize), cellSize);
	}

	/**
	 * Convert an NNG tile to a point in map coordinates.
	 * 
	 * @param tile The 2D tile index.
	 * @param cellSize The grid resolution.
	 * @return The point in map space. This is located at the top-left corner of
	 *         the tile.
	 */
	public Point<Double> tileToMap(Point<Integer> tile, CellSize cellSize) {
		return cellToMap(tileToCell(tile, cellSize), cellSize);
	}

	/**
	 * Convert map coordinates into a NNGI.
	 * 
	 * @param map The point to find a cell for in map coordinates.
	 * @param resolution The cell resolution.
	 * @return The equivalent NNGI.
	 * 
	 * @throws GridError if a valid NNGI can not be created from the coordinate.
	 */
	public String mapToNngi(Point<Double> map, CellSize resolution)
			throws IllegalArgumentException {

		Point<Integer> cell = mapToCell(map, resolution);

		// Columns in the NNGI are decimal, so we need to concatenate them as
		// strings (rather than bitwise concatenation).
		//
		// The smallest cell size is 10cm, so the NNGI has a fixed point of one
		// decimal place. NNGI components are therefore specified in decimeters.
		// We need to multiply the cell offset by the resolution times 10. Note
		// that this conversion needs to be done here, not in mapToCell or so,
		// because the cell indices are unitless.
		String resS = String.format("%02d", resolution.toInt());

		// Convert X
		long x = (long) (cell.getX() * (int)(10.0 * resolution.toDouble()));
		if (x < 0) {
			throw new IllegalArgumentException(
					"Resulting X component is negative.");
		}
		String xS = String.format("%08d", x);
		if (xS.length() > 8) {
			throw new IllegalArgumentException(
					"Resulting X component is too large.");
		}

		// Convert Y
		long y = (long) (cell.getY() * (int)(10.0 * resolution.toDouble()));
		if (y < 0) {
			throw new IllegalArgumentException(
					"Resulting Y component is negative.");
		}
		String yS = String.format("%08d", y);
		if (yS.length() > 8) {
			throw new IllegalArgumentException(
					"Resulting Y component is too large.");
		}

		if (resS.length() > 2)
			throw new IllegalArgumentException("Resolution is too large.");
		
		String repr = resS + xS + yS;
		return repr;
	}

	/**
	 * Convert a national nested grid index to a map coordinate. The
	 * point returned corresponds to the top left of the referenced cell.
	 * @param nngi The cell reference as a national nested grid index.
	 * @return Returns the resulting map coordinate.
	 */
	public Point<Double> nngiToMap(String nngi)
			throws IllegalArgumentException {

		if (nngi.length() > 18)
			throw new IllegalArgumentException("Index is too large.");
		else if (nngi.length() < 18)
			throw new IllegalArgumentException("Index is too small.");

		CellSize res = CellSize.fromInt(Integer.parseInt(nngi.substring(0, 2)));

		// Find the offset from the origin in decimeters.
		int offsetX = Integer.parseInt(nngi.substring(2, 10));
		int offsetY = Integer.parseInt(nngi.substring(10, 18));

		// Find the cell index in the current resolution.
		double indexX = ((double)offsetX) / 10.0 / res.toDouble();
		double indexY = ((double)offsetY) / 10.0 / res.toDouble();

		double fractX = indexX - (long) indexX;
		double fractY = indexY - (long) indexY;
		if (fractX > FLOAT_EPSILON || fractY > FLOAT_EPSILON) {
			System.err.println(String.format("Warning: NNGI is not " +
					"aligned to its %s grid. Differs by (%f, %f) cells.",
					res.toHumanString(), fractX, fractY));
		}

		Point<Integer> cell = new Point<Integer>((int)indexX, (int)indexY);
		return cellToMap(cell, res);
	}

	public static void main(String[] args) {
	}

	/**
	 * Find the bounds of a tile in map space.
	 * @param tile
	 * @param resolution
	 * @return Returns the bounds of a tile in a map space.
	 */
	public Box getBounds(Point<Integer> tile, CellSize resolution) {
		Point<Integer> min = tile;
		Point<Integer> max = new Point<Integer>(tile.getX() + 1, tile.getY() + 1);
		Point<Double> minMap = tileToMap(min, resolution);
		Point<Double> maxMap = tileToMap(max, resolution);
		
		return new Box(minMap, maxMap);
	}

	/**
	 * Align a bounding box to the storage grid.
	 * @param mapBox The box to transform, specified in internal projection.
	 * @param resolution The resolution of the grid to align to.
	 * @return A new bounding box that is aligned to the grid. It will match
	 * the input box as closely as possible.
	 */
	public Box alignToGrid(Box mapBox, CellSize resolution) {
		Point<Double> p1;
		Point<Double> p2;

		// NOTE: The coordinates are floored in offsetToCell, so this may reduce
		// the maximum by up to one pixel.
		// TODO: Maybe this should be fixed, like in alignToTileGrid?
		p1 = cellToMap(mapToCell(mapBox.getMin(), resolution), resolution);
		p2 = cellToMap(mapToCell(mapBox.getMax(), resolution), resolution);

		return new Box(p1, p2);
	}

	/**
	 * Align a point to the closest smaller NNG grid cell corner (x' <= x).
	 * @param map The internal coordinates to transform.
	 * @param resolution The resolution of the grid to align to.
	 * @return A new point that is aligned to the grid.
	 */
	public Point<Double> alignToGrid(Point<Double> map, CellSize resolution) {
		Point<Double> alignedPt = cellToMap(mapToCell(map, resolution), resolution);
		return alignedPt;
	}

	/**
	 * Shift a point to the centre of the cell that it occupies.
	 * @param map The internal coordinates to transform.
	 * @param resolution The resolution of the grid to align to.
	 * @return The centre of the cell.
	 */
	public Point<Double> alignToGridCentre(Point<Double> map, CellSize resolution) {
		double halfres = resolution.toDouble() * 0.5;
		Point<Double> offset = new Point<Double>(halfres, halfres);
		Point<Double> alignedPt = cellToMap(mapToCell(map, resolution), resolution);
		alignedPt = Point.add(alignedPt, offset);
		return alignedPt;
	}

	/**
	 * Align a bounding box to the tile grid by growing it to fill the tiles
	 * that it intersects.
	 * @param mapBox The box to transform, specified in internal projection.
	 * @param resolution The resolution of the grid to align to.
	 * @return A new bounding box that is aligned to the tile grid.
	 */
	public Box alignToTileGrid(Box mapBox, CellSize resolution) {
		final Point<Integer> maxShift = new Point<>(1, 1);

		Point<Double> p1;
		Point<Integer> t1;
		Point<Double> p2;
		Point<Integer> t2;

		// Get the corners of the box. These are reduced by half a cell, because
		// technically the maximum extent is *just* inside the next tile.
		Box shrunkenBbox = mapBox.expand(0.0 - (resolution.toDouble() * 0.5));

		// Find min tile. getMin is not used because the tiles increase going
		// down!
		t1 = mapToTile(shrunkenBbox.getUlCorner(), resolution);
		p1 = tileToMap(t1, resolution);

		// Find max. Due to the shrinking above, the maximum will definitely be
		// floored to the next-lower tile coordinate. So it's essential to add
		// one to it.
		t2 = mapToTile(shrunkenBbox.getLrCorner(), resolution);
		t2 = Point.addi(t2, maxShift);
		p2 = tileToMap(t2, resolution);

		return new Box(p1, p2);
	}
}
