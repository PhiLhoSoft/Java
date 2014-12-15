package org.philhosoft.string;

import org.philhosoft.fsa.Action;
import org.philhosoft.fsa.FiniteStateAutomaton;
import org.philhosoft.fsa.State;
import org.philhosoft.string.parser.StringWalker;

public class NumberParser extends FiniteStateAutomaton
{
	private enum NumberParsingState implements State
	{ INITIAL, SIGN, PREFIXING_DOT, INITIAL_DIGIT, MIDDLE_DOT, EXPONENT, EXPONENT_SIGN, EXPONENT_DIGIT, END }

	private String candidate;
	private StringWalker walker;

	public NumberParser(String candidate)
	{
		this.candidate = candidate;
		walker = new StringWalker(candidate);
		addStates();
	}

	public boolean check()
	{
		return true;
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

	private class InitialAction implements Action
	{
		@Override
		public State execute()
		{
			return null;
		}
	}
	private class SignAction implements Action
	{
		@Override
		public State execute()
		{
			return null;
		}
	}
	private class PrefixingDotAction implements Action
	{
		@Override
		public State execute()
		{
			return null;
		}
	}
	private class InitialDigitAction implements Action
	{
		@Override
		public State execute()
		{
			return null;
		}
	}
	private class MiddleDotAction implements Action
	{
		@Override
		public State execute()
		{
			return null;
		}
	}
	private class ExponentAction implements Action
	{
		@Override
		public State execute()
		{
			return null;
		}
	}
	private class ExponentSignAction implements Action
	{
		@Override
		public State execute()
		{
			return null;
		}
	}
	private class ExponentDigitAction implements Action
	{
		@Override
		public State execute()
		{
			return null;
		}
	}
	private class EndAction implements Action
	{
		@Override
		public State execute()
		{
			return null;
		}
	}
}
