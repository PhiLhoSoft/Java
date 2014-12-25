package org.philhosoft.string;

import org.philhosoft.fsa.FiniteStateAutomaton;
import org.philhosoft.fsa.State;
import org.philhosoft.fsa.TransitionEvaluation;
import org.philhosoft.parser.StringWalker;

public class NumberAsStringParser extends FiniteStateAutomaton<Character>
{
	private enum NumberParsingState implements State
	{ INITIAL, SIGN, PREFIXING_DOT, INITIAL_DIGIT, MIDDLE_DOT, EXPONENT, EXPONENT_SIGN, EXPONENT_DIGIT, END, ERROR }

	private StringWalker walker;

	private NumberAsStringParser(String candidate)
	{
		walker = new StringWalker(candidate);
		addStates();
	}

	@Override
	protected Character provide()
	{
		return walker.current();
	}

	/**
	 * Checks if the given candidate string is a valid number (by Java rules).
	 *
	 * @return true if the string is valid Java number
	 */
	public static boolean check(String candidate)
	{
		if (candidate == null || candidate.isEmpty())
			return false;

		NumberAsStringParser parser = new NumberAsStringParser(candidate);
		return parser.check();
	}

	private boolean check()
	{
		start(NumberParsingState.INITIAL);
		while (walker.hasMore())
		{
			NumberParsingState state = (NumberParsingState) next();
			if (state == NumberParsingState.ERROR)
				return false;

			walker.forward();
		}

		NumberParsingState state = (NumberParsingState) getState();
		return state == NumberParsingState.INITIAL_DIGIT || state == NumberParsingState.MIDDLE_DOT ||
				state == NumberParsingState.EXPONENT_DIGIT || state == NumberParsingState.END;
	}

	private void addStates()
	{
		addState(NumberParsingState.INITIAL, new InitialAction());
		addState(NumberParsingState.SIGN, new SignAction());
		addState(NumberParsingState.PREFIXING_DOT, new PrefixingDotAction());
		addState(NumberParsingState.INITIAL_DIGIT, new InitialDigitAction());
		addState(NumberParsingState.MIDDLE_DOT, new MiddleDotAction());
		addState(NumberParsingState.EXPONENT, new ExponentAction());
		addState(NumberParsingState.EXPONENT_SIGN, new ExponentSignAction());
		addState(NumberParsingState.EXPONENT_DIGIT, new ExponentDigitAction());
		addState(NumberParsingState.END, new EndAction());
	}

	private class InitialAction implements TransitionEvaluation<Character>
	{
		@Override
		public State evaluate(Character c)
		{
			if (c == '+' || c == '-')
				return NumberParsingState.SIGN;

			if (c == '.')
				return NumberParsingState.PREFIXING_DOT;

			if (isAsciiDigit(c))
				return NumberParsingState.INITIAL_DIGIT;

			return NumberParsingState.ERROR; // Unexpected char
		}
	}
	private class SignAction implements TransitionEvaluation<Character>
	{
		@Override
		public State evaluate(Character c)
		{
			if (c == '.')
				return NumberParsingState.PREFIXING_DOT;

			if (isAsciiDigit(c))
				return NumberParsingState.INITIAL_DIGIT;

			return NumberParsingState.ERROR; // Unexpected char
		}
	}
	private class PrefixingDotAction implements TransitionEvaluation<Character>
	{
		@Override
		public State evaluate(Character c)
		{
			// After prefixing dot, want digits.

			if (isAsciiDigit(c))
				return NumberParsingState.MIDDLE_DOT;

			return NumberParsingState.ERROR; // Unexpected char
		}
	}
	private class InitialDigitAction implements TransitionEvaluation<Character>
	{
		@Override
		public State evaluate(Character c)
		{
			// After initial digit, want more digits or dot or exponent or type

			if (c == '.')
				return NumberParsingState.MIDDLE_DOT;

			if (c == 'e' || c == 'E')
				return NumberParsingState.EXPONENT;

			if (walker.matchOneOf('f', 'F', 'd', 'D', 'l', 'L'))
				return NumberParsingState.END;

			if (isAsciiDigit(c))
				return NumberParsingState.INITIAL_DIGIT; // Check for more

			return NumberParsingState.ERROR; // Unexpected char
		}
	}
	private class MiddleDotAction implements TransitionEvaluation<Character>
	{
		@Override
		public State evaluate(Character c)
		{
			// After dot, want more digits or exponent or type

			if (isAsciiDigit(c))
				return NumberParsingState.MIDDLE_DOT; // Check for more

			if (walker.matchOneOf('f', 'F', 'd', 'D'))
				return NumberParsingState.END;

			if (c == 'e' || c == 'E')
				return NumberParsingState.EXPONENT;

			return NumberParsingState.ERROR; // Unexpected char
		}
	}
	private class ExponentAction implements TransitionEvaluation<Character>
	{
		@Override
		public State evaluate(Character c)
		{
			// After exponent, want sign or digit

			if (c == '+' || c == '-')
				return NumberParsingState.EXPONENT_SIGN;

			if (isAsciiDigit(c))
				return NumberParsingState.EXPONENT_DIGIT;

			return NumberParsingState.ERROR; // Unexpected char
		}
	}
	private class ExponentSignAction implements TransitionEvaluation<Character>
	{
		@Override
		public State evaluate(Character c)
		{
			// After exponent' sign, want digit

			if (isAsciiDigit(c))
				return NumberParsingState.EXPONENT_DIGIT;

			return NumberParsingState.ERROR; // Unexpected char
		}
	}
	private class ExponentDigitAction implements TransitionEvaluation<Character>
	{
		@Override
		public State evaluate(Character c)
		{
			if (isAsciiDigit(c))
				return NumberParsingState.EXPONENT_DIGIT; // Want more

			if (walker.matchOneOf('f', 'F', 'd', 'D'))
				return NumberParsingState.END;

			return NumberParsingState.ERROR; // Unexpected char
		}
	}
	private class EndAction implements TransitionEvaluation<Character>
	{
		@Override
		public State evaluate(Character c)
		{
			return NumberParsingState.ERROR; // Should not have a character beyond the end
		}
	}

	/**
	 * Tells if given char is an Ascii digit.
	 * <p>
	 * More restrictive, faster than Character.isDigit(c) which should be used if Unicode awareness is required.
	 */
	public static boolean isAsciiDigit(char c)
	{
		// ~ return
		return c >= '0' && c <= '9';
	}
}
