package org.philhosoft.tests;

import java.util.ArrayList;
import java.util.Arrays;

public class IntListToRanges
{
	// Assumes all numbers are above 0
	public static String[] MakeRanges(int[] numbers)
	{
		ArrayList<String> ranges = new ArrayList<String>();
		
		Arrays.sort(numbers);
		int rangeStart = 0;
		boolean bInRange = false;
		for (int i = 1; i <= numbers.length; i++)
		{
			if (i < numbers.length && numbers[i] - numbers[i - 1] == 1)
			{
				if (!bInRange)
				{
					rangeStart = numbers[i - 1];
					bInRange = true;
				}
			}
			else
			{
				if (bInRange)
				{
					ranges.add(rangeStart + "-" + numbers[i - 1]);
					bInRange = false;
				}
				else
				{
					ranges.add(String.valueOf(numbers[i - 1]));
				}
			}
		}
		return ranges.toArray(new String[ranges.size()]);
	}
	
	public static void ShowRanges(String[] ranges)
	{
		for (String range : ranges)
		{
			System.out.print(range + ","); // Inelegant but quickly coded...
		}
		System.out.println();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int[] an1 = { 1,2,3,5,7,9,10,11,12,14,15,16,22,23,27 };
		int[] an2 = { 1,2 };
		int[] an3 = { 1,3,5,7,8,9,11,12,13,14,15 };
		ShowRanges(MakeRanges(an1));
		ShowRanges(MakeRanges(an2));
		ShowRanges(MakeRanges(an3));
		int L = 100;
		int[] anr = new int[L];
		for (int i = 0, c = 1; i < L; i++)
		{
			int incr = Math.random() > 0.2 ? 1 : (int) Math.random() * 3 + 2;
			c += incr;
			anr[i] = c;
		}
		ShowRanges(MakeRanges(anr));
	}
}
