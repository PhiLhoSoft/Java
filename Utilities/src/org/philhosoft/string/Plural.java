/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.02.000 -- 2011/01/17 (PL) -- Normalize case of methods
 *  1.01.000 -- 2005/12/14 (PL) -- Transform the static class to an instanciable one,
 *              to add some flexibility.
 *  1.00.000 -- 2005/03/14 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2010 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.string;

/**
 * Simplistic plural management.
 *
 * !!TODO!!: Drop this class in favor of ChoiceFormat
 * http://java.sun.com/developer/JDCTechTips/2003/tt0926.html
 *
 * @author Philippe Lhoste
 * @version 1.01.000
 * @date 2005/12/14
 */
public class Plural
{
	private String m_pluralMark = "s";
	private boolean m_bZeroIsPlural = true;

	public Plural(String pluralMark, boolean bZeroIsPlural)
	{
		if (pluralMark != null)
		{
			m_pluralMark = pluralMark;
		}
		m_bZeroIsPlural = bZeroIsPlural;
	}

	public Plural(String pluralMark)
	{
		this(pluralMark, true);
	}

   /**
    * Computes the plural form of an English noun.
    * http://www2.gsu.edu/~wwwesl/egw/pluralsn.htm
    * I skipped some rare cases...
    *
    * @return N <plural-form-of-name>
    * @param nCount number
    * @param sName name to pluralize
    */
   public static String getPluralName_en(int nCount, String sName)
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

	/**
	 * Simple plural.
	 *
	 * @return the number followed by the given string,
	 * put as (regular) plural if needed.
	 */
	public String get(int nb, String name)
	{
		if (nb > 1 || (m_bZeroIsPlural && nb == 0))
		{
			return name + m_pluralMark;
		}
		return name;
	}

	/**
	 * Irregular plural.
	 *
	 * @return the number followed by one of the given strings,
	 * singular or plural as needed.
	 */
	public String get(int nb, String nameSingular, String namePlural)
	{
		if (nb > 1 || (m_bZeroIsPlural && nb == 0))
		{
			return nameSingular;
		}
		return namePlural;
	}

	/**
	 * Number and simple plural.
	 *
	 * @return the number followed by the given string,
	 * put as (regular) plural if needed.
	 */
	public String getN(int nb, String name)
	{
		if (nb > 1 || (m_bZeroIsPlural && nb == 0))
		{
			return nb + " " + name + m_pluralMark;
		}
		return nb + " " + name;
	}

	/**
	 * Number and irregular plural.
	 *
	 * @return the number followed by one of the given strings,
	 * singular or plural as needed.
	 */
	public String getN(int nb, String nameSingular, String namePlural)
	{
		if (nb > 1 || (m_bZeroIsPlural && nb == 0))
		{
			return nb + " " + nameSingular;
		}
		return nb + " " + namePlural;
	}

	/*----- Getters and setters (got to have them, doesn't I?) -----*/

	public void setPluralMark(String pluralMark) { m_pluralMark = pluralMark; }
	public String getPluralMark() { return m_pluralMark; }
	public void setZeroIsPlural(boolean bZeroIsPlural) { m_bZeroIsPlural = bZeroIsPlural; }
	public boolean isZeroPlural() { return m_bZeroIsPlural; }
}
