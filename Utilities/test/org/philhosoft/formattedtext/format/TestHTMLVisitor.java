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
		ContextWithStringBuilder ctx = new ContextWithStringBuilder();
		document.accept(visitor, ctx);

//		System.out.println(ctx.asString());
		assertThat(ctx.asString()).isEqualTo(
				"<div>\n" +
				"Start of text with <em>emphasis inside</em>.<br>\n" +
				"\n<strong>Strong init, followed by</strong> plain text and <a href='http://www.example.com'>a nice <em>link</em></a><br>\n" +
				"\nBoring plain text and <em>emphased text <strong>and even </strong><del>deleted text</del><code> fixed width text</code>.</em>\n" +
				"</div>\n");
	}

	@Test
	public void testBlocks_noLines() throws Exception
	{
		Block document = FormattedTextExamples.buildTypedBlocks(false);

		HTMLVisitor visitor = new HTMLVisitor();
		ContextWithStringBuilder ctx = new ContextWithStringBuilder();
		document.accept(visitor, ctx);

//		System.out.println(ctx.asString());
		assertThat(ctx.asString()).isEqualTo(
				"<div>\n" +
				"<h3>This is a title</h3>\n" +
				"<ul>\n" +
				"<li>Item 0</li>\n" +
				"<li>Item 1</li>\n" +
				"<li>Item 2</li>\n" +
				"</ul>\n" +
				"<pre><code>\n" +
				"Block of code\n" +
				"on several lines" +
				"\n</code></pre>\n" +
				"\n</div>\n");
	}

	@Test
	public void testBlocks_withLines() throws Exception
	{
		Block document = FormattedTextExamples.buildTypedBlocks(true);

		HTMLVisitor visitor = new HTMLVisitor();
		ContextWithStringBuilder ctx = new ContextWithStringBuilder();
		document.accept(visitor, ctx);

//		System.out.println(ctx.asString());
		assertThat(ctx.asString()).isEqualTo(
				"<div>\n" +
				"<h3>This is a title</h3>\n" +
				"Line Two<br>\n" +
				"\n<ul>\n" +
				"<li>Item 0</li>\n" +
				"<li>Item 1</li>\n" +
				"<li>Item 2</li>\n" +
				"</ul>\n" +
				"<br>\n" +
				"\n<pre><code>\n" +
				"Block of code\n" +
				"on several lines" +
				"\n</code></pre>\n" +
				"\nLast line" +
				"\n</div>\n");
	}
}
