package org.philhosoft.util;


import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.Random;

import org.testng.annotations.*;


// http://www.mkyong.com/unittest/testng-tutorial-6-parameterized-test/
public class IntArrayListTest
{
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
			{ 6000000 },
		};
	}

	@Test(dataProvider="Data-Generator")
	public void testSort(int size)
	{
		int[] naTest = MakeIntArray(size, false);
		int[] naReverse = MakeIntArray(size, true);
		int[] naRandomized = ArrayShuffle(Arrays.copyOf(naTest, size));

		IntArrayList dia = new IntArrayList(Arrays.copyOf(naRandomized, size));
		dia.sort(true);
		assertTrue(Arrays.equals(dia.getArray(), naTest));

		dia.sort(false);
		assertTrue(Arrays.equals(dia.getArray(), naReverse));

		dia.reverse();
		assertTrue(Arrays.equals(dia.getArray(), naTest));

		dia.clear();
		dia.addAll(Arrays.copyOf(naRandomized, size));
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
		dia.sort(icComparator);
		assertTrue(Arrays.equals(dia.getArray(), naTest));
	}

	@Test(dataProvider="Data-Generator")
	public void testAddRemove(int size)
	{
		IntArrayList dia = new IntArrayList();
		for (int i = 0; i < size; i++)
		{
			dia.add(i);
		}
		assertEquals(dia.getSize(), size);
		if (size < 4) return;

		int nSample = size / 2;
		assertEquals(dia.get(nSample), nSample);
		dia.insertAt(0, 42);
		dia.insertAt(nSample, 4242);
		dia.add(424242);
		dia.removeAt(nSample - 1);
		dia.removeAt(dia.getSize() - 2);
		assertEquals(dia.get(0), 42);
		assertEquals(dia.get(nSample - 1), 4242);
		dia.removeAt(0);
		assertEquals(dia.get(nSample - 2), 4242);
		assertEquals(dia.get(dia.getSize() - 1), 424242);
	}

	@Test(dataProvider="Data-Generator")
	public void testAddAt(int size)
	{
		if (size > 100000) return;

		IntArrayList dia1 = new IntArrayList();
		for (int i = 0; i < size; i++)
		{
			dia1.addAt(0, i);
		}
		assertEquals(dia1.getSize(), size);

		IntArrayList dia2 = new IntArrayList(0);
		for (int i = 0; i < size; i++)
		{
			dia2.addAt(0, i);
		}
		assertEquals(dia2.getSize(), size);
	}

	@Test(dataProvider="Data-Generator")
	public void testAdd(int size)
	{
		if (size > 100000) return;

		IntArrayList dia = new IntArrayList();
		for (int i = 0; i < size; i++)
		{
			dia.add(i);
			dia.addAt(0, i);
			int s = dia.getSize();
			assertEquals(s, 2 * (i + 1));
			assertEquals(dia.get(0), i);
			assertEquals(dia.get(s - 1), i);
		}
		while (dia.getSize() >= 3)
		{
			int s = dia.getSize();
			int m = s / 2;
			dia.removeAt(s - 1);
			dia.removeAt(m);
			dia.removeAt(0);
			int ns = dia.getSize();
			assertEquals(s, ns + 3);
		}
	}

	@Test(dataProvider="Data-Generator")
	public void testIterator(int size)
	{
		if (size > 20) return; // No need to test all the values

		int[] naTest = MakeIntArray(size, false);
		IntArrayList dia = new IntArrayList(naTest);
		naTest = MakeIntArray(size / 2, true);
		dia.addAll(naTest);

		IntArrayList.Iterator it = dia.getIterator();
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
		int[] naResult = dia.getArray();
		int[] naExpected = null;
		switch (size)
		{
		case 0:
			naExpected = new int[0];
			break;
		case 1:
			naExpected = new int[] { 0 };
			break;
		case 2:
			naExpected = new int[] { 0, 0 };
			break;
		case 3:
			naExpected = new int[] { 0, 2, 0 };
			break;
		case 4:
			naExpected = new int[] { 0, 2, 3, 42, 0 };
			break;
		case 7:
			naExpected = new int[] { 0, 2, 3, 42, 4, 4242, 6, 2, 0 };
			break;
		case 11:
			naExpected = new int[] { 0, 2, 3, 42, 4, 4242, 6, 7, 8, 9, 10, 4, 3, 42, 2, 0 };
			break;
		case 12:
			naExpected = new int[] { 0, 2, 3, 42, 4, 4242, 6, 7, 8, 9, 10, 11, 4242, 4, 3, 42, 2, 0 };
			break;
		}
		if (naExpected != null)
		{
			assertTrue(Arrays.equals(naExpected, naResult), ShowArgs(size, naExpected, naResult));
		}
	}

	private String ShowArgs(int s, int[] a1, int[] a2)
	{
		return "For " + s + ", " + Arrays.toString(a1) + " vs. " + Arrays.toString(a2);
	}

	private int[] MakeIntArray(int nSize, boolean bReverse)
	{
		int[] na = new int[nSize];
		for (int i = 0; i < nSize; i++)
		{
			na[i] = bReverse ? nSize - 1 - i : i;
		}
		return na;
	}

	private int[] ArrayShuffle(int[] na)
	{
		Random rnd = new Random();
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
}
