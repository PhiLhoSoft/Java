/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2005/04/03 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Keyboard handling class.
 *
 * A number of static methods to read data on stdin:
 * raw data (line, string) or processed data (tokens, integers, booleans).
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/04/03
 */
public class Keyboard
{
	private static BufferedReader ms_kin =
			new BufferedReader(new InputStreamReader(System.in));

	private static String readFullLine()
	{
		try
		{
			return ms_kin.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	public static String readString()
	{
		return readFullLine();
	}
	public static String[] readTokens(String delimiters)
	{
		String line = readFullLine();
		if (line != null)
		{
			if (delimiters.startsWith("^"))
			{
				delimiters = "\\" + delimiters;
			}
			if (delimiters.endsWith("$"))
			{
				delimiters += "\\$";	// Not very nice (twice '$') but simple...
			}
			// Successive delimiters are collapsed into one
			return line.split("[" + delimiters + "]+");
		}
		return null;
	}
	/**
	 * Read a line on stdin and convert it to integer, if possible.
	 * @return the read integer
	 * @throws NumberFormatException
	 */
	public static int readInt()
			throws NumberFormatException
	{
		String line = readFullLine();
		if (line != null)
		{
			return Integer.parseInt(line);
		}
		// Transform readFullLine exception
		throw new NumberFormatException();
	}
	public static boolean readBoolean()
			throws NumberFormatException
	{
		String line = readFullLine();
		if (line != null)
		{
			// Should be more permissive than accepting "true":
			// Should accept 1, y, t, yes, true, 0, n, f, no, false
			// and throw an exception on other cases.
			// Actually, should accept localized strings too...
			return Boolean.getBoolean(line);
		}
		// Transform readFullLine exception
		throw new NumberFormatException();
	}
}
