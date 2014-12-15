package org.philhosoft.string;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class TestNumberAsStringParser
{
	@Test
	public void test()
	{
		assertThat(NumberAsStringParser.check("0")).isTrue();
		assertThat(NumberAsStringParser.check("-1")).isTrue();
		assertThat(NumberAsStringParser.check("33333")).isTrue();
		assertThat(NumberAsStringParser.check("-33333")).isTrue();

		assertThat(NumberAsStringParser.check("7.5")).isTrue();
		assertThat(NumberAsStringParser.check("-7.5")).isTrue();
		assertThat(NumberAsStringParser.check("7.5e2")).isTrue();
		assertThat(NumberAsStringParser.check("-7.5E2")).isTrue();
		assertThat(NumberAsStringParser.check("7.5E-2")).isTrue();
		assertThat(NumberAsStringParser.check("-7.5e-2")).isTrue();
		assertThat(NumberAsStringParser.check("7.5E+2")).isTrue();
		assertThat(NumberAsStringParser.check("-7.5e+2")).isTrue();

		assertThat(NumberAsStringParser.check("0l")).isTrue();
		assertThat(NumberAsStringParser.check("-1L")).isTrue();
		assertThat(NumberAsStringParser.check("33333l")).isTrue();
		assertThat(NumberAsStringParser.check("-33333L")).isTrue();

		assertThat(NumberAsStringParser.check("7.5l")).isFalse();
		assertThat(NumberAsStringParser.check("-7.5L")).isFalse();
		assertThat(NumberAsStringParser.check("7.5e2l")).isFalse();
		assertThat(NumberAsStringParser.check("-7.5E2L")).isFalse();
		assertThat(NumberAsStringParser.check("7.5E-2l")).isFalse();
		assertThat(NumberAsStringParser.check("-7.5e-2L")).isFalse();

		assertThat(NumberAsStringParser.check("0f")).isTrue();
		assertThat(NumberAsStringParser.check("-1f")).isTrue();
		assertThat(NumberAsStringParser.check("33333F")).isTrue();
		assertThat(NumberAsStringParser.check("-33333F")).isTrue();

		assertThat(NumberAsStringParser.check("7.5f")).isTrue();
		assertThat(NumberAsStringParser.check("-7.5F")).isTrue();
		assertThat(NumberAsStringParser.check("7.5e2f")).isTrue();
		assertThat(NumberAsStringParser.check("-7.5E2F")).isTrue();
		assertThat(NumberAsStringParser.check("7.5E-2f")).isTrue();
		assertThat(NumberAsStringParser.check("-7.5e-2F")).isTrue();

		assertThat(NumberAsStringParser.check("0d")).isTrue();
		assertThat(NumberAsStringParser.check("-1d")).isTrue();
		assertThat(NumberAsStringParser.check("33333D")).isTrue();
		assertThat(NumberAsStringParser.check("-33333D")).isTrue();

		assertThat(NumberAsStringParser.check("7.5d")).isTrue();
		assertThat(NumberAsStringParser.check("-7.5D")).isTrue();
		assertThat(NumberAsStringParser.check("7.5e2d")).isTrue();
		assertThat(NumberAsStringParser.check("-7.5E2D")).isTrue();
		assertThat(NumberAsStringParser.check("7.5E-2d")).isTrue();
		assertThat(NumberAsStringParser.check("-7.5e-2D")).isTrue();

		assertThat(NumberAsStringParser.check(" 33333")).isFalse();
		assertThat(NumberAsStringParser.check("   33333")).isFalse();
		assertThat(NumberAsStringParser.check("33333 ")).isFalse();
		assertThat(NumberAsStringParser.check("33 ")).isFalse();
		assertThat(NumberAsStringParser.check("33333   ")).isFalse();

		assertThat(NumberAsStringParser.check("7.5e")).isFalse();
		assertThat(NumberAsStringParser.check("-7.5E")).isFalse();
		assertThat(NumberAsStringParser.check("7.5e-")).isFalse();
		assertThat(NumberAsStringParser.check("-7.5E-")).isFalse();
		assertThat(NumberAsStringParser.check("7.5e+")).isFalse();
		assertThat(NumberAsStringParser.check("-7.5E+")).isFalse();

		assertThat(NumberAsStringParser.check("20KB")).isFalse();
		assertThat(NumberAsStringParser.check("20 000")).isFalse();

		assertThat(NumberAsStringParser.check("e")).isFalse();
		assertThat(NumberAsStringParser.check("E")).isFalse();
		assertThat(NumberAsStringParser.check("f")).isFalse();
		assertThat(NumberAsStringParser.check("F")).isFalse();
		assertThat(NumberAsStringParser.check("d")).isFalse();
		assertThat(NumberAsStringParser.check("D")).isFalse();
		assertThat(NumberAsStringParser.check("l")).isFalse();
		assertThat(NumberAsStringParser.check("L")).isFalse();

		assertThat(NumberAsStringParser.check("-")).isFalse();
		assertThat(NumberAsStringParser.check("+")).isFalse();
		assertThat(NumberAsStringParser.check(".")).isFalse();
		assertThat(NumberAsStringParser.check("..")).isFalse();

		assertThat(NumberAsStringParser.check("")).isFalse();
		assertThat(NumberAsStringParser.check(null)).isFalse();
	}
}
