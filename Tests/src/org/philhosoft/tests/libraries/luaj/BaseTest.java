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

import java.util.Arrays;

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
		showInformation();

		// Examples from http://luaj.org/luaj/README.html


		// Run a script in a Java Application

		String scriptPath = ResourceUtil.getClassPath(bt) + "hello.lua";

		// Use the convenience function on the globals to load a chunk.
		LuaValue chunk = _G.loadFile(scriptPath);

		// Use any of the "call()" or "invoke()" functions directly on the chunk.
		chunk.call();

		// Simpler alternative...
		LuaValue script = LuaValue.valueOf(scriptPath);
		_G.get("dofile").call(script);


		// Run a script using JSR-223 Dynamic Scripting

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
			e.printStackTrace();
		}
		catch (LuaError e)
		{
			System.out.println("Error when running Lua script:");
			e.printStackTrace();
		}
		System.out.println("y1 = " + se.get("y1"));
		System.out.println("y2 = " + se.get("y2"));
		System.out.println("y3 = " + se.get("y3"));
		System.out.println("y4 = " + se.get("y4"));
	}
}
