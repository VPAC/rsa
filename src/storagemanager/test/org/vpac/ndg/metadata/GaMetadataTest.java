package org.vpac.ndg.metadata;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vpac.ndg.Utils;

public class GaMetadataTest extends TestCase {

	final private Logger log = LoggerFactory.getLogger(GaMetadataTest.class);

	private GaMetadata md;

	@Override
	public void setUp() {
		File configFile = new File("../../data/metadata/nbar_metadata.xml");
		try {
			md = GaMetadata.fromXML(configFile);
		} catch(RuntimeException e) {
			log.error("Parsing failure", e);
		}	
	}

	public void testDate() {
		// 20100124 01:50:55 in UTC format
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		// MONTH calendar field. Month value is 0-based. e.g., 0 for January
		cal.set(2010, 0, 24, 01, 50, 55);
		Date expectedDate = cal.getTime();
		log.debug("ACQUISITION_END_DT: {}", md.extent.acquisitionEndDt);
		Date actualDate = Utils.parseDate(md.extent.acquisitionEndDt);
		assertTrue(Utils.isSameDatetime(expectedDate, actualDate));
	}
}
