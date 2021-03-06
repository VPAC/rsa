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
