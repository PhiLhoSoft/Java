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
package org.philhosoft.experiments.libraries.jukito;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * An agent managing several authors.
 */
public class Agent extends RealPerson
{
	public interface Factory
	{
	    Agent create(@Assisted("first") String firstName, @Assisted("last") String lastName);
	}

	@Inject
	public Agent(@Assisted("first") String first, @Assisted("last") String last)
	{
		super(first, last);

		authors = new ArrayList<>();
	}

	public Agent addAuthor(Author author) { authors.add(author); return this; }
	public List<Author> getAuthors() { return authors; }

	private List<Author> authors;
}
