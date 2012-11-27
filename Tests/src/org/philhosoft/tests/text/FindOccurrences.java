package org.philhosoft.tests.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simplistic demonstration on how to find all occurrences of a regular expression in a string.
 * <br>
 * Refinement: this expression matches sequences of 5 digits exactly, not less (obvious) and not more (less obvious!).
 * I had to use negative look ahead & behind to ensure of the latter condition.
 *
 * @author PhiLho
 */
public class FindOccurrences
{
	private static final String strDesc = "547125: Bug 45610 is related to #78456 and old 45451alpha but not CL151512 at all, nor 144555";

	public static void main(String[] args)
	{
		Pattern p = Pattern.compile("(?<!\\d)(\\d{5})(?!\\d)");
		Matcher m = p.matcher(strDesc);
		while (m.find())
		{
			String f = m.group(1);
			System.out.println("=> " + f);
		}
	}
}
