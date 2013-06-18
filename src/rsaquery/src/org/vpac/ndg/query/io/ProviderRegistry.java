package org.vpac.ndg.query.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.vpac.ndg.query.math.BoxReal;

import ucar.nc2.dataset.NetcdfDataset;

/**
 * A provider that contains uses other providers to open files. This is a
 * Singleton; use {@link ProviderRegistry#getInstance()}. 
 *
 * @author Alex Fraser
 */
public class ProviderRegistry implements DatasetProvider {

	private static ProviderRegistry instance;
	static {
		instance = new ProviderRegistry();
		// Just for now, hard-code a provider.
		// TODO: This should be configurable.
		instance.addProivder(new FileDatasetProvider());
	}

	public static ProviderRegistry getInstance() {
		return instance;
	}

	private List<DatasetProvider> providers;

	protected ProviderRegistry() {
		providers = new ArrayList<DatasetProvider>();
	}

	/**
	 * Register a new provider. Providers will be tested in the order that they
	 * are added.
	 * @param p The provider to register.
	 */
	public void addProivder(DatasetProvider p) {
		providers.add(p);
	}
	/**
	 * Remove all registered providers.
	 */
	public void clearProivders() {
		providers.clear();
	}

	@Override
	public boolean canOpen(String uri) {
		for (DatasetProvider p : providers) {
			if (p.canOpen(uri))
				return true;
		}
		return false;
	}

	@Override
	public NetcdfDataset open(String uri, String referential,
			BoxReal boundsHint, DateTime minTimeHint, DateTime maxTimeHint,
			List<String> bands) throws IOException {

		for (DatasetProvider p : providers) {
			if (p.canOpen(uri)) {
				return p.open(uri, referential, boundsHint, minTimeHint,
						maxTimeHint, bands);
			}
		}
		throw new IOException(String.format(
				"No registered IO provider can open %s.", uri));
	}

	@Override
	public DatasetMetadata queryMetadata(String uri, String referential)
			throws IOException {

		for (DatasetProvider p : providers) {
			if (p.canOpen(uri)) {
				return p.queryMetadata(uri, referential);
			}
		}
		throw new IOException(String.format(
				"No registered IO provider can open %s.", uri));
	}

}
