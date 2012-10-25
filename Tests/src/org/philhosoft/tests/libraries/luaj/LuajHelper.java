/*
 * Tests: A collection of little test programs to explore the Java language.
 * Here, tests of the Luaj library.
 */
/* File history:
 *  1.00.000 -- 2012-10-25 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
 */
package org.philhosoft.tests.libraries.luaj;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.lib.jse.JsePlatform;

import org.philhosoft.util.ResourceUtil;


/**
 * Helper to use Luaj and Lua scripts.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2012-10-25
 */
public final class LuajHelper
{
	// Create an environment to run in
	private static final Globals _G = JsePlatform.standardGlobals();

	private ScriptEngine m_scriptEngine;
	/** Relative path to this class. */
	private String m_classPath;
	/** Absolute path to the script files, supposed to be in the same package than the class. */
	private String m_scriptPath;

	public LuajHelper(Object parentClass)
	{
		ScriptEngineManager mgr = new ScriptEngineManager();
		m_scriptEngine = mgr.getEngineByExtension(".lua");
		m_classPath = ResourceUtil.getClassPath(parentClass);
		m_scriptPath = ResourceUtil.getBinaryPath() + m_classPath;
	}

	public static Globals getGlobals() { return _G; }
	public ScriptEngine getScriptEngine() { return m_scriptEngine; }
	public String getClassPath() { return m_classPath; }
	public String getScriptPath() { return m_scriptPath; }

	public void showInformation()
	{
		System.out.println("# Generic information on the script engine");

		ScriptEngineFactory factory = m_scriptEngine.getFactory();

		System.out.println("Engine Name: " + factory.getEngineName());
		System.out.println("Engine Version: " + factory.getEngineVersion());
		System.out.println("Names: " + factory.getNames());
		System.out.println("Language Name: " + factory.getLanguageName());
		System.out.println("Language Version: " + factory.getLanguageVersion());
		System.out.println("Extensions: " + factory.getExtensions());
		System.out.println("Mime Types: " + factory.getMimeTypes());

		String statement = factory.getOutputStatement("'Hello World!'");
		System.out.println(statement);
		try
		{
			m_scriptEngine.eval(statement);
		}
		catch (ScriptException e)
		{
			e.printStackTrace();
		}

		String call = factory.getMethodCallSyntax("someString", "rep", "5");
		System.out.println(call);
	}

	public static final void handleException(Exception ex)
	{
		if (ex instanceof IOException)
		{
			System.err.println("Cannot read script file: " + ex.getMessage());
		}
		else if (ex instanceof FileNotFoundException)
		{
			System.err.println("Cannot find script at " + ex.getMessage());
		}
		else if (ex instanceof ScriptException)
		{
			System.err.println("Syntax error in script: " + ex.getMessage());
		}
		else if (ex instanceof LuaError)
		{
			System.err.println("Error when running Lua script: " + ex.getMessage());
		}
		else
		{
			System.err.println("Unexpectd exception: " + ex);
		}
		// Show where the error happened
		ex.printStackTrace();
	}

	public static final void closeReader(Reader reader)
	{
		if (reader != null)
		{
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				System.err.println("Problem when closing reader: " + e.getMessage());
			}
		}
	}
}
