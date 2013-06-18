package org.vpac.ndg;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UtilsTest extends TestCase {
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testGetAcquisitionTimeStrFromFile() {
		String filename = "../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc";
		Path filePath = FileSystems.getDefault().getPath(filename);
		String actualDatetime = FileUtils.getAcquisitionTimeStr(filePath);
		String expectedDatetime = "20100116";
		assertEquals(expectedDatetime, actualDatetime);;
	}

	@Test
	public void testGetBandNameStrFromFile() {
		String filename = "../../data/small_landsat/LS7_ETM_095_082_20100116_B30.nc";
		Path filePath = FileSystems.getDefault().getPath(filename);
		String actualBandName = FileUtils.getBandNameStr(filePath);
		String expectedBandName = "B30";
		assertEquals(expectedBandName, actualBandName);;
	}

	@Test
	public void testParseDate() {
		String date = "20100124";
		
		Date actualDate = Utils.parseDate(date);
		Calendar actualCalender = Calendar.getInstance();
		actualCalender.setTime(actualDate);
		
		assertEquals(2010, actualCalender.get(Calendar.YEAR));
		// MONTH calendar field. Month value is 0-based. e.g., 0 for January
		assertEquals(0, actualCalender.get(Calendar.MONTH));
		assertEquals(24, actualCalender.get(Calendar.DATE));
	}
	
	@Test
	public void testIsSameDatetime() {
		// Same date, Same time
		Date date1 = Utils.parseDate("2012-05-07 15:20");
		Date date2 = Utils.parseDate("2012-05-07 15:20");
		boolean bResult = Utils.isSameDatetime(date1, date2);		
		assertTrue(bResult);
		
		// Same date, Different time
		date1 = Utils.parseDate("2012-05-07 15:20");
		date2 = Utils.parseDate("2012-05-07 15:19");
		bResult = Utils.isSameDatetime(date1, date2);		
		assertFalse(bResult);
		
		date1 = Utils.parseDate("2012-05-07 15:20:00");
		date2 = Utils.parseDate("2012-05-07 15:20:01");
		bResult = Utils.isSameDatetime(date1, date2);		
		assertFalse(bResult);
		
		// Different date, Same time
		date1 = Utils.parseDate("2012-05-07 15:20");
		date2 = Utils.parseDate("2012-05-06 15:20");
		bResult = Utils.isSameDatetime(date1, date2);		
		assertFalse(bResult);
		
		// Different date, Different time
		date1 = Utils.parseDate("2012-05-07 15:20");
		date2 = Utils.now();
		bResult = Utils.isSameDatetime(date1, date2);		
		assertFalse(bResult);		
	}
}
