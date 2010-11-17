import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import org.jsoup.*;
import org.jsoup.nodes.*;

public class TestJsoup
{
  Document doc;

  public void Connect(String url)
  {
    try
    {
      doc = Jsoup.connect(url).get();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public String GetText()
  {
    doc.body().wrap("<pre></pre>");
    String text = doc.text();
    text = text.replaceAll("\u00A0", " ");
    return text;
  }
  public String GetHTML()
  {
    return doc.html();
  }

  public static void main(String args[])
  {
    TestJsoup tjs = new TestJsoup();
    tjs.Connect("http://www.particle.kth.se/~lindsey/JavaCourse/Book/Part1/Java/Chapter09/scannerConsole.html");
    if (args.length > 0)
    {
      System.out.println(tjs.GetText());
    }
    else
    {
      System.out.println(tjs.GetHTML());
    }
  }
}
