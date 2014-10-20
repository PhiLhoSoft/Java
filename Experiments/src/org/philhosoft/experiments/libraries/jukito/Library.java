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

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * A library, collection of books.
 */
public class Library implements BookRepository
{
	private Provider<Book> bookProvider;
	private AuthorFactory authorFactory;
	private Agent.Factory agentFactory;
	private List<Book> books;

	@Inject
	public Library(Provider<Book> bookProvider, AuthorFactory authorFactory, Agent.Factory agentFactory)
	{
		this.bookProvider = bookProvider;
		this.authorFactory = authorFactory;
		this.agentFactory = agentFactory;
		books = new ArrayList<Book>();
	}

	@Override
	public void addBook(Book book)
	{
		books.add(book);
	}

	@Override
	public List<Book> getBooks()
	{
		return ImmutableList.copyOf(books).asList();
	}

	@Override
	public Book getBook(int idx)
	{
		return books.get(idx);
	}

	public void addBook(String title, String authorFN, String authorLN)
	{
		Person agent = agentFactory.create("Avida", "Dollar");
		books.add(bookProvider.get().setTitle(title).setAuthor(authorFactory.create(authorFN, authorLN, agent)));
	}
}
