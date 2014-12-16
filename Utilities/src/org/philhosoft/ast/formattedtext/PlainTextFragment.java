package org.philhosoft.ast.formattedtext;

public class PlainTextFragment implements Fragment
{
	private String fragment;

	public PlainTextFragment(String fragment)
	{
		this.fragment = fragment;
	}

	public String getFragment()
	{
		return fragment;
	}

	@Override
	public int hashCode()
	{
		return fragment.hashCode();
	}
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof PlainTextFragment))
			return false;
		return ((PlainTextFragment) obj).fragment.equals(this.fragment);
	}
	@Override
	public String toString()
	{
		return "PlainTextFragment[" + fragment + "]";
	}
}
