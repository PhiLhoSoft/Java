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
package org.philhosoft.experiments.libraries.stringtemplate;

import org.joda.time.DateTime;
import org.stringtemplate.v4.*;

import org.philhosoft.experiments.libraries.guava.Book;
import org.philhosoft.util.ResourceUtil;

/**
 * Base test of StringTemplate.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2012-08-28
 */
public final class BookListing
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		BookListing bl = new BookListing();
		String strPackage = ResourceUtil.getPackage(bl);
        STGroup groupF = new STGroupFile(strPackage + "templates/Book.stg");
        ST bookT = groupF.getInstanceOf("book");
        groupF.registerRenderer(Number.class, new NumberRenderer());
        groupF.registerRenderer(DateTime.class, new JodaTimeRenderer());

		Book goodBook = new Book.Builder("The Hitchhicker's Guide to the Galaxy")
				.authors("Douglas Adams")
				.isbn("9780345391803")
				.publisher("Del Rey")
				.pageNumber(224)
				.date(new DateTime().minusYears(5))
				.price(7.99)
				.build();
		bookT.add("bk", goodBook);

        System.out.println(bookT.render());
	}
}
