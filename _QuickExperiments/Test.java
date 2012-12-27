import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class Test
{
  public static void main(String[] args)
  {
    Matcher m = Pattern.compile("(a1.*?)(?=a1|$)").matcher("a1wwa1xxa1yya1zz");
    while (m.find()) {
      String myGroup = m.group(1);
      System.out.println("> " + myGroup);
    }
  }
}
