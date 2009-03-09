import java.util.regex.*;

class Test
{
  static final String testString = "foo\nbar\n[code]\nprint'';\nprint{'c'};\n[/code]\nbar\nfoo";
  static final String replaceString = "<br>\n";
  public static void main(String args[])
  {
    Pattern p = Pattern.compile("(.+?)(\\[code\\].*?\\[/code\\])?", Pattern.DOTALL);
    Matcher m = p.matcher(testString);
    StringBuilder result = new StringBuilder();
    while (m.find()) 
    {
      result.append(m.group(1).replaceAll("\\n", replaceString));
      if (m.group(2) != null)
      {
        result.append(m.group(2));
      }
    }
    System.out.println(result.toString());
  }
}
