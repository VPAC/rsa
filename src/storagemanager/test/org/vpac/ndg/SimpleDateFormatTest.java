package org.vpac.ndg;

import java.text.DateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is to test Utils.parseDate capability with
 * supported allowable datetime patterns.
 * @author hsumanto
 *
 */
public class SimpleDateFormatTest  extends TestCase {

	final private Logger log = LoggerFactory.getLogger(SimpleDateFormatTest.class);
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testParseDate() {
		String expected = "2012-03-22T00-00-00.000";

		DateFormat formatter = Utils.getTimestampFormatter();

		String strDate, actual; Date date;

		strDate = "2012-03-22'T'00-00-00.000";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "2012-03-22'T'00-00-00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "2012-03-22";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "Thu, 22 Mar 2012 00:00:00 UTC";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "Thu Mar 22 00:00:00 UTC 2012";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "2012-03-22 00:00:00 UTC";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "2012-03-22 00:00:00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "2012-03-22 00:00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "20120322 00:00:00.000";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "20120322 00:00:00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "20120322 00:00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "20120322000000";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "201203220000";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "20120322";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/03/2012 00:00:00 UTC";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/03/2012 00:00:00 am";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/03/2012 00:00:00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/03/2012 00:00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/03/2012";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22 3 2012 00:00:00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/3/2012 00:00:00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/3/2012";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22 mar 2012 00:00:00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22 march 2012 00:00:00";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22 march 2012";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		/////////////////////////////////////////////////////
		// NEW TEST FOR HOUR MINUTE SECOND AND MILLISECOND //
		/////////////////////////////////////////////////////
		
		expected = "2012-03-22T12-34-56.000";

		strDate = "2012-03-22T12-34-56.000";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "2012-03-22T12-34-56";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "Thu, 22 Mar 2012 12:34:56 UTC";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "Thu Mar 22 12:34:56 UTC 2012";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "2012-03-22 12:34:56 UTC";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "2012-03-22 12:34:56";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "20120322 12:34:56.000";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "20120322 12:34:56";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/03/2012 12:34:56 UTC";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/03/2012 12:34:56 am";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/03/2012 12:34:56";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22 3 2012 12:34:56";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22/3/2012 12:34:56";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22 mar 2012 12:34:56";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);

		strDate = "22 march 2012 12:34:56";
		date = Utils.parseDate(strDate);
		actual = formatter.format(date);
		log.debug(actual);
		assertEquals(expected, actual);
	}
}
