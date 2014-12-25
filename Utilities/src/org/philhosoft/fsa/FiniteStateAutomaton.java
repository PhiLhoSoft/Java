package org.philhosoft.fsa;

import java.util.HashMap;
import java.util.Map;

public abstract class FiniteStateAutomaton<T>
{
	private Map<State, TransitionEvaluation<T>> automaton = new HashMap<>();
	private State currentState;

	protected void addState(State state, TransitionEvaluation<T> transitionEvaluation)
	{
		automaton.put(state, transitionEvaluation);
	}

	protected void start(State state)
	{
		currentState = state;
	}

	protected abstract T provide();

	protected State next()
	{
		TransitionEvaluation<T> transitionEvaluation = automaton.get(currentState);
		T value = provide();
		currentState = transitionEvaluation.evaluate(value);
		return currentState;
	}

	protected State getState()
	{
		return currentState;
	}
}
