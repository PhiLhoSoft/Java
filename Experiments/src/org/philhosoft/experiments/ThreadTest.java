/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2005/12/09 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.experiments;

//import java.util.*;

/**
 * A simple, classical application to test concurrent run of threads.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/09
 */
public class ThreadTest
{
	static final String sentence1 = "Everybody got something to hide except me and my monkey.";
	static final String sentence2 = "Show me the way to the next whisky bar. Oh don't ask why, oh don't ask why.";

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		RunnableClass rc1 = new RunnableClass(sentence1);
		RunnableClass rc2 = new RunnableClass(sentence2);
		Thread t1 = new Thread(rc1);
		Thread t2 = new Thread(rc2);

		t1.start(); t2.start();

		boolean bT1lives = true;
		boolean bT2lives = true;
		while (bT1lives || bT2lives)
		{
			if (bT1lives && !t1.isAlive())
			{
				bT1lives = false;
				System.out.println("\nArgh! T1 is dead!");
			}
			if (bT2lives && !t2.isAlive())
			{
				bT2lives = false;
				System.out.println("\nArgh! T2 is dead!");
			}
		}
		System.out.println("\nThis is the end...");
	}
}

/**
 * A runnable class to show concurrent running
 * of two (slow) tasks.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/09
 */
class RunnableClass implements Runnable
{
	String m_sentence;
	String[] m_aWords;
	int m_wordNb;

	RunnableClass(String sentence)
	{
		m_sentence = sentence;
		m_aWords = sentence.split(" ");
		m_wordNb = m_aWords.length;
	}

	@Override
	public void run()
	{
		for (int i = 0; i < m_wordNb; i++)
		{
			System.out.print(m_aWords[i]);
			if (i < m_wordNb - 1)
			{
				System.out.print(" ");
			}
			randomWait(2000);
		}
	}

	void randomWait(int maxTime)
	{
		try
		{
			Thread.sleep((long)(maxTime * Math.random()));
		}
		catch (InterruptedException e)
		{
			System.out.println("\nMy sleep was interrupted!");
		}
	}
}
