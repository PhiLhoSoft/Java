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
 *  1.00.000 -- 2005/12/21 (PL) -- Creation
 */
package org.philhosoft.tests.i18n;

//import java.io.File;
import java.util.*;

/**
 * Test of internationalization (i18n: 18 chars between i and n...).
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/21
 */
public class I18nTest
{
	String m_language = "en";
	String m_country = "US";
	Locale m_locale;
	ResourceBundle m_messages;

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		I18nTest i18n = new I18nTest(args);
		i18n.showMessages();
		System.exit(0);
	}

	private I18nTest(String[] args)
	{
		if (args.length == 1)
		{
			m_language = args[0];
		}
		else if (args.length == 2)
		{
			m_language = args[0];
			m_country = args[1];
		}
		m_locale = new Locale(m_language, m_country);
		m_messages = ResourceBundle.getBundle("MessagesBundle", m_locale);
	}

	private void showMessages()
	{
		System.out.println(m_messages.getString("start"));
		System.out.println(m_messages.getString("stop"));
		System.out.println(m_messages.getString("excl"));
	}
}
