/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2012/11/05 (PL) -- Creation.
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.util;


/**
 * Comparator of integers, similar to Comparator&lt;Integer>, but for this primitive type.
 *
 * @author PhiLho
 */
public interface IntComparator
{
	/**
	 * Compares two integers.
	 *
	 * @param i1 the first integer
	 * @param i2 the second integer
	 *
	 * @return -1 if i1 < i2, 0 if i1 = i2, 1 if i1 > i2.
	 */
	int compare(int i1, int i2);
}
