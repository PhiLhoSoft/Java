package prg.philhosoft.string;

import java.util.HashMap;
import java.lang.Double;

/************************************************************************
 * <i>Mathematic m_expression evaluator.</i> Supports the following functions:
 * +, -, *, /, ^, %, cos, sin, tan, acos, asin, atan, sqrt, sqr, log, min, max, ceil, floor, abs, neg, rndr.<br>
 * When the Eval() is called, a Double object is returned. If it returns null, an error occured.<p>
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
// Yes, yet another m_expression evaluator. Mostly for Java 1.5, as with Java 1.6 we can use ScriptEngine...
public class ExpressionEvaluator
{
   	protected static Operator[] s_operators;
	private Node m_node;
	private String m_expression;
	private HashMap<String, double> m_variables = new HashMap<String, double>();

	/**
	 * To run the program in command line.
	 * Usage: java ExpressionEvaluator.main "math m_expression"
	 */
	public static void main(String[] args)
	{
		if ( args == null || args.length != 1)
		{
			System.err.println("Mathematical Expression Evaluator");
			System.err.println("Usage: java ExpressionEvaluator.main \"math m_expression\"");
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
	   	if (s_operators == null)
		{
			InitializeOperators();
		}
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
			if (node.GetOperator().GetType() == 1) // Unary
			{
				npde.SetValue(EvaluateExpression(
						node.GetOperator(),
						Evaluate(node.GetLeft()),
						null));
			}
			else if (node.GetOperator().GetType() == 2)
			{
				node.SetValue(EvaluateExpression(
						node.GetOperator(),
						Evaluate(node.GetLeft()),
						Evaluate(node.GetRight())));
			}
		}
		return node.Eval();
	}

	private static double EvaluateExpression(Operator operator, double f1, double f2)
	{
		String op 	= o.getOperator();
		Double res 	= null;

		if  	 ( "+".equals(op) ) 	res = new Double( f1.doubleValue() + f2.doubleValue() );
		else if  ( "-".equals(op) ) 	res = new Double( f1.doubleValue() - f2.doubleValue() );
		else if  ( "*".equals(op) ) 	res = new Double( f1.doubleValue() * f2.doubleValue() );
		else if  ( "/".equals(op) )  	res = new Double( f1.doubleValue() / f2.doubleValue() );
		else if  ( "^".equals(op) )  	res = new Double( Math.pow(f1.doubleValue(), f2.doubleValue()) );
		else if  ( "%".equals(op) )  	res = new Double( f1.doubleValue() % f2.doubleValue() );
		else if  ( "&".equals(op) )  	res = new Double( f1.doubleValue() + f2.doubleValue() ); // todo
		else if  ( "|".equals(op) )  	res = new Double( f1.doubleValue() + f2.doubleValue() ); // todo
		else if  ( "cos".equals(op) )  	res = new Double( Math.cos(f1.doubleValue()) );
		else if  ( "sin".equals(op) )  	res = new Double( Math.sin(f1.doubleValue()) );
		else if  ( "tan".equals(op) )  	res = new Double( Math.tan(f1.doubleValue()) );
		else if  ( "acos".equals(op) )  res = new Double( Math.acos(f1.doubleValue()) );
		else if  ( "asin".equals(op) )  res = new Double( Math.asin(f1.doubleValue()) );
		else if  ( "atan".equals(op) )  res = new Double( Math.atan(f1.doubleValue()) );
		else if  ( "sqr".equals(op) )  	res = new Double( f1.doubleValue() * f1.doubleValue() );
		else if  ( "sqrt".equals(op) )  res = new Double( Math.sqrt(f1.doubleValue()) );
		else if  ( "log".equals(op) )  	res = new Double( Math.log(f1.doubleValue()) );
		else if  ( "min".equals(op) )  	res = new Double( Math.min(f1.doubleValue(), f2.doubleValue()) );
		else if  ( "max".equals(op) )  	res = new Double( Math.max(f1.doubleValue(), f2.doubleValue()) );
		else if  ( "exp".equals(op) )  	res = new Double( Math.exp(f1.doubleValue()) );
		else if  ( "floor".equals(op) ) res = new Double( Math.floor(f1.doubleValue()) );
		else if  ( "ceil".equals(op) )  res = new Double( Math.ceil(f1.doubleValue()) );
		else if  ( "abs".equals(op) )  	res = new Double( Math.abs(f1.doubleValue()) );
		else if  ( "neg".equals(op) )  	res = new Double( - f1.doubleValue() );
		else if  ( "rnd".equals(op) ) 	res = new Double( Math.random() * f1.doubleValue() );

		return res;
	}

	private void InitializeOperators()
	{
		s_operators = new Operator
		{
			// Base on http://www.uni-bonn.de/~manfear/javaoperators.php
			// Operator symbol, type (unary/binary) and priority
			new Operator("+", 1, 2), // unary plus
			new Operator("-", 1, 2), // unary minus
			new Operator("~", 1, 2), // bitwise NOT
//~ 			new Operator("!", 1, 2), // boolean (logical) NOT
			new Operator("*", 2, 3), // multiplication
			new Operator("/", 2, 3), // division
			new Operator("%", 2, 3), // remainder
			new Operator("+", 2, 4), // addition
			new Operator("-", 2, 4), // substraction
			new Operator("<<", 2, 5), // signed bit shift left
			new Operator(">>", 2, 5), // signed bit shift right
			new Operator(">>>", 2, 5), // unsigned bit shift right
//~ 			new Operator("<", 2, 6), // less than
//~ 			new Operator("<=", 2, 6), // less than or equal to
//~ 			new Operator(">", 2, 6), // greater than
//~ 			new Operator(">=", 2, 6), // greater than or equal to
//~ 			new Operator("==", 2, 7), // equal to
//~ 			new Operator("!=", 2, 7), // not equal to
			new Operator("&", 2, 8), // bitwise AND
//~ 			new Operator("&", 2, 8), // boolean (logical) XOR
			new Operator("^", 2, 9), // bitwise AND
//~ 			new Operator("^", 2, 9), // boolean (logical) XOR
			new Operator("|", 2, 10), // bitwise OR
//~ 			new Operator("|", 2, 10), // boolean (logical) OR
//~ 			new Operator("&&", 2, 11), // boolean (logical) AND with shortcut
//~ 			new Operator("||", 2, 12), // boolean (logical) OR with shortcut
//~ 			new Operator("?", 2, 13), // conditional part 1
//~ 			new Operator(":", 2, 13), // conditional part 2
//~ 			new Operator("=", 2, 14), // assignment (and combinated assignments)
			new Operator("abs", 1, 20),
			new Operator("signum", 1, 20),
			new Operator("floor", 1, 20),
			new Operator("ceil", 1, 20),
			new Operator("round", 1, 20),
			new Operator("sin", 1, 20),
			new Operator("cos", 1, 20),
			new Operator("tan", 1, 20),
			new Operator("asin", 1, 20),
			new Operator("acos", 1, 20),
			new Operator("atan", 1, 20),
			new Operator("atan2", 2, 20),
			new Operator("sinh", 1, 20),
			new Operator("cosh", 1, 20),
			new Operator("tanh", 1, 20),
			new Operator("sqr", 1, 20), // square
			new Operator("sqrt", 1, 20), // square root
			new Operator("cbrt", 1, 20), // cube root
			new Operator("log10", 1, 20),
			new Operator("log", 1, 20),
			new Operator("exp", 1, 20),
			new Operator("pow", 2, 20),
			new Operator("max", 2, 20),
			new Operator("min", 2, 20),
			new Operator("random", 0, 20),
			new Operator("toDegrees", 1, 20),
			new Operator("toRadians", 1, 20),
		}
	}

	/***
	 * gets the variable's value that was assigned previously
	 */
	public Double getVariable(String s)
	{
		return (Double) m_variables.get(s);
	}

	private Double getDouble(String s)
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

	protected class Operator
	{
		private String op;
		private int type;
		private int priority;

		public Operator(String o, int t, int p)
		{
			op = o;
			type = t;
			priority = p;
		}

		public String getOperator()
		{
			return op;
		}

		public void setOperator(String o)
		{
			op = o;
		}

		public int getType()
		{
			return type;
		}

		public int getPriority()
		{
			return priority;
		}
	}

	protected class Node
	{
		public String 	nString		= null;
		public Operator nOperator 	= null;
		public Node 	nLeft		= null;
		public Node 	nRight		= null;
		public Node 	nParent		= null;
		public int		nLevel		= 0;
		public Double  	nValue		= null;

		public Node(String s) throws Exception
		{
			init(null, s, 0);
		}

		public Node(Node parent, String s, int level) throws Exception
		{
			init(parent, s, level);
		}

		private void init(Node parent, String s, int level) throws Exception
		{
			s = removeIllegalCharacters(s);
			s = removeBrackets(s);
			s = addZero(s);
			if ( checkBrackets(s) != 0 ) throw new Exception("Wrong number of brackets in [" + s + "]");

			nParent				= parent;
			nString 			= s;
			nValue				= getDouble(s);
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
					// the m_expression must be at "root" level
					if ( inBrackets == 0 )
					{
						Operator o = getOperator(nString,i);
						if ( o != null )
						{
							// if first operator or lower priority operator
							if ( nOperator == null || nOperator.getPriority() >= o.getPriority() )
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
				if ( startOperator==0 && nOperator.getType() == 1 )
				{
					// the brackets must be ok
					if ( checkBrackets( s.substring( nOperator.getOperator().length() ) ) == 0 )
					{
						nLeft  = new Node( this, s.substring( nOperator.getOperator().length() ) , nLevel + 1);
						nRight = null;
						return;
					}
					else
						throw new Exception("Error during parsing... missing brackets in [" + s + "]");
				}
				// two operands
				else if ( startOperator > 0 && nOperator.getType() == 2 )
				{
					nOperator = nOperator;
					nLeft 	= new Node( this, s.substring(0,  startOperator), nLevel + 1 );
					nRight 	= new Node( this, s.substring(startOperator + nOperator.getOperator().length()), nLevel + 1);
				}
			}
		}

		private Operator getOperator(String s, int start)
		{
			Operator[] s_operators = getOperators();
			String temp = s.substring(start);
			temp = getNextWord(temp);
			for (int i=0; i<s_operators.length; i++)
			{
				if ( temp.startsWith(s_operators[i].getOperator()) )
					return s_operators[i];
			}
			return null;
		}

		private String getNextWord(String s)
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
		protected int checkBrackets(String s)
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
		 * returns a string that doesnt start with a + or a -
		 */
		protected String addZero(String s)
		{
			if ( s.startsWith("+") || s.startsWith("-") )
			{
				int sLength  	= s.length();
				for (int i=0; i<sLength; i++)
				{
					if ( getOperator(s, i) != null )
						return "0" + s;
				}
			}

			return s;
		}

		/***
		 * displays the tree of the m_expression
		 */
		public void trace()
		{
			String op = getOperator() == null ? " " : getOperator().getOperator() ;
			_D( op + " : " + getString() );
			if ( this.hasChild() )
			{
				if ( hasLeft() )
					getLeft().trace();
				if ( hasRight() )
					getRight().trace();
			}
		}

		protected boolean hasChild()
		{
			return ( nLeft != null || nRight != null );
		}

		protected boolean hasOperator()
		{
			return ( nOperator != null );
		}

		protected boolean hasLeft()
		{
			return ( nLeft != null );
		}

		protected Node getLeft()
		{
			return nLeft;
		}

		protected boolean hasRight()
		{
			return ( nRight != null );
		}

		protected Node getRight()
		{
			return nRight;
		}

		protected Operator getOperator()
		{
			return nOperator;
		}

		protected int getLevel()
		{
			return nLevel;
		}

		protected Double Eval()
		{
			return nValue;
		}

		protected void setValue(Double f)
		{
			nValue = f;
		}

		protected String getString()
		{
			return nString;
		}

		/***
		 * Removes spaces, tabs and brackets at the begining
		 */
		public String removeBrackets(String s)
		{
			String res = s;
			if ( s.length() > 2 && res.startsWith("(") && res.endsWith(")") && checkBrackets(s.substring(1,s.length()-1)) == 0 )
			{
				res = res.substring(1, res.length()-1 );
			}
			if ( res != s )
				return removeBrackets(res);
			else
		 	   return res;
		}

		/***
		 * Removes illegal characters
		 */
		public String removeIllegalCharacters(String s)
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

	protected static void _D(String s)
	{
		System.err.println(s);
	}
}
