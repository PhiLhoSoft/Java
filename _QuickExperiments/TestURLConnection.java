import java.io.*;
import java.net.*;
import java.util.*;

// Testing URLConnection (remote reading/writing)
class TestURLConnection
{
  public static void main(String args[])
  {
    File f = new File( "D:/Temp/Test.java.txt" );
    String strXML = "<a href='http://PhiLho.deviantART.com'>PhiLho at deviantART</a>";

    WriteFile(f, strXML);
    ReadFile(f);
  }

  public static void WriteFile(File f, String str)
  {
    BufferedWriter bw = null;
    try
    {
      URL url = f.toURI().toURL();
      OutputStream os = null;
      if (url.getProtocol().equals("file"))
      {
        // getOutputStream doesn't work for this protocol
        os = new FileOutputStream(f);
      }
      else
      {
        URLConnection connection = url.openConnection();
        os = connection.getOutputStream();
      }
      bw = new BufferedWriter(new OutputStreamWriter(os), str.length());
      bw.write(str);
    }
    catch (Exception e)
    {
      System.out.println("Error writing file: " + f.getAbsolutePath());
      e.printStackTrace();
    }
    finally
    {
      try
      {
        if (bw != null)
        {
          bw.close();
        }
      }
      catch (IOException ioe)
      {
        System.out.println("Error closing writer for file: " + f.getAbsolutePath());
        ioe.printStackTrace();
      }
    }
  }

  public static void ReadFile(File f)
  {
    BufferedReader br = null;
    try
    {
      URL url = f.toURI().toURL();
      URLConnection connection = url.openConnection();
      // Works for any protocol!
      InputStream is = connection.getInputStream();
      br = new BufferedReader(new InputStreamReader(is));
      System.out.println(br.readLine());
    }
    catch ( Exception e )
    {
      System.out.println( "Error reading file: " + f.getAbsolutePath() );
      e.printStackTrace();
    }
    finally
    {
      try
      {
        if ( br != null )
        {
          br.close();
        }
      }
      catch ( IOException ioe )
      {
        System.out.println( "Error closing reader for file: " + f.getAbsolutePath() );
        ioe.printStackTrace();
      }
    }
  }
}

