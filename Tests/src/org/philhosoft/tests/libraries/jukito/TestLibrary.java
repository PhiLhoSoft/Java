package org.philhosoft.tests.libraries.jukito;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Provider;

import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(JukitoRunner.class)
public class TestLibrary
{
	public static class Module extends JukitoModule
	{
		@Override
		protected void configureTest()
		{
			bind(BookRepository.class).to(Library.class);
//			bind(Person.class).to(Author.class);
		}
	}

	@Before
	public void init(Provider<Book> bookProvider, AuthorFactory authorFactory)
	{
		this.bookProvider = bookProvider;
		this.authorFactory = authorFactory;
	}

	@Test
	public void testStuff(Library library, Agent.Factory agentFactory)
	{
		library.addBook(bookProvider.get().setTitle("War of the Words").setAuthor(authorFactory.create("Jules", "Berne", null)));
		Book book = bookProvider.get().setTitle("LMGTFY").setAuthor(authorFactory.create("Gilles", "Google", agentFactory.create("Avida", "Dollar")));
		library.addBook(book);

		assertNotNull(library.getBooks());
		assertEquals(2, library.getBooks().size());
		assertNotNull(library.getBook(0));
	}

	private Provider<Book> bookProvider;
	private AuthorFactory authorFactory;
}
