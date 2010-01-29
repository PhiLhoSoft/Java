import java.util.Arrays;

public class DrJavaTest
{
    private String name = "instance name";
    private static String staticName = "static name";

    public static void main(String args[])
    {
      String[] sa = { "One", "Two", "Three" };
      System.out.println("Foo: " + Arrays.toString(sa));
    }
}
