/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/*
 Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 Copyright notice: For details, see the following file:
 http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
 This program is distributed under the zlib/libpng license.
 Copyright (c) 2005-2014 Philippe Lhoste / PhiLhoSoft
 */
package org.philhosoft.string;


/**
 * Static methods to parse a string to its numerical value without raising exception. If the string isn't well formed,
 * return a default value instead. This is used, for example, to convert values read in a file (properties, etc.).
 *
 * Only a few primitive types are supported, I might add more as the need may appear (trivial but boring...).
 *
 * @author Philippe Lhoste
 */
public class ParseString
{
	private ParseString()
	{
	}

	/**
	 * Parse a string to its numerical int value.
	 *
	 * @return The numeric value of the string if well formed,
	 * @return the given default value otherwise.
	 */
	public static int toInt(String numericString, int defaultValue)
	{
		int val = defaultValue;
		if (numericString != null)
		{
			try
			{
				val = Integer.parseInt(numericString);
			}
			catch (NumberFormatException e)
			{
				// Do nothing, already took default value
			}
		}

		return val;
	}

	/**
	 * Parse a string to its numerical int value.
	 *
	 * @return The numeric value of the string if well formed, 0 otherwise.
	 */
	public static int toInt(String numericString)
	{
		return toInt(numericString, 0);
	}

	/**
	 * Parse a string to its numerical float value.
	 *
	 * @return The numeric value of the string if well formed,
	 * @return the given default value otherwise.
	 */
	public static float toFloat(String numericString, float defaultValue)
	{
		float val = defaultValue;
		if (numericString != null)
		{
			try
			{
				val = Float.parseFloat(numericString);
			}
			catch (NumberFormatException e)
			{
				// Do nothing, already took default value
			}
		}

		return val;
	}

	/**
	 * Parse a string to its numerical float value.
	 *
	 * @return The numeric value of the string if well formed, 0 otherwise.
	 */
	public static float toFloat(String numericString)
	{
		return toFloat(numericString, 0.0f);
	}

	/**
	 * Parse a string to its boolean value. It accepts 0/1, but also English words (true/false, yes/no) and their
	 * abbreviations (t/f, y/n), case insensitive. It doesn't accept localized strings (eg. vrai/faux, oui/non).
	 *
	 * @return The boolean value of the string if well formed,
	 * @return the given default value otherwise.
	 */
	public static boolean toBoolean(String booleanString, boolean defaultValue)
	{
		boolean val = defaultValue;
		if (booleanString != null)
		{
			if (booleanString.equalsIgnoreCase("true") ||
					booleanString.equalsIgnoreCase("t") ||
					booleanString.equalsIgnoreCase("yes") ||
					booleanString.equalsIgnoreCase("y") ||
					booleanString.equals("1"))
			{
				val = true;
			}
			else if (booleanString.equalsIgnoreCase("false") ||
					booleanString.equalsIgnoreCase("f") ||
					booleanString.equalsIgnoreCase("no") ||
					booleanString.equalsIgnoreCase("n") ||
					booleanString.equals("0"))
			{
				val = false;
			}
		}

		return val;
	}

	private static boolean isDigit(char c)
	{
		// ~ return Character.isDigit(c); // If Unicode awareness is required
		return c >= '0' && c <= '9';
	}

	private enum NumberParsingState { INITIAL, SIGN, PREFIXING_DOT, INITIAL_DIGIT, MIDDLE_DOT, EXPONENT, EXPONENT_SIGN, EXPONENT_DIGIT, END };
	public static boolean isNumber(String candidate)
	{
		if (candidate == null) return false;
		int length = candidate.length();
		if (length == 0) return false;
		if (length == 1) return isDigit(candidate.charAt(0));

		NumberParsingState state = NumberParsingState.INITIAL;
		int cursor = 0;
		char c = ' ';
		while (cursor < length)
		{
			c = candidate.charAt(cursor++);
			switch (state)
			{
			case INITIAL:
				if (c == '+' || c == '-')
				{
					state = NumberParsingState.SIGN;
				}
				else if (c == '.')
				{
					state = NumberParsingState.PREFIXING_DOT;
				}
				else if (isDigit(c))
				{
					state = NumberParsingState.INITIAL_DIGIT;
				}
				else
				{
					return false; // Unexpected char
				}
				break;
			case SIGN:
				if (c == '.')
				{
					state = NumberParsingState.PREFIXING_DOT;
				}
				else if (isDigit(c))
				{
					state = NumberParsingState.INITIAL_DIGIT;
				}
				else
				{
					return false; // Unexpected char
				}
				break;
			case PREFIXING_DOT: // After prefixing dot, want digits
				if (isDigit(c))
				{
					state = NumberParsingState.MIDDLE_DOT;
				}
				else
				{
					return false;
				}
				break;
			case INITIAL_DIGIT: // After initial digit, want more digits or dot or exponent or type
				if (c == '.')
				{
					state = NumberParsingState.MIDDLE_DOT;
				}
				else if (c == 'e' || c == 'E')
				{
					state = NumberParsingState.EXPONENT;
				}
				else if (c == 'f' || c == 'F' || c == 'd' || c == 'D' || c == 'l' || c == 'L')
				{
					state = NumberParsingState.END;
				}
				else if (isDigit(c))
				{
					break; // Continue on this state
				}
				else
				{
					return false;
				}
				break;
			case MIDDLE_DOT: // After dot, want more digits or exponent or type
				if (c == 'e' || c == 'E')
				{
					state = NumberParsingState.EXPONENT;
				}
				else if (c == 'f' || c == 'F' || c == 'd' || c == 'D')
				{
					state = NumberParsingState.END;
				}
				else if (isDigit(c))
				{
					break; // Continue on this state
				}
				else
				{
					return false;
				}
				break;
			case EXPONENT: // After exponent
				if (c == '+' || c == '-')
				{
					state = NumberParsingState.EXPONENT_SIGN;
				}
				else if (isDigit(c))
				{
					state = NumberParsingState.EXPONENT_DIGIT;
				}
				else
				{
					return false;
				}
				break;
			case EXPONENT_SIGN: // After exponent's sign
				if (isDigit(c))
				{
					state = NumberParsingState.EXPONENT_DIGIT;
				}
				else
				{
					return false;
				}
				break;
			case EXPONENT_DIGIT: // Exponent's digits
				if (isDigit(c))
				{
					break; // Stay here
				}
				else if (c == 'f' || c == 'F' || c == 'd' || c == 'D')
				{
					state = NumberParsingState.END;
				}
				else
				{
					return false;
				}
				break;
			case END:
				return false; // (at least) one char too much
			}
		}
		// Must stop on one of these states
		return state == NumberParsingState.INITIAL_DIGIT || state == NumberParsingState.MIDDLE_DOT ||
				state == NumberParsingState.EXPONENT_DIGIT || state == NumberParsingState.END;
	}
}
