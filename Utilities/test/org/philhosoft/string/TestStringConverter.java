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
}
