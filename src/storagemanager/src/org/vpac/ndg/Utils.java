package org.vpac.ndg;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.vpac.ndg.common.Default;
import org.vpac.ndg.common.StringUtils;

/**
 * Basic utilities for storage manager debug message management
 * 
 * @author glennf
 * @author hsumanto
 */
public class Utils {

	/**
	 * Check whether the given string string matches the given patten.
	 * 
	 * @param toVerifyStr
	 *            The string to check.
	 * @param patternStr
	 *            The pattern to be used to check.
	 * @return Returns true if the given string matches the given pattern.
	 *         Otherwise, false.
	 */
	public static boolean checkPattern(String toVerifyStr, String patternStr) {
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(toVerifyStr);
		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * Get the date now.
	 * 
	 * @return Returns the date now.
	 */
	public static Date now() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		return cal.getTime();
	}

	/**
	 * Check whether the given dates are on the same day and on the same time.
	 * 
	 * @param date1
	 *            The given first date.
	 * @param date2
	 *            The given second date.
	 * @return Returns true if the given dates are on the same day and on the
	 *         same time.
	 */
	public static boolean isSameDatetime(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal2.setTime(date2);

		// a bit of hack only compare hour minute second
		boolean sameHour = cal1.get(Calendar.HOUR_OF_DAY) == cal2
				.get(Calendar.HOUR_OF_DAY);
		boolean sameMinute = cal1.get(Calendar.MINUTE) == cal2
				.get(Calendar.MINUTE);
		boolean sameSecond = cal1.get(Calendar.SECOND) == cal2
				.get(Calendar.SECOND);
		boolean sameTime = sameHour && sameMinute && sameSecond;

		boolean bResult = DateUtils.isSameDay(date1, date2) && sameTime;

		return bResult;
	}

	/**
	 * Tries a list of allowable date formats when parsing the specified date.
	 * 
	 * @param str
	 *            A string representation of the date.
	 * @return The parsed date.
	 * @throws IllegalArgumentException
	 *             if the date can't be parsed.
	 */
	public static Date parseDate(String str) throws IllegalArgumentException {
		Date result = null;

		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter
				.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
		for (String pattern : StringUtils.ALLOWABLE_DATETIME_PATTERNS) {
			try {
				formatter.applyPattern(pattern);
				result = formatter.parse(str);
				if (result != null) {
					break;
				}
			} catch (ParseException e) {
			} catch (IllegalArgumentException e) {
			}
		}

		if (result == null) {
			throw new IllegalArgumentException(String.format(
					"Could not parse date \"%s\".", str));
		}

		return result;
	}

	private static DateFormat DATE_FORMATTER = null;

	/**
	 * @return A date formatter that can be used for internal storage.
	 * @see #parseDate(String)
	 */
	public static DateFormat getTimestampFormatter() {
		if (DATE_FORMATTER == null) {
			DATE_FORMATTER = new SimpleDateFormat(Default.MILLISECOND_PATTERN);
			DATE_FORMATTER.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
		}
		return DATE_FORMATTER;
	}

	private static Pattern TIME_PATTERN_SI = Pattern
			.compile("([0-9]+) *([a-z]*)");

	public static long parseTemporalPrecision(String precision)
			throws IllegalArgumentException {

		Matcher m;
		m = TIME_PATTERN_SI.matcher(precision);
		if (m.matches()) {
			String number = m.group(1);
			String units = m.group(2);
			long n = Long.parseLong(number);

			switch (units) {
			case "":
			case "ms":
			case "millisecond":
			case "milliseconds":
				return n;
			case "s":
			case "second":
			case "seconds":
				return n * DateUtils.MILLIS_PER_SECOND;
			case "m":
			case "minute":
			case "minutes":
				return n * DateUtils.MILLIS_PER_MINUTE;
			case "h":
			case "hour":
			case "hours":
				return n * DateUtils.MILLIS_PER_HOUR;
			case "d":
			case "day":
			case "days":
				return n * DateUtils.MILLIS_PER_DAY;
			case "week":
			case "weeks":
				return n * DateUtils.MILLIS_PER_DAY * 7;
			case "month":
			case "months":
				// http://en.wikipedia.org/wiki/Month#Synodic_month
				return n * 2551442890l;
			case "a":
			case "y":
			case "year":
			case "years":
				// 365.25 days
				return n * DateUtils.MILLIS_PER_SECOND * 31557600;
			}
		}

		throw new IllegalArgumentException(String.format(
				"Could not parse precision \"%s\".", precision));
	}

	/**
	 * Hash code used in hashing int
	 */
	public static int HASH_CODE = 123456789;

	/**
	 * Hash the specified int using implementation proposed in Josh Bloch's
	 * "Effective Java". Refer to
	 * http://stackoverflow.com/questions/113511/hash-code-implementation
	 * 
	 * @param num
	 *            The int to be hashed.
	 * @return Returns the hash code of the int.
	 */
	public static int hashCode(int num) {
		return 37 * HASH_CODE + num;
	}
}