/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2008/10/31 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.experiments;


import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;


/**
 * Template class for test applications.
 *
 * @author Philippe Lhoste
 * @version 1.01.000
 * @date 2008/10/31
 */
public class TestMain
{
	private TestMain()
	{
	}

	static String GetRandomString(int length)
	{
		UUID uuid = UUID.randomUUID();
		return uuid.toString().substring(0, length);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ArrayList<String> al = new ArrayList<String>(20);
		for (int i = 0; i < 10; i++)
		{
			al.add(GetRandomString(7));
		}
		ArrayList<String> cloneArray = new ArrayList<String>(al);
		Collections.copy(cloneArray, al);
		System.out.println(al);
		System.out.println(cloneArray);
		for (int i = 9; i >= 0; i -= 2)
		{
			al.remove(i);
		}
		System.out.println(al);
		System.out.println(cloneArray);
	}
}
