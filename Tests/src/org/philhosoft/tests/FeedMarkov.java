/*
 * Created: 09 may 2005
 *
 * Read a file describing a Markov graph and build the corresponding data structure.
 */
package org.philhosoft.tests;

import java.io.*;

/**
 * @author lhoste
 *
 * Foo!
 */
public class FeedMarkov
{
	private static final int BUF_SIZE = 4096;
	private static final boolean HTML_OUTPUT = true;

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
		}
		catch (IOException ex)
		{
			System.err.println("Can't close the test file!");
		}
	}
}
