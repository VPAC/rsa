package org.vpac.ndg.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.QueryDefinition.CacheDefinition;
import org.vpac.ndg.query.QueryDefinition.DatasetInputDefinition;
import org.vpac.ndg.query.coordinates.GridProjected;
import org.vpac.ndg.query.coordinates.HasCoordinateSystem;
import org.vpac.ndg.query.coordinates.QueryCoordinateSystem;
import org.vpac.ndg.query.coordinates.TimeAxis;
import org.vpac.ndg.query.io.DatasetMetadata;
import org.vpac.ndg.query.io.DatasetProvider;
import org.vpac.ndg.query.io.ProviderRegistry;
import org.vpac.ndg.query.math.BoxReal;

import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Wraps a {@link NetcdfDataset} to provide enhanced access to metadata.
 * @author Alex Fraser
 */
public class DatasetInput implements DatasetMeta, HasCoordinateSystem {

	final Logger log = LoggerFactory.getLogger(DatasetInput.class);

	private DatasetInputDefinition def;
	private String referential;
	private CacheDefinition cacheDef;
	private NetcdfDataset dataset;
	private QueryCoordinateSystem csys;
	private DatasetUtils datasetUtils;
	private GridUtils gridUtils;
	private List<String> requestedBands;
	private List<String> variableNames;
	private Map<String, VariableAdapter> adapters;

	/**
	 * Create a new dataset wrapper. The dataset is not opened here.
	 * @param def The memento of the dataset to open.
	 * @param referential The root path of the query (for relative URIs).
	 * @see #peekGrid()
	 * @see #open(BoxReal boundsHint, DateTime minTime, DateTime maxTime)
	 */
	public DatasetInput(DatasetInputDefinition def, String referential,
			CacheDefinition cacheDef) {
		datasetUtils = new DatasetUtils();
		gridUtils = new GridUtils();

		this.def = def;
		this.referential = referential;
		this.cacheDef = cacheDef;
		dataset = null;
		requestedBands = new ArrayList<String>();
		adapters = new HashMap<String, VariableAdapter>();
	}

	/**
	 * Populate the grid for this dataset. If possible, the file will not be
	 * fully opened. After this call, {@link #getGrid()} will return the natural
	 * bounds of the dataset. However {@link #getDataset()} may return null.
	 *
	 * @see #getGrid()
	 * @see #open(BoxReal boundsHint, DateTime minTime, DateTime maxTime)
	 */
	public void peekGrid() throws IOException, QueryConfigurationException {
		log.debug("Inspecting grid of {}", def.id);
		DatasetProvider provider = ProviderRegistry.getInstance();
		DatasetMetadata meta = provider.queryMetadata(def.href, referential);
		csys = meta.getCsys();
		variableNames = meta.getVariables();
	}

	/**
	 * Mark a band as one that should be loaded from disk.
	 * @param name The band to load.
	 */
	public void requestBand(String name) {
		requestedBands.add(name);
	}

	/**
	 * Open the dataset. After this call, {@link #getDataset()} will return
	 * non-null, and {@link #getGrid()} will return the actual bounds of the
	 * data that has been opened.
	 * 
	 * @param boundsHint The bounds that are required by the query, in the same
	 *        coordinate system as this dataset (see {@link #peekGrid()}). If
	 *        this is smaller than the available bounds of the dataset, the
	 *        {@link DatasetProvider} might choose to open a subsection of the
	 *        image.
	 * @param minTime The first time requested. May be different to the
	 *        result. May be null.
	 * @param maxTime The last time requested. May be different to the
	 *        result. May be null.
	 * @see #getGrid()
	 * @see #peekGrid()
	 */
	public void open(BoxReal boundsHint, DateTime minTime, DateTime maxTime)
			throws IOException, QueryConfigurationException {
		DatasetProvider provider = ProviderRegistry.getInstance();
		dataset = provider.open(def.href, referential, boundsHint, minTime,
				maxTime, requestedBands);
		try {
			GridProjected grid = gridUtils.findBounds(dataset);
			TimeAxis timeAxis = datasetUtils.findTimeCoordinates(dataset);
			csys = new QueryCoordinateSystem(grid, timeAxis);
		} catch (IOException e) {
			close();
			throw e;
		} catch (RuntimeException e) {
			close();
			throw e;
		}
	}

	@Override
	public void close() throws IOException {
		if (dataset == null)
			return;
		dataset.close();
		dataset = null;
	}

	@Override
	public QueryCoordinateSystem getCoordinateSystem() {
		return csys;
	}

	public List<String> getOutputNames() {
		return variableNames;
	}

	@Override
	public GridProjected getGrid() {
		return csys.getGrid();
	}

	@Override
	public NetcdfDataset getDataset() {
		// Don't implicitely open the dataset here: it is important not to open
		// it before the bounds are specified.
		if (dataset == null)
			throw new IllegalStateException("Dataset not open yet.");
		return dataset;
	}

	@Override
	public String getName() {
		return def.id;
	}

	@Override
	public Variable findVariable(String name) {
		return dataset.findVariable(name);
	}

	@Override
	public String toString() {
		return String.format("DatasetInput(%s)", def.id);
	}

	@Override
	public VariableAdapter getVariableAdapter(String name)
			throws QueryConfigurationException {

		VariableAdapter var = adapters.get(name);

		if (var != null) {
			log.debug("Reusing existing variable adapter for {}", name);
			return var;
		}

		Variable innerVar = findVariable(name);
		if (innerVar == null) {
			throw new QueryConfigurationException(String.format(
					"Variable \"%s\" can't be found in dataset \"%s\".", name,
					def.id));
		}

		VariableAdapter adapter = new VariableAdapter(innerVar, name, this);
		// As this is an input dataset, configure the page cache.
		adapter.getPageCache().configure(cacheDef);
		adapters.put(name, adapter);
		return adapter;
	}
}
