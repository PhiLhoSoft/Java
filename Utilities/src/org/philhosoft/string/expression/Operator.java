/*
 * org.philhosoft.*: A collection of utility classes for Java.
 * Expression evaluation.
 */
/* File history:
 *  1.01.000 -- 2011/01/17 (PL) -- Normalize case of methods
 *  1.00.000 -- 2009/11/24 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2009-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.string.expression;

import java.util.HashMap;
import java.lang.Double;

public static class Operators
{
   	protected static Operator[] s_operators =
	{
		// Based on http://www.uni-bonn.de/~manfear/javaoperators.php
		// Commented out boolean operations (working only on numbers)
		// Operator symbol, type (unary/binary) and priority
		new UnaryOperator("+", 2), // unary plus
		new UnaryOperator("-", 2), // unary minus
		new UnaryOperator("~", 2), // bitwise NOT
//~ 			new UnaryOperator("!", 2), // boolean (logical) NOT
		new BinaryOperator("*", 3), // multiplication
		new BinaryOperator("/", 3), // division
		new BinaryOperator("%", 3), // remainder
		new BinaryOperator("+", 4), // addition
		new BinaryOperator("-", 4), // subtraction
		new BinaryOperator("<<", 5), // signed bit shift left
		new BinaryOperator(">>", 5), // signed bit shift right
		new BinaryOperator(">>>", 5), // unsigned bit shift right
//~ 			new BinaryOperator("<", 6), // less than
//~ 			new BinaryOperator("<=", 6), // less than or equal to
//~ 			new BinaryOperator(">", 6), // greater than
//~ 			new BinaryOperator(">=", 6), // greater than or equal to
//~ 			new BinaryOperator("==", 7), // equal to
//~ 			new BinaryOperator("!=", 7), // not equal to
		new BinaryOperator("&", 8), // bitwise AND
//~ 			new BinaryOperator("&", 8), // boolean (logical) XOR
		new BinaryOperator("^", 9), // bitwise AND
//~ 			new BinaryOperator("^", 9), // boolean (logical) XOR
		new BinaryOperator("|", 10), // bitwise OR
//~ 			new BinaryOperator("|", 10), // boolean (logical) OR
//~ 			new BinaryOperator("&&", 11), // boolean (logical) AND with shortcut
//~ 			new BinaryOperator("||", 12), // boolean (logical) OR with shortcut
//~ 			new BinaryOperator("?", 13), // conditional part 1
//~ 			new BinaryOperator(":", 13), // conditional part 2
//~ 			new BinaryOperator("=", 14), // assignment (and combined assignments)
		new Function1Operator("abs", 20),
		new Function1Operator("signum", 20),
		new Function1Operator("floor", 20),
		new Function1Operator("ceil", 20),
		new Function1Operator("round", 20),
		new Function1Operator("sin", 20),
		new Function1Operator("cos", 20),
		new Function1Operator("tan", 20),
		new Function1Operator("asin", 20),
		new Function1Operator("acos", 20),
		new Function1Operator("atan", 20),
		new Function2Operator("atan2", 20),
		new Function1Operator("sinh", 20),
		new Function1Operator("cosh", 20),
		new Function1Operator("tanh", 20),
		new Function1Operator("sqr", 20), // square
		new Function1Operator("sqrt", 20), // square root
		new Function1Operator("cbrt", 20), // cube root
		new Function1Operator("log10", 20),
		new Function1Operator("log", 20),
		new Function1Operator("exp", 20),
		new Function2Operator("pow", 20),
		new Function2Operator("max", 20),
		new Function2Operator("min", 20),
		new Function0Operator("random", 20),
		new Function1Operator("toDegrees", 20),
		new Function1Operator("toRadians", 20),
	};

	// Not pretty-pretty, but seems like a reasonable compromize between pure procedural solution
	// and creating an anonymous class for each operator!
	private static double evaluateExpression(Operator operator, double d1, double d2)
	{
		String op = operator.getOperator();
		double res = 0;

		if      (op.equals("+"))     res =  d1 + d2;
		else if (op.equals("-"))     res =  d1 - d2;
		else if (op.equals("*"))     res =  d1 * d2;
		else if (op.equals("/"))     res =  d1 / d2;
		else if (op.equals("^"))     res =  Math.pow(d1, d2);
		else if (op.equals("%"))     res =  d1 % d2;
		else if (op.equals("&"))     res =  d1 + d2; // todo
		else if (op.equals("|"))     res =  d1 + d2; // todo
		else if (op.equals("cos"))   res =  Math.cos(d1);
		else if (op.equals("sin"))   res =  Math.sin(d1);
		else if (op.equals("tan"))   res =  Math.tan(d1);
		else if (op.equals("acos"))  res =  Math.acos(d1);
		else if (op.equals("asin"))  res =  Math.asin(d1);
		else if (op.equals("atan"))  res =  Math.atan(d1);
		else if (op.equals("sqr"))   res =  d1 * d1;
		else if (op.equals("sqrt"))  res =  Math.sqrt(d1);
		else if (op.equals("log"))   res =  Math.log(d1);
		else if (op.equals("min"))   res =  Math.min(d1, d2);
		else if (op.equals("max"))   res =  Math.max(d1, d2);
		else if (op.equals("exp"))   res =  Math.exp(d1);
		else if (op.equals("floor")) res =  Math.floor(d1);
		else if (op.equals("ceil"))  res =  Math.ceil(d1);
		else if (op.equals("abs"))   res =  Math.abs(d1);
		else if (op.equals("neg"))   res =  - d1;
		else if (op.equals("rnd"))   res =  Math.random() * d1;

		return res;
	}
}

abstract class Operator
{
	private String m_operator;
	private int m_priority;

	public Operator(String o, int p)
	{
		m_operator = o;
		m_priority = p;
	}

	public String getOperator()
	{
		return m_operator;
	}

	public String getPriority()
	{
		return m_priority;
	}

	public double evaluate();
}
class UnaryOperator extends Operator
{
}
class BinaryOperator extends Operator
{
}
class Function0Operator extends Operator
{
}
class Function1Operator extends Operator
{
}
class Function2Operator extends Operator
{
}
