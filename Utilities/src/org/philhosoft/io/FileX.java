/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2005/12/07 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.io;

import java.io.*;

/**
 * Extended file handling class.
 *
 * Tries to provide some utility functions missing in standard File class.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/07
 */
public class FileX
{
	/**
	 * Return the extension portion of the file's name.
	 */
	public static String getFileExtension(File f)
	{
		if (f != null)
		{
			String filename = f.getName();
			int pos = filename.lastIndexOf('.');
			if (pos > 0 && pos < filename.length() - 1)
			{
				return filename.substring(pos + 1);
			}
			// Starts or ends with dot: no extension
		}
		// No dot
		return null;
	}

}
