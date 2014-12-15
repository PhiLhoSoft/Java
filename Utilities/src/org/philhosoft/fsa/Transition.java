package org.philhosoft.fsa;

public interface Transition<T>
{
	State evaluate(T toEvaluate);
}
