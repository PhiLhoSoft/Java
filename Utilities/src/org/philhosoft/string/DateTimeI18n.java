package org.philhosoft.string;

public interface DateTimeI18n
{
	String year_singular();
	String year_plural();

	String month_singular();
	String month_plural();

	String week_singular();
	String week_plural();

	String day_singular();
	String day_plural();

	String hour_singular();
	String hour_plural();

	String minute_singular();
	String minute_plural();

	String second_singular();
	String second_plural();

	String small_time();

	String relative_time(String value, String timeUnit);

	String yesterday_time(String formattedHour);

	String week_time(String weekday, String formattedHour);
}
