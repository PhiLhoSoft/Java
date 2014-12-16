package org.philhosoft.ast.formattedtext;

public class LinkFragment implements Fragment
{
	private Fragment textFragment; // source anchor
	private String url; // destination anchor

	public LinkFragment(Fragment textFragment, String url)
	{
		this.textFragment = textFragment;
		this.url = url;
	}

	public Fragment getTextFragment()
	{
		return textFragment;
	}
	public String getUrl()
	{
		return url;
	}

	@Override
	public int hashCode()
	{
		return textFragment.hashCode() * 31 + url.hashCode();
	}
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof LinkFragment))
			return false;
		LinkFragment uf = (LinkFragment) obj;
		return uf.textFragment.equals(this.textFragment) && uf.url.equals(this.url);
	}
	@Override
	public String toString()
	{
		return "LinkFragment[" + textFragment + "](" + url + ")";
	}
}
