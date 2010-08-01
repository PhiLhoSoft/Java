import java.util.*;
import java.io.*;
import javax.xml.stream.*;

public class DrJavaTest
{
  public static void main(String args[])
  {
    String dirname = "D:/Dev/PhiLhoSoft/Java";
    File f1 = new File(dirname);
    FilenameFilter fnf = new ShowFile();
    String s[] = f1.list(fnf);
    /*
    for (String fileName : s)
    {
      System.out.println(fileName);
    }
    */
  }
}

class ShowFile implements FilenameFilter
{
  public boolean accept(File dir, String fileName)
  {
    System.out.println(dir.toString() + " -> " + fileName);
    return true;
  }
}

abstract class Test
{
  public static void Gah()
  {
    System.out.println("In Gah");
    double x = 3.1415926d;
  }
}
