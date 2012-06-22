package org.philhosoft.tests;

import java.io.*;
import java.util.StringTokenizer;

public class Tokenize
{
	static int UseStringTokenizer(String bigString)
	{
		int tokenNb = 0;
		StringTokenizer st = new StringTokenizer(bigString);
		while (st.hasMoreTokens())
		{
			String s = st.nextToken();
			System.out.println("Tokenizer, next token: " + s);
			tokenNb++;
		}
		return tokenNb;
	}

	static int UseSplit(String bigString)
	{
		int tokenNb = 0;
		String[] tokens = bigString.split("\\s");
		for (int i = 0; i < tokens.length; i++)
		{
			String s = tokens[i];
			System.out.println("Split, next token: " + s);
			tokenNb++;
		}
		return tokenNb;
	}

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

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		String fileContent = null;
		try
		{
			fileContent = ReadFile("C:/Personnel/PhiLhoSoft/Java/JavaTricks.txt");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		int n1 = UseStringTokenizer(fileContent);
		int n2 = UseSplit(fileContent);
		System.out.println(n1 + " " + n2);
	}
}
