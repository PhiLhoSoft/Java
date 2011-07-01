/*
 * Testing performance of various operations with Caliper.
 */
/* File history:
 *  1.00.000 -- 2011/05/18 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2011 Philippe Lhoste / PhiLhoSoft
*/
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.util.regex.*;

// http://code.google.com/p/caliper/
import com.google.caliper.Param;
import com.google.caliper.SimpleBenchmark;
import com.google.caliper.Runner;

// java -cp .;C:\Java\libraries\caliper.jar BenchmarkTester
class BenchmarkTester
{
  public static final String[] testStringsOK =
  {
    "A", "B",
  };
  public static final String[] testStringsKO =
  {
    "a", "b",
  };

  public static boolean check(String toTest)
  {
    return Character.isUpperCase(toTest.charAt(0));
  }
  public static boolean checkToo(String toTest)
  {
    return Character.isUpperCase(toTest.trim().charAt(0));
  }

  private static void testCheckers()
  {
    for (String testString : testStringsOK)
    {
      if (!check(testString)) System.out.println("C: Error on " + testString);
      if (!checkToo(testString)) System.out.println("CT: Error on " + testString);
    }
    for (String testString : testStringsKO)
    {
      if (check(testString)) System.out.println("C: OK on " + testString);
      if (checkToo(testString)) System.out.println("CT: OK on " + testString);
    }
  }

  // Run the benchmark functions once to check if any exception is thrown
  private static void testBenchmark()
  {
    CaliperBenchmarkTemplate bm = new CaliperBenchmarkTemplate();
    bm.timeCheckingOK(1);
    bm.timeCheckingKO(1);
    bm.timeCheckingTooOK(1);
    bm.timeCheckingTooKO(1);
  }

  public static void main(String args[])
  {
    if (args.length == 0)
    {
//~       System.err.println("Usage: BenchmarkTester <something>");
//~       System.exit(1);
      testCheckers();
      testBenchmark();
      System.out.println("Done");
      return;
    }
  }
}

// javac -cp C:\Java\libraries\caliper.jar CaliperBenchmarkTemplate.java
// java -cp .;C:\Java\libraries\caliper.jar CaliperBenchmarkTemplate
public class CaliperBenchmarkTemplate extends SimpleBenchmark
{
  public static void main(String[] args) throws Exception
  {
    Runner.main(CaliperBenchmarkTemplate.class, args);
  }

  @Override
  protected void setUp()
  {
//~     BenchmarkTester.makePattern();
  }
  public void timeCheckingOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : BenchmarkTester.testStringsOK)
      {
        BenchmarkTester.check(testString);
      }
    }
  }
  public void timeCheckingKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : BenchmarkTester.testStringsKO)
      {
        BenchmarkTester.check(testString);
      }
    }
  }

  public void timeCheckingTooOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : BenchmarkTester.testStringsOK)
      {
        BenchmarkTester.checkToo(testString);
      }
    }
  }
  public void timeCheckingTooKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : BenchmarkTester.testStringsKO)
      {
        BenchmarkTester.checkToo(testString);
      }
    }
  }
}
