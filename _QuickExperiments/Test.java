import java.util.*;
import java.text.*;

public class Test
{
  int x = 42;

  public Test()
  {
  }

  public static void main(String args[])
  {
    System.out.println(Arrays.toString(args));
    Test t1 = new Test();
    System.out.println(t1.hashCode());
    Test t2 = new Test();
    System.out.println(t2.hashCode());

    List<String> l = new ArrayList<>();
    l.add("a");
    l.add(null);
    l.add("c");
    System.out.println(l);

    long ts1 = 1112857200000L;
    long ts2 = 1112853600000L;
    printDate(ts1);
    printDate(ts2);

    try
    {
      System.out.println(java.net.URLEncoder.encode("/ D\u00F8 + w\u00E5p %\r\n", "UTF-8"));
    }
    catch (Exception e)
    {
      System.out.println(e);
    }

    System.out.println("Compare strings");
    final String F = "Foo";
    String a = F;
    String b = F;
    assert a == b; // Works!
    String c = "F" + F.substring(1); // Still "Foo"
    System.out.println(c);
    assert c.equals(a); // Works
    assert c == a; // Fails

    System.out.println("Reverse strings");
    System.out.println(reverseString("Reverse strings"));
    System.out.println(reverseString("x"));
    System.out.println(reverseString("xy"));
    System.out.println(reverseString("xyz"));
  }

  static void printDate(long ts)
  {
    Date d = new Date(ts);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    System.out.println(sdf.format(d));
  }

  static String reverseString(String str)
  {
    char[] ca = str.toCharArray();
    for (int i = 0, cal = ca.length; i < cal / 2; i++)
    {
      char c = ca[i];
      ca[i] = ca[cal - 1 - i];
      ca[cal - 1 - i] = c;
    }
    return new String(ca);
  }
}
