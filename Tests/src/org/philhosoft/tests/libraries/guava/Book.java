/*
 * Tests: A collection of little test programs to explore Java language.
 * Here, tests of the Guava library.
 */
/* File history:
 *  1.00.000 -- 2012-08-28 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.libraries.guava;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;

import org.joda.time.DateTime;

import com.google.common.base.*;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

/**
 * A book description.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2012-08-28
 */
public class Book implements Comparable<Book>
{
	private static Logger s_log = Logger.getLogger(Book.class.getName());

	public static class Builder
	{
		private Book m_book;

		public Builder(String title)
		{
            checkNotNull(Strings.emptyToNull(title), "The book must have a title");

            m_book = new Book();

            m_book.m_title = title;
		}

		public Book build()
		{
            checkState(m_book.m_publisher != null, "The book must have an publisher");
            checkState(m_book.m_isbn != null, "The ISBN of the book is mandatory");

			final Book built = new Book();
			built.m_title = m_book.m_title;
			built.m_authors = Objects.firstNonNull(m_book.m_authors, ImmutableList.<String>of());
			built.m_publisher = m_book.m_publisher;
			built.m_isbn = m_book.m_isbn;
			built.m_publishingDate = m_book.m_publishingDate;
			built.m_pageNumber = m_book.m_pageNumber;
			built.m_price = m_book.m_price;

			return built;
		}

		/**
		 * Adds a list of authors, one per provided String.
		 *
		 * @param values  the authors
		 * @return the builder
		 */
		public Builder authors(String... values)
		{
			checkNotNull(values, "Why call authors() with null? Frankly!");
			m_book.m_authors = Arrays.asList(values);
			return this;
		}
		/**
		 * Adds a list of semi-colon-separated author names.
		 *
		 * @param values  the authors
		 * @return the builder
		 */
		public Builder authors(String value)
		{
			checkNotNull(value, "Seriously...");
			Iterable<String> authors = Splitter.on(';').omitEmptyStrings().trimResults().split(value);
			m_book.m_authors = ImmutableList.copyOf(authors);
			return this;
		}

		public Builder publisher(String value)
		{
            checkNotNull(Strings.emptyToNull(value), "The book must have an publisher");
			m_book.m_publisher = value;
			return this;
		}

		public Builder isbn(String value)
		{
			// Old books have no ISBN, but we are not in a real application...
            checkNotNull(value, "The ISBN of the book is mandatory");
            // Keep only the digits (can be 978-2-484177-143-1 for example)
			// Note: we should also accept 10-digit ISBN numbers (before 2007) which can have X as last "digit"
			// That's out of the scope of this simple demo code...
			String isbn = CharMatcher.DIGIT.retainFrom(value);
			// Since this shows off Guava capabilities, I must mention a variant,
			// should I want to retain the dashes:
			// String isbn = CharMatcher.DIGIT.or(CharMatcher.is('-')).retainFrom(value);

			// Now, verify it is a valid ISBN number
            checkISBN(isbn);
			m_book.m_isbn = isbn;
			return this;
		}

		public Builder date(DateTime value)
		{
			m_book.m_publishingDate = Optional.fromNullable(value);
			return this;
		}

		public Builder pageNumber(int value)
		{
			checkArgument(value > 0, "A book without pages isn't useful...", value);
			m_book.m_pageNumber = value;
			return this;
		}

		public Builder price(double value)
		{
			checkArgument(value >= 0, "Price (%s) cannot be negative!", value);
			m_book.m_price = value;
			return this;
		}

		/**
		 * @param isbn
		 */
		private static void checkISBN(String isbn)
		{
            checkArgument(isbn.length() == 13, "Only 13 digit ISBN numbers are supported");

            int check = 0;
            for (int i = 0; i < 12; i += 2)
            {
                check += digitAt(isbn, i);
            }
            for (int i = 1; i < 12; i += 2)
            {
                check += digitAt(isbn, i) * 3;
            }
            check += digitAt(isbn, 12);
            checkArgument(check % 10 == 0, "Incorrect ISBN number");
		}
		private static int digitAt(String s, int idx)
		{
			return s.charAt(idx) - '0';
		}
	}

	// These fields are not final so they can be reused in the builder,
	// but since they have only getters, the class is actually immutable.
	private String m_title;
	private List<String> m_authors;
	private String m_publisher;
	private String m_isbn;
	private Optional<DateTime> m_publishingDate = Optional.<DateTime>absent();
	private int m_pageNumber = 1; // Minimum
	private double m_price;

	private Book()
	{
	}

	/**
	 * @return title
	 */
	public String getTitle()
	{
		return m_title;
	}

	/**
	 * @return list of authors
	 */
	public List<String> getAuthors()
	{
		return m_authors;
	}

	/**
	 * @return publisher
	 */
	public String getPublisher()
	{
		return m_publisher;
	}

	/**
	 * @return ISBN
	 */
	public String getISBN()
	{
		return m_isbn;
	}

	/**
	 * @return publishing date
	 */
	@Nullable public DateTime getPublishingDate()
	{
		return m_publishingDate.orNull();
	}

	/**
	 * @return number of pages
	 */
	public int getPageNumber()
	{
		return m_pageNumber;
	}

	/**
	 * @return price
	 */
	public double getPrice()
	{
		return m_price;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || obj.getClass() != getClass())
			return false;
		if (obj == this)
			return true;

		final Book that = (Book) obj;
		// In a real application, I would test only the ISBN, supposed to be an unique ID...
		// Although there are old books without this ID.
		return equal(m_isbn, that.m_isbn) && equal(m_title, that.m_title) &&
				equal(m_authors, that.m_authors) && equal(m_publisher, that.m_publisher);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(m_authors, m_publisher, m_isbn, m_title);
	}

	@Override
	public int compareTo(Book that)
	{
		return ComparisonChain.start()
				.compare(m_title, that.m_title)
				.compare(getAuthorList(), that.getAuthorList())
				.compare(m_publisher, that.m_publisher)
				.compare(m_publishingDate.orNull(), that.m_publishingDate.orNull(), Ordering.natural().nullsLast())
				.result();
	}

	@Override
	public String toString()
	{
		return Objects.toStringHelper(this).omitNullValues()
				.add("Title", m_title).add("Authors", getAuthorList()).add("Publisher", m_publisher)
				.add("ISBN", m_isbn).add("Number of pages", m_pageNumber).add("Price", m_price)
				.add("Published", m_publishingDate.isPresent() ? m_publishingDate.get().toString("yyyy-MM-dd") : null)
				.toString();
	}

	public String getAuthorList()
	{
		// Show off Joiner
		return Joiner.on("; ").skipNulls().join(m_authors);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Stopwatch watch = new Stopwatch().start();
		Book goodBook = new Book.Builder("The Hitchhicker's Guide to the Galaxy")
				.authors("Douglas Adams")
				.isbn("9780345391803")
				.publisher("Del Rey")
				.pageNumber(224)
				.date(new DateTime().minusYears(5))
				.price(7.99)
				.build();
		Book techBook = new Book.Builder("Effective Java, 2nd ed.")
				.authors("Joshua Bloch")
				.isbn("9780321356680")
				.publisher("Addison-Wesley")
				.pageNumber(346)
				.date(new DateTime().minusMonths(40))
				.price(35)
				.build();
		Book oldBook = new Book.Builder("Alice in Wonderland")
				.authors("Lewis Carroll")
				.isbn("9780123456786") // Yeah, made up number!
				.publisher("Project Gutemberg")
//				.pageNumber(-5)
				.build();
		watch.stop();
		System.out.println(techBook);
		System.out.println(goodBook);
		System.out.println(oldBook);
		s_log.info("Time spent: " + watch);
	}
}
