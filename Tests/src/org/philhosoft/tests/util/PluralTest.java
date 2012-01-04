/*
 * Tests: A collection of little test programs to explore Java language.
 * Test of the Plural class.
 */
/* File history:
 *  1.02.000 -- 2011/01/17 (PL) -- Normalize case of methods
 *  1.00.000 -- 2010/03/09 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2010-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.util;

/**
 * Testing plural of several English words.
 */
public class PluralTest
{
	private static final String aWordList[] =
	{
		"baby", "spy", "try", "day", "prey", "boy", "guy",
		"glass", "buzz", "box", "bush", "switch", "beach",
		"potato", "echo", "hero",
//		"studio", "piano", "kangaroo", "zoo",
		"appendix", "index", "shelf", "wolf", "knife", "wife", "half",
		"analysis", "basis", "parenthesis",
		"formula", "antenna", "criterion", "phenomenon", "datum", "curriculum",
		"focus", "stimulus", //"corpus", "genus",
	};

   /**
    *   Gets the string for a number
    *   1 => "1st"
    *   2 => "2nd"
    *   ...
    *   works only for small integers...
    *
    */
   public static String getOrdinalNumberShortString(int nNumber)
   {
      String sRet = "" + nNumber;

      if (nNumber > 0)
      {
         String sSuffix = null;

         if (nNumber % 100 > 3 && nNumber % 100 < 20)
            sSuffix = "th";
         else if (nNumber % 10 == 1)
            sSuffix = "st";
         else if (nNumber % 10 == 2)
            sSuffix = "nd";
         else if (nNumber % 10 == 3)
            sSuffix = "rd";
         else
            sSuffix = "th";
         sRet += sSuffix;
      }
      return sRet;
   }

   /**
    * Computes the plural form of a noun.
    * http://www2.gsu.edu/~wwwesl/egw/pluralsn.htm
    * I skipped some rare cases...
    *
    * @return N <plural-form-of-name>
    * @param nCount number
    * @param sName name to pluralize
    */
   public static String getPluralName(int nCount, String sName)
   {
      String sCount = "" + nCount + " ";

      if (nCount == 1 || nCount < 0)
      {
         // Singular
         // In English, we write: 0 foos, 1 foo, 2 foos, etc.
         return sCount + sName;
      }
      char cLastChar = sName.charAt(sName.length() - 1);
      char cPreviousChar = '?';
      if (sName.length() > 1)
      {
         cPreviousChar = sName.charAt(sName.length() - 2);
      }
      if (cLastChar == 'y')
      {
         if (cPreviousChar != 'a' && cPreviousChar != 'e' &&
               cPreviousChar != 'o' && cPreviousChar != 'u')
         {
            // Not a voyel
            // baby/babies, spy/spies, try/tries
            return sCount + sName.substring(0, sName.length() - 1) + "ies";
         }
         // days, preys, boys, guys: fall through
      }
      else if ((cPreviousChar == 'e' || cPreviousChar == 'i') &&
            cLastChar == 'x')
      {
         // appendix/appendices, index/indices
         return sCount + sName.substring(0, sName.length() - 2) + "ices";
      }
      else if (cPreviousChar == 'i' && cLastChar == 's')
      {
         // analysis/analyses, basis/bases, parenthesis/parentheses
         return sCount + sName.substring(0, sName.length() - 2) + "es";
      }
      else if (cPreviousChar == 'f' && cLastChar == 'e')
      {
         // knife/knives, wife/wives
         return sCount + sName.substring(0, sName.length() - 2) + "ves";
      }
      else if (cPreviousChar == 'o' && cLastChar == 'n' ||
            cPreviousChar == 'u' && cLastChar == 'm')
      {
         // criterion/criteria, phenomenon/phenomena
         // datum/data, curriculum/curricula
        return sCount + sName.substring(0, sName.length() - 2) + "a";
      }
      else if (cPreviousChar == 'u' && cLastChar == 's')
      {
         // focus/foci, stimulus/stimuli
         // but also corpus/corpora, genus/genera, left here
         return sCount + sName.substring(0, sName.length() - 2) + "i";
      }
      else if (cLastChar == 'f')
      {
         // shelf/shelves, wolf/wolves, half/halves
         return sCount + sName.substring(0, sName.length() - 1) + "ves";
      }
      else if (cLastChar == 'a')
      {
         // formula/formulae, antenna/antennae
         return sCount + sName + "e";
      }
      else if (cLastChar == 's' || cLastChar == 'z' ||
            cLastChar == 'x' || cLastChar == 'o' ||
            (cPreviousChar == 's' || cPreviousChar == 'c') && cLastChar == 'h')
      {
         // glass/glasses, buzz/buzzes, box/boxes, bush/bushes, switch/switches
         // potato/potatoes, echo/echoes, hero/heroes
         // Except: studio/studios, piano/pianos, kangaroo/kangaroos, zoo/zoos
         // but they aren't frequent in PrOptima...
         return sCount + sName + "es";
      }
      // Last resort, the most common one...
      return sCount + sName + "s";
   }

	public static void main(String[] args)
	{
		for (int i = 0; i < aWordList.length; i++)
		{
			System.out.println(getPluralName(1, aWordList[i]));
			System.out.println(getPluralName(2, aWordList[i]));
		}
		for (int i = 1; i < 35; i++)
		{
			System.out.println(getOrdinalNumberShortString(i));
		}
			for (int i = 99; i < 135; i++)
		{
			System.out.println(getOrdinalNumberShortString(i));
		}

		System.exit(0);
	}
}
