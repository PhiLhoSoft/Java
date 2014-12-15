package org.philhosoft.fsa;

import java.util.HashMap;
import java.util.Map;

public abstract class FiniteStateAutomaton<T>
{
	private Map<State, Transition<T>> automaton = new HashMap<>();
	private State currentState;

	protected void addState(State state, Transition<T> transition)
	{
		automaton.put(state, transition);
	}

	protected void start(State state)
	{
		currentState = state;
	}

	protected abstract T provide();

	protected State next()
	{
		Transition<T> transition = automaton.get(currentState);
		T value = provide();
		currentState = transition.evaluate(value);
		return currentState;
	}

	protected State getState()
	{
		return currentState;
	}
}
