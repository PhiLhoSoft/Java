package org.philhosoft.string;

import org.philhosoft.fsa.Action;
import org.philhosoft.fsa.FiniteStateAutomaton;
import org.philhosoft.fsa.State;

public class NumberParser extends FiniteStateAutomaton
{
	private enum NumberParsingState implements State
	{ INITIAL, SIGN, PREFIXING_DOT, INITIAL_DIGIT, MIDDLE_DOT, EXPONENT, EXPONENT_SIGN, EXPONENT_DIGIT, END }

	private String candidate;;

	public NumberParser(String candidate)
	{
		this.candidate = candidate;
		addStates();
	}

	public boolean check()
	{
		return true;
	}

	private void addStates()
	{
		addState(NumberParsingState.INITIAL, new InitialAction());
	}

	private class InitialAction implements Action
	{
		@Override
		public State execute()
		{
			// TODO Auto-generated method stub
			return null;
		}
	}
}
