package org.philhosoft.tests.bugs.eclipse.package_protected;

abstract class SeveralClasses
{
	protected int data;

	public SeveralClasses(int d)
	{
		data = d;
	}
	public int getData()
	{
		return data;
	}
	public abstract void doStuff();
}

final class OneClass extends SeveralClasses
{
	OneClass(int d)
	{
		super(d);
	}
	@Override
	public void doStuff()
	{
		data += 1;
	}
}

class SecondClass extends SeveralClasses
{
	SecondClass(int d)
	{
		super(d);
	}
	@Override
	public void doStuff()
	{
		data *= 2;
	}
}
