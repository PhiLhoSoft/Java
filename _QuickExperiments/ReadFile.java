import java.io.*;

class ReadFile
{
  static String PATH = "E:/Dev/PhiLhoSoft/Java/_QuickExperiments/x";

  public static void main(String[] args)
  {
    BufferedReader reader = null;
    File f = new File(PATH);
    try
    {
      reader = new BufferedReader(new FileReader(f));
    }
    catch (FileNotFoundException e)
    {
      System.err.println("File not found: " + PATH);
      return;
    }
    try
    {
      String line = null;
      int lineNb = 0;

      line = reader.readLine();
      while (line != null)
      {
        System.out.println("Line " + lineNb + ": " + line);
        line = reader.readLine();
        lineNb++;
      }
    }
    catch (IOException e)
    {
      System.err.println("Error while reading file: " + e.getMessage());
    }
    finally
    {
      try { reader.close(); } catch (IOException e) {}
    }
  }
}
