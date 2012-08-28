import java.util.*;

// http://code.google.com/p/caliper/
import com.google.caliper.Param;
import com.google.caliper.SimpleBenchmark;
import com.google.caliper.Runner;


public final class PrintfVsPlus extends SimpleBenchmark
{
  public static void main(String[] args) throws Exception
  {
    PrintfVsPlus b = new PrintfVsPlus();
    b.timePrintfA(2);        System.out.print("PrintfA:        " + b.result);
    b.timePrintfB(2);        System.out.print("PrintfB:        " + b.result);
    b.timeStringFormat(2);   System.out.print("StringFormat:   " + b.result);
    b.timePlus(2);           System.out.print("Plus:           " + b.result);
    b.timeStringBufferA(2);  System.out.print("StringBufferA(: " + b.result);
    b.timeStringBufferB(2);  System.out.print("StringBufferB(: " + b.result);
    b.timeStringBuilderA(2); System.out.print("StringBuilderA: " + b.result);
    b.timeStringBuilderB(2); System.out.print("StringBuilderB: " + b.result);
    b.timeStringBuilderC(2); System.out.print("StringBuilderC: " + b.result);
    b.time2Printf(2);        System.out.print("2Printf:        " + b.result);
    b.time2Plus(2);          System.out.print("2Plus:          " + b.result);
    b.time2StringBuilder(2); System.out.print("2StringBuilder: " + b.result);
    b.time3Printf(2);        System.out.print("3Printf:        " + b.result);
    b.time3Plus(2);          System.out.print("3Plus:          " + b.result);
    b.time3StringBuilder(2); System.out.print("3StringBuilder: " + b.result);

    Runner.main(PrintfVsPlus.class, args);
  }

  String result;

  @Override protected void setUp() throws Exception
  {
  }

  public void timePrintfA(int reps)
  {
    final StringBuilder sb = new StringBuilder();
    final Formatter formatter = new Formatter(sb);
    for (int i = 0; i < reps; i++)
    {
      formatter.format("result = '%d'\n", i);
      result = formatter.toString();
      sb.delete(0, sb.length());
    }
  }

  public void timePrintfB(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      Formatter formatter = new Formatter();
      formatter.format("result = '%d'\n", i);
      result = formatter.toString();
    }
  }

  public void timeStringFormat(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      result = String.format("result = '%d'\n", i);
    }
  }

  public void timePlus(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      result = "result = '" + i + "'\n";
    }
  }

  public void timeStringBufferA(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      StringBuffer sb = new StringBuffer();
      sb.append("result = '").append(i).append("'\n");
      result = sb.toString();
    }
  }

  public void timeStringBufferB(int reps)
  {
    final StringBuffer sb = new StringBuffer();
    for (int i = 0; i < reps; i++)
    {
      sb.delete(0, sb.length());
      sb.append("result = '").append(i).append("'\n");
      result = sb.toString();
    }
  }

  public void timeStringBuilderA(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      StringBuilder sb = new StringBuilder();
      sb.append("result = '").append(i).append("'\n");
      result = sb.toString();
    }
  }

  public void timeStringBuilderB(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      StringBuilder sb = new StringBuilder("result = '");
      sb.append(i).append("'\n");
      result = sb.toString();
    }
  }

  public void timeStringBuilderC(int reps)
  {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < reps; i++)
    {
      sb.delete(0, sb.length());
      sb.append("result = '").append(i).append("'\n");
      result = sb.toString();
    }
  }

  // Trying with more parameters

  public void time2Printf(int reps)
  {
    final StringBuilder sb = new StringBuilder();
    final Formatter formatter = new Formatter(sb);
    for (int i = 0; i < reps; i++)
    {
      formatter.format("result = '%d' * 2 = '%d'\n", i, i * 2);
      result = formatter.toString();
      sb.delete(0, sb.length());
    }
  }

  public void time2Plus(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      result = "result = '" + i + "' * 2 = '" + i * 2 + "'\n";
    }
  }

  public void time2StringBuilder(int reps)
  {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < reps; i++)
    {
      sb.delete(0, sb.length());
      sb.append("result = '").append(i).append("' * 2 = '").append(i * 2).append("'\n");
      result = sb.toString();
    }
  }

  // And more...

  public void time3Printf(int reps)
  {
    final StringBuilder sb = new StringBuilder();
    final Formatter formatter = new Formatter(sb);
    for (int i = 0; i < reps; i++)
    {
      formatter.format("result = '%d' * 2 = '%d'; * 3 * '%d'\n", i, i * 2, i * 3);
      result = formatter.toString();
      sb.delete(0, sb.length());
    }
  }

  public void time3Plus(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      result = "result = '" + i + "' * 2 = '" + i * 2 + "'; * 3 * '" + i * 3 + "'\n";
    }
  }

  public void time3StringBuilder(int reps)
  {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < reps; i++)
    {
      sb.delete(0, sb.length());
      sb.append("result = '").append(i).append("' * 2 = '").append(i * 2).append("'; * 3 = '").append(i * 3).append("'\n");
      result = sb.toString();
    }
  }
}

/* Results:
     benchmark     ns linear runtime
       PrintfA  735,0 ===========
       PrintfB  869,6 ==============
  StringFormat  880,9 ==============
          Plus   45,8 =
 StringBufferA  106,7 =
 StringBufferB   78,4 =
StringBuilderA   89,1 =
StringBuilderB   75,8 =
StringBuilderC   58,9 =
       2Printf 1282,0 ====================
         2Plus   86,0 =
2StringBuilder   95,0 =
       3Printf 1853,7 ==============================
         3Plus  128,4 ==
3StringBuilder  132,5 ==
*/
