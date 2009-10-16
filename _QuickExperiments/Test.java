import java.util.*;

class Test
{
  public static void main(String args[])
  {
    List<String> l = new ArrayList<String>();
    l.add("foo");
    Object[] la = l.toArray();
    System.out.println(la.length);
  }
}

