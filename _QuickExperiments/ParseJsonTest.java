import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

class ParseJsonTest
{
  public static void main(String args[])
  {
    String fileName;
    if (args.length == 0)
    {
      // Default to a known file
      fileName = "D:/Dev/PhiLhoSoft/Processing/_QuickExperiments/_XML_JSON/UsingJackson/data/weather.json";
    }
    else
    {
      fileName = args[0];
    }
    ParseWeather pw = new ParseWeather(fileName);
    pw.parse();
  }
}

// Specifically designed to parse a given weather Json file.
class ParseWeather
{
  JsonParser m_parser;

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

  public void parse()
  {
    try
    {
      goToPath("data/wheather/hourly");
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
    String[] nodes = path.split("/");
    int currentNodeIndex = 0;
    JsonToken token;
    do
    {
      token = m_parser.nextToken(); // should return the START_OBJECT
      println("Token A: " + token);
      if (!token.equals(JsonToken.START_OBJECT))
      {
        println("No start object");
        return false;
      }
      token = m_parser.nextToken(); // should return the node token
      println("Token B: " + token);
      String nodeName = m_parser.getCurrentName();
      println("Reading node: " + nodeName);
      if (!nodeName.equals(nodes[currentNodeIndex++]))
      {
        token = m_parser.nextToken(); // Try the next token
        println("Token C: " + token);
        continue;
      }
      if (currentNodeIndex == nodes.length)
      {
        println("Yeah!");
        return true; // We reached the path!
      }

      token = m_parser.nextToken(); // move to value, or START_OBJECT/START_ARRAY
      println("Token D: " + token);
      if (token.equals(JsonToken.END_OBJECT))
      {
        println("End of Json");
        return false;
      }
      if (!token.equals(JsonToken.START_OBJECT))
      {
        println("No start object");
        return false;
      }
    } while (token != JsonToken.END_OBJECT);
    return false;
  }

  void println(String message)
  {
    System.out.println(message);
  }
}

