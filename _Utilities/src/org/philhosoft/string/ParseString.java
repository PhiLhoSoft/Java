/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 *
 * Copyright notice: See the PhiLhoSoftLicence.txt file for details.
 * This file is distributed under the zlib/libpng license.
 * Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
 */
/* File history:
 *  1.00.000 -- 2005/12/30 (PL) -- Creation
 */
package org.philhosoft.string;


/**
 * Static methods to parse a string to its numerical value
 * without raising exception.
 * If the string isn't well formed, return a default value instead.
 * This is used, for example, to convert values read in a file (properties, etc.).
 *
 * Only a few primitive types are supported, I might add more
 * as the need may appear (trivial but boring...).
 * byte, short, int, long, float, double, boolean
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/30
 */
public class ParseString
{
	/**
	 * Parse a string to its numerical int value.
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
	 * @return The numeric value of the string if well formed, 0 otherwise.
	 */
	public static int toInt(String numericString)
	{
		return toInt(numericString, 0);
	}


	/**
	 * Parse a string to its numerical float value.
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
	 * @return The numeric value of the string if well formed, 0 otherwise.
	 */
	public static float toFloat(String numericString)
	{
		return toFloat(numericString, 0.0f);
	}


	/**
	 * Parse a string to its boolean value.
	 * It accepts 0/1, but also English words (true/false, yes/no)
	 * and their abbreviations (t/f, y/n), case insensitive.
	 * It doesn't accept localized strings (eg. vrai/faux, oui/non).
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

	// Test - I could/should use asserts...
	public static void main(String[] args)
	{
		System.out.println("! " + toInt("0", 666));
		System.out.println("! " + toInt("-1", 666));
		System.out.println("! " + toInt("33333", 666));
		System.out.println("! " + toInt("-33333", 666));
		System.out.println("? " + toInt("20KB", 666));
		System.out.println("? " + toInt("7.5", 666));
		System.out.println("? " + toInt("", 666));
		System.out.println("? " + toInt(null, 666));

		System.out.println("! " + toFloat("7.5", 3.14159f));
		System.out.println("! " + toFloat("-7.5", 3.14159f));
		System.out.println("! " + toFloat("7.5e2", 3.14159f));
		System.out.println("! " + toFloat("-7.5E2", 3.14159f));
		System.out.println("! " + toFloat("7.5E-2", 3.14159f));
		System.out.println("! " + toFloat("-7.5e-2", 3.14159f));
		System.out.println("! " + toFloat("0", 3.14159f));
		System.out.println("! " + toFloat("-1", 3.14159f));
		System.out.println("! " + toFloat("33333", 3.14159f));
		System.out.println("! " + toFloat("-33333", 3.14159f));
		System.out.println("? " + toFloat("20KB", 3.14159f));
		System.out.println("? " + toFloat("", 3.14159f));
		System.out.println("? " + toFloat(null, 3.14159f));

		System.out.println("! " + toBoolean("true", false));
		System.out.println("! " + toBoolean("T", false));
		System.out.println("! " + toBoolean("yEs", false));
		System.out.println("! " + toBoolean("Y", false));
		System.out.println("! " + toBoolean("1", false));
		System.out.println("? " + toBoolean("7", false));
		System.out.println("? " + toBoolean("oui", false));
		System.out.println("! " + toBoolean("false", true));
		System.out.println("! " + toBoolean("F", true));
		System.out.println("! " + toBoolean("nO", true));
		System.out.println("! " + toBoolean("N", true));
		System.out.println("! " + toBoolean("0", true));
		System.out.println("? " + toBoolean("-1", true));
		System.out.println("? " + toBoolean("non", true));
	}
}
