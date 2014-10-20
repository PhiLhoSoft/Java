/*
 * Created: 09 may 2005
 *
 * Test browser sniffer algorithm
 * by parsing the whole list of User-Agent strings.
 */
package org.philhosoft.experiments;

import java.io.*;

import org.philhosoft.string.BrowserSniffer;
import org.philhosoft.string.HTMLEncoder;

/**
 * @author lhoste
 *
 * Foo!
 */
public class TestBrowserSniffer
{
	private static final int BUF_SIZE = 4096;
	private static final boolean HTML_OUTPUT = true;
	private static final boolean FULL_OUTPUT = false;

	public static void main(String[] args)
	{
		// Open the file.
		String path = "/usr/local/lhoste/EclipseWorkspace/Tests/src/org/philhosoft/tests/";
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

		String bt = "";
		BrowserSniffer bs;
		if (HTML_OUTPUT)
		{
			System.out.print("<html><head><title>User Agents</title></head>\n");
			System.out.print("<style type='text/css'>\ntd { font: 8px/9px Tahoma; }\n</style>\n");
			System.out.print("</head>\n");
			System.out.print("<body bgcolor='white'><table border='1'>\n");
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
				bs = new BrowserSniffer(line);
				if (!FULL_OUTPUT)
				{
					if (bt.equals(bs.getBrowserType()))
					{
						continue;	// Almost identical, skip it
					}
					bt = bs.getBrowserType();
				}
				if (HTML_OUTPUT)
				{
					System.out.print("<tr><td>" +
							HTMLEncoder.encode(bs.getBrowserType()) +
							" (" + bs.getVersionMajor() + "." + bs.getVersionMinor() + ")</td><td>" +
							bs.getBrowserEngine() + "</td><td>" + bs.getOS() + " (" +
							HTMLEncoder.encode(bs.getOSFullName()) + ")</td>\n" +
							"<td>" + HTMLEncoder.encode(line) + "</td></tr>\n");
				}
				else
				{
					System.out.println(">>> " + line);
					System.out.println("=== " +
							bs.getBrowserType() + " (" + bs.getVersionMajor() + "." + bs.getVersionMinor() + "), " +
							bs.getBrowserEngine() + ", " + bs.getOS() + " (" + bs.getOSFullName() + ")");
				}
			}
		} while (line != null);
		if (HTML_OUTPUT)
		{
			System.out.print("</table></body></html>\n");
		}

		// Close the file.
		try
		{
			br.close();
		}
		catch (IOException ex)
		{
			System.err.println("Can't close the test file!");
		}
	}
}
