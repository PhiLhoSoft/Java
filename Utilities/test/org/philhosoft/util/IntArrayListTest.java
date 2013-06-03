package org.philhosoft.util;


import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.testng.annotations.*;


// http://www.mkyong.com/unittest/testng-tutorial-6-parameterized-test/
public class IntArrayListTest
{
	private Random rnd = new Random();

	@DataProvider(name="Data-Generator")
	public static Object[][] generateData()
	{
		return new Object[][]
		{
			{ 0 },
			{ 1 },
			{ 2 },
			{ 3 },
			{ 4 },
			{ 7 },
			{ 11 },
			{ 12 },
			{ 111 },
			{ 127 },
			{ 128 },
			{ 129 },
			{ 12345 },
			{ 36000 },
			// Long tests, can be used to see performance
			{ 500000 },
//			{ 6000000 }, // Need a big chunk of memory to run testSort2 with that
		};
	}

	@Test(dataProvider="Data-Generator")
	public void testSort(int size)
	{
		int[] testArray = makeIntArray(size, false);
		int[] reverseArray = makeIntArray(size, true);
		int[] randomizedArray = arrayShuffle(Arrays.copyOf(testArray, size));

		IntArrayList ial = new IntArrayList(Arrays.copyOf(randomizedArray, size));
		ial.sort(true);
		assertTrue(Arrays.equals(ial.getArray(), testArray));

		ial.sort(false);
		assertTrue(Arrays.equals(ial.getArray(), reverseArray));

		ial.reverse();
		assertTrue(Arrays.equals(ial.getArray(), testArray));

		ial.clear();
		ial.addAll(Arrays.copyOf(randomizedArray, size));
		IntComparator icComparator = new IntComparator()
		{
			@Override
			public int compare(int i1, int i2)
			{
				if (i1 < i2) return -1;
				if (i1 > i2) return 1;
				return 0;
			}
		};
		ial.sort(icComparator);
		assertTrue(Arrays.equals(ial.getArray(), testArray));
	}

	@Test(dataProvider="Data-Generator")
	public void testSort2(int size)
	{
		final String[] stringArray = new String[size];
		for (int i = 0; i < size; i++)
		{
			stringArray[i] = makeRandomString();
		}
		int[] testArray = makeIntArray(size, false);

		IntArrayList ial = new IntArrayList(Arrays.copyOf(testArray, size));
		IntComparator comparator = new IntComparator()
		{
			@Override
			public int compare(int i1, int i2)
			{
				String s1 = stringArray[i1];
				String s2 = stringArray[i2];
				if (s1 == null)
				{
					if (s2 == null)
					return 0;
					return -1;
				}
				if (s2 == null)
				return 1;
				return s1.compareTo(s2);
			}
		};
		ial.sort(comparator);

		for (int i = 1; i < size; i++)
		{
			String prev = stringArray[ial.get(i - 1)];
			String curr = stringArray[ial.get(i)];
			assertTrue(prev.compareTo(curr) <= 0, prev + " > " + curr);
		}
	}

	@Test(dataProvider="Data-Generator")
	public void testAddRemove(int size)
	{
		IntArrayList ial = new IntArrayList();
		for (int i = 0; i < size; i++)
		{
			ial.add(i);
		}
		assertEquals(ial.getSize(), size);
		if (size < 4) return;

		int sample = size / 2;
		assertEquals(ial.get(sample), sample);
		ial.insertAt(0, 42);
		ial.insertAt(sample, 4242);
		ial.add(424242);
		ial.removeAt(sample - 1);
		ial.removeAt(ial.getSize() - 2);
		assertEquals(ial.get(0), 42);
		assertEquals(ial.get(sample - 1), 4242);
		ial.removeAt(0);
		assertEquals(ial.get(sample - 2), 4242);
		assertEquals(ial.get(ial.getSize() - 1), 424242);
	}

	@Test(dataProvider="Data-Generator")
	public void testAddAt(int size)
	{
		if (size > 100000) return;

		IntArrayList ia11 = new IntArrayList();
		for (int i = 0; i < size; i++)
		{
			ia11.addAt(0, i);
		}
		assertEquals(ia11.getSize(), size);

		IntArrayList ia2 = new IntArrayList(0);
		for (int i = 0; i < size; i++)
		{
			ia2.addAt(0, i);
		}
		assertEquals(ia2.getSize(), size);
	}

	@Test(dataProvider="Data-Generator")
	public void testAdd(int size)
	{
		if (size > 100000) return;

		IntArrayList ial = new IntArrayList();
		for (int i = 0; i < size; i++)
		{
			ial.add(i);
			ial.addAt(0, i);
			int s = ial.getSize();
			assertEquals(s, 2 * (i + 1));
			assertEquals(ial.get(0), i);
			assertEquals(ial.get(s - 1), i);
		}
		while (ial.getSize() >= 3)
		{
			int s = ial.getSize();
			int m = s / 2;
			ial.removeAt(s - 1);
			ial.removeAt(m);
			ial.removeAt(0);
			int ns = ial.getSize();
			assertEquals(s, ns + 3);
		}
	}

	@Test(dataProvider="Data-Generator")
	public void testIterator(int size)
	{
		if (size > 20) return; // No need to test all the values

		int[] testArray = makeIntArray(size, false);
		IntArrayList ial = new IntArrayList(testArray);
		testArray = makeIntArray(size / 2, true);
		ial.addAll(testArray);

		IntArrayList.Iterator it = ial.getIterator();
		while (it.hasNext())
		{
			int val = it.getNext();
			if (val == 1)
			{
				it.remove();
			}
			else if (val == 3)
			{
				it.add(42);
			}
			else if (val == 5)
			{
				it.set(4242);
			}
		}
		int[] results = ial.getArray();
		int[] expected = null;
		switch (size)
		{
		case 0:
			expected = new int[0];
			break;
		case 1:
			expected = new int[] { 0 };
			break;
		case 2:
			expected = new int[] { 0, 0 };
			break;
		case 3:
			expected = new int[] { 0, 2, 0 };
			break;
		case 4:
			expected = new int[] { 0, 2, 3, 42, 0 };
			break;
		case 7:
			expected = new int[] { 0, 2, 3, 42, 4, 4242, 6, 2, 0 };
			break;
		case 11:
			expected = new int[] { 0, 2, 3, 42, 4, 4242, 6, 7, 8, 9, 10, 4, 3, 42, 2, 0 };
			break;
		case 12:
			expected = new int[] { 0, 2, 3, 42, 4, 4242, 6, 7, 8, 9, 10, 11, 4242, 4, 3, 42, 2, 0 };
			break;
		}
		if (expected != null)
		{
			assertTrue(Arrays.equals(expected, results), showArgs(size, expected, results));
		}
	}

	private String showArgs(int s, int[] a1, int[] a2)
	{
		return "For " + s + ", " + Arrays.toString(a1) + " vs. " + Arrays.toString(a2);
	}

	private int[] makeIntArray(int nSize, boolean bReverse)
	{
		int[] na = new int[nSize];
		for (int i = 0; i < nSize; i++)
		{
			na[i] = bReverse ? nSize - 1 - i : i;
		}
		return na;
	}

	private int[] arrayShuffle(int[] na)
	{
		// http://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
		for (int i = na.length - 1; i > 0; i--)
		{
			int pos = rnd.nextInt(i + 1);
			int v = na[i];
			na[i] = na[pos];
			na[pos] = v;
		}
		return na;
	}

	private String makeRandomString()
	{
		char[] text = new char[7];
		for (int i = 0; i < text.length; i++)
		{
			text[i] = (char) (97 + rnd.nextInt(26));
		}
		return new String(text);
	}
}
