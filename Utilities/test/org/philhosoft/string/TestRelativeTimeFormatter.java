package org.philhosoft.string;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;


public class TestRelativeTimeFormatter
{
	private RelativeTimeFormatter relativeTimeFormatter;

	@SuppressWarnings("deprecation")
	// 2014-12-11 10:19:00
	private long now = new Date(2014 - 1900, 12 - 1, 11, 10, 19, 0).getTime();

	@Before
	public void setUp()
	{
		DateTimeI18n i18n = mock(DateTimeI18n.class);
		when(i18n.day_singular()).thenReturn("day");
		when(i18n.day_plural()).thenReturn("days");
		when(i18n.hour_singular()).thenReturn("hour");
		when(i18n.hour_plural()).thenReturn("hours");
		when(i18n.minute_singular()).thenReturn("minute");
		when(i18n.minute_plural()).thenReturn("minutes");
		when(i18n.month_singular()).thenReturn("month");
		when(i18n.month_plural()).thenReturn("months");
		when(i18n.second_singular()).thenReturn("second");
		when(i18n.second_plural()).thenReturn("seconds");
		when(i18n.week_singular()).thenReturn("week");
		when(i18n.week_plural()).thenReturn("weeks");
		when(i18n.year_singular()).thenReturn("year");
		when(i18n.year_plural()).thenReturn("years");
		when(i18n.small_time()).thenReturn("less than 1 second ago");

		when(i18n.relative_time(anyString(), anyString())).thenAnswer(new Answer<String>()
		{
			@Override
			public String answer(InvocationOnMock invocation)
			{
				Object[] args = invocation.getArguments();
				return args[0].toString() + " " + args[1].toString() + " ago";
			}
		});
		when(i18n.yesterday_time(anyString())).thenAnswer(new Answer<String>()
		{
			@Override
			public String answer(InvocationOnMock invocation)
			{
				Object[] args = invocation.getArguments();
				return "Yesterday at " + args[0].toString();
			}
		});
		when(i18n.week_time(anyString(), anyString())).thenAnswer(new Answer<String>()
		{
			@Override
			public String answer(InvocationOnMock invocation)
			{
				Object[] args = invocation.getArguments();
				return args[0].toString() + " at " + args[1].toString();
			}
		});

		DateTimeFormatter dateTimeFormatter = new DumbDateTimeFormat();
		relativeTimeFormatter = new RelativeTimeFormatter(i18n, dateTimeFormatter);
	}

	@Test
	public void testFormatAllRelative()
	{
		assertThat(relativeTimeFormatter.formatAllRelative(500L, now)).isEqualTo("less than 1 second ago");
		assertThat(relativeTimeFormatter.formatAllRelative(1000L, now)).isEqualTo("1 second ago");
		assertThat(relativeTimeFormatter.formatAllRelative(2000L, now)).isEqualTo("2 seconds ago");
		assertThat(relativeTimeFormatter.formatAllRelative(59 * 1000L, now)).isEqualTo("59 seconds ago");
		assertThat(relativeTimeFormatter.formatAllRelative(60 * 1000L, now)).isEqualTo("1 minute ago");
		assertThat(relativeTimeFormatter.formatAllRelative(125 * 1000L, now)).isEqualTo("2 minutes ago");
		assertThat(relativeTimeFormatter.formatAllRelative(59 * 60 * 1000L, now)).isEqualTo("59 minutes ago");
		assertThat(relativeTimeFormatter.formatAllRelative(60 * 60 * 1000L, now)).isEqualTo("1 hour ago");
		assertThat(relativeTimeFormatter.formatAllRelative(125 * 60 * 1000L, now)).isEqualTo("2 hours ago");
		assertThat(relativeTimeFormatter.formatAllRelative(23 * 60 * 60 * 1000L, now)).isEqualTo("23 hours ago");
		assertThat(relativeTimeFormatter.formatAllRelative(24 * 60 * 60 * 1000L, now)).isEqualTo("1 day ago");
		assertThat(relativeTimeFormatter.formatAllRelative(50 * 60 * 60 * 1000L, now)).isEqualTo("2 days ago");
		assertThat(relativeTimeFormatter.formatAllRelative(6 * 24 * 60 * 60 * 1000L, now)).isEqualTo("6 days ago");
		assertThat(relativeTimeFormatter.formatAllRelative(7 * 24 * 60 * 60 * 1000L, now)).isEqualTo("1 week ago");
		assertThat(relativeTimeFormatter.formatAllRelative(15 * 24 * 60 * 60 * 1000L, now)).isEqualTo("2 weeks ago");
		assertThat(relativeTimeFormatter.formatAllRelative(29 * 24 * 60 * 60 * 1000L, now)).isEqualTo("4 weeks ago");
		assertThat(relativeTimeFormatter.formatAllRelative(30 * 24 * 60 * 60 * 1000L, now)).isEqualTo("1 month ago");
		assertThat(relativeTimeFormatter.formatAllRelative(31 * 24 * 60 * 60 * 1000L, now)).isEqualTo("1 month ago");
		assertThat(relativeTimeFormatter.formatAllRelative(65 * 24 * 60 * 60 * 1000L, now)).isEqualTo("2 months ago");
		assertThat(relativeTimeFormatter.formatAllRelative(364 * 24 * 60 * 60 * 1000L, now)).isEqualTo("12 months ago");
		assertThat(relativeTimeFormatter.formatAllRelative(365 * 24 * 60 * 60 * 1000L, now)).isEqualTo("1 year ago");
		assertThat(relativeTimeFormatter.formatAllRelative(500 * 24 * 60 * 60 * 1000L, now)).isEqualTo("1 year ago");
		assertThat(relativeTimeFormatter.formatAllRelative(1000 * 24 * 60 * 60 * 1000L, now)).isEqualTo("2 years ago");
	}

	@Test
	public void testFormatSemiRelative()
	{
		assertThat(relativeTimeFormatter.formatSemiRelative(500L, now)).isEqualTo("less than 1 second ago");
		assertThat(relativeTimeFormatter.formatSemiRelative(1000L, now)).isEqualTo("1 second ago");
		assertThat(relativeTimeFormatter.formatSemiRelative(2000L, now)).isEqualTo("2 seconds ago");
		assertThat(relativeTimeFormatter.formatSemiRelative(59 * 1000L, now)).isEqualTo("59 seconds ago");
		assertThat(relativeTimeFormatter.formatSemiRelative(60 * 1000L, now)).isEqualTo("1 minute ago");
		assertThat(relativeTimeFormatter.formatSemiRelative(125 * 1000L, now)).isEqualTo("2 minutes ago");
		assertThat(relativeTimeFormatter.formatSemiRelative(59 * 60 * 1000L, now)).isEqualTo("59 minutes ago");
		assertThat(relativeTimeFormatter.formatSemiRelative(60 * 60 * 1000L, now)).isEqualTo("1 hour ago");
		assertThat(relativeTimeFormatter.formatSemiRelative(125 * 60 * 1000L, now)).isEqualTo("2 hours ago");
		assertThat(relativeTimeFormatter.formatSemiRelative(23 * 60 * 60 * 1000L, now)).isEqualTo("23 hours ago");
		assertThat(relativeTimeFormatter.formatSemiRelative((24 * 60 * 60 - 1) * 1000L, now)).isEqualTo("23 hours ago");
		assertThat(relativeTimeFormatter.formatSemiRelative((24 * 60 * 60 + 5 * 60) * 1000L, now)).isEqualTo("Yesterday at 10:14");
		assertThat(relativeTimeFormatter.formatSemiRelative((25 * 60 * 60 + 59 * 60) * 1000L, now)).isEqualTo("Yesterday at 08:20");
		assertThat(relativeTimeFormatter.formatSemiRelative((2 * 24 * 60 * 60 - 60) * 1000L, now)).isEqualTo("Yesterday at 10:20"); // Wednesday
		assertThat(relativeTimeFormatter.formatSemiRelative(2 * 24 * 60 * 60 * 1000L, now)).isEqualTo("Tuesday at 10:19");
		assertThat(relativeTimeFormatter.formatSemiRelative(3 * 24 * 60 * 60 * 1000L, now)).isEqualTo("Monday at 10:19");
		assertThat(relativeTimeFormatter.formatSemiRelative(4 * 24 * 60 * 60 * 1000L, now)).isEqualTo("Sunday at 10:19");
		assertThat(relativeTimeFormatter.formatSemiRelative(5 * 24 * 60 * 60 * 1000L, now)).isEqualTo("Saturday at 10:19");
		assertThat(relativeTimeFormatter.formatSemiRelative(6 * 24 * 60 * 60 * 1000L, now)).isEqualTo("Friday at 10:19");
		assertThat(relativeTimeFormatter.formatSemiRelative((7 * 24 * 60 * 60 - 60) * 1000L, now)).isEqualTo("Thursday at 10:20");
		assertThat(relativeTimeFormatter.formatSemiRelative(7 * 24 * 60 * 60 * 1000L, now)).isEqualTo("2014-12-04 10:19");
		assertThat(relativeTimeFormatter.formatSemiRelative((8 * 24 * 60 * 60 - 60) * 1000L, now)).isEqualTo("2014-12-03 10:20");
		assertThat(relativeTimeFormatter.formatSemiRelative(8 * 24 * 60 * 60 * 1000L, now)).isEqualTo("2014-12-03 10:19");
		assertThat(relativeTimeFormatter.formatSemiRelative(365 * 24 * 60 * 60 * 1000L, now)).isEqualTo("2013-12-11 10:19");
	}
}
