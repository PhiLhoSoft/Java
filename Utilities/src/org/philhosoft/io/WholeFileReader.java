/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2008/11/28 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.io;

import java.io.*;

/**
 * Reads a file and returns it as a StringBuilder.
 */
public class WholeFileReader
{
	private static final int BUFFER_SIZE = 8192;

	public static StringBuilder read(String fileName)
			throws IOException
	{
		return read(fileName, null);
	}

	public static StringBuilder read(String fileName, String charsetName)
			throws IOException
	{
		StringBuilder builder = new StringBuilder();

		FileInputStream stream = null;
		InputStreamReader reader = null;
		BufferedReader buffering = null;
		try
		{
			stream = new FileInputStream(fileName);
			if (charsetName == null)
			{
				reader = new InputStreamReader(stream);
			}
			else
			{
				reader = new InputStreamReader(stream, charsetName);
			}

			buffering = new BufferedReader(reader);
			char[] buffer = new char[BUFFER_SIZE];
			int read;
			while ((read = buffering.read(buffer, 0, BUFFER_SIZE)) > 0)
			{
				builder.append(buffer, 0, read);
			}
		}
		finally
		{
			if (buffering != null)
			{
				try
				{
					buffering.close();
					// These are closed by their wrapper
					reader = null;
					stream = null;
				}
				catch (IOException e) {} // Should log the problem...
			}
			if (reader != null) // buffering failed to close?
			{
				try
				{
					reader.close();
					stream = null;
				}
				catch (IOException e) {} // Should log the problem...
			}
			if (stream != null) // buffering and reader failed to close?
			{
				try
				{
					stream.close();
				}
				catch (IOException e) {} // Should log the problem...
			}
		}
		return builder;
	}
}
