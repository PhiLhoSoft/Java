import java.util.*;

public class TestInner
{
	public class InnerClass
	{
		public String prefix;
		public InnerClass(String p)
		{
			prefix = p;
		}
		public String prefix(String v)
		{
			return prefix + " - " + v;
		}
	}
	public static class NestedClass
	{
		public String prefix;
		public NestedClass(String p)
		{
			prefix = p;
		}
		public String prefix(String v)
		{
			return prefix + " - " + v;
		}
	}
}

class OtherClass
{
	public String prefix;
	public OtherClass(String p)
	{
		prefix = p;
	}
	public String prefix(String v)
	{
		return prefix + " - " + v;
	}
}
