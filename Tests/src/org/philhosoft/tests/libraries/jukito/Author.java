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
 * A book author.
 */
public class Author extends RealPerson
{
	@Inject
	public Author(@Assisted("first") String first, @Assisted("last") String last, @Assisted Person agent)
	{
		super();
		setFirstName(first);
		setLastName(last);

		this.agent = agent;
	}

	@Override public Author setFirstName(String name) { super.setFirstName(name); return this; }
	@Override public Author setLastName(String name) { super.setLastName(name); return this; }

	public Person getAgent()
	{
		return agent;
	}

	private Person agent;
}
