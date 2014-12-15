/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/*
 Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 Copyright notice: For details, see the following file:
 http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
 This program is distributed under the zlib/libpng license.
 Copyright (c) 2005-2014 Philippe Lhoste / PhiLhoSoft
 */
package org.philhosoft.string;



/**
 * Static methods to parse a string to its numerical value without raising exception. If the string isn't well formed,
 * return a default value instead. This is used, for example, to convert values read in a file (properties, etc.).
 *
 * Only a few primitive types are supported, I might add more as the need may appear (trivial but boring...).
 *
 * @author Philippe Lhoste
 */
public class StringConverter
{
	private StringConverter()
	{
	}

	/**
	 * Parse a string to its numerical int value.
	 *
	 * @return The numeric value of the string if well formed,
	 * @return the given default value otherwise.
	 */
	public static int toInt(String numericString, int defaultValue)
	{
		int val = defaultValue;
		if (numericString != null)
		{
			try
			{
				val = Integer.parseInt(numericString);
			}
			catch (NumberFormatException e)
			{
				// Do nothing, already took default value
			}
		}

		return val;
	}

	/**
	 * Parse a string to its numerical int value.
	 *
	 * @return The numeric value of the string if well formed, 0 otherwise.
	 */
	public static int toInt(String numericString)
	{
		return toInt(numericString, 0);
	}

	/**
	 * Parse a string to its numerical float value.
	 *
	 * @return The numeric value of the string if well formed,
	 * @return the given default value otherwise.
	 */
	public static float toFloat(String numericString, float defaultValue)
	{
		float val = defaultValue;
		if (numericString != null)
		{
			try
			{
				val = Float.parseFloat(numericString);
			}
			catch (NumberFormatException e)
			{
				// Do nothing, already took default value
			}
		}

		return val;
	}

	/**
	 * Parse a string to its numerical float value.
	 *
	 * @return The numeric value of the string if well formed, 0 otherwise.
	 */
	public static float toFloat(String numericString)
	{
		return toFloat(numericString, 0.0f);
	}

	/**
	 * Parse a string to its boolean value. It accepts 0/1, but also English words (true/false, yes/no) and their
	 * abbreviations (t/f, y/n), case insensitive. It doesn't accept localized strings (eg. vrai/faux, oui/non).
	 *
	 * @return The boolean value of the string if well formed,
	 * @return the given default value otherwise.
	 */
	public static boolean toBoolean(String booleanString, boolean defaultValue)
	{
		boolean val = defaultValue;
		if (booleanString != null)
		{
			if (booleanString.equalsIgnoreCase("true") ||
					booleanString.equalsIgnoreCase("t") ||
					booleanString.equalsIgnoreCase("yes") ||
					booleanString.equalsIgnoreCase("y") ||
					booleanString.equals("1"))
			{
				val = true;
			}
			else if (booleanString.equalsIgnoreCase("false") ||
					booleanString.equalsIgnoreCase("f") ||
					booleanString.equalsIgnoreCase("no") ||
					booleanString.equalsIgnoreCase("n") ||
					booleanString.equals("0"))
			{
				val = false;
			}
		}

		return val;
	}
}
