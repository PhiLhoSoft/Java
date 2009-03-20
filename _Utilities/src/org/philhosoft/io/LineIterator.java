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
 *  1.00.000 -- 2005/06/07 (PL) -- Creation
 */
package org.philhosoft.io;

import java.io.*;

/**
 * Read a file and allow to access it line by line.
 *
 * Currently, this is more a template than a generic class,
 * since the line process code is hard-coded.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/06/07
 */
public class LineIterator
{
	private static final int BUF_SIZE = 4096;
	private static final boolean HTML_OUTPUT = true;

	public LineIterator(String path, String file)
	{
		// Open the file.
		FileReader fr = null;
		try
		{
			fr = new FileReader(path + "User-Agent_list.txt");
//			fr = new FileReader(path + "Test.txt");
		}
		catch (IOException ex)
		{
			System.err.println("Can't open the test file!");
			System.exit(1);
		}
		BufferedReader br = new BufferedReader(fr, BUF_SIZE);

		if (HTML_OUTPUT)
		{
			System.out.print("<html><head><title>Feed Markov</title></head>\n");
			System.out.print("<style type='text/css'>\ntd { font: 8px/9px Tahoma; }\n</style>\n");
			System.out.print("</head>\n");
			System.out.print("<body bgcolor='white'>\n");
		}

		// Read the file, line by line.
		String line = null;
		do
		{
			try
			{
				line = br.readLine();
			}
			catch (IOException ex)
			{
				System.err.println("Can't read the test file!");
			}
			if (line != null && line.length() > 3)
			{
				// TODO: Do something with line
			}
		} while (line != null);
		if (HTML_OUTPUT)
		{
			System.out.print("</body></html>\n");
		}

		// Close the file.
		try
		{
			br.close();
			fr.close();
		}
		catch (IOException ex)
		{
			System.err.println("Can't close the test file!");
		}
	}
}
