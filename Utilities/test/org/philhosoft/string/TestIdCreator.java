package org.philhosoft.string;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import org.philhosoft.string.IdCreator;


public class TestIdCreator
{
	@Test
	public void testLimits()
	{
		try
		{
			new IdCreator(-1, '*', '*');
			fail("Should have thrown an exception");
		}
		catch (IllegalArgumentException e)
		{
		}
		try
		{
			new IdCreator(2, '*', '*');
			fail("Should have thrown an exception");
		}
		catch (IllegalArgumentException e)
		{
		}
		try
		{
			new IdCreator(13, '*', '*');
			fail("Should have thrown an exception");
		}
		catch (IllegalArgumentException e)
		{
		}
	}

	@Test
	public void testLongToBase64_default()
	{
		IdCreator idCreator = new IdCreator();

		assertThat(idCreator.longToBase64(  0)).isEqualTo("_______");
		assertThat(idCreator.longToBase64(  1)).isEqualTo("______A");
		assertThat(idCreator.longToBase64(  2)).isEqualTo("______B");
		assertThat(idCreator.longToBase64(  4)).isEqualTo("______D");
		assertThat(idCreator.longToBase64(  8)).isEqualTo("______H");
		assertThat(idCreator.longToBase64( 16)).isEqualTo("______P");
		assertThat(idCreator.longToBase64( 32)).isEqualTo("______f");
		assertThat(idCreator.longToBase64( 63)).isEqualTo("______.");
		assertThat(idCreator.longToBase64( 64)).isEqualTo("_____A_");
		assertThat(idCreator.longToBase64(128)).isEqualTo("_____B_");

		// 7 chars -> 11 digits, 5 for random, 6 for time
		assertThat(idCreator.longToBase64(      999999L)).isEqualTo("___CzH.");
		assertThat(idCreator.longToBase64(    1_000000L)).isEqualTo("___CzI_");
		assertThat(idCreator.longToBase64(    1_000001L)).isEqualTo("___CzIA");
		assertThat(idCreator.longToBase64(    1_999999L)).isEqualTo("___GnQ.");
		assertThat(idCreator.longToBase64(10000_999999L)).isEqualTo("_ITFxX.");
		assertThat(idCreator.longToBase64(99999_999999L)).isEqualTo("AcHcte.");

		assertThat(idCreator.longToBase64(0x55AA5AA5)).isEqualTo("_AUpkpk");
		assertThat(idCreator.longToBase64(0x1234567890ABCDEFL)).isEqualTo("mhPp72u");
		assertThat(idCreator.longToBase64(0x7FFFFFFFFFFFFFFFL)).isEqualTo(".......");
	}

	@Test
	public void testLongToBase64_custom()
	{
		IdCreator idCreator = new IdCreator(5, '=', '!');

		assertThat(idCreator.longToBase64(  0)).isEqualTo("=====");
		assertThat(idCreator.longToBase64(  1)).isEqualTo("====A");
		assertThat(idCreator.longToBase64(  2)).isEqualTo("====B");
		assertThat(idCreator.longToBase64(  4)).isEqualTo("====D");
		assertThat(idCreator.longToBase64(  8)).isEqualTo("====H");
		assertThat(idCreator.longToBase64( 16)).isEqualTo("====P");
		assertThat(idCreator.longToBase64( 32)).isEqualTo("====f");
		assertThat(idCreator.longToBase64( 63)).isEqualTo("====!");
		assertThat(idCreator.longToBase64( 64)).isEqualTo("===A=");
		assertThat(idCreator.longToBase64(128)).isEqualTo("===B=");

		// 7 chars -> 8 digits, 4 for random, 4 for time
		assertThat(idCreator.longToBase64(     9999L)).isEqualTo("==BbO");
		assertThat(idCreator.longToBase64(   1_0000L)).isEqualTo("==BbP");
		assertThat(idCreator.longToBase64(   1_0001L)).isEqualTo("==BbQ");
		assertThat(idCreator.longToBase64(   1_9999L)).isEqualTo("==D3e");
		assertThat(idCreator.longToBase64(1000_9999L)).isEqualTo("=lK1O");
		assertThat(idCreator.longToBase64(9999_9999L)).isEqualTo("E8dC!");

		assertThat(idCreator.longToBase64(0x55AA5AA5)).isEqualTo("Upkpk");
		assertThat(idCreator.longToBase64(0x1234567890ABCDEFL)).isEqualTo("Pp72u");
		assertThat(idCreator.longToBase64(0x7FFFFFFFFFFFFFFFL)).isEqualTo("!!!!!");

		IdCreator smallIdCreator = new IdCreator(3, '@', '~');
		assertThat(smallIdCreator.longToBase64(64 * 64 * 64 - 1)).isEqualTo("~~~");
		IdCreator bigIdCreator = new IdCreator(12, '@', '~');
		assertThat(bigIdCreator.longToBase64(9_223_372_036_854_775_807L)).isEqualTo("@G~~~~~~~~~~");

	}

	@Test
	public void testGenerate()
	{
		IdCreator idCreator = new IdCreator(9, '=', '#');

		for (int i = 0; i < 20; i++)
		{
			String id = idCreator.create();
			if (!id.startsWith("="))
					System.out.println(id);
		}
		System.out.println(Long.MAX_VALUE);
	}

	@Test
	public void testIntegerPower()
	{
		assertThat(IdCreator.integerPower(-5, 2)).isEqualTo(25);
		assertThat(IdCreator.integerPower(-5, 3)).isEqualTo(-125);

		assertThat(IdCreator.integerPower(0, 1)).isEqualTo(0);
		assertThat(IdCreator.integerPower(1, 1)).isEqualTo(1);
		assertThat(IdCreator.integerPower(1, 2)).isEqualTo(1);

		assertThat(IdCreator.integerPower(2, 0)).isEqualTo(1);
		assertThat(IdCreator.integerPower(2, 1)).isEqualTo(2);
		assertThat(IdCreator.integerPower(2, 2)).isEqualTo(4);
		assertThat(IdCreator.integerPower(2, 6)).isEqualTo(64);

		assertThat(IdCreator.integerPower(10, 0)).isEqualTo(1);
		assertThat(IdCreator.integerPower(10, 1)).isEqualTo(10);
		assertThat(IdCreator.integerPower(10, 2)).isEqualTo(100);
		assertThat(IdCreator.integerPower(10, 6)).isEqualTo(1_000_000);
	}
}
