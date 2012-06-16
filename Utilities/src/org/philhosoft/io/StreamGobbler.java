/*
 * I/O: a package to do various I/O tasks.
 */
/* File history:
 *  1.00.000 -- 2008/10/31 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.io;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Generic stream gobbler for running a process.
 * Ref.: <a href="http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html">When Runtime.exec() won't</a>
 */
public class StreamGobbler extends Thread
{
	InputStream m_is;
	String m_type;

	public StreamGobbler(InputStream is)
	{
		m_is = is;
		m_type = "";
	}

	public StreamGobbler(InputStream is, String type)
	{
		m_is = is;
		m_type = type;
	}

	@Override
	public void run()
	{
		InputStreamReader isr = new InputStreamReader(m_is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		try
		{
			while ((line = br.readLine()) != null)
			{
				handleOutputLine(line);
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	public String handleOutputLine(String line)
	{
		// Default implementation, to override
		String l = m_type + "> " + line;
		System.out.println(l);
		return l;
	}
}
