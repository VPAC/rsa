package org.vpac.ndg.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.configuration.NdgConfig;
import org.vpac.ndg.configuration.NdgConfigManager;

/**
 * The Tile Manager class is responsible for creating tiles for a list of resolutions.
 * It could also be used when trying to get the tile(s) for a specified extent or point. 
 * 
 * @author hsumanto
 *
 */
public class TileManager {

	final Logger log = LoggerFactory.getLogger(TileManager.class);

	private Point<Double> origin;
	private Map<CellSize, Integer> resolutionList;	
	private NestedGrid nngGrid;

	NdgConfigManager ndgConfigManager;

	public TileManager() {
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		ndgConfigManager = (NdgConfigManager) appContext.getBean("ndgConfigManager");
		NdgConfig cfg = ndgConfigManager.getConfig();
		this.origin = cfg.getGridOriginPointInTargetSrs();
		this.resolutionList = cfg.getResolutionList();	
	}

	public Point<Double> getOrigin() {
		return origin;
	}

	public Map<CellSize, Integer> getResolutionList() {
		return resolutionList;
	}
	

	public NestedGrid getNngGrid() {
		if(nngGrid == null) {
			nngGrid = new NestedGrid(origin, resolutionList);
		}
		return nngGrid;		
	}

	/**
	 * Get tile which intersects with the specified point for the specified resolution.
	 * Note: The point coordinate must be in the same projection as the internal default projection.
	 * @param mapCoord The specified map coordinate.
	 * @param resolution The specified resolution.
	 * @return Returns tile which intersect with the specified point for the specified resolution.
	 */
	public Tile getTile(Point<Double> mapCoord, CellSize resolution) {
		// Align map coordinate into NNG grid
		Point<Double> alignedMapCoord = getNngGrid().alignToGrid(mapCoord, resolution);
		Point<Integer> tileIndex = getNngGrid().mapToTile(alignedMapCoord, resolution);
		return  new Tile(tileIndex);
	}

	public BoxInt mapToTile(Box bbox, CellSize resolution) {
		NestedGrid nng = getNngGrid();

		// Get the corners of the box. These are reduced by half a cell, because
		// technically the maximum extent is *just* inside the next tile.
		Box shrunkenBbox = bbox.expand(0.0 - (resolution.toDouble() * 0.5));

		Point<Integer> firstTile = nng.mapToTile(shrunkenBbox.getMin(), resolution);
		Point<Integer> lastTile = nng.mapToTile(shrunkenBbox.getMax(), resolution);

		log.debug("Tile query: {}", bbox);
		log.debug("... touches tiles ranging from {} to {}", firstTile, lastTile);

		return new BoxInt(firstTile, lastTile);
	}

	/**
	 * Get all tiles which intersects the specified bounding box. Note: bounding
	 * box is in INTERNAL projection == {@link Projection#getDefaultMapEpsg()}
	 * 
	 * @param bbox
	 *            The bounding box.
	 * @param resolution
	 *            The tile resolution.
	 * @return Returns all tiles which intersects the specified bounding box.
	 */
	public List<Tile> getTiles(Box bbox, CellSize resolution) {
		BoxInt tileBox = mapToTile(bbox, resolution);
		return getTiles(tileBox);
	}

	public List<Tile> getTiles(BoxInt tileBox) {
		List<Tile> tiles = new ArrayList<Tile>();

		if (ndgConfigManager.getConfig().isUpPositive()) {
			// Tile order is opposite to pixel order on the y axis.
			for (int y = tileBox.getMax().getY(); y >= tileBox.getMin().getY(); y--) {
				for (int x = tileBox.getMin().getX(); x <= tileBox.getMax().getX(); x++) {
					tiles.add(new Tile(x, y));
				}
			}

		} else {
			// Tile order is the same as pixel order.
			for (int y = tileBox.getMin().getY(); y <= tileBox.getMax().getY(); y++) {
				for (int x = tileBox.getMin().getX(); x <= tileBox.getMax().getX(); x++) {
					tiles.add(new Tile(x, y));
				}
			}
		}

		return tiles;
	}
}
