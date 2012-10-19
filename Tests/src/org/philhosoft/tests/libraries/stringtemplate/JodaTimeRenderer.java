package org.philhosoft.tests.libraries.stringtemplate;

import java.util.Locale;

import org.joda.time.DateTime;
import org.stringtemplate.v4.AttributeRenderer;

/**
 * @author Philippe Lhoste
 */
public class JodaTimeRenderer implements AttributeRenderer
{
	@Override
	public String toString(Object o, String formatString, Locale locale)
	{
        // o will be instanceof JodaTime's DateTime
        if (formatString == null)
        	return o.toString();
        return ((DateTime) o).toString(formatString, locale);
	}
}
