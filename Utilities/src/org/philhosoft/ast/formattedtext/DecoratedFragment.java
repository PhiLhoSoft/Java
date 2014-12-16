package org.philhosoft.ast.formattedtext;

import java.util.ArrayList;
import java.util.List;


public class DecoratedFragment
{
	private FragmentDecoration decoration;
	private List<Fragment> fragments = new ArrayList<Fragment>();

	public DecoratedFragment(FragmentDecoration decoration)
	{
		this.decoration = decoration;
	}

	public FragmentDecoration getDecoration()
	{
		return decoration;
	}
	public List<Fragment> getFragments()
	{
		return fragments;
	}

	@Override
	public int hashCode()
	{
		return 31 * decoration.hashCode() + fragments.hashCode();
	}
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof TypedBlock))
			return false;
		DecoratedFragment tb = (DecoratedFragment) obj;
		return tb.decoration == this.decoration && tb.fragments.equals(this.fragments);
	}
	@Override
	public String toString()
	{
		return "DecoratedFragment{decoration=" + decoration + ", fragments=" + fragments + "}";
	}
}
