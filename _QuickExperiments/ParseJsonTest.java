import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

// javac -cp C:\Java\libraries\jackson-core-asl-1.7.6.jar ParseJsonTest.java
// java -cp .;C:\Java\libraries\jackson-core-asl-1.7.6.jar ParseJsonTest
class ParseJsonTest
{
  public static void main(String args[])
  {
    String fileName;
    if (args.length == 0)
    {
      // Default to a known file
      fileName = "D:/Dev/PhiLhoSoft/Processing/_QuickExperiments/_XML_JSON/UsingJackson/data/weather.json";
//~       fileName = "H:/PhiLhoSoft/Processing/_QuickExperiments/_XML_JSON/UsingJackson/data/weather.json";
    }
    else
    {
      fileName = args[0];
    }
    ParseWeather pw = new ParseWeather(fileName);
    // Test path
    String[] temperatures = null;
    try
    {
      temperatures = pw.parse("data/weather:0/hourly/@waterTemp_C");
    }
    catch (IllegalArgumentException ex)
    {
      System.out.println("Incorrect path!");
      System.exit(-1);
    }
    if (temperatures != null)
    {
      for (String temp : temperatures)
      {
        System.out.println("* " + temp);
      }
    }
    else
    {
      System.out.println("Data not found.");
    }
  }
}

// Specifically designed to parse a given weather Json file.
class ParseWeather
{
  JsonParser m_parser;
  String[] m_path;
  int m_currentJsonLevel;
  int m_currentPathLevel;
  ArrayList<String> m_elements = new ArrayList<String>();

  public ParseWeather(String fileName)
  {
    JsonFactory jf = new JsonFactory();
    try
    {
      m_parser = jf.createJsonParser(new File(fileName));
    }
    catch (IOException ioe)
    {
      println("Cannot read file: " + ioe);
    }
  }

  public String[] parse(String path)
  {
    try
    {
      if (!goToPath(path))
      {
        return null;
      }
    }
    catch (IOException ioe)
    {
      println("Cannot read file: " + ioe);
      return null;
    }
    finally
    {
      try
      {
        if (m_parser != null)
        {
          m_parser.close(); // ensure resources get cleaned up timely and properly
        }
      }
      catch (IOException ioe)
      {
        println("Cannot close file: " + ioe);
        return null;
      }
    }
    String[] result = new String[m_elements.size()];
    return m_elements.toArray(result);
  }

  // Path is a simple path of intermediary nodes whole values are objects, and a final node (attribute),
  // with the names separated by a slash.
  boolean goToPath(String path) throws IOException
  {
    PathHandler ph = new PathHandler(path);

    boolean bInArray = false;
    boolean bCollectValue = false;
    int level = 0;
    int currentArrayIndex = -1;
    int targetArrayIndex = -1;

    JsonToken token = m_parser.nextToken(); // Start of top-level Json object
    println("Token 0: " + token);
    if (token != JsonToken.START_OBJECT)
    {
      println("No start object");
      return false;
    }

    do
    {
      token = m_parser.nextToken();
      if (token == null)
      {
        // End of Json data
        println("End...");
        return true;
      }
      println("Token: " + token);
      switch (token)
      {
        case START_OBJECT:
          level++;
          println("Start Object: " + level);
          if (bInArray && currentArrayIndex < targetArrayIndex)
          {
            m_parser.skipChildren();
          }
          break;
        case START_ARRAY:
          level++;
          println("Start Array: " + level);
          bInArray = true;
          currentArrayIndex = 0;
          break;
        case FIELD_NAME:
          String name = m_parser.getCurrentName();
          println("Name: " + name);
          if (ph.isAttribute(name))
          {
            if (level != ph.getAttributeLevel())
            {
              println("Almost! Found at " + level + ", expected at " + ph.getAttributeLevel());
            }
            else
            {
              println("FOUND!");
              bCollectValue = true;
            }
          }
          /*
          if (level == ph.getLevel() && ph.isAttribute(name))
          {
            println("FOUND!");
            bCollectValue = true;
          }
          */
          break;
        case VALUE_STRING:
          String value = m_parser.getText();
          println("Value: " + value);
          if (bCollectValue)
          {
            println("GOT!");
            bCollectValue = false;
            m_elements.add(value);
          }
          break;
        case END_OBJECT:
          level--;
          println("End Object: " + level + " for " + m_parser.getCurrentName());
          break;
        case END_ARRAY:
          level--;
          println("End Array: " + level + " for " + m_parser.getCurrentName());
          bInArray = false;
          break;
        default:
          println("Unexpected token: " + token);
      }
    } while (token != null);
    return true;
  }

  /*
  */

  public static void println(String message)
  {
    System.out.println(message);
  }
}

class PathHandler
{
  /** Raw path. */
  private String m_path;
  /** The parts of the path. */
  private PathPart[] m_parts;
  /** The attribute to find. */
  private String m_attribute;
  /** Index used by the iterator. */
  private int m_index;

  public PathHandler(String path)
  {
    int pos = path.indexOf("@");
    if (pos < 0)
      throw new IllegalArgumentException("Missing attribute in the path");

    m_attribute = path.substring(pos + 1);
    m_path = path.substring(0, pos - 1);
    System.out.println("Finding " + m_attribute + " in " + m_path);

    String[] nodes = path.split("/");
    m_parts = new PathPart[nodes.length];

    int count = 0;
    for (String nodeName : nodes)
    {
      m_parts[count++] = parseName(nodeName);
    }
  }

  // Almost the iterator interface, but not quite as next() is void (I keep PathPart private)
  public void next()
  {
    if (m_index == m_parts.length)
    {
      throw new NoSuchElementException();
    }
    m_index++;
  }
  public boolean hasNext()
  {
    return m_index < m_parts.length - 1;
  }
  public void remove()
  {
    throw new UnsupportedOperationException();
  }

  public String getCurrentName()
  {
    return m_parts[m_index].m_name;
  }
  public int getCurrentPos()
  {
    return m_parts[m_index].m_pos;
  }
  public int getLevel()
  {
    return m_index;
  }
  public boolean isCurrentAnArray()
  {
    return m_parts[m_index].m_pos >= 0;
  }
  public boolean isAttribute(String name)
  {
    return name.equals(m_attribute);
  }
  public int getAttributeLevel()
  {
    return m_parts.length + 1;
  }

  private PathPart parseName(String name)
  {
    if (name.contains(":"))
    {
      String[] ps = name.split(":");
      int pos = 0;
      try
      {
        pos = Integer.parseInt(ps[1]);
      }
      catch (Exception e)
      {
        System.out.println("Bad index: " + name);
        return null;
      }
      return new PathPart(ps[0], pos);
    }
    else
    {
      return new PathPart(name);
    }
  }

  private class PathPart
  {
    String m_name;
    int m_pos;

    public PathPart(String name, int pos)
    {
      m_name = name;
      m_pos = pos;
    }
    public PathPart(String name)
    {
      m_name = name;
      m_pos = -1;
    }
  }
}
