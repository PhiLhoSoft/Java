/*
 * Tests: A collection of little test programs to explore the Java language.
 * Here, tests of the Jukito test library.
 */
/* File history:
 *  1.00.000 -- 2013-03-04 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2013 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.libraries.jukito;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * A book borrower.
 */
public class Borrower extends RealPerson
{
	public interface Factory
	{
	    Borrower create(@Assisted("first") String firstName, @Assisted("last") String lastName);
	}

	@Inject
	public Borrower(@Assisted("first") String first, @Assisted("last") String last)
	{
		super(first, last);
	}
}
