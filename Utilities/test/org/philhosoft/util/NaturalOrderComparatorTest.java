package org.philhosoft.util;


import static org.testng.Assert.*;

import java.util.*;

import org.testng.annotations.*;


public class NaturalOrderComparatorTest
{
	private static final boolean bShowOutput = false;
	private NaturalOrderComparator noc = new NaturalOrderComparator();

	@Test
	public void testCompareNaturalNumericalOrder()
	{
		show("\n\n## Pair test\n");
		for (int i = 0; i < TEST_DATA.length; i++)
		{
			String s1 = TEST_DATA[i][0];
			String s2 = TEST_DATA[i][1];
			assertTrue(testPair(i, s1, s2));
		}

		// Same test, starting with number(s)
		show("\n\n## Shifted pair test 1\n");
		for (int i = 0; i < TEST_DATA.length; i++)
		{
			String s1 = TEST_DATA[i][0];
			String s2 = TEST_DATA[i][1];
			assertTrue(testPair(i, "5:" + s1, "5:" + s2));
		}
		show("\n\n## Shifted pair test 2\n");
		for (int i = 0; i < TEST_DATA.length; i++)
		{
			String s1 = TEST_DATA[i][0];
			String s2 = TEST_DATA[i][1];
			assertTrue(testPair(i, "75:" + s1, "75:" + s2));
		}

		show("\n\n## Global sort test\n");
		ArrayList<String> data = new ArrayList<String>();
		for (int i = 11; i < TEST_DATA.length; i++)
		{
			data.add(TEST_DATA[i][0]);
			data.add(TEST_DATA[i][1]);
			data.add("3%" + TEST_DATA[i][0]);
			data.add("3%" + TEST_DATA[i][1]);
			data.add("42*" + TEST_DATA[i][0]);
			data.add("42*" + TEST_DATA[i][1]);
		}
		data.addAll(Arrays.asList(BULK_DATA));
		Collections.shuffle(data);
		Collections.sort(data, noc);
		if (bShowOutput)
		{
			for (String s : data)
			{
				System.out.println(s);
			}
		}
	}

	private boolean testPair(int i, String s1, String s2)
	{
		showInline("At " + i + " for '" + s1 + "' / '" + s2 + "'");
		int t1 = noc.compare(s1, s2);
		// Ensure symmetry
		int t2 = noc.compare(s2, s1);
		String info = ": " + t1 + " / " + t2;
		if (t1 != -t2 || t1 > 0 || (t1 == 0 && TEST_DATA[i].length != 3))
		{
			show(" !!! Bad test" + info);
			return false;
		}
		else
		{
			show(" -- Test" + info);
			return true;
		}
	}

	private static final String EQU = "=";
	// First one is always smaller than second one, except when marked
	private static final String[][] TEST_DATA =
	{
		{ "", "", EQU },
		{ "", "a" },
		{ "", "1" },
		{ "1", "a" },
		{ "1", "1", EQU },
		{ "1", "2" },
		{ "1", "11" },
		{ "2", "11" },
		{ "a", "a", EQU },
		{ "a", "b" },
		{ "a", "aa" },
		{ "a1", "a1", EQU },
		{ "a1", "a2" },
		{ "a1", "a11" },
		{ "a2", "a11" },
		{ "a1", "a1+" },
		{ "a12", "a12+" },
		{ "abc1", "abc1", EQU },
		{ "abc1", "abc2" },
		{ "abc1", "abc11" },
		{ "abc2", "abc11" },
		{ "abc123", "abc123", EQU },
		{ "abc123", "abc231" },
		{ "abc123", "abc1231" },
		{ "abc223", "abc1231" },
		{ "abc1", "abd1", EQU },
		{ "abc1", "abd2" },
		{ "abc1", "abd11" },
		{ "abc2", "abd11" },
		{ "abc123-1", "abc123-1", EQU },
		{ "abc123x1", "abc123y1" },
		{ "abc123-x1", "abc123-y1" },
		{ "abc123-1", "abc123-2" },
		{ "abc123-1", "abc123-11" },
		{ "abc123-2", "abc123-11" },
		{ "abc123-157//1", "abc123-157//1", EQU },
		{ "abc123-157//1", "abc123-157//2" },
		{ "abc123-157//1", "abc123-157//11" },
		{ "abc123-157//2", "abc123-157//11" },
		{ "xyz32-157//H", "xyz32-157//H", EQU },
		{ "xyz32-157//H", "xyz32-158//H" },
		{ "xyz32-157//H", "xyz32-1571//H" },
		{ "xyz32-257//H", "xyz32-1570//H" },
	};
	private static final String[] BULK_DATA =
	{
		"Foo-304/BAR-99/Stuff-60840/ohm-1",
		"Foo-304/BAR-99/Stuff-60840/ohm-2",
		"Foo-306/BAR-10/Stuff-52504/ohm-1",
		"Foo-306/BAR-10/Stuff-52504/ohm-10",
		"Foo-306/BAR-10/Stuff-52504/ohm-11",
		"Foo-306/BAR-10/Stuff-52504/ohm-12",
		"Foo-306/BAR-10/Stuff-52504/ohm-13",
		"Foo-306/BAR-10/Stuff-52504/ohm-14",
		"Foo-306/BAR-10/Stuff-52504/ohm-15",
		"Foo-306/BAR-10/Stuff-52504/ohm-16",
		"Foo-306/BAR-10/Stuff-52504/ohm-17",
		"Foo-306/BAR-10/Stuff-52504/ohm-18",
		"Foo-306/BAR-10/Stuff-52504/ohm-19",
		"Foo-306/BAR-10/Stuff-52504/ohm-2",
		"Foo-306/BAR-10/Stuff-52504/ohm-20",
		"Foo-306/BAR-10/Stuff-52504/ohm-21",
		"Foo-306/BAR-10/Stuff-52504/ohm-22",
		"Foo-306/BAR-10/Stuff-52504/ohm-3",
		"Foo-306/BAR-10/Stuff-52504/ohm-4",
		"Foo-306/BAR-10/Stuff-52504/ohm-5",
		"Foo-306/BAR-10/Stuff-52504/ohm-6",
		"Foo-306/BAR-10/Stuff-52504/ohm-7",
		"Foo-306/BAR-10/Stuff-52504/ohm-8",
		"Foo-306/BAR-10/Stuff-52504/ohm-9",
	};


	private void showInline(String msg)
	{
		if (bShowOutput)
		{
			System.out.print(msg);
		}
	}

	private void show(String msg)
	{
		if (bShowOutput)
		{
			System.out.println(msg);
		}
	}
}
