/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2012/10/03 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Compare two strings by respecting the natural order of numbers.
 * Ie. foo2 &lt; foo10, a5b4 &lt; a15b4 and so on.
 *
 * @author PhiLho
 */
public class NaturalOrderComparator implements Comparator<String>
{
    @Override
    public int compare(String s1, String s2)
    {
    	if (s1.isEmpty() && s2.isEmpty())
    		return 0; // Identical...
    	if (s1.isEmpty())
    		return -1; // Empty is smaller than anything else
        if (s2.isEmpty())
            return 1;
        char c1 = s1.charAt(0);
        char c2 = s2.charAt(0);
        boolean b1 = Character.isDigit(c1);
        boolean b2 = Character.isDigit(c2);
        // Fast exit
        if (b1 && !b2 || !b1 && b2)
            return s1.compareTo(s2); // One is a digit, the other isn't: do regular comparison

        int len1 = s1.length();
        int len2 = s2.length();

        int pos1 = 0, pos2 = 0;
        boolean bIsDigit = b1;
        while (true)
        {
            if (bIsDigit)
            {
                // Do number comparison
                long n1 = 0;
                long n2 = 0;
				// Convert the sequence of digits to a long number
                do
                {
                    c1 = s1.charAt(pos1);
                    b1 = Character.isDigit(c1);
                    if (b1)
                    {
                        n1 = n1 * 10 + (c1 - '0');
                    }
                // Stop if one non-digit is found or if we reached the end of one string
                } while (b1 && ++pos1 < len1);
				// Idem, in the second string
                do
                {
                    c2 = s2.charAt(pos2);
                    b2 = Character.isDigit(c2);
                    if (b2)
                    {
                        n2 = n2 * 10 + (c2 - '0');
                    }
                // Stop if one non-digit is found or if we reached the end of one string
                } while (b2 && ++pos2 < len2);

				// Compare the numbers
                if (n1 < n2)
                    return -1;
                if (n1 > n2)
                    return 1;
                // Here, the numbers are equal. If we reached the end of the strings,
                // we say they are equal, otherwise we continue on comparing strings
                if (pos1 == len1 && pos2 == len2)
                	return 0;
            }
            else
            {
                // Do string comparison, character by character
                do
                {
                    c1 = s1.charAt(pos1);
                    c2 = s2.charAt(pos2);
                    b1 = !Character.isDigit(c1);
                    b2 = !Character.isDigit(c2);

                    // Two non-digits, different
                    if (b1 && b2 && c1 != c2)
                        return c1 - c2;
	                // One is digit, and the other isn't one
	                if (b1 && !b2 || !b1 && b2)
	                    return c1 - c2; // Just compare these different chars

                    // Next chars
                    ++pos1; ++pos2;
                // Stop if one digit is found or if we reached the end of one string
                } while (b1 && b2 && pos1 < len1 && pos2 < len2);

                if (b1 && pos1 == len1 && pos2 == len2)
                	return 0; // At the end with non-digits without finding differences
            }
            // Have we reached one end?
            if (pos1 == len1 && len1 < len2)
                return -1; // s1 is shorter, so smaller (all chars were equal so far)
            if (pos2 == len2 && len2 < len1)
                return 1; // s2 is shorter, so smaller

            // Not at the end, we stopped on different kind of char (digit vs. non-digits), let's process them
    		if (!bIsDigit) // Compared chars, we went one char too far, into digits
    		{
    			// Put back current chars into the comparison
	            --pos1; --pos2;
    		}
			// Switch the comparion mode
            bIsDigit = !bIsDigit;
        }
    }

    public static void main(String[] args)
    {
        // Quick test
        NaturalOrderComparator noc = new NaturalOrderComparator();
        boolean bHasError = false;

        System.out.println("\n\n## Pair test\n");
        for (int i = 0; i < TEST_DATA.length; i++)
        {
        	String s1 = TEST_DATA[i][0];
        	String s2 = TEST_DATA[i][1];
        	if (!testPair(noc, i, s1, s2))
        	{
        		bHasError = true;
        	}
        }

        // Same test, starting with number(s)
        System.out.println("\n\n## Shifted pair test 1\n");
        for (int i = 0; i < TEST_DATA.length; i++)
        {
        	String s1 = TEST_DATA[i][0];
        	String s2 = TEST_DATA[i][1];
        	if (!testPair(noc, i, "5:" + s1, "5:" + s2))
        	{
        		bHasError = true;
        	}
        }
        System.out.println("\n\n## Shifted pair test 2\n");
        for (int i = 0; i < TEST_DATA.length; i++)
        {
        	String s1 = TEST_DATA[i][0];
        	String s2 = TEST_DATA[i][1];
        	if (!testPair(noc, i, "75:" + s1, "75:" + s2))
        	{
        		bHasError = true;
        	}
        }

        System.out.println("\n\n## Global sort test\n");
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
        for (String s : data)
        {
        	System.out.println(s);
        }

        if (bHasError)
        {
        	System.out.println("\n!! At least one problem found!");
        }
    }
    private static boolean testPair(NaturalOrderComparator noc, int i, String s1, String s2)
    {
        String info = "At " + i + " for '" + s1 + "' / '" + s2 + "'";
        System.out.print(info);
        int t1 = noc.compare(s1, s2);
        // Ensure symmetry
        int t2 = noc.compare(s2, s1);
        info = ": " + t1 + " / " + t2;
        if (t1 != -t2 || t1 > 0 || (t1 == 0 && TEST_DATA[i].length != 3))
        {
            System.out.println(" !!! Bad test" + info);
            return false;
        }
        else
        {
            System.out.println(" -- Test" + info);
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
}
