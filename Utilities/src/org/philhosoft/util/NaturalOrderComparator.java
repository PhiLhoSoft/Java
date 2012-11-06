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
        if (b1 != b2)
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
	                if (b1 != b2)
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
			// Switch the comparison mode
            bIsDigit = !bIsDigit;
        }
    }
}
