/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2008/10/31 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests;

import java.util.Map;

import javax.swing.*;

/**
 * Template class for test applications.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/08
 */
public class ShowInfo
{
	public static String m_prefix = "  ";

	public ShowInfo()
	{
		DoFooBar();
		ShowVersion();
		ShowJavaInfo();
		ShowOSInfo();
		ShowUserInfo();
		ShowEnvironment();
	}

	public void PrintProperty(String propName)
	{
		String propValue = System.getProperty(propName);
		if (propValue == null)
			propValue = "<null>";
		else
			propValue = propValue.replace("\n", "\\n").replace("\r", "\\r");
		System.out.println(m_prefix + propName + " = " + propValue);
	}

	private void DoFooBar()
	{
		System.out.println("ShowInfo!");
	}

	private void ShowVersion()
	{
		String className = this.getClass().getName();
		String classJar = this.getClass().getResource("/" + className.replace('.', '/') +
				".class").toString();
		System.out.println("Class name: " + className);
		System.out.println("Class path: " + classJar);
		if (classJar.startsWith("jar:"))
		{
			Package p = this.getClass().getPackage();
			System.out.println("Specification version: " + p.getSpecificationVersion());
			System.out.println("Implementation version: " + p.getImplementationVersion());
		}
	}

	private void ShowUserInfo()
	{
		System.out.println("\nUser info:");
		PrintProperty("user.name");
		PrintProperty("user.timezone");
		PrintProperty("user.country");
		PrintProperty("user.language");
		PrintProperty("user.home");
		PrintProperty("user.dir");
		PrintProperty("user.variant");
	}

	private void ShowJavaInfo()
	{
		System.out.println("\nJava info:");
		PrintProperty("java.class.version");
		PrintProperty("java.version");
		PrintProperty("java.home");
		PrintProperty("java.vendor");
		PrintProperty("java.vendor.url");

		System.out.println("\nJava spec.:");
		PrintProperty("java.specification.name");
		PrintProperty("java.specification.vendor");
		PrintProperty("java.specification.version");

		System.out.println("\nJava VM spec.:");
		PrintProperty("java.vm.specification.name");
		PrintProperty("java.vm.specification.vendor");
		PrintProperty("java.vm.specification.version");

		System.out.println("\nJava VM:");
		PrintProperty("java.vm.name");
		PrintProperty("java.vm.vendor");
		PrintProperty("java.vm.version");
		PrintProperty("java.vm.info");

		System.out.println("\nJava runtime:");
		PrintProperty("java.runtime.name");
		PrintProperty("java.runtime.version");

		System.out.println("\nPath: " + System.getProperty("java.library.path"));
		System.out.println("\nClasspath: " + System.getProperty("java.class.path"));
		System.out.println("\nExtDirs: " + System.getProperty("java.ext.dirs"));
		System.out.println("\nBoot classpath: " + System.getProperty("sun.boot.class.path"));
		System.out.println("\nBoot library path: " + System.getProperty("sun.boot.library.path"));
	}

	private void ShowOSInfo()
	{
		System.out.println("\nOS info:");
		PrintProperty("file.encoding");
		PrintProperty("file.separator");
		PrintProperty("line.separator");
		PrintProperty("path.separator");
		PrintProperty("os.arch");
		PrintProperty("os.name");
		PrintProperty("os.version");
		PrintProperty("sun.os.patch.level");
		PrintProperty("java.io.tmpdir");
	}

	private void ShowEnvironment()
	{
		Map<String, String> env = System.getenv();
		System.out.println("\nEnvironment:");
		for (Map.Entry<String, String> e : env.entrySet())
		{
			System.out.println("  " + e.getKey() + ": " + e.getValue());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new ShowInfo();
				System.exit(0);
			}
		});
	}
}
