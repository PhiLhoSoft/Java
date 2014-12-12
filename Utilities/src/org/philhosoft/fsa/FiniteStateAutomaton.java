package org.philhosoft.fsa;

import java.util.HashMap;
import java.util.Map;

public class FiniteStateAutomaton
{
	private Map<State, Action> automaton = new HashMap<>();
	private State currentState;

	public void addState(State state, Action action)
	{
		automaton.put(state, action);
	}

	public void start(State state)
	{
		currentState = state;
		next();
	}

	public void next()
	{
		Action action = automaton.get(currentState);
		currentState = action.execute();
	}

	public State getState()
	{
		return currentState;
	}
}
