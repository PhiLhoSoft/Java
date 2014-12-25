package org.philhosoft.fsa;

public interface TransitionEvaluation<T>
{
	State evaluate(T toEvaluate);
}
