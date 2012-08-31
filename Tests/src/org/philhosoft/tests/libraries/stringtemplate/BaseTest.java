/*
 * Tests: A collection of little test programs to explore Java language.
 * Here, tests of the StringTemplate library.
 */
/* File history:
 *  1.00.000 -- 2012-08-29 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.libraries.stringtemplate;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.stringtemplate.v4.*;

/**
 * Base test of StringTempalte.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2012-08-28
 */
public final class BaseTest
{
	private static Logger s_log = Logger.getLogger(BaseTest.class.getName());

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		ST hello = new ST("Hello, <name>\nWelcome to <names; separator=\", \">");
		hello.add("name", "World");
		hello.add("name", "!");
		hello.add("names", "John");
		hello.add("names", "Jean");
		hello.add("names", "Jan");
        hello.add("names", "Yann");
        System.out.println(hello.render());

        System.out.println("\nBook 1");
        // See all .st files of the given dir (here, from classpath)
        STGroup groupRD = new STRawGroupDir("org/philhosoft/tests/libraries/stringtemplate/templates");
        ST bookT1 = groupRD.getInstanceOf("BaseTestBook"); // Load BaseTestBook.st, the whole file is the template
        bookT1.add("title", "Alice in Wonderland");
        bookT1.add("authors", "Lewis Carroll");
        bookT1.add("authors", "John Tenniel");
        bookT1.add("publisher", "Dover");
        bookT1.add("pageNumber", 212);
        System.out.println(bookT1.render());

        System.out.println("\nBook 2");
        // See all .st files of the given classpath dir
        STGroup groupD = new STGroupDir("org/philhosoft/tests/libraries/stringtemplate/templates");
        ST bookT2 = groupD.getInstanceOf("info"); // Load info.st, defining an 'info' template
        bookT2.add("title", "Alice in Wonderland");
        bookT2.add("author", "Lewis Carroll");
        bookT2.add("date", 1865);
        System.out.println(bookT2.render());

        System.out.println("\nBook 3");
        // Load the BaseTestBook.stg group file defining several templates
        STGroup groupF = new STGroupFile("org/philhosoft/tests/libraries/stringtemplate/templates/BaseTestBook.stg");
        ST bookT3 = groupF.getInstanceOf("info"); // Get the 'info' template
        bookT3.add("title", "Throught the Looking Glass");
        bookT3.add("author", "Lewis Carroll");
        System.out.println(bookT3.render());

        System.out.println("\nBook 4");
        ST bookT4 = groupF.getInstanceOf("book"); // Get the multiline 'book' template from the group
        bookT4.add("title", "Throught the Looking Glass");
        bookT4.add("author", "Lewis Carroll");
        bookT4.add("date", 1865);
        System.out.println(bookT4.render());

        System.out.println("\nBook 5");
        ST bookT5 = groupF.getInstanceOf("bookToo"); // Get the multiline 'bookToo' template from the group
        bookT5.add("title", "Throught the Looking Glass");
        bookT5.add("author", "Lewis Carroll");
        bookT5.add("date", 1865);
        System.out.println(bookT5.render());
	}
}
