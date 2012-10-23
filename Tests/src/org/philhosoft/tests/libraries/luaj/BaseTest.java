/*
 * Tests: A collection of little test programs to explore the Java language.
 * Here, tests of the Luaj library.
 */
/* File history:
 *  1.00.000 -- 2012-10-17 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.libraries.luaj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

import org.philhosoft.util.ResourceUtil;


/**
 * Base test of Luaj.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2012-10-17
 */
public final class BaseTest
{
	// Create an environment to run in
	private static Globals _G = JsePlatform.standardGlobals();

	public static ScriptEngine getLuaScriptEngine()
	{
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine se = mgr.getEngineByExtension(".lua");
		return se;
	}

	public static void showInformation()
	{
		ScriptEngine engine = getLuaScriptEngine();
        ScriptEngineFactory factory = engine.getFactory();

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
			engine.eval(statement);
		}
		catch (ScriptException e)
		{
			e.printStackTrace();
		}

        String call = factory.getMethodCallSyntax("someString", "rep", "5");
        System.out.println(call);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		BaseTest bt = new BaseTest();
		System.out.println("# Generic information on the script engine");
		showInformation();

		// Examples from http://luaj.org/luaj/README.html


		// Run a script in a Java Application

		System.out.println("# Running a simple script");
		String classPath = ResourceUtil.getClassPath(bt);
		String scriptPath = classPath + "hello.lua";
		System.out.println("-> " + scriptPath);

		// Use the convenience function on the globals to load a chunk.
		LuaValue chunk = _G.loadFile(scriptPath);

		// Use any of the "call()" or "invoke()" functions directly on the chunk.
		chunk.call();

		// Simpler alternative...
		scriptPath = classPath + "info.lua";
		System.out.println("-> " + scriptPath);
		LuaValue script = LuaValue.valueOf(scriptPath);
		Varargs va = _G.get("dofile").invoke(script);
		for (int i = 1, n = va.narg(); i <= n; i++)
		{
			if (va.isstring(i))
			{
				String s = va.tojstring(i);
				System.out.println(i + ": " + s);
			}
			else
			{
				System.out.println(i + "= " + va.arg(i) + " (" + va.type(i) + ")");
			}
		}
		System.out.println(va.tojstring());

//		_G.get("dofile").call(LuaValue.valueOf(classPath + "SwingTestToo.lua"));
//		System.out.println("Done");


		// Run a script using JSR-223 Dynamic Scripting

		System.out.println("# Testing script snippet running and runtime error handing");
		ScriptEngine se = getLuaScriptEngine();
		se.put("x", 25);
		try
		{
			se.eval("y1 = math.sqrt(x)");
			se.eval("y2 = math.sqrt(-x)");
			se.eval("y3 = x / 0");
			se.eval("y4 = xx * x");
		}
		catch (ScriptException e)
		{
			System.err.println("Incorrect Lua script: " + e.getMessage());
//			e.printStackTrace();
		}
		catch (LuaError e)
		{
			System.err.println("Error when running Lua script: " + e.getMessage());
//			e.printStackTrace();
		}
		System.out.println("y1 = " + se.get("y1"));
		System.out.println("y2 = " + se.get("y2"));
		System.out.println("y3 = " + se.get("y3"));
		System.out.println("y4 = " + se.get("y4"));

		System.out.println("# Testing script syntax error handing");
		try
		{
			// Let's make an error in the script itself!
			se.eval("y == math.sqrt(x)");
		}
		catch (ScriptException e)
		{
			System.err.println("Error in script run: " + e.getMessage());
//			e.printStackTrace();
		}


		// Launch a Swing script with the script engine

		System.out.println("# Running Swing script");
		String scriptFilePath = ResourceUtil.getBinaryPath() + classPath;
		String scriptFile = scriptFilePath + "SwingTest.lua";
		System.out.println("-> " + scriptFile);
		try
		{
			// Add the path to the script to the environment, so it is usable from the script to load resources
			// Literal string to behave correctly with Windows paths...
			se.eval("PATH = [[" + scriptFilePath + "]]");
		    Reader reader = new FileReader(new File(scriptFile));
		    Object result = se.eval(reader);
		    System.out.println(result);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Cannot find script at " + scriptFilePath);
		}
		catch (ScriptException e)
		{
			e.printStackTrace();
		}
		catch (LuaError e)
		{
			System.err.println("Error when running Lua script: " + e.getMessage());
//			e.printStackTrace();
		}
	}
}
