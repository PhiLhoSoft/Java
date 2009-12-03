package org.philhosoft.string.expression;

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

		protected void _D(String s)
		{
			String nbSpaces = "";
			for (int i=0; i<nLevel; i++) nbSpaces += "  ";
			System.out.println(nbSpaces + "|" + s);
		}
	}

	protected static void _D(String s)
	{
		System.err.println(s);
	}
}
