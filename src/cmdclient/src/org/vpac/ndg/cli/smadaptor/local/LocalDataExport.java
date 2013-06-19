package org.vpac.ndg.cli.smadaptor.local;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.vpac.ndg.Utils;
import org.vpac.ndg.cli.smadaptor.DataExport;
import org.vpac.ndg.exceptions.TaskException;
import org.vpac.ndg.exceptions.TaskInitialisationException;
import org.vpac.ndg.geometry.Box;
import org.vpac.ndg.geometry.Point;
import org.vpac.ndg.storage.dao.JobProgressDao;
import org.vpac.ndg.task.Exporter;

public class LocalDataExport implements DataExport {

	final Logger log = LoggerFactory.getLogger(LocalDataExport.class);

	private String datasetId;
	private Box extents;
	private Date start;
	private Date end;
	private List<String> bandNamesFilter;
	private Boolean useBilinearInterpolation;
	@Autowired
	JobProgressDao jobProgressDao;
	
	
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
	public String start()
			throws TaskInitialisationException, IOException, TaskException {

		Exporter exporter = new Exporter();
		exporter.setDatasetId(datasetId);
		if (extents != null)
			exporter.setExtents(extents);
		if (start != null)
			exporter.setStart(start);
		if (end != null)
			exporter.setEnd(end);
		if (bandNamesFilter != null)
			exporter.setBandNamesFilter(bandNamesFilter);
		if (useBilinearInterpolation != null)
			exporter.setUseBilinearInterpolation(useBilinearInterpolation);

		exporter.configure();
		String taskId = exporter.getTaskId();

		// Run the task synchronously. See also Application#runInBackground()
		//exporter.call();
		// changed this code to asynchronous because need to display progress
		exporter.runInBackground();
		return taskId;
	}
}
