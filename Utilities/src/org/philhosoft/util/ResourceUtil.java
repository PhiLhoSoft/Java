/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  0.01.000 -- 2012/10/21 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.util;

import java.io.File;

public class ResourceUtil
{
	/**
	 * Gives the class path of the given object, ie. for an instance of
	 * org.philhosoft.foo.bar.Baz, gives "org/philhosoft/foo/bar/".
	 * (Slashes as file separator work well in most systems, including Windows.)
	 *
	 * @param obj  the object for which we want the path of the .class file
	 * @return the path, with a final slash
	 */
	public static String getClassPath(Object obj)
	{
		String cp = getPackage(obj).replaceAll("\\.", "/");
		return cp;
	}

	/**
	 * Gives the package of the given object, ie. for an instance of
	 * org.philhosoft.foo.bar.Baz, gives "org.philhosoft.foo.bar.".
	 *
	 * @param obj  the object providing the package part
	 * @return the package, with a final dot
	 */
	public static String getPackage(Object obj)
	{
		String pkg = obj.getClass().getCanonicalName();
		int pos = pkg.lastIndexOf("."); // Slash before the class name
		if (pos == -1)
			return ""; // No package
		return pkg.substring(0, pos + 1); // Keep the ending dot
	}

	/**
	 * Gives the path to the binary folder, ie. the root folder where the classes are,
	 * or the folder where the jar is.
	 * By adding getClassPath to it, we get the path to the class file.
	 *
	 * @param obj  the object providing the package part
	 * @return the path, with a final slash
	 */
	public static String getBinaryPath()
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		File classpathRoot = new File(classLoader.getResource("").getPath());
		return classpathRoot.getPath() + "/";
	}
}
