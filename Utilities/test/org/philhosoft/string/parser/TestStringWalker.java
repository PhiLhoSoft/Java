package org.philhosoft.string.parser;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class TestStringWalker
{
	@Test
	public void testSimple()
	{
		String s = "Simple";
		StringWalker walker = new StringWalker(s);

		assertThat(walker.hasMore()).isTrue();
		assertThat(walker.atLineEnd()).isFalse();
		assertThat(walker.atLineStart()).isTrue();

		assertThat(walker.previous()).isEqualTo(' ');
		assertThat(walker.current()).isEqualTo('S');
		assertThat(walker.next()).isEqualTo('i');

		assertThat(walker.match('S')).isTrue();
		assertThat(walker.match('s')).isFalse();
		assertThat(walker.match('S', 'i')).isTrue();
		assertThat(walker.match('S', 'I')).isFalse();
		assertThat(walker.match("Simple")).isTrue();
		assertThat(walker.match("Sample")).isFalse();

		walker.forward();

		assertThat(walker.atLineEnd()).isFalse();
		assertThat(walker.atLineStart()).isFalse();

		assertThat(walker.previous()).isEqualTo('S');
		assertThat(walker.current()).isEqualTo('i');
		assertThat(walker.next()).isEqualTo('m');

		assertThat(walker.match('i')).isTrue();
		assertThat(walker.match('S')).isFalse();
		assertThat(walker.match('i', 'm')).isTrue();
		assertThat(walker.match('i', 'x')).isFalse();
		assertThat(walker.match("impl")).isTrue();
		assertThat(walker.match("mple")).isFalse();

		walker.forward(3);

		assertThat(walker.hasMore()).isTrue();
		assertThat(walker.atLineEnd()).isFalse();
		assertThat(walker.atLineStart()).isFalse();

		assertThat(walker.previous()).isEqualTo('p');
		assertThat(walker.current()).isEqualTo('l');
		assertThat(walker.next()).isEqualTo('e');

		assertThat(walker.match('l')).isTrue();
		assertThat(walker.match('x')).isFalse();
		assertThat(walker.match('l', 'e')).isTrue();
		assertThat(walker.match('x', 'x')).isFalse();
		assertThat(walker.match("le  ")).isTrue(); // Mmm...
		assertThat(walker.match("leet")).isFalse();

		walker.forward(3);

		assertThat(walker.hasMore()).isFalse();
		assertThat(walker.atLineEnd()).isTrue();
		assertThat(walker.atLineStart()).isFalse();

		assertThat(walker.previous()).isEqualTo(' ');
		assertThat(walker.current()).isEqualTo(' ');
		assertThat(walker.next()).isEqualTo(' ');

		assertThat(walker.match('x')).isFalse();
		assertThat(walker.match('x', 'x')).isFalse();
		assertThat(walker.match("meet")).isFalse();
	}

	@Test
	public void testUnixNewline()
	{
		String s = "Line\nBreak";
		StringWalker walker = new StringWalker(s);
	
		walker.forward(3);
	
		assertThat(walker.hasMore()).isTrue();
		assertThat(walker.atLineEnd()).isFalse();
		assertThat(walker.atLineStart()).isFalse();
	
		assertThat(walker.previous()).isEqualTo('n');
		assertThat(walker.current()).isEqualTo('e');
		assertThat(walker.next()).isEqualTo('\n');
	
		assertThat(walker.match('e', 'e')).isFalse();
		assertThat(walker.match("en")).isFalse();
	
		walker.forward();
	
		assertThat(walker.hasMore()).isTrue();
		assertThat(walker.atLineEnd()).isTrue();
		assertThat(walker.atLineStart()).isFalse();
	
		assertThat(walker.previous()).isEqualTo('e');
		assertThat(walker.current()).isEqualTo('\n');
		assertThat(walker.next()).isEqualTo('B');
	
		walker.forward();
	
		assertThat(walker.hasMore()).isTrue();
		assertThat(walker.atLineEnd()).isFalse();
		assertThat(walker.atLineStart()).isTrue();
	
		assertThat(walker.previous()).isEqualTo('\n');
		assertThat(walker.current()).isEqualTo('B');
		assertThat(walker.next()).isEqualTo('r');
	}

	@Test
	public void testWindowsNewline()
	{
		String s = "Line\r\nBreak";
		StringWalker walker = new StringWalker(s);

		walker.forward(3);

		assertThat(walker.hasMore()).isTrue();
		assertThat(walker.atLineEnd()).isFalse();
		assertThat(walker.atLineStart()).isFalse();

		assertThat(walker.previous()).isEqualTo('n');
		assertThat(walker.current()).isEqualTo('e');
		assertThat(walker.next()).isEqualTo('\r');

		assertThat(walker.match('e', 'e')).isFalse();
		assertThat(walker.match("en")).isFalse();

		walker.forward();

		assertThat(walker.hasMore()).isTrue();
		assertThat(walker.atLineEnd()).isTrue();
		assertThat(walker.atLineStart()).isFalse();

		assertThat(walker.previous()).isEqualTo('e');
		assertThat(walker.current()).isEqualTo('\r');
		assertThat(walker.next()).isEqualTo('B');

		walker.forward();

		assertThat(walker.hasMore()).isTrue();
		assertThat(walker.atLineEnd()).isFalse();
		assertThat(walker.atLineStart()).isTrue();

		assertThat(walker.previous()).isEqualTo('\r');
		assertThat(walker.current()).isEqualTo('B');
		assertThat(walker.next()).isEqualTo('r');
	}
}
