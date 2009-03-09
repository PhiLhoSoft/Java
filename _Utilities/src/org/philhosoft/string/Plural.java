/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 *
 * Copyright notice: See the PhiLhoSoftLicence.txt file for details.
 * This file is distributed under the zlib/libpng license.
 * Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
 */
/* File history:
 *  1.00.000 -- 2005/03/14 (PL) -- Creation
 */
package org.philhosoft.string;

/**
 * Simplistic plural management.
 *
 * Currently, this is more a template than a generic class,
 * since the line process code is hardcoded.
 *
 * TODO: Accept a different simple plural syntax ('j' for Esperanto).
 * TODO: In French, we have 0 euro, in English we have 0 euros,
 *       ie. plural is only for 1.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/03/14
 */
public class Plural
{
	/**
	 * Simple plural.
	 *
	 * @return the number followed by the given string,
	 * put as (French/English) plural if number > 1.
	 */
	public static String get(int nb, String name)
	{
		if (nb > 1)
		{
			return nb + " " + name + "s";
		}
		else
		{
			return nb + " " + name;
		}
	}

	/**
	 * Irregular plural.
	 *
	 * @return the number followed by the given string,
	 * put as (French/English) plural if number > 1.
	 */
	public static String get(int nb, String nameSing, String namePlur)
	{
		if (nb > 1)
		{
			return nb + " " + nameSing;
		}
		else
		{
			return nb + " " + namePlur;
		}
	}
}
