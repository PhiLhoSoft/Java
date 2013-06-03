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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Entry point of the application.
 */
public class Main
{
	@Inject
	public Main(BookRepository library, Provider<Book> bookProvider, AuthorFactory authorFactory)
	{
		Person borrower = new RealPerson();
		library.addBook(bookProvider.get().setTitle("Best Jokes Ever").setBorrower(borrower));
		library.addBook(bookProvider.get().setTitle("War of the Words").setAuthor(authorFactory.create("Jules", "Berne", null)));
		System.out.println(library.getBooks());
	}

	public static void main(String[] args)
	{
		Injector injector = Guice.createInjector(new Module());
		Main main = injector.getInstance(Main.class);

//	    Library library = injector.getInstance(Library.class);
//	    library.addBook();
//	    library.listBooks();
    }

	public static class Module extends AbstractModule
	{
		@Override
		protected void configure()
		{
			bind(BookRepository.class).to(Library.class);
			bind(Person.class).to(RealPerson.class);

			install(new FactoryModuleBuilder().build(AuthorFactory.class));
		}
	}
}
