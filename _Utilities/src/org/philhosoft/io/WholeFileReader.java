/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 *
 * Copyright notice: See the PhiLhoSoftLicence.txt file for details.
 * This file is distributed under the zlib/libpng license.
 * Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
 */
/* File history:
 *  1.00.000 -- 2008/11/28 (PL) -- Creation
 */
package org.philhosoft.io;

import java.io.*;

/**
 * Read a file and return it as a StringBuilder.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2008/11/28
 */
public class WholeFileReader
{
	private static final int BUFFER_SIZE = 8192;

	public static StringBuilder ReadFile(String fileName)
			throws IOException
	{
		return ReadFile(fileName, null);
	}

	public static StringBuilder ReadFile(String fileName, String charsetName)
			throws IOException
	{
		// No real need to close the BufferedReader/InputStreamReader
		// as they're only wrapping the stream
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
		try
		{
			Reader reader = new BufferedReader(isr);
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[BUFFER_SIZE];
			int read;
			while ((read = reader.read(buffer, 0, BUFFER_SIZE)) > 0)
			{
				builder.append(buffer, 0, read);
			}
			return builder;
		}
		finally
		{
			stream.close();
		}
	}
}
