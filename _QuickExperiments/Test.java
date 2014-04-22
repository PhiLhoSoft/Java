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
  }

  static void printDate(long ts)
  {
    Date d = new Date(ts);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    System.out.println(sdf.format(d));
  }
}
