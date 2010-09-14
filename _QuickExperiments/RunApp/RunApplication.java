import java.util.*;
import java.io.*;

public class RunApplication
{
	StringBuilder raResult;

	public static void main(String args[])
	{
		RunApplication ra = new RunApplication();
	}

	RunApplication()
	{
		AppDesc trid = new AppDesc("TriD", "TriD File Identifier", false, "C:/PrgCmdLine");
		RunApp ra = new RunApp(trid, trid.GetPath(), "");
		ra.SetProgramArgs(new String[] { "C:/PrgCmdLine/curl-ca-bundle.crt" });
		raResult = new StringBuilder();
		ra.SetOutputGobbler(new StreamGobbler()
		{
			protected String HandleOutputLine(String line)
			{
				raResult.append(line).append("\n");
				return null;
			}
		});

		System.out.println("Running " + trid.GetName());
		ra.run();
		System.out.println("Result:\n" + raResult);
	}
}

