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
  public static final int SIZE = 10000;
  public ArrayList<String> stuff = new ArrayList<String>(SIZE);

  public void init()
  {
    System.out.println("Init");
    for (int i = 0; i < SIZE; i++)
    {
      stuff.add("Test " + i);
    }
  }

  public boolean check1()
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < SIZE; i++)
    {
      String el = stuff.get(i);
      sb.append(el);
    }
//~     return true;
    return sb.length() > 0;
  }
  public boolean check2()
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < stuff.size(); i++)
    {
      String el = stuff.get(i);
      sb.append(el);
    }
//~     return true;
    return sb.length() > 0;
  }
  public boolean check3()
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0, s = stuff.size(); i < s; i++)
    {
      String el = stuff.get(i);
      sb.append(el);
    }
//~     return true;
    return sb.length() > 0;
  }
  public boolean check4()
  {
    StringBuilder sb = new StringBuilder();
    for (String str : stuff)
    {
      String el = str;
      sb.append(el);
    }
//~     return true;
    return sb.length() > 0;
  }

  private void testCheckers()
  {
    if (!check1()) System.out.println("Error on 1");
    if (!check2()) System.out.println("Error on 2");
    if (!check3()) System.out.println("Error on 3");
    if (!check4()) System.out.println("Error on 4");
  }

  // Run the benchmark functions once to check if any exception is thrown
  private void testBenchmark()
  {
    CaliperBenchmark cb = new CaliperBenchmark();
    cb.timeChecking1(1);
    cb.timeChecking2(1);
    cb.timeChecking3(1);
    cb.timeChecking4(1);
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

// javac -cp C:\Java\libraries\caliper.jar CaliperBenchmark.java
// java -cp .;C:\Java\libraries\caliper.jar CaliperBenchmark
public class CaliperBenchmark extends SimpleBenchmark
{
  public static void main(String[] args) throws Exception
  {
    Runner.main(CaliperBenchmark.class, args);
  }

  BenchmarkTester objectToTest = new BenchmarkTester();

  @Override
  protected void setUp()
  {
    objectToTest.init();
  }
  public void timeChecking1(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      objectToTest.check1();
    }
  }
  public void timeChecking2(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      objectToTest.check2();
    }
  }
  public void timeChecking3(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      objectToTest.check3();
    }
  }
  public void timeChecking4(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      objectToTest.check4();
    }
  }
}
