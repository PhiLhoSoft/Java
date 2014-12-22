package org.philhosoft.formattedtext.format;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import org.philhosoft.formattedtext.ast.Block;


public class TestPlainTextVisitor
{
	@Test
	public void testFragments() throws Exception
	{
		Block document = FormattedTextExamples.buildFragments();

		PlainTextVisitor visitor = new PlainTextVisitor();
		StringBuilder sb = new StringBuilder();
		document.accept(visitor, sb);

//		System.out.println(sb.toString());
		assertThat(sb.toString()).isEqualTo("Start of text with emphasis inside.\n" +
				"Strong init, followed by plain text and a nice link - http://www.example.com\n" +
				"Boring plain text and emphased text and even deleted text fixed width text.\n");
	}

	@Test
	public void testBlocks() throws Exception
	{
		Block document = FormattedTextExamples.buildTypedBlocks();

		PlainTextVisitor visitor = new PlainTextVisitor();
		StringBuilder sb = new StringBuilder();
		document.accept(visitor, sb);

//		System.out.println(sb.toString());
		assertThat(sb.toString()).isEqualTo("This is a title\n" +
				"Line Two\n" +
				"Item 0\n" +
				"Item 1\n" +
				"Item 2\n" +
				"\n" +
				"Block of code\n" +
				"on several lines\n" +
				"Last line\n");
	}
}
