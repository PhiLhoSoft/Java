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

  public void init()
  {
    System.out.println("Init");
  }

  public boolean check(String toTest)
  {
    return Character.isUpperCase(toTest.charAt(0));
  }
  public boolean checkToo(String toTest)
  {
    return Character.isUpperCase(toTest.trim().charAt(0));
  }

  private void testCheckers()
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
  private void testBenchmark()
  {
    CaliperBenchmarkTemplate cbt = new CaliperBenchmarkTemplate();
    cbt.timeCheckingOK(1);
    cbt.timeCheckingKO(1);
    cbt.timeCheckingTooOK(1);
    cbt.timeCheckingTooKO(1);
  }

  public static void main(String args[])
  {
    BenchmarkTester bt = new BenchmarkTester();
    if (args.length == 0)
    {
//~       System.err.println("Usage: BenchmarkTester <something>");
//~       System.exit(1);
      bt.init();
      bt.testCheckers();
      bt.testBenchmark();
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

  BenchmarkTester objectToTest = new BenchmarkTester();

  @Override
  protected void setUp()
  {
    objectToTest.init();
  }
  public void timeCheckingOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : BenchmarkTester.testStringsOK)
      {
        objectToTest.check(testString);
      }
    }
  }
  public void timeCheckingKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : BenchmarkTester.testStringsKO)
      {
        objectToTest.check(testString);
      }
    }
  }

  public void timeCheckingTooOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : BenchmarkTester.testStringsOK)
      {
        objectToTest.checkToo(testString);
      }
    }
  }
  public void timeCheckingTooKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : BenchmarkTester.testStringsKO)
      {
        objectToTest.checkToo(testString);
      }
    }
  }
}
