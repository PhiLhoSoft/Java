package org.philhosoft.string;

import java.util.concurrent.TimeUnit;

/**
 * Formats time in a relative way: 5 minutes ago or 1 year ago.
 */
public class RelativeTimeFormatter
{
	private static final long[] TIMES =
	{
		// The first two are approximative, but it is not important, as it is "social" time.
		TimeUnit.DAYS.toMillis(365), // Year
		TimeUnit.DAYS.toMillis(30), // Month
		TimeUnit.DAYS.toMillis(7), // Week
		TimeUnit.DAYS.toMillis(1), // Day
		TimeUnit.HOURS.toMillis(1), // Hour
		TimeUnit.MINUTES.toMillis(1), // Minute
		TimeUnit.SECONDS.toMillis(1) // Second
	};
	private String[] timeUnitsSingular = new String[0];
	private String[] timeUnitsPlural = new String[0];

	private DateTimeI18n i18n;
	private DateTimeFormatter formatter;

	public RelativeTimeFormatter(DateTimeI18n i18n, DateTimeFormatter formatter)
	{
		this.i18n = i18n;
		this.formatter = formatter;

		timeUnitsSingular = new String[]
		{
			i18n.year_singular(),
			i18n.month_singular(),
			i18n.week_singular(),
			i18n.day_singular(),
			i18n.hour_singular(),
			i18n.minute_singular(),
			i18n.second_singular(),
		};

		timeUnitsPlural = new String[]
		{
			i18n.year_plural(),
			i18n.month_plural(),
			i18n.week_plural(),
			i18n.day_plural(),
			i18n.hour_plural(),
			i18n.minute_plural(),
			i18n.second_plural(),
		};
	}

	/**
	 * Formats the time in relative units (5 hours ago, 1 year ago).
	 *
	 * @param relativeTime  time relative to the reference
	 * @param referenceTime  absolute reference time (eg. server time, in a client-server architecture where timestamps are saved using server time)
	 * @return the formatted result
	 */
	// The system call can be replaced by a call returning the
	public String formatAllRelative(long relativeTime, long referenceTime)
	{
		String formatted = "";
		for (int i = 0; i < TIMES.length; i++)
		{
			long ratio = relativeTime / TIMES[i];
			if (ratio > 0)
			{
				formatted = i18n.relative_time(Long.toString(ratio), ratio == 1 ? timeUnitsSingular[i] : timeUnitsPlural[i]);
				break;
			}
		}
		if (formatted.isEmpty())
			return i18n.small_time();
		return formatted;
	}

	/**
	 * Formats the time in relative units, absolute after some time.<br>
	 * <ul>
	 * <li>&lt; 24 hours => x time ago (1 second / minute / hour ago)
	 * <li>&lt; 48 hours => Yesterday at 18:12
	 * <li>&lt; 1 week => Thuesday at 09:24
	 * <li>&gt; 1 week => full formatted date
	 * </ul>
	 *
	 * @param relativeTime  time relative to the reference
	 * @param referenceTime  absolute reference time (eg. server time, in a client-server architecture where timestamps are saved using server time)
	 * @return the formatted result
	 */
	public String formatSemiRelative(long relativeTime, long referenceTime)
	{
		long millisInDay = TimeUnit.DAYS.toMillis(1);
		if (relativeTime < millisInDay)
			return formatAllRelative(relativeTime, referenceTime);

		if (relativeTime < 2 * millisInDay)
			return formatForYesterday(relativeTime, referenceTime);

		if (relativeTime < 7 * millisInDay)
			return formatForWeek(relativeTime, referenceTime);

		return formatFullTime(referenceTime - relativeTime);
	}

	/**
	 * Formats the given timestamp in absolute date / time format.
	 *
	 * @param time  the timestamp
	 * @return the formatted result
	 */
	public String formatFullTime(long time)
	{
		return formatter.formatDateTime(time);
	}

	private String formatForYesterday(long relativeTime, long referenceTime)
	{
		long timestamp = referenceTime - relativeTime;
		String formattedHour = formatter.formatTime(timestamp);
		return i18n.yesterday_time(formattedHour);
	}

	private String formatForWeek(long relativeTime, long referenceTime)
	{
		long timestamp = referenceTime - relativeTime;
		String weekday = formatter.getWeekday(timestamp);
		String formattedHour = formatter.formatTime(timestamp);
		return i18n.week_time(weekday, formattedHour);
	}
}
