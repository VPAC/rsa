package org.vpac.ndg.query;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.query.coordinates.TimeAxis;

import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.time.CalendarDate;
import ucar.nc2.time.CalendarDateUnit;

public class DatasetUtils {

	final Logger log = LoggerFactory.getLogger(DatasetUtils.class);

	static final Pattern EPOCH_PATTERN = Pattern.compile("(.*) since (.*)");

	/**
	 * Get all the coordinate values from a time axis. The coordinates will be
	 * translated into global time, i.e. milliseconds since the epoch.
	 * @param ds The dataset to process.
	 * @return The coordinate values in milliseconds since the epoch.
	 * @throws IOException If the data could not be read.
	 * @throws QueryConfigurationException If the units are not recognised.
	 */
	public TimeAxis findTimeCoordinates(NetcdfDataset ds) throws IOException {

		Variable var = ds.findVariable("time");
		if (var == null)
			return null;
		if (var.getDimensions().size() != 1) {
			log.debug("Ignoring time variable: is not 1D.");
			return null;
		}

		Attribute attr = var.findAttribute("units");
		if (attr == null)
			return null;

		CalendarDateUnit units;
		try {
			units = CalendarDateUnit.of("proleptic_gregorian",
					attr.getStringValue());
		} catch (IllegalArgumentException e) {
			throw new IOException(String.format(
					"Unrecognised temporal units"), e);
		}

		List<CalendarDate> coordinates = new ArrayList<CalendarDate>();
		Array array = var.read();
		if (var.getDataType().isIntegral()) {
			for (int i = 0; i < array.getSize(); i++) {
				CalendarDate value = units.makeCalendarDate(array.getInt(i));
				coordinates.add(value);
			}
		} else {
			for (int i = 0; i < array.getSize(); i++) {
				CalendarDate value = units.makeCalendarDate(array.getDouble(i));
				coordinates.add(value);
			}
		}

		TimeAxis timeAxis = new TimeAxis();
		timeAxis.setUnits(units);
		timeAxis.setValues(coordinates);
		return timeAxis;
	}

	static final String MILLISECOND_PATTERN = "yyyy-MM-dd'T'HH-mm-ss.SSS"; // 1999-04-28'T'13-45-20.001
	static final String SECOND_PATTERN = "yyyy-MM-dd'T'HH-mm-ss"; // 1999-04-28'T'13-45-20
	static final String MINUTE_PATTERN = SECOND_PATTERN; // 1999-04-28'T'13-45-00
	static final String HOUR_PATTERN = MINUTE_PATTERN; // 1999-04-28'T'13-00-00
	static final String DAY_PATTERN = "yyyy-MM-dd"; // 1999-04-28 (midnight)

	static final DateFormat[] DATE_FORMATS = new DateFormat[] {
		new SimpleDateFormat(MILLISECOND_PATTERN),
		new SimpleDateFormat(SECOND_PATTERN),
		new SimpleDateFormat(MINUTE_PATTERN),
		new SimpleDateFormat(HOUR_PATTERN),
		new SimpleDateFormat(DAY_PATTERN)};

	Date parseDate(String dateStr) throws ParseException {
		for (DateFormat format : DATE_FORMATS) {
			try {
				return format.parse(dateStr);
			} catch (ParseException e) {
				continue;
			}
		}
		throw new ParseException("Could not match date string to any registered format.", 0);
	}
}
