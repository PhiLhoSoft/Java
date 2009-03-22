package org.philhosoft.tests;

import java.lang.ProcessBuilder;
import java.io.*;
import java.util.*;

public class ProcessBuilderTest
{
	static String ReadFile(String fileName)
			throws FileNotFoundException, IOException
	{
		File file = new File(fileName);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		StringBuilder sb = new StringBuilder();
		try
		{
			String s;
			do
			{
				s = br.readLine();
				sb.append(s).append("\n");
			} while (s != null);
		}
		finally
		{
			br.close();
		}
		return sb.toString();
	}
	
	static void CaptureRun(Process process)
			throws IOException
	{
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null)
		{
			System.out.println("=> " + line);
		}
	}

	static void RunArgs(String[] args)
			throws IOException
	{
		if (args.length <= 1)
		{
			System.err.println("Need command to run");
			System.exit(-1);
		}
		Process process = new ProcessBuilder(args[0], args[1]).start();
		System.out.printf("RunArgs with %s is:\n",
				Arrays.toString(args));
		CaptureRun(process);
	}

	static void RunStuff(String[] args)
			throws IOException
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
		CaptureRun(process);
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
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
