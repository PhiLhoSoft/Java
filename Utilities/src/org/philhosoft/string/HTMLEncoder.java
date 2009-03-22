/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 *
 * Copyright notice: See the PhiLhoSoftLicence.txt file for details.
 * This file is distributed under the zlib/libpng license.
 * Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
 */
/* File history:
 *  1.00.000 -- 2005/05/11 (PL) -- Creation
 */
package org.philhosoft.string;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Encode a string to HTML.
 *
 * Encode '<', '>' and '&' to entities, end-of-lines to <br>
 * and control characters to dot.
 * Assumes most accented characters are supported in the page encoding,
 * so doesn't transform them to entities.
 *
 * TODO: get an encoding, and if find a char outside this encoding,
 * transform it to Unicode entity.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/05/11
 */
public class HTMLEncoder
{
	public static String encode(String raw, String encoding)
			throws UnsupportedEncodingException
	{
		if (!Charset.isSupported(encoding))
		{
			throw new UnsupportedEncodingException();
		}
		return encode(raw);	// Charset not implemented yet...
	}

	public static String encode(String raw)
	{
		if (raw == null)
		{
			return null;
		}
		int length = raw.length();
		if (length == 0)
		{
			return raw;
		}
		// Try to guess how bigger the new string will be...
		// Will grow if needed, but this is costly
		StringBuffer encoded = new StringBuffer((int)(length * 1.25));
		char rc, nrc;
		String ec;
		for (int p = 0; p < length; p++)
		{
			rc = raw.charAt(p);
			switch (rc)
			{
			case '<':
				ec = "&lt;";
				break;
			case '>':
				ec = "&gt;";
				break;
			case '&':
				ec = "&amp;";
				break;
			/* I won't do case '"': as it is, actually, not necessary */
			case '\r':
				if (p < length - 1)
				{
					nrc = raw.charAt(p - 1);
					if (nrc == '\n')
					{
						// Windows end-of-line
						p++;
					}
				}
				// Fall through (this is useful, sometime...)
			case '\n':
				ec = "<br />";	// XHTML!
				break;
			default:
				if (rc < ' ' && rc != '\t' && rc != '\f' /*&& rc != '\n' && rc != '\r'*/)
				{
					// Control character
					rc = '.';
				}
				ec = "" + rc;	// Transform to string
			}
			encoded.append(ec);
		}
		// I should also encode characters outside the given character set
		// but I will do it later...
		return encoded.toString();
	}
}
