/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2009/04/17 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2009-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.io;

import java.util.regex.*;
import java.io.*;


public class GetFreeDiskSpace
{
	public static long getFreeSpace(String strPath) throws IOException
	{
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows"))
		{
			return getFreeSpaceOnWindows(strPath);
		}
		return 0;
	}

	private static long getFreeSpaceOnWindows(String strPath) throws IOException
	{
		// Build and run the 'dir' command
		String[] straCmdAttribs = new String[4];
		straCmdAttribs[0] = "cmd.exe";   // WinNT/Win2k/WinXP specific, but so is the /-C option of dir
		straCmdAttribs[1] = "/C";
		// Dir with no thousand separators
		straCmdAttribs[2] = "dir/-C";
		straCmdAttribs[3] = strPath;
		Process proc = Runtime.getRuntime().exec(straCmdAttribs);

		// Read the output until we find the last non-empty line
		String prevLine = null;
		String line = null;
		// We will parse the last line of the dir command
		// In French system, we have "              17 Rép(s)     72054841344 octets libres"
		// In English one, probably something like "              17 Dir(s)     72054841344 bytes free"
		Pattern p = Pattern.compile("^\\s*\\d+\\D+(\\d+)");

		long bytes = -1;
		BufferedReader brIn = null;
		try
		{
			brIn = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			do
			{
				line = brIn.readLine();
				if (line == null || line.length() == 0)
					continue;
				prevLine = line;
			} while (line != null);
			if (prevLine != null)
			{
				Matcher m = p.matcher(prevLine);
				if (m.find())
				{
					bytes = Long.parseLong(m.group(1));
				}
			}
		}
		finally
		{
			try
			{
				if (brIn != null)
					brIn.close();
			}
			catch (IOException e) {} // Quietly ignore that...
		}

		if (bytes == -1)
		{
			throw new IOException("Unable to get free disk space for path '" + strPath + "'");
		}
		return bytes;
	}

	// Testing...
	public static void main(String[] args)
	{
		long space = 0;
		try
		{
			space = getFreeSpace("C:\\");
			System.out.println("Free disk space for C: " + space);
			space = GetFreeSpace("D:\\");
			System.out.println("Free disk space for D: " + space);
		}
		catch (IOException e)
		{
			System.err.println("Error getting free disk space: " + e.toString());
		}
	}
}
