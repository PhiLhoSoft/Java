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

/**
 * A book description.
 */
public class Book
{
	private String title;
	private Author author;
	private Person borrower;

//	Book() {}
//	public Book(String title, Author author)
//	{
//		this.setTitle(title);
//		this.setAuthor(author);
//	}

	public String getTitle() { 	return title; }
	public Book setTitle(String title) { this.title = title; return this; }
	public Author getAuthor() { return author; }
	public Book setAuthor(Author author) { 	this.author = author; return this; }
	public Person getBorrower() { return borrower; }
	public Book setBorrower(Person borrower) { 	this.borrower = borrower; return this; }

	@Override
	public String toString()
	{
		return title + " by " + (author == null ? "(anonymous)" : author) +
				(borrower == null ? " (available)" : " borrowed by " + borrower);
	}
}
