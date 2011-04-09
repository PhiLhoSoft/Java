import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

// javac -cp C:\Java\libraries\jackson-core-asl-1.7.2.jar ParseJsonTest.java
// java -cp .;C:\Java\libraries\jackson-core-asl-1.7.2.jar ParseJsonTest
class ParseJsonTest
{
  public static void main(String args[])
  {
    String fileName;
    if (args.length == 0)
    {
      // Default to a known file
//~       fileName = "D:/Dev/PhiLhoSoft/Processing/_QuickExperiments/_XML_JSON/UsingJackson/data/weather.json";
      fileName = "H:/PhiLhoSoft/Processing/_QuickExperiments/_XML_JSON/UsingJackson/data/weather.json";
    }
    else
    {
      fileName = args[0];
    }
    ParseWeather pw = new ParseWeather(fileName);
    pw.parse("data/weather:0/hourly");
  }
}

// Specifically designed to parse a given weather Json file.
class ParseWeather
{
  JsonParser m_parser;
  String[] m_path;
  int m_currentJsonLevel;
  int m_currentPathLevel;

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

  public void parse(String path)
  {
    try
    {
      goToPath(path);
    }
    catch (IOException ioe)
    {
      println("Cannot read file: " + ioe);
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
      }
    }
  }

  // Path is a simple path of intermediary nodes whole values are objects, and a final node (any kind),
  // with the names separated by a slash.
  boolean goToPath(String path) throws IOException
  {
    PathHandler ph = new PathHandler(path);

    int level = 0;
    int currentArrayIndex = -1;

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
        return false;
      }
      println("Token A: " + token);
      switch (token)
      {
        case START_OBJECT:
          println("Start O");
          break;
        case START_ARRAY:
          println("Start A");
          break;
        case FIELD_NAME:
          println("Name: " + m_parser.getCurrentName());
          break;
        case VALUE_STRING:
          println("Value: " + m_parser.getText());
          break;
        case END_OBJECT:
          println("End O " + m_parser.getCurrentName());
          break;
        case END_ARRAY:
          println("End A " + m_parser.getCurrentName());
          break;
        default:
          println("Unexpected token: " + token);
      }
/*
    int targetArrayIndex = 0;

      if (arrayIndex >= 0)
      {
        if (targetArrayIndex > arrayIndex)
        {
          println("Skipping " + arrayIndex);
          m_parser.skipChildren();
          arrayIndex++;
        }
      }
      else if (token == JsonToken.FIELD_NAME)
      {
      }
      else
      {
        println("Wrong kind of token: " + token);
        return false;
      }

      token = m_parser.nextToken(); // move to value, or START_OBJECT/START_ARRAY
      println("Token C: " + token);
      if (token == JsonToken.START_OBJECT)
      {
        continue; // Next object
      }
      if (token == JsonToken.START_ARRAY)
      {
        arrayIndex = 0;
        continue;
      }
      println("No start: " + token);
      if (token == JsonToken.FIELD_NAME)
      {
        println("@ " + m_parser.getCurrentName());
      }
      return false;
    } while (token != JsonToken.END_OBJECT);
*/
    } while (token != null);
    return false;
  }

  /*
  */

  void println(String message)
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
  /** Index used by the iterator. */
  private int m_index;

  public PathHandler(String path)
  {
    m_path = path;
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

  class PathPart
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
