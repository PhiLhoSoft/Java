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

	public static StringBuilder readFile(String fileName)
			throws IOException
	{
		return readFile(fileName, null);
	}

	public static StringBuilder readFile(String fileName, String charsetName)
			throws IOException
	{
		// No real need to close the BufferedReader/InputStreamReader
		// as they're only wrapping the stream: they are closed automatically by closing the stream.
		FileInputStream stream = new FileInputStream(fileName);
		InputStreamReader isr = null;
		if (charsetName == null)
		{
			isr = new InputStreamReader(stream);
		}
		else
		{
			isr = new InputStreamReader(stream, charsetName);
		}
		StringBuilder builder = new StringBuilder();
		try
		{
			Reader reader = new BufferedReader(isr);
			char[] buffer = new char[BUFFER_SIZE];
			int read;
			while ((read = reader.read(buffer, 0, BUFFER_SIZE)) > 0)
			{
				builder.append(buffer, 0, read);
			}
		}
		finally
		{
			stream.close();
		}
		return builder;
	}
}
