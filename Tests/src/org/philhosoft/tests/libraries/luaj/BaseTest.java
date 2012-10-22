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

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;


/**
 * Base test of Luaj.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2012-10-17
 */
public final class BaseTest
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Run a script in a Java Application

		String scriptPath = "org/philhosoft/tests/libraries/luaj/hello.lua";
		LuaValue script = LuaValue.valueOf(scriptPath);

		// Create an environment to run in
		Globals _G = JsePlatform.standardGlobals();

		// Use the convenience function on the globals to load a chunk.
		LuaValue chunk = _G.loadFile(scriptPath);

		// Use any of the "call()" or "invoke()" functions directly on the chunk.
		chunk.call();

		// Alternative
		_G.get("dofile").call(script);


		// Run a script using JSR-223 Dynamic Scripting

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine se = mgr.getEngineByExtension(".lua");
		se.put("x", 25);
		try
		{
			se.eval("y = math.sqrt(x)");
		}
		catch (ScriptException e)
		{
			e.printStackTrace();
		}
		System.out.println("y=" + se.get("y"));
	}
}
