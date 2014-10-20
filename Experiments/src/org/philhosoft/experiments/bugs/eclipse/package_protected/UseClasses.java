package org.philhosoft.experiments.bugs.eclipse.package_protected;

public class UseClasses
{
	private OneClass ichi;

	public UseClasses()
	{
		ichi = new OneClass(10);

		SecondClass ni = new SecondClass(20);
		ni.doStuff();
		System.out.println(ni.getData());
	}
	public void doStuff()
	{
		ichi.doStuff();
	}
	public void showResult()
	{
		System.out.println(ichi.getData());
	}

	public static void main(String[] args)
	{
		UseClasses uc = new UseClasses();
		uc.doStuff();
		uc.showResult();
	}
}
