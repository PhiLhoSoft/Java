package org.philhosoft.formattedtext.format;

import java.util.List;

import org.junit.Test;

import org.philhosoft.formattedtext.ast.Block;
import org.philhosoft.formattedtext.ast.BlockType;
import org.philhosoft.formattedtext.ast.DecoratedFragment;
import org.philhosoft.formattedtext.ast.Fragment;
import org.philhosoft.formattedtext.ast.FragmentDecoration;
import org.philhosoft.formattedtext.ast.LinkFragment;
import org.philhosoft.formattedtext.ast.PlainTextFragment;
import org.philhosoft.formattedtext.ast.TypedBlock;


public class TestPlainTextVisitor
{
	@Test
	public void test() throws Exception
	{
		// First line
		DecoratedFragment sdf = new DecoratedFragment(FragmentDecoration.STRONG);
		List<Fragment> fl = sdf.getFragments();
		fl.add(new PlainTextFragment("Plain text "));
		DecoratedFragment idf = new DecoratedFragment(FragmentDecoration.EMPHASIS);
		idf.getFragments().add(new PlainTextFragment("Text with emphasis."));
		fl.add(idf);
		fl.add(new PlainTextFragment(" Really plain text "));

		PlainTextFragment ptf = new PlainTextFragment("Text of ");
		DecoratedFragment linkE = new DecoratedFragment(FragmentDecoration.EMPHASIS);
		linkE.getFragments().add(new PlainTextFragment("Link"));
		LinkFragment lf = new LinkFragment(ptf, "http://www.example.com");
		fl.add(ptf);

//		Line l1 = new Line(text);

		PlainTextFragment plain = new PlainTextFragment("Boring plain text");

		TypedBlock tb = new TypedBlock(BlockType.DOCUMENT);
		List<Block> blocks = tb.getBlocks();

	}
}
