/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.02.000 -- 2014/11/04 (PL) -- Using UTF-8 resources
 *  1.01.000 -- 2012/10/22 (PL) -- A little update, moving the resources to a specific folder, with a package
 *  1.00.000 -- 2005/12/21 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2014 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.experiments.i18n;

//import java.io.File;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.philhosoft.util.ResourceUtil;
import org.philhosoft.util.UTF8ResourceBundle;

/**
 * Test of internationalization (i18n: 18 chars between i and n...).
 *
 * @author Philippe Lhoste
 * @version 1.02.000
 * @date 2012/10/22
 */
public class I18nTest
{
	String m_language = "en";
	String m_country = "US";
	Locale m_locale;
	ResourceBundle m_messages;

	/**
	 * @param args  command line arguments
	 */
	public static void main(String[] args)
	{
		I18nTest i18n = new I18nTest(args);
		i18n.showMessages();

		String[] supportedLanguages =
		{
			"de-DE",
			"en-US",
			"fr-FR",
			"fr-CA",
		};
		for (String lang : supportedLanguages)
		{
			System.out.println("\n# Language: " + lang);
			String[] langArgs = lang.split("-");
			I18nTest i18nL = new I18nTest(langArgs);
			i18nL.showMessages();
		}

		System.exit(0);
	}

	I18nTest(String[] args)
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
		String baseName = ResourceUtil.getPackage(this) + "MessagesBundle";
		m_locale = new Locale(m_language, m_country);
		m_messages = UTF8ResourceBundle.getBundle(baseName, m_locale);
	}

	void showMessages()
	{
		try
		{
			System.out.println(m_messages.getString("start"));
			System.out.println(m_messages.getString("stop"));
			System.out.println(m_messages.getString("missing_with_default"));
			System.out.println(m_messages.getString("missing"));
		}
		catch (MissingResourceException e)
		{
			System.out.println("Missing a translation for: \"" + e.getKey() + "\" (" + e.getMessage() + ")");
		}
	}
}
