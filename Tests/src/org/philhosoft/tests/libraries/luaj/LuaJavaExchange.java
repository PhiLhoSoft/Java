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

import java.io.FileReader;

import javax.script.*;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;


/**
 * Test more on the exchanges between Lua and Java.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2012-10-25
 */
public final class LuaJavaExchange
{
	private LuajHelper m_luaj;
	private ScriptEngine m_scriptEngine;

	public LuaJavaExchange()
	{
		m_luaj = new LuajHelper(this);
		m_scriptEngine = m_luaj.getScriptEngine();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		LuaJavaExchange lje = new LuaJavaExchange();

		lje.runScript();
	}

	/**
	 * Runs a Lua script file with the script engine.
	 */
	private void runScript()
	{
		String scriptPath = m_luaj.getScriptPath() +  "SimpleTest.lua";
		System.out.println("-> " + scriptPath);
		// Expose one Java function to the Lua script
		LuaFunction rep = new TwoArgFunction()
		{
			@Override
			public LuaValue call(LuaValue arg1, LuaValue arg2)
			{
				System.out.println("arg1: " + arg1 + ", arg2: " + arg2);
				if (!arg1.isint() || !arg2.isstring())
					return LuaValue.NIL;
				int n = arg1.toint();
				String s = arg2.tojstring();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < n; i++)
				{
					sb.append(s);
				}
				return LuaValue.valueOf(sb.toString());
			}
		};
		// And another one, that can return several values (traditional 'nil and error message' if error)
		LuaFunction fun = new VarArgFunction()
		{
			@Override
			public Varargs invoke(Varargs args)
			{
				System.out.println("args: " + args.tojstring());
				LuaValue arg = args.arg1(); // Ignore the extra arguments!
				if (arg.isint())
					return LuaValue.valueOf(arg.checkint() * 100);
				if (arg.isstring())
					return LuaValue.valueOf(">" + arg.tojstring() + "<");
				Varargs error = LuaValue.varargsOf(new LuaValue[] { LuaValue.NIL, LuaValue.valueOf("Call with an integer or a string") });
				return error;
			}
		};
		m_scriptEngine.put("javaFun", fun);
		FileReader reader = null;
		try
		{
			reader = new FileReader(scriptPath);

			// We can compile a script to be able to call it repeatedly without re-parsing it each time
	        CompiledScript script = ((Compilable) m_scriptEngine).compile(reader);

	        // The script engine has a kind of global state.
	        // We can create bindings to manage local states, an environment independent of the others
            Bindings sb = new SimpleBindings();
            sb.put("rep", rep);
            sb.put("javaFun", fun);
			Object result = script.eval(sb);
			System.out.println(result);

			LuaFunction luaFun = (LuaFunction) sb.get("calledByJava");
			System.out.println(">> " + luaFun);
			for ( Object o : sb.keySet() )
			{
				System.out.println(o + " " + sb.get(o));
			}
			System.out.println(luaFun.call("From Java to Lua and back"));

			System.out.println("# Lua callbacks");
			@SuppressWarnings("unused") // Methods called by reflection...
			class Dog
			{
				public String name;
				Dog(String n) { name = n; }
				public void talk() { System.out.println("Dog " + name + " barks!"); }
				public void walk() { System.out.println("Dog " + name + " walks..."); }
			}
			@SuppressWarnings("unused") // Methods called by reflection!
			class Cat
			{
				String name;
				Cat(String n) { name = n; }
				public void talk() { System.out.println("Cat " + name + " meows!"); }
				public void walk() { System.out.println("Cat " + name + " walks..."); }
			}
			Dog dog = new Dog("Rex");
//			LuaValue luaDog = LuaValue.userdataOf(dog); // No metatable on this one, not usable directly
			LuaValue luaDog = CoerceJavaToLua.coerce(dog);
			luaDog.method("talk");

			Cat cat = new Cat("Felix");
			LuaValue luaCat = CoerceJavaToLua.coerce(cat);

			LuaFunction onTalk = (LuaFunction) sb.get("onTalk");
			LuaValue b = onTalk.call(luaDog);
			System.out.println("onTalk answered: " + b);
			LuaValue[] cats = { luaCat };
			Varargs vb = onTalk.invoke(LuaValue.varargsOf(cats));
			System.out.println("onTalk answered: " + vb);

			LuaFunction onWalk = (LuaFunction) sb.get("onWalk");
			Varargs dist = onWalk.invoke(LuaValue.varargsOf(cats));
			System.out.println("onWalk returned: " + dist);
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
}
