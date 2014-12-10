package org.philhosoft.string;

/**
 * Allows to create ids with some control on their length and characters they use.
 * <p>
 * Ids are generated using the current time (with millisecond precision) and random numbers.<br>
 * An id is made of letters (upper and lower case), of digits (0 to 9) and of two characters
 * (used to reach a total number of 64 different characters), using a method similar to Base64 encoding.
 */
public class IdCreator
{
	// Defaults to short id, with URL-safe chars...
	private static final int DEFAULT_ID_LENGTH = 7;
	private static final char DEFAULT_ZERO_CHAR = '_';
	private static final char DEFAULT_MAX_CHAR = '.';

	private final int idLength;
	private final char zeroChar;
	private final char maxChar;
	private final long randomFactor;
	private final long timeFactor;

	/**
	 * Creates an id creator with default parameters: length 7, chars are underscore and dot, URL-safe.
	 */
	public IdCreator()
	{
		this(DEFAULT_ID_LENGTH, DEFAULT_ZERO_CHAR, DEFAULT_MAX_CHAR);
	}
	/**
	 * Creates an id creator with given parameters.
	 *
	 * @param idLength  length of the generated ids. Must be 3 or more, up to 12.
	 * @param char1  first supplementary character
	 * @param char2  second supplementary character
	 */
	public IdCreator(int idLength, char char1, char char2)
	{
		if (idLength < 3 || idLength > 12)
			throw new IllegalArgumentException("idLength is below 3 or above 12.");
		// Long.MAX_VALUE is 9_223_372_036_854_775_807, 19 digits, so at most 12 characters

		this.idLength = idLength;
		this.zeroChar = char1;
		this.maxChar = char2;

		// 3 digits will give 2 id characters (4 bits to 6 bits)
		int digitNb = (int) Math.ceil(idLength * 1.5);
		int randomDigitNb = digitNb / 2;
		int timeDigitNb = digitNb - randomDigitNb;
		randomFactor = integerPower(10, randomDigitNb + 1); // +1 to get a little overshot, for more regular last char
		timeFactor = integerPower(10, timeDigitNb);
	}

	/**
	 * Better than (long) Math.pow().
	 * http://stackoverflow.com/questions/101439/the-most-efficient-way-to-implement-an-integer-based-power-function-powint-int
	 */
	public static long integerPower(int base, int exponent)
	{
		if (exponent < 0)
			return 1;
	    int result = 1;
	    while (exponent != 0)
	    {
	        if ((exponent & 1) == 1) // Odd
	        {
	            result *= base;
	        }
	        exponent >>= 1;
	        base *= base;
	    }

	    return result;
	}

	/**
	 * Creates a string id from current time and random number.
	 */
	public String create()
	{
		long ts = System.currentTimeMillis() % timeFactor;
		long r = (long) Math.floor(Math.random() * randomFactor);
		long rts = r * timeFactor + ts;
//		System.out.print(rts + "\t" + Long.toHexString(rts) + "\t");
		return longToBase64(rts);
	}

	// VisibleForTesting
	// Note: this "base 64" isn't the Adobe's Base64...
	String longToBase64(long value)
	{
		char[] b64 = new char[idLength];
		final long mask = 0x3F; // 6 bits
		for (int idx = 0; idx < b64.length; idx++)
		{
			int b = (int) (value & mask);
			value >>= 6;
			setBase64Digit(b64, idx, b);
		}
		return new String(b64);
	}

	private void setBase64Digit(char[] b64, int idx, int value)
	{
		int v = 0;
		if (value == 0)
		{
			v = zeroChar;
		}
		else if (value < 27)
		{
			v = 'A' + value - 1;
		}
		else if (value < 53)
		{
			v = 'a' + value - 27;
		}
		else if (value < 63)
		{
			v = '0' + value - 53;
		}
		else if (value == 63)
		{
			v = maxChar;
		}
		b64[b64.length - 1 - idx] = (char) v;
	}
}
