package org.philhosoft.string;

import java.util.HashMap;
import java.lang.Double;

/************************************************************************
 * <i>Mathematical expression evaluator.</i>
 *  Supports the following functions:
 * +, -, *, /, ^, %, cos, sin, tan, acos, asin, atan, sqrt, sqr, log, min, max, ceil, floor, abs, neg, rndr.<br>
 * When the Eval() is called, a Double object is returned. If it returns null, an error occurred.<p>
 * <pre>
 * Sample:
 * ExpressionEvaluator m = new ExpressionEvaluator("-5-6/(-2) + sqr(15+x)");
 * m.addVariable("x", 15.1d);
 * System.out.println( m.Eval() );
 * </pre>
 * @version 1.1
 * @author 	The-Son LAI, <a href="mailto:Lts@writeme.com">Lts@writeme.com</a>
 * @date	 April 2001
 ************************************************************************/
// Yes, yet another expression evaluator. Mostly for Java 1.5, as with Java 1.6 we can use ScriptEngine...
public class ExpressionEvaluator
{
   	protected static Operator[] s_operators =
	{
		// Base on http://www.uni-bonn.de/~manfear/javaoperators.php
		// Operator symbol, type (unary/binary) and priority
		new Operator("+", OperatorType.UNARY, 2), // unary plus
		new Operator("-", OperatorType.UNARY, 2), // OperatorType.UNAry minus
		new Operator("~", OperatorType.UNARY, 2), // bitwise NOT
//~ 			new Operator("!", OperatorType.UNARY, 2), // boolean (logical) NOT
		new Operator("*", OperatorType.BINARY, 3), // multiplication
		new Operator("/", OperatorType.BINARY, 3), // division
		new Operator("%", OperatorType.BINARY, 3), // remainder
		new Operator("+", OperatorType.BINARY, 4), // addition
		new Operator("-", OperatorType.BINARY, 4), // subtraction
		new Operator("<<", OperatorType.BINARY, 5), // signed bit shift left
		new Operator(">>", OperatorType.BINARY, 5), // signed bit shift right
		new Operator(">>>", OperatorType.BINARY, 5), // unsigned bit shift right
//~ 			new Operator("<", OperatorType.BINARY, 6), // less than
//~ 			new Operator("<=", OperatorType.BINARY, 6), // less than or equal to
//~ 			new Operator(">", OperatorType.BINARY, 6), // greater than
//~ 			new Operator(">=", OperatorType.BINARY, 6), // greater than or equal to
//~ 			new Operator("==", OperatorType.BINARY, 7), // equal to
//~ 			new Operator("!=", OperatorType.BINARY, 7), // not equal to
		new Operator("&", OperatorType.BINARY, 8), // bitwise AND
//~ 			new Operator("&", OperatorType.BINARY, 8), // boolean (logical) XOR
		new Operator("^", OperatorType.BINARY, 9), // bitwise AND
//~ 			new Operator("^", OperatorType.BINARY, 9), // boolean (logical) XOR
		new Operator("|", OperatorType.BINARY, 10), // bitwise OR
//~ 			new Operator("|", OperatorType.BINARY, 10), // boolean (logical) OR
//~ 			new Operator("&&", OperatorType.BINARY, 11), // boolean (logical) AND with shortcut
//~ 			new Operator("||", OperatorType.BINARY, 12), // boolean (logical) OR with shortcut
//~ 			new Operator("?", OperatorType.BINARY, 13), // conditional part 1
//~ 			new Operator(":", OperatorType.BINARY, 13), // conditional part 2
//~ 			new Operator("=", OperatorType.BINARY, 14), // assignment (and comOperatorType.BINed assignments)
		new Operator("abs", OperatorType.FUNCTION_1, 20),
		new Operator("signum", OperatorType.FUNCTION_1, 20),
		new Operator("floor", OperatorType.FUNCTION_1, 20),
		new Operator("ceil", OperatorType.FUNCTION_1, 20),
		new Operator("round", OperatorType.FUNCTION_1, 20),
		new Operator("sin", OperatorType.FUNCTION_1, 20),
		new Operator("cos", OperatorType.FUNCTION_1, 20),
		new Operator("tan", OperatorType.FUNCTION_1, 20),
		new Operator("asin", OperatorType.FUNCTION_1, 20),
		new Operator("acos", OperatorType.FUNCTION_1, 20),
		new Operator("atan", OperatorType.FUNCTION_1, 20),
		new Operator("atan2", OperatorType.FUNCTION_2, 20),
		new Operator("sinh", OperatorType.FUNCTION_1, 20),
		new Operator("cosh", OperatorType.FUNCTION_1, 20),
		new Operator("tanh", OperatorType.FUNCTION_1, 20),
		new Operator("sqr", OperatorType.FUNCTION_1, 20), // square
		new Operator("sqrt", OperatorType.FUNCTION_1, 20), // square root
		new Operator("cbrt", OperatorType.FUNCTION_1, 20), // cube root
		new Operator("log10", OperatorType.FUNCTION_1, 20),
		new Operator("log", OperatorType.FUNCTION_1, 20),
		new Operator("exp", OperatorType.FUNCTION_1, 20),
		new Operator("pow", OperatorType.FUNCTION_2, 20),
		new Operator("max", OperatorType.FUNCTION_2, 20),
		new Operator("min", OperatorType.FUNCTION_2, 20),
		new Operator("random", OperatorType.FUNCTION_0, 20),
		new Operator("toDegrees", OperatorType.FUNCTION_1, 20),
		new Operator("toRadians", OperatorType.FUNCTION_1, 20),
	};

   	private Node m_node;
	private String m_expression;
	private HashMap<String, Double> m_variables = new HashMap<String, Double>();

	/**
	 * To run the program in command line.
	 * Usage: java ExpressionEvaluator.main "math expression"
	 */
	public static void main(String[] args)
	{
		if ( args == null || args.length != 1)
		{
			System.err.println("Mathematical Expression Evaluator");
			System.err.println("Usage: java ExpressionEvaluator.main \"math expression\"");
			System.exit(0);
		}

		try
		{
		   	ExpressionEvaluator m = new ExpressionEvaluator(args[0]);
			System.out.println(m.Eval());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Creates an empty ExpressionEvaluator.
	 * Use SetExpression(String s) to assign an expression to it.
	 */
	public ExpressionEvaluator()
	{
		Init();
	}

	/**
	 * Creates a ExpressionEvaluator and assign the expression.
	 */
	public ExpressionEvaluator(String s)
	{
		Init();
		SetExpression(s);
	}

	private void Init()
	{
		m_variables.put("E", Math.E);
		m_variables.put("PI", Math.PI);
	}

	/**
	 * Adds a variable and its value in the ExpressionEvaluator.
	 */
	public void AddVariable(String variable, double value)
	{
		m_variables.put(variable, value);
	}

	/**
	 * Sets the expression.
	 */
	public void SetExpression(String expression)
	{
		m_expression = expression;
	}

	/**
	 * Resets the evaluator but keep the variables.
	 */
	public void Reset()
	{
		m_node = null;
		m_expression = null;
	}

	/**
	 * Resets the evaluator.
	 */
	public void ResetAll()
	{
		Reset();
		m_variables.clear();
		Init();
	}

	/**
	 * Dump the binary tree for debug.
	 */
	public void Dump()
	{
		try
		{
			m_node = new Node(m_expression);
			m_node.Dump();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Evaluates and returns the value of the expression.
	 */
	public double Eval()
	{
	   	if (m_expression == null)
			return 0;	// Should throw exception

		try
		{
			m_node = new Node(m_expression);
			return Evaluate(m_node);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return 0;
		}
	}

	private static double Evaluate(Node node)
	{
		if (node.HasOperator() && node.HasChild() )
		{
			if (node.GetOperator().GetType() == OperatorType.UNARY) // Unary
			{
				node.SetValue(EvaluateExpression(
						node.GetOperator(),
						Evaluate(node.GetLeft()),
						0));
			}
			else if (node.GetOperator().GetType() == OperatorType.BINARY)
			{
				node.SetValue(EvaluateExpression(
						node.GetOperator(),
						Evaluate(node.GetLeft()),
						Evaluate(node.GetRight())));
			}
		}
		return node.Eval();
	}

	private static double EvaluateExpression(Operator operator, double d1, double d2)
	{
		String op = operator.GetOperator();
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

	/***
	 * gets the variable's value that was assigned previously
	 */
	public Double getVariable(String s)
	{
		return (Double) m_variables.get(s);
	}

	protected Double getDouble(String s)
	{
		if ( s == null ) return null;

		Double res = null;
		try
		{
			res = new Double(Double.parseDouble(s));
		}
		catch(Exception e)
		{
			return getVariable(s);
		}

		return res;
	}

	protected Operator[] getOperators()
	{
		return s_operators;
	}

	protected static class Operator
	{
		private String m_operator;
		private OperatorType m_type;
		private int m_priority;

		public Operator(String o, OperatorType t, int p)
		{
			m_operator = o;
			m_type = t;
			m_priority = p;
		}

		public String GetOperator()
		{
			return m_operator;
		}

		public void SetOperator(String o)
		{
			m_operator = o;
		}

		public OperatorType GetType()
		{
			return m_type;
		}

		public int GetPriority()
		{
			return m_priority;
		}
	}

	protected static class Node
	{
		public String  	nString;
		public Operator nOperator;
		public Node 	nLeft;
		public Node 	nRight;
		public Node 	nParent;
		public int		nLevel;
		public Double  	nValue;

		public Node(String s) throws Exception
		{
			init(null, s, 0);
		}

		public void Dump()
		{
			// TODO Auto-generated method stub

		}

		public Node(Node parent, String s, int level) throws Exception
		{
			init(parent, s, level);
		}

		private void init(Node parent, String s, int level) throws Exception
		{
			s = RemoveIllegalCharacters(s);
			s = RemoveBrackets(s);
			s = AddZero(s);
			if ( CheckBrackets(s) != 0 ) throw new Exception("Wrong number of brackets in [" + s + "]");

			nParent				= parent;
			nString 			= s;
			nValue				= 0d;//!!!getDouble(s);
			nLevel 				= level;
			int sLength  		= s.length();
			int inBrackets		= 0;
			int startOperator   = 0;

			for (int i=0; i<sLength; i++)
			{
				if ( s.charAt(i) == '(' )
					inBrackets++;
				else if ( s.charAt(i) == ')' )
					inBrackets--;
				else
				{
					// the expression must be at "root" level
					if ( inBrackets == 0 )
					{
						Operator o = GetOperator(nString,i);
						if ( o != null )
						{
							// if first operator or lower priority operator
							if ( nOperator == null || nOperator.GetPriority() >= o.GetPriority() )
							{
								nOperator 		= o;
								startOperator 	= i;
							}
						}
					}
				}
			}

			if ( nOperator != null )
			{
				// one operand, should always be at the beginning
				if ( startOperator==0 && nOperator.GetType() == OperatorType.UNARY )
				{
					// the brackets must be ok
					if ( CheckBrackets( s.substring( nOperator.GetOperator().length() ) ) == 0 )
					{
						nLeft  = new Node( this, s.substring( nOperator.GetOperator().length() ) , nLevel + 1);
						nRight = null;
						return;
					}
					else
						throw new Exception("Error during parsing... missing brackets in [" + s + "]");
				}
				// two operands
				else if ( startOperator > 0 && nOperator.GetType() == OperatorType.BINARY )
				{
					nOperator = nOperator;
					nLeft 	= new Node( this, s.substring(0,  startOperator), nLevel + 1 );
					nRight 	= new Node( this, s.substring(startOperator + nOperator.GetOperator().length()), nLevel + 1);
				}
			}
		}

		private Operator GetOperator(String s, int start)
		{
			String temp = s.substring(start);
			temp = GetNextWord(temp);
			for (int i=0; i<s_operators.length; i++)
			{
				if ( temp.startsWith(s_operators[i].GetOperator()) )
					return s_operators[i];
			}
			return null;
		}

		private String GetNextWord(String s)
		{
			int sLength = s.length();
			for (int i=1; i<sLength; i++)
			{
				char c = s.charAt(i);
				if ( (c > 'z' || c < 'a') && (c > '9' || c < '0') )
					return s.substring(0, i);
			}
			return s;
		}

		/***
		 * checks if there is any missing brackets
		 * @return true if s is valid
		 */
		protected int CheckBrackets(String s)
		{
			int sLength  	= s.length();
			int inBracket   = 0;

			for (int i=0; i<sLength; i++)
			{
				if	  ( s.charAt(i) == '(' && inBracket >= 0 )
					inBracket++;
				else if ( s.charAt(i) == ')' )
					inBracket--;
			}

			return inBracket;
		}

		/***
		 * returns a string that doesn't start with a + or a -
		 */
		protected String AddZero(String s)
		{
			if ( s.startsWith("+") || s.startsWith("-") )
			{
				int sLength  	= s.length();
				for (int i=0; i<sLength; i++)
				{
					if ( GetOperator(s, i) != null )
						return "0" + s;
				}
			}

			return s;
		}

		/***
		 * displays the tree of the expression
		 */
		public void trace()
		{
			String op = nOperator == null ? " " : nOperator.GetOperator() ;
			_D( op + " : " + getString() );
			if (HasChild())
			{
				if (HasLeft())
					GetLeft().trace();
				if (HasRight())
					GetRight().trace();
			}
		}

		protected boolean HasChild()
		{
			return nLeft != null || nRight != null;
		}

		protected boolean HasOperator()
		{
			return nOperator != null;
		}

		protected boolean HasLeft()
		{
			return nLeft != null;
		}

		protected Node GetLeft()
		{
			return nLeft;
		}

		protected boolean HasRight()
		{
			return nRight != null;
		}

		protected Node GetRight()
		{
			return nRight;
		}

		protected Operator GetOperator()
		{
			return nOperator;
		}

		protected int getLevel()
		{
			return nLevel;
		}

		protected double Eval()
		{
			return nValue;
		}

		protected void SetValue(double d)
		{
			nValue = d;
		}

		protected String getString()
		{
			return nString;
		}

		/***
		 * Removes spaces, tabs and brackets at the beginning
		 */
		public String RemoveBrackets(String s)
		{
			String res = s;
			if ( s.length() > 2 && res.startsWith("(") && res.endsWith(")") && CheckBrackets(s.substring(1,s.length()-1)) == 0 )
			{
				res = res.substring(1, res.length()-1 );
			}
			if ( res != s )
				return RemoveBrackets(res);
			else
		 	   return res;
		}

		/***
		 * Removes illegal characters
		 */
		public String RemoveIllegalCharacters(String s)
		{
			char[] illegalCharacters = { ' ' };
			String res = s;

			for ( int j=0; j<illegalCharacters.length; j++)
			{
				int i = res.lastIndexOf(illegalCharacters[j], res.length());
				while ( i != -1 )
				{
					String temp = res;
					res = temp.substring(0,i);
					res += temp.substring(i + 1);
					i = res.lastIndexOf(illegalCharacters[j], s.length());
				}
			}
			return res;
		}

		protected void _D(String s)
		{
			String nbSpaces = "";
			for (int i=0; i<nLevel; i++) nbSpaces += "  ";
			System.out.println(nbSpaces + "|" + s);
		}
	}

	protected static enum OperatorType
	{
		UNARY(1), BINARY(2), FUNCTION_0(0), FUNCTION_1(1), FUNCTION_2(2);

		private final int m_cardinality;

		OperatorType(int cardinality)
		{
			m_cardinality = cardinality;
		}
	}

	protected static void _D(String s)
	{
		System.err.println(s);
	}
}
