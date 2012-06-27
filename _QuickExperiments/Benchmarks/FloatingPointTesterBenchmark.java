/*
 * Testing performance of various floating point checkers,
 * telling if a string is a valid number.
 * Shows abd test hand-coded automatons.
 * See http://phi.lho.free.fr/serendipity/index.php?/archives/28-Is-this-string-a-number.html
 */
/* File history:
 *  1.02.000 -- 2011/08/20 (PL) -- Even more test cases, add the FSM version.
 *  1.01.000 -- 2010/12/07 (PL) -- More test cases
 *  1.00.000 -- 2010/12/01 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2010-2011 Philippe Lhoste / PhiLhoSoft
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

// This implements the checkers, and allows to test them against a test set.
// java -cp .;C:\Java\libraries\caliper.jar FloatingPointTester
// Just displays Done if all is OK, will shows some error messages otherwise.
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

    "0e0",    "+0E0",    "-0e1",     "0E+1d",    "+0e-1D",    "-0E+0d",     "0e-0f",    "+0e987F",    "-0E-357F",
    "42e1",   "+42E2",   "-42e3",    "42E+4D",   "+42e-5d",   "-42E+6d",    "42e-7F",   "+42e987f",   "-42E-357F",
    "8.e1",   "+8.E2",   "-8.e3",    "8.E+4d",   "+8.e-5D",   "-8.E+6d",    "8.e-7f",   "+8.e987F",   "-8.E-357F",
    "18.e1",  "+18.E2",  "-18.e3",   "18.E+4D",  "+18.e-5d",  "-18.E+6D",   "18.e-7F",  "+18.e987f",  "-18.E-357F",
    "0.1e1",  "+0.1E2",  "-0.1e3",   "0.1E+4d",  "+0.1e-5D",  "-0.1E+6D",   "0.1e-7f",  "+0.1e987F",  "-0.1E-357f",
    "0.11e1", "+0.11E2", "-0.11e3",  "0.11E+4D", "+0.11e-5d", "-0.11E+6D",  "0.11e-7F", "+0.11e987f", "-0.11E-357f",
    ".3e1",   "+.3E2",   "-.3e3",    ".3E+4d",   "+.3e-5D",   "-.3E+6d",    ".3e-7f",   "+.3e987F",   "-.3E-357f",
    ".37e1",  "+.37E2",  "-.37e3",   ".37E+4D",  "+.37e-5d",  "-.37E+6D",   ".37e-7F",  "+.37e987f",  "-.37E-357f",

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
//~     "299792458l",
//~     "-299792458L",
  };
  public static final String[] testStringsKO =
  {
    "", ".",
    "+", "-",
    "+.", "-.",
    "55.e", "55.e-", "55.e+",
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
    "-666#",
    "42F7",
    "1f-7",
    "55.E.-77",
    ".33.3",
    "-.333.e-1",
    "-77.77e-77.",
//~     "42e7l",
//~     "-42E7L",
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
  // Update: improved FSM (no bCheckAgain!)
  public static boolean checkFloatingPointNumberWithFSM(String pfp)
  {
    int len = pfp.length();
    if (len == 0)
      return false;
//~     System.out.println("Checking: " + pfp);
    if (len == 1)
      return Character.isDigit(pfp.charAt(0));

    int state = 0;
    int cursor = 0;
    char c = ' ';
    while (cursor < len)
    {
      c = pfp.charAt(cursor++);
//~       System.out.println("State: " + state);
//~       System.out.println("Char: " + c);
      switch (state)
      {
        case 0:
          if (c == '+' || c == '-')
          {
            state = 1;
          }
          else if (c == '.')
          {
            state = 2;
          }
          else if (isDigit(c))
          {
            state = 3;
          }
          else
          {
            return false; // Unexpected char
          }
          break;
        case 1:
          if (c == '.')
          {
            state = 2;
          }
          else if (isDigit(c))
          {
            state = 3;
          }
          else
          {
            return false; // Unexpected char
          }
          break;
        case 2: // After prefixing dot, want digits
          if (isDigit(c))
          {
            state = 4;
          }
          else
          {
            return false;
          }
          break;
        case 3: // After initial digit, want more digits or dot or exponent or type
          if (c == '.')
          {
            state = 4;
          }
          else if (c == 'e' || c == 'E')
          {
            state = 5;
          }
          else if (c == 'f' || c == 'F' || c == 'd' || c == 'D' || c == 'l' || c == 'L')
          {
            state = 8;
          }
          else if (isDigit(c))
          {
            break; // Continue on this state
          }
          else
          {
            return false;
          }
          break;
        case 4: // After dot, want more digits or exponent or type
          if (c == 'e' || c == 'E')
          {
            state = 5;
          }
          else if (c == 'f' || c == 'F' || c == 'd' || c == 'D')
          {
            state = 8;
          }
          else if (isDigit(c))
          {
            break; // Continue on this state
          }
          else
          {
            return false;
          }
          break;
        case 5: // After exponent
          if (c == '+' || c == '-')
          {
            state = 6;
          }
          else if (isDigit(c))
          {
            state = 7;
          }
          else
          {
            return false;
          }
          break;
        case 6: // After exponent's sign
          if (isDigit(c))
          {
            state = 7;
          }
          else
          {
            return false;
          }
          break;
        case 7: // Exponent's digits
          if (isDigit(c))
          {
            break; // Stay here
          }
          else if (c == 'f' || c == 'F' || c == 'd' || c == 'D')
          {
            state = 8;
          }
          else
          {
            return false;
          }
          break;
        case 8:
          return false; // (at least) one char too much
      }
    }
//~     System.out.println("Stopping on state " + state);
    // Must stop on one of these states
    return state == 3 || state == 4 || state == 7 || state == 8;
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
      if (!checkFloatingPointNumberWithFSM(testString)) System.out.println("FSM: Error on " + testString);
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
      if (checkFloatingPointNumberWithFSM(testString)) System.out.println("FSM: OK on " + testString);
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
      System.out.println("Done");
      return;
    }
    System.out.println("Is '" + args[0] + "' a valid floating point number? " +
        checkFloatingPointNumber(args[0]));
  }
}

// javac -cp C:\Java\libraries\caliper.jar FloatingPointTesterBenchmark.java
// java -cp .;C:\Java\libraries\caliper.jar FloatingPointTesterBenchmark
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

  public void timeFPCheckingWithFSMOK(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsOK)
      {
        FloatingPointTester.checkFloatingPointNumberWithFSM(testString);
      }
    }
  }
  public void timeFPCheckingWithFSMKO(int reps)
  {
    for (int i = 0; i < reps; i++)
    {
      for (String testString : FloatingPointTester.testStringsKO)
      {
        FloatingPointTester.checkFloatingPointNumberWithFSM(testString);
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
> java -cp .;C:\Java\libraries\caliper.jar FloatingPointTesterBenchmark
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

>>> On a recent computer, with the FSM test
>  java -cp .;C:\Java\libraries\caliper.jar FloatingPointTesterBenchmark
 0% Scenario{vm=java, trial=0, benchmark=FPCheckingOK} 164210,37 ns; ?=1451,06 ns @ 3 trials
 6% Scenario{vm=java, trial=0, benchmark=FPCheckingKO} 29515,60 ns; ?=260,96 ns @ 5 trials
11% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleAOK} 100756,16 ns; ?=41,59 ns @ 3 trials
17% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleAKO} 16940,59 ns; ?=69,69 ns @ 3 trials
22% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleBOK} 94456,71 ns; ?=29,36 ns @ 3 trials
28% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleBKO} 16273,40 ns; ?=45,41 ns @ 3 trials
33% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleCOK} 72934,06 ns; ?=98,86 ns @ 3 trials
39% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleCKO} 11513,43 ns; ?=108,00 ns @ 4 trials
44% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleDOK} 72510,03 ns; ?=385,10 ns @ 3 trials
50% Scenario{vm=java, trial=0, benchmark=FPCheckingSimpleDKO} 11431,43 ns; ?=111,21 ns @ 7 trials
56% Scenario{vm=java, trial=0, benchmark=FPCheckingFastOK} 73240,36 ns; ?=773,96 ns @ 10 trials
61% Scenario{vm=java, trial=0, benchmark=FPCheckingFastKO} 10809,08 ns; ?=37,43 ns @ 3 trials
67% Scenario{vm=java, trial=0, benchmark=FPCheckingByHandOK} 9781,84 ns; ?=89,06 ns @ 3 trials
72% Scenario{vm=java, trial=0, benchmark=FPCheckingByHandKO} 1127,43 ns; ?=6,54 ns @ 3 trials
78% Scenario{vm=java, trial=0, benchmark=FPCheckingWithFSMOK} 7594,79 ns; ?=47,73 ns @ 3 trials
83% Scenario{vm=java, trial=0, benchmark=FPCheckingWithFSMKO} 676,69 ns; ?=5,10 ns @ 3 trials
89% Scenario{vm=java, trial=0, benchmark=FPCheckingAccurateOK} 22172,04 ns; ?=26,03 ns @ 3 trials
94% Scenario{vm=java, trial=0, benchmark=FPCheckingAccurateKO} 35037,21 ns; ?=20,58 ns @ 3 trials

           benchmark     ns logarithmic runtime
        FPCheckingOK 164210 ==============================
        FPCheckingKO  29516 ====================
 FPCheckingSimpleAOK 100756 ===========================
 FPCheckingSimpleAKO  16941 ==================
 FPCheckingSimpleBOK  94457 ===========================
 FPCheckingSimpleBKO  16273 =================
 FPCheckingSimpleCOK  72934 =========================
 FPCheckingSimpleCKO  11513 ===============
 FPCheckingSimpleDOK  72510 =========================
 FPCheckingSimpleDKO  11431 ===============
    FPCheckingFastOK  73240 =========================
    FPCheckingFastKO  10809 ===============
  FPCheckingByHandOK   9782 ===============
  FPCheckingByHandKO   1127 ===
 FPCheckingWithFSMOK   7595 =============
 FPCheckingWithFSMKO    677 =
FPCheckingAccurateOK  22172 ===================
FPCheckingAccurateKO  35037 =====================

vm: java
trial: 0
*/
