package network.myproptima.synchronisation;


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
	String m_processName;

	public StreamGobbler(InputStream is, String processName)
	{
		m_is = is;
		m_type = "";
		m_processName = processName;
	}

	public StreamGobbler(InputStream is, String type, String processName)
	{
		m_is = is;
		m_type = type;
		m_processName = processName;
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
		String l = m_processName + " " + m_type + "> " + line;
		System.out.println(l);
		return l;
	}
}
