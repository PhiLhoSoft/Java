import java.io.*;

public class CopyFile
{
  public static void main(String[] args)
  {
    final int BUFFER_SIZE = 4096;
    String fileNameIn = "G:/Tmp/LargeImage.jpg";
    String fileNameOut = "G:/Tmp/LargeImageC.jpg";

    byte[] buffer = new byte[BUFFER_SIZE];
    InputStream in = null;
    OutputStream out = null;
    try
    {
      in = new BufferedInputStream(new FileInputStream(fileNameIn));
      out = new BufferedOutputStream(new FileOutputStream(fileNameOut));
      int n = in.read(buffer, 0, BUFFER_SIZE);
      while (n > 0)
      {
        out.write(buffer);
        n = in.read(buffer, 0, BUFFER_SIZE);
      }
    }
    catch (IOException e)
    {
      System.out.println("Error: " + e);
    }
    finally
    {
      try
      {
        if (in != null)
          in.close();
        if (out != null)
          out.close();
      }
      catch (IOException e)
      {
        System.out.println("Error on close: " + e);
      }
    }
  }
}
