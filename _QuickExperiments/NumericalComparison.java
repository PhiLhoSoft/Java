import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class NumericalComparison
{
  static final String[] strs =
  {
    "eee 5 ddd jpeg2001 eee",
    "eee 123 ddd jpeg2000 eee",
    "ddd",
    "aaa 5 yy 6",
    "ccc 555",
    "bbb 3 ccc",
    "bbb 9 a",
    "",
    "eee 4 ddd jpeg2001 eee",
    "ccc 11",
    "bbb 12 ccc",
    "aaa 5 yy 22",
    "aaa",
    "eee 3 ddd jpeg2000 eee",
    "ccc 5",
  };

  public static void main(String[] args)
  {
    Arrays.sort(strs, new InternalNumberComparator());
    for (int i = 0; i < strs.length; i++)
    {
//      ArrayList matches = split(strs[i]);
      System.out.println(strs[i]);
    }
  }

  /*
  Pattern p = Pattern.compile("a*b");
  Matcher m = p.matcher("aaaaab");
  boolean b = m.matches();
  */
}

class InternalNumberComparator implements Comparator
{
  static Pattern splitter = Pattern.compile("(\\d+|\\D+)");

  public int compare(Object o1, Object o2)
  {
    // I deliberately use the Java 1.4 syntax,
    // all this can be improved with 1.5's generics
    String s1 = (String)o1, s2 = (String)o2;
    // We split each string as runs of number/non-number strings
    ArrayList sa1 = split(s1);
    ArrayList sa2 = split(s2);
    // Nothing or different structure
    if (sa1.size() == 0 || sa1.size() != sa2.size())
    {
      // Just compare the original strings
      return s1.compareTo(s2);
    }
    int i = 0;
    String si1 = "";
    String si2 = "";
    // Compare beginning of string
    for (; i < sa1.size(); i++)
    {
      si1 = (String)sa1.get(i);
      si2 = (String)sa2.get(i);
      if (!si1.equals(si2))
        break;  // Until we find a difference
    }
    // No difference found?
    if (i == sa1.size())
      return 0; // Same strings!

    // Try to convert the different run of characters to number
    int val1, val2;
    try
    {
      val1 = Integer.parseInt(si1);
      val2 = Integer.parseInt(si2);
    }
    catch (NumberFormatException e)
    {
      return s1.compareTo(s2);  // Strings differ on a non-number
    }

    // Compare remainder of string
    for (i++; i < sa1.size(); i++)
    {
      si1 = (String)sa1.get(i);
      si2 = (String)sa2.get(i);
      if (!si1.equals(si2))
      {
        return s1.compareTo(s2);  // Strings differ
      }
    }

    // Here, the strings differ only on a number
    return val1 < val2 ? -1 : 1;
  }

  ArrayList split(String s)
  {
    ArrayList r = new ArrayList();
    Matcher matcher = splitter.matcher(s);
    while (matcher.find())
    {
      String m = matcher.group(1);
      r.add(m);
    }
    return r;
  }
}
