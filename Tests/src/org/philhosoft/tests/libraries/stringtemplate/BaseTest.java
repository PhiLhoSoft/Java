/*
 * Tests: A collection of little test programs to explore the Java language.
 * Here, tests of the StringTemplate library.
 */
/* File history:
 *  1.00.000 -- 2012-08-29 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.libraries.stringtemplate;

import org.stringtemplate.v4.*;
import org.stringtemplate.v4.compiler.CompiledST;

/**
 * Base test of StringTemplate.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2012-08-28
 */
public final class BaseTest
{
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
        bookT4.add("price", 12.5);
        groupF.registerRenderer(Number.class, new NumberRenderer());
        System.out.println(bookT4.render());

        System.out.println("\nBook 5");
        ST bookT5 = groupF.getInstanceOf("bookToo"); // Get the multiline 'bookToo' template from the group
        bookT5.add("title", "Throught the Looking Glass");
        bookT5.add("author", "Lewis Carroll");
        bookT5.add("date", 1865);
        System.out.println(bookT5.render());

        System.out.println("\nBook 6");
        ST bookT6 = groupF.getInstanceOf("bookToo"); // Get the multiline 'bookToo' template from the group
        bookT6.add("title", "Throught the Looking Glass");
        bookT6.add("author", "Lewis Carroll");
        bookT6.add("date", 1865);
        System.out.println(bookT6.render());

        // Using a different set of attribute delimiters

        System.out.println("\nOther 1");
        STGroup groupS = new STGroupString("some group", "val(value) ::= \"<span>Value is <b>{value; format=\\\"%1.5f\\\"}</b></span>\"", '{', '}');
        groupS.registerRenderer(Number.class, new NumberRenderer());
        ST other1 = groupS.getInstanceOf("val");
        other1.add("value", 3.14159265358979353);
        System.out.println(other1.render());

        // http://stackoverflow.com/questions/11146113/how-to-get-stringtemplate-v4-to-ignore-as-delimiter
        System.out.println("\nOther 2");
        STGroup groupR = new STGroup('@', '#');
        groupR.registerRenderer(Number.class, new NumberRenderer());
        CompiledST compiledTemplate = groupR.defineTemplate("val", "<span>Value is @value; format=\"%1.3f\"#</span>");
        compiledTemplate.hasFormalArgs = false;
        ST other2 = groupR.getInstanceOf("val");
        other2.add("value", 3.1415926);
        System.out.println(other2.render());
	}
}
