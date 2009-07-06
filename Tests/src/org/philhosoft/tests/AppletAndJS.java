/*
 * Tests: A collection of little test programs to explore Java language.
 * Testing communication between Java and JavaScript.
 * Compile with: javac -cp C:\Java\jdk1.6.0_13\jre\lib\plugin.jar AppletAndJS.java
 *
 * Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 *
 * Copyright notice: See the PhiLhoSoftLicence.txt file for details.
 * This file is distributed under the zlib/libpng license.
 * Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
 */
/* File history:
 *  1.01.000 -- 2009/07/06 (PL) -- Some more tests.
 *  1.00.000 -- 2008/11/17 (PL) -- Creation
 */
//~ package org.philhosoft.tests;


import java.applet.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JLabel;

import netscape.javascript.*;


/**
 * Template class for test applications.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2008/11/17
 */
@SuppressWarnings("serial")
public class AppletAndJS extends Applet implements ActionListener
{
	Button exec, cb;
	TextField command, param;
	String callbackFunction;
	JLabel status;

	public void init()
	{
		command = new TextField(20);
		add(new Label("Command:"));
		add(command);
		add(new Label("Param:"));
		param = new TextField(50);
		add(param);
		exec = new Button("Execute JavaScript");
		add(exec);
		exec.addActionListener(this);
		cb = new Button("Execute Callback");
		add(cb);
		cb.addActionListener(this);
		status = new JLabel(" ");
		add(status);
	}

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() == exec)
		{
			JSObject win = (JSObject) JSObject.getWindow(this);
			String[] arguments = { param.getText() };
			win.call(command.getText(), arguments);
//			win.eval(command.getText());
		}
		else if (ae.getSource() == cb)
		{
			status.setText("Callback");
			param.setText("CB");
			if (callbackFunction == null) return;
			command.setText(callbackFunction);
			JSObject win = (JSObject) JSObject.getWindow(this);
			win.call(callbackFunction, null);
		}
	}

	public void RegisterCallback(String functionName)
	{
		callbackFunction = functionName;
	}

	public int Do()
	{
		JSObject win = (JSObject) JSObject.getWindow(this);
		JSObject doc = (JSObject) win.getMember("document");
		JSObject fun = (JSObject) win.getMember("DumberTest");
		JSObject loc = (JSObject) doc.getMember("location");
		String href = (String) loc.getMember("href");
		String[] arguments = { href, fun.toString() };
		win.call("DumbTest", arguments);
		return fun.toString().length();
	}

	public void Call(String message)
	{
		JSObject win = (JSObject) JSObject.getWindow(this);
		String[] arguments = { "Call JS function DumbTest with String", message };
		win.call("DumbTest", arguments);
	}

	public void Call(JSObject jso)
	{
		JSObject win = (JSObject) JSObject.getWindow(this);
		String[] arguments = { "Call JS function DumbTest with JSObject", jso.toString() };
		win.call("DumbTest", arguments);
	}
}
