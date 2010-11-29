import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.util.regex.*;

// http://code.google.com/p/caliper/
import com.google.caliper.Param;
import com.google.caliper.SimpleBenchmark;
import com.google.caliper.Runner;

class FloatingPointTester
{
  private static final String Digits    = "(\\p{Digit}+)";
  private static final String HexDigits = "(\\p{XDigit}+)";
  private static final String Exp       = "[eE][+-]?" + Digits;
  // The RE as given by Sun in description of Double.valueOf
  private static final String fpRegex   =
      "[\\x00-\\x20]*[+-]?(NaN|Infinity|(((" + Digits +
      "(\\.)?(" + Digits + "?)(" + Exp + ")?)|(\\.(" + Digits + ")(" + Exp + ")?)|" +
      "(((0[xX]" + HexDigits + "(\\.)?)|(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
      ")[pP][+-]?" + Digits + "))[fFdD]?))[\\x00-\\x20]*";
  // A simplified version, covering only the most common cases (skipping hexa),
  // but deliberately keeping the numerous captures of the original
  private static final String fpSimpleRegexA =
      "[+-]?(" +
      "((\\d+)(\\.?)(\\d+)?([eE][+-]?(\\d+))?)|" +
      "(\\.(\\d+)([eE][+-]?(\\d+))?)" +
      ")[fFdD]?";
  // Idem, avoiding captures. Doesn't seem to make a significant difference...
  private static final String fpSimpleRegexB =
      "[+-]?(?:" +
      "(?:(?:\\d+)(?:\\.?)(?:\\d+)?(?:[eE][+-]?(?:\\d+))?)|" +
      "(?:\\.(?:\\d+)(?:[eE][+-]?(?:\\d+))?)" +
      ")[fFdD]?";
  // Removing all the unneeded captures
  private static final String fpSimpleRegexC =
      "[+-]?(" +
      "\\d+\\.?\\d*([eE][+-]?\\d+)?|" +
      "\\.\\d+([eE][+-]?\\d+)?" +
      ")[fFdD]?";
  // Smaller alternative branch
  private static final String fpSimpleRegexD =
      "[+-]?(" +
      "\\d+\\.?\\d*|" +
      "\\.\\d+" +
      ")([eE][+-]?\\d+)?[fFdD]?";
  // Splitting  the simple regex in two to avoid the slow alternation operator, replaced with some logic
  private static final String fpFastRegex1 =
      "[+-]?" +
      "(\\d+\\.?\\d*([eE][+-]?\\d+)?)" +
      "[fFdD]?";
  private static final String fpFastRegex2 =
      "[+-]?" +
      "\\.\\d+([eE][+-]?\\d+)?" +
      "[fFdD]?";

  public static final String[] testStringsOK =
  {
    "0",
    "0.",
    "0.1",
    ".1",
    "-.1",
    "3.14159265358979323d",
    "14140728F",
    "-0.001",
    "-898",
    "666",
    "42E7",
    "1e-7",
    "55.e-7",
    ".333",
  };
  public static final String[] testStringsKO =
  {
    ".0.",
    "0..",
    "0.1-",
    "..1",
    "--.1",
    "3,14159265358979323D",
    "14 140 728f",
    "0.1%",
    "-0.001$",
    "-8/98",
    "#666",
    "42F7",
    "1f-7",
    "55.E.-7",
    ".33.3",

  };

  private static final Pattern fpPattern = Pattern.compile(fpRegex);
  private static final Pattern fpSimplePatternA = Pattern.compile(fpSimpleRegexA);
  private static final Pattern fpSimplePatternB = Pattern.compile(fpSimpleRegexB);
  private static final Pattern fpSimplePatternC = Pattern.compile(fpSimpleRegexC);
  private static final Pattern fpSimplePatternD = Pattern.compile(fpSimpleRegexD);
  private static final Pattern fpFastPattern1 = Pattern.compile(fpFastRegex1);
  private static final Pattern fpFastPattern2 = Pattern.compile(fpFastRegex2);

  public static boolean checkFloatingPointNumber(String wouldBeFloatingPoint)
  {
    Matcher m = fpPattern.matcher(wouldBeFloatingPoint);
    return m.matches();
  }
  public static boolean checkFloatingPointNumberSimpleA(String wouldBeFloatingPoint)
  {
    Matcher m = fpSimplePatternA.matcher(wouldBeFloatingPoint);
    return m.matches();
  }
  public static boolean checkFloatingPointNumberSimpleB(String wouldBeFloatingPoint)
  {
    Matcher m = fpSimplePatternB.matcher(wouldBeFloatingPoint);
    return m.matches();
  }
  public static boolean checkFloatingPointNumberSimpleC(String wouldBeFloatingPoint)
  {
    Matcher m = fpSimplePatternC.matcher(wouldBeFloatingPoint);
    return m.matches();
  }
  public static boolean checkFloatingPointNumberSimpleD(String wouldBeFloatingPoint)
  {
    Matcher m = fpSimplePatternD.matcher(wouldBeFloatingPoint);
    return m.matches();
  }
  public static boolean checkFloatingPointNumberFast(String wouldBeFloatingPoint)
  {
    if (wouldBeFloatingPoint.length() >= 2)
    {
      char c0 = wouldBeFloatingPoint.charAt(0);
      char c1 = wouldBeFloatingPoint.charAt(1);
      if (c0 == '.' ||
          (c0 == '+' || c0 == '-') && c1 == '.')
      {
        Matcher m = fpFastPattern2.matcher(wouldBeFloatingPoint);
        return m.matches();
      }
    }
    Matcher m = fpFastPattern1.matcher(wouldBeFloatingPoint);
    return m.matches();
  }
  public static boolean checkFloatingPointNumberByHand(String pfp)
  {
    int len = pfp.length();
    if (len == 0)
      return false;
    if (len == 1)
      return Character.isDigit(pfp.charAt(0));

    int cursor = 0;
    boolean hasDot = false;
    while (cursor < len)
    {
      char c = pfp.charAt(cursor++);
      if (cursor == 0 && (c == '+' || c == '-'))
        continue;
    }

/*
    int cursor = 0;
    char c = pfp.charAt(0);
    if (c == '+' || c == '-') cursor++;
    c = pfp.charAt(cursor);
    boolean hasDot = false;
    if (c == '.')
    {
      hasDot = true;
      cursor++;
      c = pfp.charAt(cursor);
    }
    int digitNb = 0;
    while (Character.isDigit(c))
    {
      digitNb++;
      cursor++;
      c = pfp.charAt(cursor);
    }
    if (digitNb == 0) return false;
    if (!hasDot && c == '.')
    {
      c = pfp.charAt(++cursor);
      while (Character.isDigit(c))
      {
        c = pfp.charAt(++cursor);
      }
    }
*/
return true;
  }
  public static boolean checkFloatingPointNumberAccurate(String wouldBeFloatingPoint)
  {
    try
    {
      Double d = Double.valueOf(wouldBeFloatingPoint);
      return true;
    }
    catch (NumberFormatException e)
    {
      return false;
    }
  }

  // Run the benchmark functions once to check if any exception is thrown
  private static void testBenchmark()
  {
    FloatingPointTesterBenchmark fptb = new FloatingPointTesterBenchmark();
    fptb.timeFPCheckingOK(1);
    fptb.timeFPCheckingKO(1);
    fptb.timeFPCheckingSimpleAOK(1);
    fptb.timeFPCheckingSimpleAKO(1);
    fptb.timeFPCheckingSimpleBOK(1);
    fptb.timeFPCheckingSimpleBKO(1);
    fptb.timeFPCheckingSimpleCOK(1);
    fptb.timeFPCheckingSimpleCKO(1);
    fptb.timeFPCheckingSimpleDOK(1);
    fptb.timeFPCheckingSimpleDKO(1);
    fptb.timeFPCheckingFastOK(1);
    fptb.timeFPCheckingFastKO(1);
    fptb.timeFloatingPointNumberAccurateOK(1);
    fptb.timeFloatingPointNumberAccurateKO(1);
  }
  private static void testCheckers()
  {
    for (String testString : testStringsOK)
    {
      if (!checkFloatingPointNumber(testString)) System.out.println("RE: Error on " + testString);
      if (!checkFloatingPointNumberSimpleA(testString)) System.out.println("SimpleA: Error on " + testString);
      if (!checkFloatingPointNumberSimpleB(testString)) System.out.println("SimpleB: Error on " + testString);
      if (!checkFloatingPointNumberSimpleC(testString)) System.out.println("SimpleC: Error on " + testString);
      if (!checkFloatingPointNumberSimpleD(testString)) System.out.println("SimpleD: Error on " + testString);
      if (!checkFloatingPointNumberFast(testString)) System.out.println("Fast: Error on " + testString);
//~       if (!checkFloatingPointNumberByHand(testString)) System.out.println("ByHand: Error on " + testString);
      if (!checkFloatingPointNumberAccurate(testString)) System.out.println("Accurate: Error on " + testString);
    }
    for (String testString : testStringsKO)
    {
      if (checkFloatingPointNumber(testString)) System.out.println("RE: OK on " + testString);
      if (checkFloatingPointNumberSimpleA(testString)) System.out.println("SimpleA: OK on " + testString);
      if (checkFloatingPointNumberSimpleB(testString)) System.out.println("SimpleB: OK on " + testString);
      if (checkFloatingPointNumberSimpleC(testString)) System.out.println("SimpleC: OK on " + testString);
      if (checkFloatingPointNumberSimpleD(testString)) System.out.println("SimpleD: OK on " + testString);
      if (checkFloatingPointNumberFast(testString)) System.out.println("Fast: OK on " + testString);
//~       if (checkFloatingPointNumberByHand(testString)) System.out.println("ByHand: OK on " + testString);
      if (checkFloatingPointNumberAccurate(testString)) System.out.println("Accurate: OK on " + testString);
    }
  }

  public static void main(String args[])
  {
    if (args.length == 0)
    {
//~       System.err.println("Usage: FloatingPointTester <floating point value>");
//~       System.exit(1);
      testCheckers();
      testBenchmark();
      return;
    }
    System.out.println("Is '" + args[0] + "' a valid floating point number? " +
        checkFloatingPointNumber(args[0]));
  }
}

// javac -cp D:\Archives\_Recent\caliper.jar FloatingPointTesterBenchmark.java
// java -cp .;D:\Archives\_Recent\caliper.jar FloatingPointTesterBenchmark
public class FloatingPointTesterBenchmark extends SimpleBenchmark
{
  @Override
  protected void setUp()
  {
//~     FloatingPointTester.makePattern();
  }
  public void timeFPCheckingOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsOK)
      {
        FloatingPointTester.checkFloatingPointNumber(testString);
      }
    }
  }
  public void timeFPCheckingKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsKO)
      {
        FloatingPointTester.checkFloatingPointNumber(testString);
      }
    }
  }

  public void timeFPCheckingSimpleAOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsOK)
      {
        FloatingPointTester.checkFloatingPointNumberSimpleA(testString);
      }
    }
  }
  public void timeFPCheckingSimpleAKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsKO)
      {
        FloatingPointTester.checkFloatingPointNumberSimpleA(testString);
      }
    }
  }

  public void timeFPCheckingSimpleBOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsOK)
      {
        FloatingPointTester.checkFloatingPointNumberSimpleB(testString);
      }
    }
  }
  public void timeFPCheckingSimpleBKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsKO)
      {
        FloatingPointTester.checkFloatingPointNumberSimpleB(testString);
      }
    }
  }

  public void timeFPCheckingSimpleCOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsOK)
      {
        FloatingPointTester.checkFloatingPointNumberSimpleC(testString);
      }
    }
  }
  public void timeFPCheckingSimpleCKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsKO)
      {
        FloatingPointTester.checkFloatingPointNumberSimpleC(testString);
      }
    }
  }

  public void timeFPCheckingSimpleDOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsOK)
      {
        FloatingPointTester.checkFloatingPointNumberSimpleD(testString);
      }
    }
  }
  public void timeFPCheckingSimpleDKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsKO)
      {
        FloatingPointTester.checkFloatingPointNumberSimpleD(testString);
      }
    }
  }

  public void timeFPCheckingFastOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsOK)
      {
        FloatingPointTester.checkFloatingPointNumberFast(testString);
      }
    }
  }
  public void timeFPCheckingFastKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsKO)
      {
        FloatingPointTester.checkFloatingPointNumberFast(testString);
      }
    }
  }

  public void timeFloatingPointNumberAccurateOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsOK)
      {
        FloatingPointTester.checkFloatingPointNumberAccurate(testString);
      }
    }
  }
  public void timeFloatingPointNumberAccurateKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsKO)
      {
        FloatingPointTester.checkFloatingPointNumberAccurate(testString);
      }
    }
  }

  public static void main(String[] args) throws Exception
  {
    Runner.main(FloatingPointTesterBenchmark.class, args);
  }
}
