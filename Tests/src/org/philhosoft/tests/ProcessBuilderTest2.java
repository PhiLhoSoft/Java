package org.philhosoft.tests;


import java.lang.ProcessBuilder;
import java.io.*;
import java.util.*;


import org.philhosoft.io.StreamGobbler;


public class ProcessBuilderTest2
{
	/**
	 * Launches stream gobblers for stderr and stdout of the given process
	 * in separate threads to avoid buffer overflowing.
	 *
	 * @param process
	 * @return error code returned by the process when it ends
	 * @throws IOException
	 * @throws InterruptedException
	 */
	static int CaptureProcessOutput(Process process)
			throws IOException, InterruptedException
	{
        // Capture error messages
		StreamGobbler errorGobbler = new
				StreamGobbler(process.getErrorStream(), "ERR");

        // Capture output
        StreamGobbler outputGobbler = new
				StreamGobbler(process.getInputStream(), "OUT");

		// Start output gobbler threads
		errorGobbler.start();
		outputGobbler.start();

		// Get exit value
		return process.waitFor();
	}

	static void RunArgs(String[] args)
			throws IOException, InterruptedException
	{
		if (args.length <= 1)
		{
			System.err.println("Need command to run");
			System.exit(-1);
		}
		Process process = new ProcessBuilder(args[0], args[1]).start();
		System.out.printf("RunArgs with %s is:\n",
				Arrays.toString(args));
		System.out.println("RunArgs ends with code: " + CaptureProcessOutput(process));
	}

	static void RunStuff(String[] args)
			throws IOException, InterruptedException
	{
		if (args.length <= 1)
		{
			System.err.println("Need command to run");
			System.exit(-1);
		}
		ProcessBuilder processBuilder = new ProcessBuilder(args[0], args[1]);
		Map<String, String> env = processBuilder.environment();
		// Manipulate env
		env.put("Server.Home", "some path");
		env.remove("Temp");
		processBuilder.directory(new File("E:/temp"));
		Process process = processBuilder.start();
		System.out.printf("RunStuff with %s is:\n",
				Arrays.toString(args));
		System.out.println("RunStuff ends with code: " + CaptureProcessOutput(process));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
//~ 		String fileContent = ReadFile("E:/Documents/Perso/DA/Deviations.txt");
		try
		{
			RunArgs(args);
			RunStuff(args);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}
}
