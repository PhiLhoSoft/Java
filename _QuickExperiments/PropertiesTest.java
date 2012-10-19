import java.io.*;
import java.util.Properties;

public class Test
{
  public static void main(String[] args)
  {
//~     writeProps(null);
    writeProps("Some relevant comment...");
  }

  static void writeProps(String comment)
  {
    Properties props = new Properties();
    props.setProperty("one", "Value of one");
    props.setProperty("t w o", "Déjà vu");
    writeProps(comment, props);

    Properties props2 = new Properties(props);
    props2.setProperty("Three", "Trois");
    writeProps(comment, props2);

    Properties props3 = (Properties) props.clone();
    props3.setProperty("Three", "Trois Trois");
    writeProps(comment, props3);
  }

  static void writeProps(String comment, Properties props)
  {
    System.out.println("\nProperties to XML\n");
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try
    {
       props.storeToXML(baos, comment, "UTF-8");
    }
    catch (IOException e)
    {
       e.printStackTrace();
    }
    String xml = baos.toString();
    System.out.println(xml);

    System.out.println("\nProperties to plain text\n");
    baos.reset();
    try
    {
       props.store(baos, comment);
    }
    catch (IOException e)
    {
       e.printStackTrace();
    }
    try
    {
      String p = baos.toString("UTF-8");
      System.out.println(p);
    }
    catch (UnsupportedEncodingException e)
    {
       e.printStackTrace();
    }
  }
}
