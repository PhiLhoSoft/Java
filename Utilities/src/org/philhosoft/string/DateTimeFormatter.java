package org.philhosoft.string;

public interface DateTimeFormatter
{
	String formatDateTime(long timestamp);

	String formatTime(long timestamp);

	String getWeekday(long timestamp);
}
