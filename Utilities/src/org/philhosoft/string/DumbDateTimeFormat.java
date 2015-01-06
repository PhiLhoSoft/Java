package org.philhosoft.string;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Dumb (rigid, no i18n) date / time format.<br>
 * Can be used for unit tests, at least.
 */
public class DumbDateTimeFormat implements DateTimeFormatter
{
	private static final String[] WEEK_DAYS =
	{
		"Sunday",
		"Monday",
		"Tuesday",
		"Wednesday",
		"Thursday",
		"Friday",
		"Saturday"
	};
	private DateFormat dateTimeFormat;
	private DateFormat timeFormat;

	public DumbDateTimeFormat()
	{
		dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
		timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
	}

	@Override
	public String formatDateTime(long timestamp)
	{
		Date date = new Date(timestamp);
		return dateTimeFormat.format(date);
	}

	@Override
	public String formatTime(long timestamp)
	{
		Date date = new Date(timestamp);
		return timeFormat.format(date);
	}

	@Override
	public String getWeekday(long timestamp)
	{
		Date date = new Date(timestamp);
		@SuppressWarnings("deprecation")
		int weekday = date.getDay();
		return WEEK_DAYS[weekday];
	}
}
