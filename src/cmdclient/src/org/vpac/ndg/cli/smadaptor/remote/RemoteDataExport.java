package org.vpac.ndg.cli.smadaptor.remote;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.vpac.ndg.Utils;
import org.vpac.ndg.cli.smadaptor.DataExport;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Point;
import org.vpac.web.model.response.ExportResponse;

public class RemoteDataExport implements DataExport {
	
//	public static String DATA_EXPORT_URL = "/SpatialCubeService/Data/Export.xml?datasetId={datasetId}&bandId={bandId}&searchStartDate={searchStartDate}" +
//											"&searchEndDate={searchEndDate&projection={projection}&topLeft.x={topleftX}&topLeft.y={topleftY}&bottomRight.x={bottomRightX}&bottomRight.y={bottomRightY}";
	public static String DATA_EXPORT_URL = "/SpatialCubeService/Data/Export.xml?datasetId={datasetId}";

	final Logger log = LoggerFactory.getLogger(RemoteDataExport.class);

	private String datasetId;
	@SuppressWarnings("unused")
	private Box extents;
	private Date start;
	private Date end;
	@SuppressWarnings("unused")
	private List<String> bandNamesFilter;
	@SuppressWarnings("unused")
	private Boolean useBilinearInterpolation;

	private String baseUri;
	@Autowired
	protected RestTemplate restTemplate;

	public String getBaseUri() {
		return baseUri;
	}

	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}

	@Override
	public void setDatasetId(String id) {
		this.datasetId = id;
	}

	@Override
	public void setExtents(String x1, String y1, String x2, String y2)
			throws NumberFormatException {

		Point<Double> p1;
		Point<Double> p2;
		p1 = new Point<Double>(Double.parseDouble(x1), Double.parseDouble(y1));
		p2 = new Point<Double>(Double.parseDouble(x2), Double.parseDouble(y2));
		extents = new Box(p1, p2);
	}

	@Override
	public void setTimeRange(String start, String end) {
		log.info("Temporal extents: {} / {}", start, end);
		this.start = Utils.parseDate(start);
		this.end = Utils.parseDate(end);
		if (log.isDebugEnabled()) {
			DateFormat formatter = Utils.getTimestampFormatter();
			log.debug("Dates interpreted as {} / {}",
					formatter.format(this.start), formatter.format(this.end));
		}
	}

	@Override
	public void setBandNamesFilter(List<String> bandNamesFilter) {
		this.bandNamesFilter = bandNamesFilter;
	}

	@Override
	public void setUseBilinearInterpolation(Boolean value) {
		useBilinearInterpolation = value;
	}

	@Override
	public String start() throws TaskInitialisationException, TaskException,
			IOException {
		ExportResponse response = restTemplate.postForObject(baseUri + DATA_EXPORT_URL, null, ExportResponse.class, this.datasetId);
		return response.getTaskId();
	}

}
