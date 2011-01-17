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

class Node
{
	public String m_string;
	public Operator m_operator;
	public Node m_left;
	public Node m_right;
	public Node m_parent;
	public int m_level;
	public Double m_value;

	public Node(String s) throws Exception
	{
		init(null, s, 0);
	}

	public Node(Node parent, String s, int level) throws Exception
	{
		init(parent, s, level);
	}

	private Operator getOperator(String str, int start)
	{
		String temp = str.substring(start);
		temp = getNextWord(temp);
		for (int i = 0; i < s_operators.length; i++)
		{
			if (temp.startsWith(s_operators[i].getOperator()))
				return s_operators[i];
		}
		return null;
	}

	private String getNextWord(String str)
	{
		int sLength = str.length();
		for (int i = 1; i < sLength; i++)
		{
			char c = str.charAt(i);
			if ( (c < 'a' || c > 'z') && (c < '0' || c > '9') )
				return str.substring(0, i);
		}
		return s;
	}

	/**
	 * Checks if there is any missing brackets.
	 * @return 0 if str is valid
	 */
	protected int checkBrackets(String str)
	{
		int sLength = str.length();
		int inBracket = 0;

		for (int i = 0; i < sLength; i++)
		{
			char c = str.charAt(i);
			if (c == '(' && inBracket >= 0)
				inBracket++;
			else if (c == ')')
				inBracket--;
		}

		return inBracket;
	}

	/**
	 * Returns a string that doesn't start with a + or a -
	 */
	protected String addZero(String str)
	{
		if (str.startsWith("+") || str.startsWith("-"))
		{
			int sLength = str.length();
			for (int i = 0; i < sLength; i++)
			{
				if (getOperator(s, i) != null)
					return "0" + s;
			}
		}

		return s;
	}

	/***
	 * Displays the tree of the expression.
	 */
	public void trace()
	{
		String op = m_operator == null ? " " : m_operator.getOperator() ;
		_D(op + " : " + getString());
		if (hasChild())
		{
			if (hasLeft())
			{
				getLeft().trace();
			}
			if (hasRight())
			{
				getRight().trace();
			}
		}
	}

	protected boolean hasChild()
	{
		return m_left != null || m_right != null;
	}

	protected boolean hasOperator()
	{
		return m_operator != null;
	}

	protected boolean hasLeft()
	{
		return m_left != null;
	}

	protected Node getLeft()
	{
		return m_left;
	}

	protected boolean hasRight()
	{
		return m_right != null;
	}

	protected Node getRight()
	{
		return m_right;
	}

	protected Operator getOperator()
	{
		return m_operator;
	}

	protected int getLevel()
	{
		return m_level;
	}

	protected double eval()
	{
		return m_value;
	}

	protected void setValue(double d)
	{
		m_value = d;
	}

	protected String getString()
	{
		return m_string;
	}

	/***
	 * Removes spaces, tabs and brackets at the beginning.
	 */
	public String removeBrackets(String str)
	{
		String res = str;
		if (str.length() > 2 && res.startsWith("(") && res.endsWith(")") &&
				checkBrackets(str.substring(1, str.length()-1)) == 0)
		{
			res = res.substring(1, res.length()-1);
		}
		if (res != str)
			return removeBrackets(res);
		else
		   return res;
	}

	/***
	 * Removes illegal characters.
	 */
	public String removeIllegalCharacters(String str)
	{
		char[] illegalCharacters = { ' ' };
		String res = str;

		for (int j = 0; j < illegalCharacters.length; j++)
		{
			int i = res.lastIndexOf(illegalCharacters[j], res.length());
			while (i != -1)
			{
				String temp = res;
				res = temp.substring(0, i);
				res += temp.substring(i + 1);
				i = res.lastIndexOf(illegalCharacters[j], str.length());
			}
		}
		return res;
	}

	protected void _D(String str)
	{
		String nbSpaces = "";
		for (int i = 0; i < m_level; i++)
			nbSpaces += "  ";
		System.out.println(nbSpaces + "|" + str;
	}
}
