import java.util.*;

public final class Test
{
  public static void main(String[] args) throws Exception
  {
    StringBuilder sb = new StringBuilder();
    final Formatter formatter = new Formatter(sb);
    for (int i = 0; i < 3; i++)
    {
      formatter.format("result = '%d'", i);
      System.out.println(formatter.toString());
      sb.delete(0, sb.length());
    }
  }
}
