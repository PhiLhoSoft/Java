import java.util.Locale;

public class DrJavaTest
{
    private String name = "instance name";
    private static String staticName = "static name";

    public static void main(String args[])
    {
      System.out.println(Locale.getDefault().getCountry());
      Foo foo = new Foo()
      {
        int af(int y) { return x * y; }
      };
    }
}

abstract class Foo
{
  protected int x;

  abstract int af(int y);
  int cf(int y) { return x + y; }
}
