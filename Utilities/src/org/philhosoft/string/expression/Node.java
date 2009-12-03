package org.philhosoft.string.expression;

import java.util.HashMap;
import java.lang.Double;

class Node
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
