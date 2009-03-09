/*
 * I/O: a package to do various I/O tasks.
 *
 * Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 *
 * Copyright notice: See the PhiLhoSoftLicence.txt file for details.
 * This file is distributed under the zlib/libpng license.
 * Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
 */
/* File history:
 *  1.00.000 -- 2008/10/31 (PL) -- Creation
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

	public void run()
	{
		InputStreamReader isr = new InputStreamReader(m_is);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		try
		{
			while ((line = br.readLine()) != null)
			{
				HandleOutputLine(line);
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}

	public String HandleOutputLine(String line)
	{
		// Default implementation, to override
		String l = m_type + "> " + line;
		System.out.println(l);
		return l;
	}
}
