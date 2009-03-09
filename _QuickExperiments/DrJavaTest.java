import java.util.Locale;

public class DrJavaTest
{
    private String name = "instance name";
    private static String staticName = "static name";

    public static void main(String args[]) 
    {
      System.out.println(Locale.getDefault().getCountry());
    }
} 