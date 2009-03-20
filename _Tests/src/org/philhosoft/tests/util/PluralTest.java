/*
 * PluralTest.java
 *
 * Created on 23 mars 2006, 11:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.philhosoft.tests.util;

/**
 *
 * @author PhiLho
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
    *   Get the string for a number
    *   1 => "1st"
    *   2 => "2nd"
    *   ...
    *   works only for small integers...
    *
    */
   public static String GetOrdinalNumberShortString(int nNumber)
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
   public static String GetPluralName(int nCount, String sName)
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
			System.out.println(GetPluralName(1, aWordList[i]));
			System.out.println(GetPluralName(2, aWordList[i]));
		}
		for (int i = 1; i < 35; i++)
		{
			System.out.println(GetOrdinalNumberShortString(i));
		}
			for (int i = 99; i < 135; i++)
		{
			System.out.println(GetOrdinalNumberShortString(i));
		}

		System.exit(0);
	}
}
