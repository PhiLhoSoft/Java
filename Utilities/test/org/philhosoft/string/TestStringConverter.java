package org.philhosoft.string;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TestStringConverter
{
	@Test
	public void testToInt()
	{
		assertThat(StringConverter.toInt("0", 666)).isEqualTo(0);
		assertThat(StringConverter.toInt("-1", 666)).isEqualTo(-1);
		assertThat(StringConverter.toInt("33333", 666)).isEqualTo(33333);
		assertThat(StringConverter.toInt("-33333", 666)).isEqualTo(-33333);

		assertThat(StringConverter.toInt(" 33333", 666)).isEqualTo(666);
		assertThat(StringConverter.toInt("33333 ", 666)).isEqualTo(666);
		assertThat(StringConverter.toInt("7.5", 666)).isEqualTo(666);
		assertThat(StringConverter.toInt("20KB", 666)).isEqualTo(666);
		assertThat(StringConverter.toInt("", 666)).isEqualTo(666);
		assertThat(StringConverter.toInt(null, 666)).isEqualTo(666);
	}

	@Test
	public void testToFloat()
	{
		assertThat(StringConverter.toFloat("7.5", 3.14159f)).isEqualTo(7.5f);
		assertThat(StringConverter.toFloat("-7.5", 3.14159f)).isEqualTo(-7.5f);
		assertThat(StringConverter.toFloat("7.5e2", 3.14159f)).isEqualTo(7.5e2f);
		assertThat(StringConverter.toFloat("-7.5E2", 3.14159f)).isEqualTo(-7.5e2f);
		assertThat(StringConverter.toFloat("7.5E-2", 3.14159f)).isEqualTo(7.5E-2f);
		assertThat(StringConverter.toFloat("-7.5e-2", 3.14159f)).isEqualTo(-7.5e-2f);
		assertThat(StringConverter.toFloat("0", 3.14159f)).isEqualTo(0f);
		assertThat(StringConverter.toFloat("-1", 3.14159f)).isEqualTo(-1f);
		assertThat(StringConverter.toFloat("33333", 3.14159f)).isEqualTo(33333f);
		assertThat(StringConverter.toFloat("-33333", 3.14159f)).isEqualTo(-33333f);
		assertThat(StringConverter.toFloat(" 33333", 3.14159f)).isEqualTo(33333f);
		assertThat(StringConverter.toFloat("   33333", 3.14159f)).isEqualTo(33333f);
		assertThat(StringConverter.toFloat("33333 ", 3.14159f)).isEqualTo(33333f);
		assertThat(StringConverter.toFloat("33333   ", 3.14159f)).isEqualTo(33333f);

		assertThat(StringConverter.toFloat("20KB", 3.14159f)).isEqualTo(3.14159f);
		assertThat(StringConverter.toFloat("", 3.14159f)).isEqualTo(3.14159f);
		assertThat(StringConverter.toFloat(null, 3.14159f)).isEqualTo(3.14159f);
	}

	@Test
	public void testToBoolean()
	{
		assertThat(StringConverter.toBoolean("true", false)).isEqualTo(true);
		assertThat(StringConverter.toBoolean("T", false)).isEqualTo(true);
		assertThat(StringConverter.toBoolean("yEs", false)).isEqualTo(true);
		assertThat(StringConverter.toBoolean("Y", false)).isEqualTo(true);
		assertThat(StringConverter.toBoolean("1", false)).isEqualTo(true);

		assertThat(StringConverter.toBoolean("7", false)).isEqualTo(false);
		assertThat(StringConverter.toBoolean("oui", false)).isEqualTo(false);

		assertThat(StringConverter.toBoolean("false", true)).isEqualTo(false);
		assertThat(StringConverter.toBoolean("F", true)).isEqualTo(false);
		assertThat(StringConverter.toBoolean("nO", true)).isEqualTo(false);
		assertThat(StringConverter.toBoolean("N", true)).isEqualTo(false);
		assertThat(StringConverter.toBoolean("0", true)).isEqualTo(false);

		assertThat(StringConverter.toBoolean("-1", true)).isEqualTo(true);
		assertThat(StringConverter.toBoolean("non", true)).isEqualTo(true);
	}

	@Test
	public void testIsNumber()
	{
		assertThat(StringConverter.isNumber("0")).isTrue();
		assertThat(StringConverter.isNumber("-1")).isTrue();
		assertThat(StringConverter.isNumber("33333")).isTrue();
		assertThat(StringConverter.isNumber("-33333")).isTrue();

		assertThat(StringConverter.isNumber("7.5")).isTrue();
		assertThat(StringConverter.isNumber("-7.5")).isTrue();
		assertThat(StringConverter.isNumber("7.5e2")).isTrue();
		assertThat(StringConverter.isNumber("-7.5E2")).isTrue();
		assertThat(StringConverter.isNumber("7.5E-2")).isTrue();
		assertThat(StringConverter.isNumber("-7.5e-2")).isTrue();

		assertThat(StringConverter.isNumber("0l")).isTrue();
		assertThat(StringConverter.isNumber("-1L")).isTrue();
		assertThat(StringConverter.isNumber("33333l")).isTrue();
		assertThat(StringConverter.isNumber("-33333L")).isTrue();

		assertThat(StringConverter.isNumber("7.5l")).isFalse();
		assertThat(StringConverter.isNumber("-7.5L")).isFalse();
		assertThat(StringConverter.isNumber("7.5e2l")).isFalse();
		assertThat(StringConverter.isNumber("-7.5E2L")).isFalse();
		assertThat(StringConverter.isNumber("7.5E-2l")).isFalse();
		assertThat(StringConverter.isNumber("-7.5e-2L")).isFalse();

		assertThat(StringConverter.isNumber("0f")).isTrue();
		assertThat(StringConverter.isNumber("-1f")).isTrue();
		assertThat(StringConverter.isNumber("33333F")).isTrue();
		assertThat(StringConverter.isNumber("-33333F")).isTrue();

		assertThat(StringConverter.isNumber("7.5f")).isTrue();
		assertThat(StringConverter.isNumber("-7.5F")).isTrue();
		assertThat(StringConverter.isNumber("7.5e2f")).isTrue();
		assertThat(StringConverter.isNumber("-7.5E2F")).isTrue();
		assertThat(StringConverter.isNumber("7.5E-2f")).isTrue();
		assertThat(StringConverter.isNumber("-7.5e-2F")).isTrue();

		assertThat(StringConverter.isNumber("0d")).isTrue();
		assertThat(StringConverter.isNumber("-1d")).isTrue();
		assertThat(StringConverter.isNumber("33333D")).isTrue();
		assertThat(StringConverter.isNumber("-33333D")).isTrue();

		assertThat(StringConverter.isNumber("7.5d")).isTrue();
		assertThat(StringConverter.isNumber("-7.5D")).isTrue();
		assertThat(StringConverter.isNumber("7.5e2d")).isTrue();
		assertThat(StringConverter.isNumber("-7.5E2D")).isTrue();
		assertThat(StringConverter.isNumber("7.5E-2d")).isTrue();
		assertThat(StringConverter.isNumber("-7.5e-2D")).isTrue();

		assertThat(StringConverter.isNumber("7.5e")).isFalse();
		assertThat(StringConverter.isNumber("-7.5E")).isFalse();
		assertThat(StringConverter.isNumber("7.5e-")).isFalse();
		assertThat(StringConverter.isNumber("-7.5E-")).isFalse();
		assertThat(StringConverter.isNumber("7.5E+")).isFalse();
		assertThat(StringConverter.isNumber("-7.5e+")).isFalse();

		assertThat(StringConverter.isNumber(" 33333")).isFalse();
		assertThat(StringConverter.isNumber("   33333")).isFalse();
		assertThat(StringConverter.isNumber("33333 ")).isFalse();
		assertThat(StringConverter.isNumber("33333   ")).isFalse();

		assertThat(StringConverter.isNumber("20KB")).isFalse();
		assertThat(StringConverter.isNumber("20 000")).isFalse();

		assertThat(StringConverter.isNumber("e")).isFalse();
		assertThat(StringConverter.isNumber("E")).isFalse();
		assertThat(StringConverter.isNumber("f")).isFalse();
		assertThat(StringConverter.isNumber("F")).isFalse();
		assertThat(StringConverter.isNumber("d")).isFalse();
		assertThat(StringConverter.isNumber("D")).isFalse();
		assertThat(StringConverter.isNumber("l")).isFalse();
		assertThat(StringConverter.isNumber("L")).isFalse();

		assertThat(StringConverter.isNumber("-")).isFalse();
		assertThat(StringConverter.isNumber("+")).isFalse();
		assertThat(StringConverter.isNumber(".")).isFalse();
		assertThat(StringConverter.isNumber("..")).isFalse();

		assertThat(StringConverter.isNumber("")).isFalse();
		assertThat(StringConverter.isNumber(null)).isFalse();
	}
}
