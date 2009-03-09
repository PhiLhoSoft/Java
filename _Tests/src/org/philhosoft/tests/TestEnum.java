/*
 * Tests: A collection of little test programs to explore Java language.
 *
 * Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 *
 * Copyright notice: See the PhiLhoSoftLicence.txt file for details.
 * This file is distributed under the zlib/libpng license.
 * Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
 */
/* File history:
 *  1.00.000 -- 2008/11/05 (PL) -- Creation
 */
package org.philhosoft.tests;




/**
 * Template class for test applications.
 *
 * @author Philippe Lhoste
 * @version 1.01.000
 * @date 2008/11/05
 */
public enum TestEnum
{
   ICHI(1, "un", false),
   NI(2, "deux", true),
   SAN(3, "trois", false),
   SHI(4, "quatre", true);

   private final int m_value;
   private final String m_frenchName;
   private final boolean m_bEven;

   TestEnum(int value, String frenchName, boolean bEven)
   {
      m_value = value;
      m_frenchName = frenchName;
      m_bEven = bEven;
   }

   String GetName()
   {
      return m_frenchName;
   }

   int GetValue()
   {
      return m_value;
   }

   boolean IsEven()
   {
      return m_bEven;
   }

   // Test function
   public static void main(String[] args)
   {
      for (TestEnum ev : TestEnum.values())
      {
         System.out.printf("Enumeration: %s (%s) = %d -> '%s'\n",
               ev, ev.name(), ev.ordinal(), TestEnum.valueOf(ev.name()));
         System.out.printf("             %s (%b) = %d\n",
               ev.GetName(), ev.IsEven(), ev.GetValue());
      }
   }
}
