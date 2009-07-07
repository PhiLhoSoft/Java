/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2005/04/29 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.string;

import java.util.regex.*;

/**
 * Detects the browser type, version, and OS from the User-Agent string.
 *
 * Based on the long list of User-Agent strings
 * found at: http://www.pgts.com.au/pgtsj/pgtsj0212d.html
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/04/29
 */
public class BrowserSniffer
{
	public static final String TYPE_MSIE = "Internet Explorer";
	public static final String TYPE_AOL = "AOL";

	public static final String TYPE_OPERA = "Opera";

	public static final String TYPE_MOZILLA = "Mozilla";
	public static final String TYPE_NETSCAPE = "Netscape";
//	public static final String TYPE_NETSCAPE6 = "Netscape6";
	public static final String TYPE_FIREFOX = "Firefox";
	public static final String TYPE_FIREBIRD = "Firebird";
	public static final String TYPE_CAMINO = "Camino";
	public static final String TYPE_EPIPHANY = "Epiphany";
	public static final String TYPE_GALEON = "Galeon";
	public static final String TYPE_KMELEON = "K-Meleon";
	public static final String TYPE_MULTIZILLA = "MultiZilla";

	public static final String TYPE_KONQUEROR = "Konqueror";
	public static final String TYPE_SAFARI = "Safari";
	public static final String TYPE_OMNIWEB = "OmniWeb";

	public static final String TYPE_ICAB = "iCab";
	public static final String TYPE_LYNX = "Lynx";

	public static final String TYPE_NN = "Netscape Navigator";

	public static final String TYPE_JUNK = "Junk";


	public static final String OS_WINDOWS = "Windows";
	public static final String OS_UNIX = "Unix";
	public static final String OS_MACINTOSH = "Macintosh";
	public static final String OS_OTHER = "Other";
	// For parsing
	private static final String OS_WIN = "Win";	// Can trigger on Winner...
	private static final String OS_UNX = "X11";
	private static final String OS_MAC = "Mac";	// Can trigger on Machine...

	public static final String ENGINE_MSIE = "MSIE";
	public static final String ENGINE_OPERA = "Opera";
	public static final String ENGINE_GECKO = "Gecko";
	public static final String ENGINE_KHTML = "KHTML";
	public static final String ENGINE_NN = "Netscape";	// Old Netscape Navigator

	public static final String UNKNOWN = "Unknown";

	/*-- Lists of browser types grouped by engine --*/

	public static final String[] BL_KHTML =
	{
		TYPE_KONQUEROR, TYPE_SAFARI, TYPE_OMNIWEB
	};
	public static final String[] BL_GECKO =
	{
		TYPE_GALEON, TYPE_CAMINO, TYPE_EPIPHANY, TYPE_KMELEON,
		TYPE_FIREFOX, TYPE_FIREBIRD, TYPE_MULTIZILLA,
		TYPE_NETSCAPE//, TYPE_NETSCAPE6
	};

	/*-- Lists of OSes grouped by family --*/

	public static final String[] OSL_MACS =
	{
		"PPC", "Mac_", "Darwin", "Mac ", "Mac"
	};
	public static final String[] OSL_UNICES =
	{
		"Linux", "Sun", "OpenBSD", "FreeBSD", "NetBSD",
		"UNI", "IRIX", "HP-UX", "AIX", "OSF"
	};
	public static final String[] OSL_OTHERS =
	{
		"OS/2", "AmigaOS", "BeOS", "Symbian", "Palm",
		"Java", "Profile", "RISC"
	};

	/*-- The private data --*/

	/// The given user agent string.
	private String m_userAgentString;

	// With accessors
	/**
	 * The browser engine, ie. the core that interprets HTML/CSS
	 * and renders it more or less acurately...
	 * This is probably the most signifiant information.
	 */
	private String m_browserEngine = UNKNOWN;
	/**
	 * The browser type, ie. the name of the browser itself.
	 * Obviously, I can list here only the most common ones.
	 * The numerous IE-based browsers are just put under the
	 * generic name "Internet Explorer"...
	 */
	private String m_browserType = UNKNOWN;
	/**
	 * The OS type, roughtly broken down in four categories:
	 * Windows, Macintosh-based, Unices and others...
	 */
	private String m_os = UNKNOWN;
	/**
	 * The exact OS name as extracted from the UA string.
	 */
	private String m_osFullName = UNKNOWN;
	/**
	 * Major version number: most versions numbers are in the form
	 * 1.1, 2.05 or 4.3.5
	 * The major version number is the first digit.
	 */
	private int m_versionMajor;
	/**
	 * Minor version number: the second digit, if any.
	 * The other numbers, often minor updates (bug fixes) or
	 * compilation numbers, are ignored.
	 */
	private int m_versionMinor;

	/**
	 * Constructor of the object.
	 */
	public BrowserSniffer(String userAgentString)
	{
		m_userAgentString = userAgentString;

		if (findBrowser())	// OK
		{
			// Get OS type
			findOS();
		}
	}

	/*--- The two main methods ---*/

	/**
	 * Parse the user agent string.
	 */
	private boolean findBrowser()
	{
		int enginePos, browserPos;

		// The order in which we test the engines/types is important, because some
		// UA puts "false" information to trick "dumb" sniffers, so they don't reject them...
		// So I try and detect first the most specific engines/types, then the most common/popular ones...

		// Starts with two specific cases...
		enginePos = m_userAgentString.indexOf(TYPE_ICAB);
		if (enginePos >= 0)
		{
			// iCab/2.9.8 (Macintosh; U; PPC; Mac OS X)
			// Mozilla/5.0 (compatible; iCab 2.9.8; Macintosh; U, PPC; Mac OS X)
			m_browserEngine = UNKNOWN;
			m_browserType = TYPE_ICAB;
			extractVersion(enginePos + TYPE_ICAB.length() + 1);

			return true;
		}
		enginePos = m_userAgentString.indexOf(TYPE_LYNX);
		if (enginePos >= 0)
		{
			// Lynx/2.8.4rel.1 libwww-FM/2.14 SSL-MM/1.4.1 OpenSSL/0.9.6g
			m_browserEngine = UNKNOWN;
			m_browserType = TYPE_LYNX;
			extractVersion(enginePos + TYPE_LYNX.length() + 1);

			return true;
		}

		// Try Opera
		enginePos = m_userAgentString.indexOf(ENGINE_OPERA);
		if (enginePos >= 0)
		{
			m_browserEngine = ENGINE_OPERA;
			m_browserType = TYPE_OPERA;

			// Two common cases:
			// Mozilla/4.0 (compatible; MSIE 5.0; Windows 2000) Opera 5.12 [en]
			// Opera/7.23 (Windows NT 5.1; U)  [fr]
			extractVersion(enginePos + ENGINE_OPERA.length() + 1);

			return true;
		}

		// Then try KHTML, as it guises itself as Gecko
		// Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en-us) AppleWebKit/125.5.5 (KHTML, like Gecko) Safari/125
		// Mozilla/5.0 (compatible; Konqueror/3.3; Linux 2.6.8.1-24mdk; X11; i686; sl) KHTML/3.3.92 (like Gecko)
		// Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en-US) AppleWebKit/125.4 (KHTML, like Gecko, Safari) OmniWeb/v563.33
		enginePos = m_userAgentString.indexOf(ENGINE_KHTML);
		if (enginePos >= 0)
		{
			m_browserEngine = ENGINE_KHTML;
			m_browserType = getFromList(m_userAgentString, BL_KHTML, false);
			if (m_browserType != null)
			{
				// Known browser, get its name and version
				browserPos = m_userAgentString.indexOf(m_browserType);
				extractVersion(browserPos + m_browserType.length() + 1);
			}
			else
			{
				// Not a known type?
				// In most of the other cases, the UA name is after the closing brace.
				int brPos = m_userAgentString.indexOf(")", enginePos);
				if (brPos >= 0)
				{
					brPos += 2;
					if (brPos < m_userAgentString.length())
					{
						m_browserType = getName(m_userAgentString, brPos, false);
						extractTypeVers(brPos);
					}
					else
					{
						m_browserType = UNKNOWN;
					}
				}
				else
				{
					m_browserType = UNKNOWN;
				}
			}

			return true;
		}

		// See if it is Gecko (which may guise as MSIE to fool dumb scripts...)
		enginePos = m_userAgentString.indexOf(ENGINE_GECKO);
		if (enginePos >= 0)
		{
			m_browserEngine = ENGINE_GECKO;
			m_browserType = getFromList(m_userAgentString, BL_GECKO, false);
			if (m_browserType != null)
			{
				// Known browser, get its name and version
				browserPos = m_userAgentString.indexOf(m_browserType);
				extractVersion(browserPos + m_browserType.length() + 1);
			}
			else
			{
				// Not a known type? Classify it as Mozilla...
				m_browserType = TYPE_MOZILLA;
				// Search the real version
				int rvPos = m_userAgentString.indexOf("rv:");
				if (rvPos >= 0)
				{
					extractVersion(rvPos + 3);
				}
				else
				{
					// No rv, take the Gecko version
					extractVersion(enginePos + ENGINE_GECKO.length() + 1);
				}
			}

			return true;
		}

		// Then continue by the most common one (currently...).
		enginePos = m_userAgentString.indexOf(ENGINE_MSIE);
		if (enginePos >= 0)
		{
			m_browserEngine = ENGINE_MSIE;
			m_browserType = TYPE_MSIE;

			// Common case: Mozilla/4.0 (compatible; MSIE 4.01; Windows 98; MyIE2)
			extractVersion(enginePos + ENGINE_MSIE.length() + 1);

			// AOL is (was?) using the IE engine
			// Mozilla/4.0 (compatible; MSIE 5.0; AOL 5.0; Windows 95; DigExt; DT)
			if (m_userAgentString.indexOf(TYPE_AOL) >= 0)
			{
				m_browserType = TYPE_AOL;
			}

			return true;
		}

		// Check if it is this good old pre-Gecko Netscape Navigator...
		enginePos = m_userAgentString.indexOf(ENGINE_NN);
		if (enginePos >= 0)
		{
			m_browserEngine = ENGINE_NN;
			m_browserType = TYPE_NN;

			// Common case(?): Mozilla/4.79 [en] (Netscape 4.79; Windows NT 5.1; U)
			extractVersion(enginePos + ENGINE_NN.length() + 1);

			if (m_versionMajor > 4)
			{
				// Oh, well, non standard, but probably Gecko powered Netscape...
				m_browserEngine = ENGINE_GECKO;
				m_browserType = TYPE_NETSCAPE;
			}

			return true;
		}

		// If we reached this point, the UA string is probably not very parsable/standard...
		// We will try to guess if the string is a browser/robot name or just junk...

		// If name followed by a slash and number,
		// it may be the name and version.
		if (extractNameVersion("^([A-Za-z][A-Za-z_ -]+)/(\\d+)\\.?(\\d*).*"))
		{
			return true;
		}
		// If name starts with upper case and followed by an optional
		// space and a number, it may be the name and version.
		if (extractNameVersion("^([A-Z][A-Za-z_ -]+)(\\d+)\\.?(\\d*).*"))
		{
			return true;
		}
		// If name starts with upper case, take it in account
		char fc = m_userAgentString.charAt(0);
		if (fc >= 'A' && fc <= 'Z')
		{
			// Take first part of UA string, up to '(' or ';'
			String[] bt = m_userAgentString.split("[(;]");
			m_browserType = bt[0].trim();

			return true;
		}

		// Most of the remaining junk starts with a lowercase letter
		// or any other char.
		m_browserType = TYPE_JUNK;
		return false;
	}

	/**
	 * Try to find the operating system.
	 */
	private void findOS()
	{
		// I saw:
		// Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7) Gecko/20040520 K-Meleon/0.8
		// So I look up the last string
		int osPos = m_userAgentString.lastIndexOf(OS_WIN);
		if (osPos >= 0)
		{
			m_os = OS_WINDOWS;
			m_osFullName = getName(m_userAgentString, osPos, true);
		}
		else
		{
			osPos = m_userAgentString.indexOf(OS_MAC);
			if (osPos >= 0)
			{
				m_os = OS_MACINTOSH;
				m_osFullName = getFromList(m_userAgentString, OSL_MACS, true);
				if (m_osFullName == null)
				{
					m_osFullName = OS_UNX;
				}
			}
			else
			{
				m_osFullName = getFromList(m_userAgentString, OSL_UNICES, true);
				if (m_osFullName != null)
				{
					m_os = OS_UNIX;
				}
				else
				{
					m_osFullName = getFromList(m_userAgentString, OSL_OTHERS, true);
					if (m_osFullName != null)
					{
						m_os = OS_OTHER;
					}
					else
					{
						m_os = UNKNOWN;
						m_osFullName = UNKNOWN;
					}
				}
			}
		}
	}

	/*--- Various utility methods ---*/

	private String getName(String full, int startPos, boolean bAllowSpaces)
	{
		String re;
		if (bAllowSpaces)
		{
			re = "[;()]";
		}
		else
		{
			re = "[;()/ ]";
		}
		String[] aNames = full.substring(startPos).split(re);
		return aNames[0].trim();
	}

	private String getFromList(String full, String[] list, boolean bAllowSpaces)
	{
		int pos;
		for (int i = 0; i < list.length; i++)
		{
			pos = full.indexOf(list[i]);
			if (pos >= 0)
			{
				return getName(full, pos, bAllowSpaces);
			}
		}
		return null;
	}

	private void extractVersion(int versionStartPos)
	{
		if (versionStartPos >= m_userAgentString.length())
		{
			// End of string, no version number (defaults to 0)
			return;
		}
		String[] aVers = m_userAgentString.substring(versionStartPos).split("\\D");
		try
		{
			m_versionMajor = Integer.parseInt(aVers[0]);
		}
		catch (Exception ex)
		{
			m_versionMajor = 0;
		}
		if (aVers.length > 1)
		{
			try
			{
				m_versionMinor = Integer.parseInt(aVers[1]);
			}
			catch (Exception ex)
			{
				m_versionMinor = 0;
			}
		}
	}

	private void extractTypeVers(int browserPos)
	{
		int versPos = m_userAgentString.indexOf("/", browserPos);
		if (versPos == -1)
		{
			// Whole string
			m_browserType = m_userAgentString.substring(browserPos);
		}
		else
		{
			// Up to slash
			m_browserType = m_userAgentString.substring(browserPos, versPos);
			extractVersion(versPos + 1);
		}
	}

	/**
	 * Takes a string pattern with two or three groups,
	 * and if it matches, dispatches the groups in
	 * the browser type and the version numbers.
	 *
	 * The second and optional third groups must be purely numerical! (0-9)
	 */
	private boolean extractNameVersion(String patternStr)
	{
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(m_userAgentString);
		boolean bFound = matcher.find();
		if (bFound)
		{
			m_browserType = matcher.group(1).trim();
			m_versionMajor = Integer.parseInt(matcher.group(2));
			if (matcher.groupCount() == 3)
			{
				try	// Might be empty
				{
					m_versionMinor = Integer.parseInt(matcher.group(3));
				}
				catch (Exception ex)
				{
					m_versionMinor = 0;
				}
			}
		}
		return bFound;
	}

	/**
	 * @return browserEngine (normalized to ENGINE_*).
	 */
	public String getBrowserEngine()
	{
		return m_browserEngine;
	}
	/**
	 * @return browserType (can be TYPE_* or free).
	 */
	public String getBrowserType()
	{
		return m_browserType;
	}
	/**
	 * @return os name (normalized to OS_*).
	 */
	public String getOS()
	{
		return m_os;
	}
	/**
	 * @return osFullName (free).
	 */
	public String getOSFullName()
	{
		return m_osFullName;
	}
	/**
	 * @return versionMajor.
	 */
	public int getVersionMajor()
	{
		return m_versionMajor;
	}
	/**
	 * @return versionMinor.
	 */
	public int getVersionMinor()
	{
		return m_versionMinor;
	}
}
