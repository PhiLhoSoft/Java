package org.philhosoft.formattedtext.format;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.philhosoft.formattedtext.ast.Block;


public class TestHTMLVisitor
{
	@Test
	public void testFragments() throws Exception
	{
		Block document = FormattedTextExamples.buildFragments();

		HTMLVisitor visitor = new HTMLVisitor();
		StringBuilder sb = new StringBuilder();
		document.accept(visitor, sb);

//		System.out.println(sb.toString());
		assertThat(sb.toString()).isEqualTo("<div>Start of text with <em>emphasis inside</em>.\n" +
				"<strong>Strong init, followed by</strong> plain text and <a href='http://www.example.com'>a nice <em>link</em></a>\n" +
				"Boring plain text and <em>emphased text <strong>and even </strong><del>deleted text</del><code> fixed width text</code>.</em>\n" +
				"</div>");
	}

	@Test
	public void testBlocks() throws Exception
	{
		Block document = FormattedTextExamples.buildTypedBlocks();

		HTMLVisitor visitor = new HTMLVisitor();
		StringBuilder sb = new StringBuilder();
		document.accept(visitor, sb);

//		System.out.println(sb.toString());
		assertThat(sb.toString()).isEqualTo("\n<div>\n" +
				"<h3>This is a title</h3>\n" +
				"Line Two\n" +
				"<ul>\n" +
				"<li>Item 0</li>\n" +
				"<li>Item 1</li>\n" +
				"<li>Item 2</li>\n" +
				"</ul>\n" +
				"<pre><code>Block of code\n" +
				"on several lines\n" +
				"</code></pre>\n" +
				"Last line\n" +
				"</div>");
	}
}
