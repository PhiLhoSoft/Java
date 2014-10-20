/*
 * Tests: A collection of little test programs to explore the Java language.
 * Here, tests of the Luaj library.
 * Based on examples from http://luaj.org/luaj/README.html and other examples coming wih the library.
 */
/* File history:
 *  1.02.000 -- 2012-10-25 (PL) -- Create LuajHelper
 *  1.01.000 -- 2012-10-24 (PL) -- Make the base tests more modular
 *  1.00.000 -- 2012-10-17 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
 */
package org.philhosoft.experiments.libraries.luaj;

import java.io.FileReader;
import java.io.Reader;

import javax.script.*;

import org.luaj.vm2.*;


/**
 * Base test of Luaj.
 *
 * @author Philippe Lhoste
 * @version 1.02.000
 * @date 2012-10-25
 */
public final class BaseTest
{
	private static final boolean SHOW_ERRORS = false;
	private static final boolean SHOW_SWING = false;

	private LuajHelper m_luaj;
	private ScriptEngine m_scriptEngine;

	public BaseTest()
	{
		m_luaj = new LuajHelper(this);
		m_scriptEngine = m_luaj.getScriptEngine();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		BaseTest bt = new BaseTest();

		bt.m_luaj.showInformation();
		bt.simpleRunScript();
		if (SHOW_ERRORS)
		{
			bt.runScriptWithErrors();
		}
		bt.runJSRScript();
		if (SHOW_SWING)
		{
			bt.runSwing();
		}
	}

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

	/**
	 * Runs a script in a Java Application.
	 */
	private void simpleRunScript()
	{
		System.out.println("# Running a simple script");

		String classPath = m_luaj.getClassPath();
		String scriptPath = classPath + "hello.lua";
		System.out.println("-> " + scriptPath);

		Globals _G = LuajHelper.getGlobals();
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
	}

	/**
	 * Runs a script using JSR-223 Dynamic Scripting.
	 * Voluntarily throws some kinds of errors, to show how they are handled.
	 */
	private void runScriptWithErrors()
	{
		System.out.println("# Testing script snippet running and runtime error handing");

		m_scriptEngine.put("x", 25);
		try
		{
			m_scriptEngine.eval("y1 = math.sqrt(x)");
			m_scriptEngine.eval("y2 = math.sqrt(-x)");
			m_scriptEngine.eval("y3 = x / 0");
			m_scriptEngine.eval("y4 = xx * x");
		}
		catch (ScriptException e) // Mandatory...
		{
			System.err.println("Incorrect Lua script: " + e.getMessage());
//			e.printStackTrace();
		}
		catch (LuaError e) // Should have been LuaErrorException or similar, this name is a bit misleading... (again conventions)
		{
			System.err.println("Error when running Lua script: " + e.getMessage());
//			e.printStackTrace();
		}
		System.out.println("y1 = " + m_scriptEngine.get("y1"));
		System.out.println("y2 = " + m_scriptEngine.get("y2"));
		System.out.println("y3 = " + m_scriptEngine.get("y3"));
		System.out.println("y4 = " + m_scriptEngine.get("y4"));

		System.out.println("# Testing script syntax error handing");
		try
		{
			// Let's make an error in the script itself!
			m_scriptEngine.eval("y == math.sqrt(x)");
		}
		catch (ScriptException e)
		{
			System.err.println("Error in script run: " + e.getMessage());
//			e.printStackTrace();
		}
	}

	/**
	 * Runs a Lua script file with the script engine.
	 */
	private void runJSRScript()
	{
		System.out.println("# Run a Lua script file with the script engine");

		String scriptPath = m_luaj.getScriptPath() +  "SimpleTest.lua";
		System.out.println("-> " + scriptPath);

		FileReader reader = null;
		try
		{
			reader = new FileReader(scriptPath);

			// We can compile a script to be able to call it repeatedly without re-parsing it each time
	        CompiledScript script = ((Compilable) m_scriptEngine).compile(reader);

	        // The script engine has a kind of global state.
	        // We can create bindings to manage local states, an environment independent of the others
            Bindings sb = new SimpleBindings();
            sb.put("foo", 42);
			Object result = script.eval(sb);
			System.out.println(result);

			// Create new bindings, distinct environments
	        Bindings b1 = m_scriptEngine.createBindings();
	        Bindings b2 = m_scriptEngine.createBindings();

	        b1.put("javaParam", "This is a Java program");
	        b2.put("javaParam", "This Java code calls a Lua script");
	        Varargs v1 = (Varargs) script.eval(b1);
	        Varargs v2 = (Varargs) script.eval(b2);
	        System.out.println("First: " + v1);
	        System.out.println("Second: " + v2);

			LuaFunction luaFun = (LuaFunction) b1.get("calledByJava");
			System.out.println(">> " + luaFun);
			for ( Object o : b1.keySet() )
			{
				System.out.println(o + ": " + sb.get(o));
			}
			System.out.println(luaFun.call("From Java to Lua and back"));
		}
		catch (Exception e)
		{
			LuajHelper.handleException(e);
		}
		finally
		{
			LuajHelper.closeReader(reader);
			reader = null;
		}
	}

	/**
	 * Launches a Swing script with the script engine.
	 */
	private final void runSwing()
	{
		System.out.println("# Running Swing script");

		String scriptPath = m_luaj.getScriptPath();
		String scriptFile = scriptPath + "SwingTest.lua";
		System.out.println("-> " + scriptFile);
		Reader reader = null;
		try
		{
			// Add the path to the script to the environment, so it is usable from the script to load resources
			// Literal string to behave correctly with Windows paths...
			m_scriptEngine.eval("PATH = [[" + scriptPath + "]]");
			reader = new FileReader(scriptFile);
			m_scriptEngine.eval(reader);
		}
		catch (Exception e)
		{
			LuajHelper.handleException(e);
		}
		finally
		{
			LuajHelper.closeReader(reader);
		}
	}
}
