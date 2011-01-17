/*
 * Testing performance of various floating point checkers,
 * telling if a string is a valid number.
 * See http://phi.lho.free.fr/serendipity/index.php?/archives/28-Is-this-string-a-number.html
 */
/* File history:
 *  1.01.000 -- 2010/12/07 (PL) -- Most test cases
 *  1.00.000 -- 2010/12/01 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2010 Philippe Lhoste / PhiLhoSoft
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

// java -cp .;D:\Archives\_Recent\caliper.jar FloatingPointTester
class FloatingPointTester
{
  private static final String Digits    = "(\\p{Digit}+)";
  private static final String HexDigits = "(\\p{XDigit}+)";
  private static final String Exp       = "[eE][+-]?" + Digits;
  // The RE as given by Sun in description of Double.valueOf
  private static final String fpRegex   =
      "[\\x00-\\x20]*" +  // Optional leading "whitespace"
      "[+-]?(" + // Optional sign character
      "NaN|" +           // "NaN" string
      "Infinity|" +      // "Infinity" string
      // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
      "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +
      // . Digits ExponentPart_opt FloatTypeSuffix_opt
      "(\\.(" + Digits + ")(" + Exp + ")?)|" +
      // Hexadecimal strings
      "((" +
      // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
      "(0[xX]" + HexDigits + "(\\.)?)|" +
      // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
      "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +
      ")[pP][+-]?" + Digits + "))" +
      "[fFdD]?))" +
      "[\\x00-\\x20]*";// Optional trailing "whitespace"
  // A simplified version, covering only the most common cases
  // (dropping NaN & Infinity, better checked independely anyway, skipping hexa, only Ascii digits),
  // but deliberately keeping the numerous captures of the original.
  private static final String fpSimpleRegexA =
      "[+-]?(" +
      "((\\d+)(\\.)?((\\d+)?)([eE][+-]?(\\d+))?)|" +
      "(\\.((\\d+))([eE][+-]?(\\d+))?)" +
      ")[fFdD]?";
  // Idem, avoiding captures. Doesn't seem to make a significant difference...
  private static final String fpSimpleRegexB =
      "[+-]?(?:" +
      "(?:(?:\\d+)(?:\\.)?(?:(?:\\d+)?)(?:[eE][+-]?(?:\\d+))?)|" +
      "(?:\\.(?:(?:\\d+))(?:[eE][+-]?(?:\\d+))?)" +
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
    "0",    "+0",    "-0",     "0d",    "+0D",    "-0d",     "0f",    "+0F",    "-0F",
    "42",   "+42",   "-42",    "42D",   "+42d",   "-42d",    "42F",   "+42f",   "-42F",
    "8.",   "+8.",   "-8.",    "8.d",   "+8.D",   "-8.d",    "8.f",   "+8.F",   "-8.F",
    "18.",  "+18.",  "-18.",   "18.D",  "+18.d",  "-18.D",   "18.F",  "+18.f",  "-18.F",
    "0.1",  "+0.1",  "-0.1",   "0.1d",  "+0.1D",  "-0.1D",   "0.1f",  "+0.1F",  "-0.1f",
    "0.11", "+0.11", "-0.11",  "0.11D", "+0.11d", "-0.11D",  "0.11F", "+0.11f", "-0.11f",
    ".3",   "+.3",   "-.3",    ".3d",   "+.3D",   "-.3d",    ".3f",   "+.3F",   "-.3f",
    ".37",  "+.37",  "-.37",   ".37D",  "+.37d",  "-.37D",   ".37F",  "+.37f",  "-.37f",

    "0",    "+0",    "-0",     "0d",    "+0D",    "-0d",     "0f",    "+0F",    "-0F",
    "42",   "+42",   "-42",    "42D",   "+42d",   "-42d",    "42F",   "+42f",   "-42F",
    "8.",   "+8.",   "-8.",    "8.d",   "+8.D",   "-8.d",    "8.f",   "+8.F",   "-8.F",
    "18.",  "+18.",  "-18.",   "18.D",  "+18.d",  "-18.D",   "18.F",  "+18.f",  "-18.F",
    "0.1",  "+0.1",  "-0.1",   "0.1d",  "+0.1D",  "-0.1D",   "0.1f",  "+0.1F",  "-0.1f",
    "0.11", "+0.11", "-0.11",  "0.11D", "+0.11d", "-0.11D",  "0.11F", "+0.11f", "-0.11f",
    ".3",   "+.3",   "-.3",    ".3d",   "+.3D",   "-.3d",    ".3f",   "+.3F",   "-.3f",
    ".37",  "+.37",  "-.37",   ".37D",  "+.37d",  "-.37D",   ".37F",  "+.37f",  "-.37f",

    "3.14159265358979323d",
    "14140728F",
    "-0.001",
    "-898",
    "666",
    "42E7",
    "1e-7",
    "55.e-77",
    ".333",
    "-.333e-1",
    "-77.77e-77",
  };
  public static final String[] testStringsKO =
  {
    ".0.",
    "8..",
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
    "55.E.-77",
    ".33.3",
    "-.333.e-1",
    "-77.77e-77.",
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
  private static boolean isDigit(char c)
  {
//~     return Character.isDigit(c); // If Unicode awareness if required
    return c >= '0' && c <= '9';
  }
  public static boolean checkFloatingPointNumberByHand(String pfp)
  {
    int len = pfp.length();
    if (len == 0)
      return false;
//~     System.out.println("Checking: " + pfp);
    if (len == 1)
      return Character.isDigit(pfp.charAt(0));

    int state = 0;
    int cursor = 0;
    boolean bCheckAgain = false;
    char c = ' ';
    while (cursor < len || bCheckAgain)
    {
      if (!bCheckAgain)
      {
        c = pfp.charAt(cursor++);
      }
      else
      {
        bCheckAgain = false;
      }
//~       System.out.println("State: " + state);
//~       System.out.println("Char: " + c);
      switch (state)
      {
        case 0:
          state = 1;
          if (c == '+' || c == '-')
          {
            break;
          }
          bCheckAgain = true; // Not a sign, process current char
          break;
        case 1:
          if (c == '.')
          {
            state = 2;
          }
          else if (isDigit(c))
          {
            state = 4;
          }
          else
          {
            return false; // Unexpected char
          }
          break;
        case 2: // After prefixing dot, want digits
          if (isDigit(c))
          {
            state = 3;
          }
          else
          {
            return false;
          }
          break;
        case 3: // After dot, want more digits
          if (isDigit(c))
          {
            break; // Continue on this state
          }
          else
          {
            state = 5; // Before exponent, if any
            bCheckAgain = true; // Check for exponent or type symbol
          }
          break;
        case 4: // After initial digit, want more digits or dot
          if (c == '.')
          {
            state = 3;
          }
          else if (isDigit(c))
          {
            break; // Continue on this state
          }
          else
          {
            state = 5;
            bCheckAgain = true; // Check for exponent or type symbol
          }
          break;
        case 5: // Before exponent or type symbol
          if (c == 'e' || c == 'E')
          {
            state = 6;
          }
          else
          {
            state = 9; // Check for type symbol
            bCheckAgain = true; // Not an exponent, process current char
          }
          break;
        case 6:
          state = 7;
          if (c == '+' || c == '-')
          {
            break;
          }
          bCheckAgain = true; // Not a sign, process current char
          break;
        case 7: // Expect a digit
          if (isDigit(c))
          {
            state = 8;
          }
          else
          {
            return false;
          }
          break;
        case 8: // Loop on digits
          if (isDigit(c))
          {
            break;  // Continue on this state
          }
          else
          {
            state = 9;
            bCheckAgain = true; // Not a sign, process current char
          }
          break;
        case 9:
          state = 10;
          if (c == 'f' || c == 'F' || c == 'd' || c == 'D')
          {
            break;
          }
          return false; // We should have no more chars here!
        case 10:
          return false; // (at least) one char too much
      }
    }
    if (state == 3 || state == 4 || state == 8 || state == 10)
    {
      return cursor == len; // OK if there are no trailing chars
    }
//~     System.out.println("Stopping on state " + state);
    return false;
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
      if (!checkFloatingPointNumberByHand(testString)) System.out.println("ByHand: Error on " + testString);
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
      if (checkFloatingPointNumberByHand(testString)) System.out.println("ByHand: OK on " + testString);
      if (checkFloatingPointNumberAccurate(testString)) System.out.println("Accurate: OK on " + testString);
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
    fptb.timeFPCheckingByHandOK(1);
    fptb.timeFPCheckingByHandKO(1);
    fptb.timeFPCheckingAccurateOK(1);
    fptb.timeFPCheckingAccurateKO(1);
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

  public void timeFPCheckingByHandOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsOK)
      {
        FloatingPointTester.checkFloatingPointNumberByHand(testString);
      }
    }
  }
  public void timeFPCheckingByHandKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsKO)
      {
        FloatingPointTester.checkFloatingPointNumberByHand(testString);
      }
    }
  }

  public static void main(String[] args) throws Exception
  {
    Runner.main(FloatingPointTesterBenchmark.class, args);
  }

  public void timeFPCheckingAccurateOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsOK)
      {
        FloatingPointTester.checkFloatingPointNumberAccurate(testString);
      }
    }
  }
  public void timeFPCheckingAccurateKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsKO)
      {
        FloatingPointTester.checkFloatingPointNumberAccurate(testString);
      }
    }
  }
}

/*
>>> On an old computer.
> java -cp .;D:\Archives\_Recent\caliper.jar FloatingPointTesterBenchmark
 0% Scenario{vm=java, trial=0, benchmark=FPCheckingOK} 29170,40 ns; ?=486,58 ns @ 10 trials
 6% Scenario{vm=java, trial=0, benchmark=FPCheckingKO} 46397,68 ns; ?=846,52 ns @ 10 trials
13% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleAOK} 18295,58 ns; ?=141,39 ns @ 3 trials
19% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleAKO} 24767,98 ns; ?=95,57 ns @ 3 trials
25% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleBOK} 17565,72 ns; ?=113,22 ns @ 3 trials
31% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleBKO} 24203,24 ns; ?=147,63 ns @ 3 trials
38% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleCOK} 13368,73 ns; ?=120,19 ns @ 4 trials
44% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleCKO} 17449,91 ns; ?=234,34 ns @ 10 trials
50% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleDOK} 13567,15 ns; ?=159,90 ns @ 10 trials
56% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleDKO} 18279,78 ns; ?=162,53 ns @ 4 trials
63% Scenario{vm=java, trial=0, benchmark=FPCheckingFastOK} 13042,81 ns; ?=124,76 ns @ 8 trials
69% Scenario{vm=java, trial=0, benchmark=FPCheckingFastKO} 15943,08 ns; ?=132,36 ns @ 4 trials
75% Scenario{vm=java, trial=0, benchmark=FPCheckingAccurateOK} 7039,20 ns; ?=33,15 ns @ 3 trials
81% Scenario{vm=java, trial=0, benchmark=FPCheckingAccurateKO} 67122,62 ns; ?=437,61 ns @ 3 trials
88% Scenario{vm=java, trial=0, benchmark=FPCheckingByHandOK} 1394,36 ns; ?=23,77 ns @ 10 trials
94% Scenario{vm=java, trial=0, benchmark=FPCheckingByHandKO} 1241,15 ns; ?=14,37 ns @ 10 trials

           benchmark    us logarithmic runtime
        FPCheckingOK 29,17 =======================
        FPCheckingKO 46,40 ===========================
 FPCheckingSimpleAOK 18,30 ====================
 FPCheckingSimpleAKO 24,77 ======================
 FPCheckingSimpleBOK 17,57 ====================
 FPCheckingSimpleBKO 24,20 ======================
 FPCheckingSimpleCOK 13,37 ==================
 FPCheckingSimpleCKO 17,45 ====================
 FPCheckingSimpleDOK 13,57 ==================
 FPCheckingSimpleDKO 18,28 ====================
    FPCheckingFastOK 13,04 ==================
    FPCheckingFastKO 15,94 ===================
FPCheckingAccurateOK  7,04 =============
FPCheckingAccurateKO 67,12 ==============================
  FPCheckingByHandOK  1,39 =
  FPCheckingByHandKO  1,24 =

vm: java
trial: 0
*/
