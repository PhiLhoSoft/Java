package org.philhosoft.string;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TestParseString
{
	@Test
	public void testToInt()
	{
		assertThat(ParseString.toInt("0", 666)).isEqualTo(0);
		assertThat(ParseString.toInt("-1", 666)).isEqualTo(-1);
		assertThat(ParseString.toInt("33333", 666)).isEqualTo(33333);
		assertThat(ParseString.toInt("-33333", 666)).isEqualTo(-33333);

		assertThat(ParseString.toInt(" 33333", 666)).isEqualTo(666);
		assertThat(ParseString.toInt("33333 ", 666)).isEqualTo(666);
		assertThat(ParseString.toInt("7.5", 666)).isEqualTo(666);
		assertThat(ParseString.toInt("20KB", 666)).isEqualTo(666);
		assertThat(ParseString.toInt("", 666)).isEqualTo(666);
		assertThat(ParseString.toInt(null, 666)).isEqualTo(666);
	}

	@Test
	public void testToFloat()
	{
		assertThat(ParseString.toFloat("7.5", 3.14159f)).isEqualTo(7.5f);
		assertThat(ParseString.toFloat("-7.5", 3.14159f)).isEqualTo(-7.5f);
		assertThat(ParseString.toFloat("7.5e2", 3.14159f)).isEqualTo(7.5e2f);
		assertThat(ParseString.toFloat("-7.5E2", 3.14159f)).isEqualTo(-7.5e2f);
		assertThat(ParseString.toFloat("7.5E-2", 3.14159f)).isEqualTo(7.5E-2f);
		assertThat(ParseString.toFloat("-7.5e-2", 3.14159f)).isEqualTo(-7.5e-2f);
		assertThat(ParseString.toFloat("0", 3.14159f)).isEqualTo(0f);
		assertThat(ParseString.toFloat("-1", 3.14159f)).isEqualTo(-1f);
		assertThat(ParseString.toFloat("33333", 3.14159f)).isEqualTo(33333f);
		assertThat(ParseString.toFloat("-33333", 3.14159f)).isEqualTo(-33333f);
		assertThat(ParseString.toFloat(" 33333", 3.14159f)).isEqualTo(33333f);
		assertThat(ParseString.toFloat("   33333", 3.14159f)).isEqualTo(33333f);
		assertThat(ParseString.toFloat("33333 ", 3.14159f)).isEqualTo(33333f);
		assertThat(ParseString.toFloat("33333   ", 3.14159f)).isEqualTo(33333f);

		assertThat(ParseString.toFloat("20KB", 3.14159f)).isEqualTo(3.14159f);
		assertThat(ParseString.toFloat("", 3.14159f)).isEqualTo(3.14159f);
		assertThat(ParseString.toFloat(null, 3.14159f)).isEqualTo(3.14159f);
	}

	@Test
	public void testToBoolean()
	{
		assertThat(ParseString.toBoolean("true", false)).isEqualTo(true);
		assertThat(ParseString.toBoolean("T", false)).isEqualTo(true);
		assertThat(ParseString.toBoolean("yEs", false)).isEqualTo(true);
		assertThat(ParseString.toBoolean("Y", false)).isEqualTo(true);
		assertThat(ParseString.toBoolean("1", false)).isEqualTo(true);

		assertThat(ParseString.toBoolean("7", false)).isEqualTo(false);
		assertThat(ParseString.toBoolean("oui", false)).isEqualTo(false);

		assertThat(ParseString.toBoolean("false", true)).isEqualTo(false);
		assertThat(ParseString.toBoolean("F", true)).isEqualTo(false);
		assertThat(ParseString.toBoolean("nO", true)).isEqualTo(false);
		assertThat(ParseString.toBoolean("N", true)).isEqualTo(false);
		assertThat(ParseString.toBoolean("0", true)).isEqualTo(false);

		assertThat(ParseString.toBoolean("-1", true)).isEqualTo(true);
		assertThat(ParseString.toBoolean("non", true)).isEqualTo(true);
	}

	@Test
	public void testIsNumber()
	{
		assertThat(ParseString.isNumber("0")).isTrue();
		assertThat(ParseString.isNumber("-1")).isTrue();
		assertThat(ParseString.isNumber("33333")).isTrue();
		assertThat(ParseString.isNumber("-33333")).isTrue();

		assertThat(ParseString.isNumber(" 33333")).isFalse();
		assertThat(ParseString.isNumber("33333 ")).isFalse();
		assertThat(ParseString.isNumber("20KB")).isFalse();
		assertThat(ParseString.isNumber("")).isFalse();
		assertThat(ParseString.isNumber(null)).isFalse();

		assertThat(ParseString.isNumber("7.5")).isTrue();
		assertThat(ParseString.isNumber("-7.5")).isTrue();
		assertThat(ParseString.isNumber("7.5e2")).isTrue();
		assertThat(ParseString.isNumber("-7.5E2")).isTrue();
		assertThat(ParseString.isNumber("7.5E-2")).isTrue();
		assertThat(ParseString.isNumber("-7.5e-2")).isTrue();
		assertThat(ParseString.isNumber("0")).isTrue();
		assertThat(ParseString.isNumber("-1")).isTrue();
		assertThat(ParseString.isNumber("33333")).isTrue();
		assertThat(ParseString.isNumber("-33333")).isTrue();

		assertThat(ParseString.isNumber(" 33333")).isFalse();
		assertThat(ParseString.isNumber("   33333")).isFalse();
		assertThat(ParseString.isNumber("33333 ")).isFalse();
		assertThat(ParseString.isNumber("33333   ")).isFalse();

		assertThat(ParseString.isNumber("20KB")).isFalse();
		assertThat(ParseString.isNumber("")).isFalse();
		assertThat(ParseString.isNumber(null)).isFalse();
	}
}
