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

/**
 * A real person.
 */
public class RealPerson implements Person
{
	private String firstName;
	private String lastName;
	// Other information...

	@Override public RealPerson setFirstName(String name) { firstName = name; return this; }
	@Override public String getFirstName() { return firstName; }
	@Override public RealPerson setLastName(String name) { lastName = name; return this; }
	@Override public String getLastName() { return lastName; }

	@Override
	public String toString()
	{
		return firstName + " " + lastName;
	}
}
