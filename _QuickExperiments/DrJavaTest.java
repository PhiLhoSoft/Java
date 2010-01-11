import java.util.Vector;

public class DrJavaTest
{
    private String name = "instance name";
    private static String staticName = "static name";

    public static void main(String args[])
    {
      Vector<Integer> numbers = new Vector();
      numbers.add(1);
      numbers.add(2);
      numbers.add(3);
      for (int i=0;i<numbers.size();i++) {
         Integer num = numbers.elementAt(i);
         System.out.println(num);
      }
    }
}
