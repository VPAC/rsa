package org.vpac.ndg.datamodel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.vpac.ndg.ApplicationContextProvider;
import org.vpac.ndg.common.datamodel.CellSize;
import org.vpac.ndg.storage.dao.DatasetDao;
import org.vpac.ndg.storage.model.Dataset;

import ucar.nc2.dataset.NetcdfDataset;

public class RsaDatasetReader {

	// Dataset identifier looks like "rsa:name/resolution"
	private static final Pattern DATASET_PATTERN = Pattern.compile("^([^/]+)/(\\w+)");
	// TODO: Allow datasets to be selected by ID too.

	final Logger log = LoggerFactory.getLogger(RsaDatasetReader.class);

	RsaAggregationFactory factory;
	AggregationOpener opener;

	DatasetDao datasetDao;

	public RsaDatasetReader() {
		factory = new RsaAggregationFactory();
		opener = new AggregationOpener();
		ApplicationContext appContext = ApplicationContextProvider.getApplicationContext();
		datasetDao = (DatasetDao) appContext.getBean("datasetDao");
	}

	public boolean canOpen(String uri) {
		// Just check whether the URI makes sense for this provider - not
		// whether the dataset exists.
		URI parsedUri;
		try {
			parsedUri = new URI(uri);
		} catch (URISyntaxException e) {
			return false;
		}

		String scheme = parsedUri.getScheme();
		if (scheme == null)
			return false;
		if (!scheme.equals("rsa"))
			return false;

		return true;
	}

	public NetcdfDataset open(String uri) throws IOException {
		Dataset dataset = findDataset(uri);
		log.trace("Opening {}", dataset);
		AggregationDefinition def = factory.create(dataset);
		return opener.open(def, uri);
	}

	protected Dataset findDataset(String uri)
			throws IOException {

		URI parsedUri;
		try {
			parsedUri = new URI(uri);
		} catch (URISyntaxException e) {
			throw new IOException("Could not open dataset", e);
		}

		String path = parsedUri.getSchemeSpecificPart();
		Matcher matcher = DATASET_PATTERN.matcher(path);
		if (!matcher.matches()) {
			throw new FileNotFoundException(
					String.format("Invalid dataset name %s", path));
		}
		String name = matcher.group(1);
		CellSize res = CellSize.fromHumanString(matcher.group(2));

		return datasetDao.findDatasetByName(name, res);
	}
}
