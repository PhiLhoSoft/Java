/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2005/12/16 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests;

//import java.util.*;

/**
 * Try to run a load of threads.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/16
 */
public class ThreadStressTest
{
	static final int MAX_THREADS = 300;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		StressRun[] aSR = new StressRun[MAX_THREADS - 1];
		Thread[] aTh = new Thread[MAX_THREADS - 1];

		for (int i = 0; i < MAX_THREADS - 1; i++)
		{
			aSR[i] = new StressRun(i);
			aTh[i] = new Thread(aSR[i]);
		}
		for (int i = 0; i < MAX_THREADS - 1; i++)
		{
			aTh[i].start();
		}
//		System.out.println("\nThis is the end...");
	}
}

/**
 * A runnable class to show concurrent running
 * of lot of (slow) tasks.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/16
 */
class StressRun implements Runnable
{
	static final int MAX_LOOPS = 500;
	static final int DISPLAY_RATIO = 50;

	String m_output;

	StressRun(int v)
	{
		m_output = ":" + String.valueOf(v);
	}

	public void run()
	{
		for (int i = 0; i < MAX_LOOPS; i++)
		{
			if (i % DISPLAY_RATIO == 0)
			{
				System.out.print(m_output);
				if (Math.random() < 0.1)
				{
					System.out.println("");
				}
			}
			randomWait(200);
		}
	}

	void randomWait(int maxTime)
	{
		try
		{
			Thread.sleep((long)(maxTime * Math.random()));
		} catch (InterruptedException e)
		{
			System.out.println("\nMy sleep was interrupted!");
		}
	}
}
