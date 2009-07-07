/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2005/12/31 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.ui;

import java.io.*;

import java.util.Properties;
//import java.util.prefs.Preferences;

import java.awt.*;
//import java.awt.event.*;

import org.philhosoft.string.ParseString;


/**
 * Track a frame's size, position and state (maximized).
 * Allow to store and retrieve these values to/from properties files
 * and to set these settings when recreating the frame (on startup).
 */
public class FrameState
{
	private static String m_propertiesFilename = System.getProperty("user.name") + ".properties";
	/// Dimension & position of the window when not maximized
	private Rectangle m_bounds;
	/// Name of the frame
	private String m_frameName;
	/// True if system can do a real maximize
	private boolean m_bCanMaximize;
	/// True if window was maximized or must be maximized
	/// (real maximizing or approximated by getting near screen size).
	private boolean m_bMaximized;

	private final static String PROP_WIDTH  = ".width";
	private final static String PROP_HEIGHT = ".height";
	private final static String PROP_XPOS   = ".xPos";
	private final static String PROP_YPOS   = ".yPos";
	private final static String PROP_MAX    = ".isMaximized";

	/**
	 * Reads the user properties file
	 * and tries to get his prefered window size/position/state.
	 */
	public FrameState(String frameName)
	{
		m_frameName = frameName;
		loadState();
	}

	/**
	 * If no name is given, defaults to "window".
	 */
	public FrameState()
	{
		m_frameName = "window";
		loadState();
	}

	private void loadState()
	{
		int x, y, w, h;
		boolean bMax;

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		m_bCanMaximize = toolkit.isFrameStateSupported(Frame.MAXIMIZED_BOTH);

		// Cannot read the properties, take default values
		Dimension screenSize = toolkit.getScreenSize();
		// Default size is two third of screen, centered
		w = screenSize.width * 2 / 3;
		h = screenSize.height * 2 / 3;
		x = (screenSize.width - w) / 2;
		y = (screenSize.height - h) / 2;
		bMax = false;

		// Read properties file
		Properties properties = new Properties();
		String prop;
		try
		{
			properties.load(new FileInputStream(m_propertiesFilename));
			prop = properties.getProperty(m_frameName + PROP_WIDTH);
			w = ParseString.toInt(prop, w);
			prop = properties.getProperty(m_frameName + PROP_HEIGHT);
			h = ParseString.toInt(prop, h);
			prop = properties.getProperty(m_frameName + PROP_XPOS);
			x = ParseString.toInt(prop, x);
			prop = properties.getProperty(m_frameName + PROP_YPOS);
			y = ParseString.toInt(prop, y);
			prop = properties.getProperty(m_frameName + PROP_MAX);
			bMax = ParseString.toBoolean(prop, bMax);
		}
		catch (IOException e)
		{
			// Do nothing, use default values
		}
		m_bounds = new Rectangle(x, y, w, h);
		m_bMaximized = bMax;
	}

	/**
	 * Store bounds (size & position) and maximized state if relevant.
	 */
	public void store(Frame window)
	{
		Properties properties = new Properties();

		properties.setProperty(m_frameName + PROP_WIDTH, Integer.toString(m_bounds.width));
		properties.setProperty(m_frameName + PROP_HEIGHT, Integer.toString(m_bounds.height));
		properties.setProperty(m_frameName + PROP_XPOS, Integer.toString(m_bounds.x));
		properties.setProperty(m_frameName + PROP_YPOS, Integer.toString(m_bounds.y));
		if (m_bCanMaximize)
		{
			properties.setProperty(m_frameName + PROP_MAX,
					m_bMaximized ? "true" : "false");
		}
		// Write properties file
		try
		{
			properties.store(new FileOutputStream(m_propertiesFilename), null);
		}
		catch (IOException e)
		{
			System.err.println("Error while writing properties to '" +
					m_propertiesFilename +
					"': " + e.getMessage());
		}
	}

	/**
	 * Get current bounds (size & position) and maximized state if relevant.
	 */
	public void get(Frame window)
	{
		if (window.getExtendedState() == Frame.MAXIMIZED_BOTH)
		{
			// Don't memorize maximized position / size
			m_bMaximized = true;
		}
		else
		{
			m_bounds = window.getBounds();
			m_bMaximized = false;
		}
	}

	/**
	 * Set bounds (size & position) and maximized state if relevant.
	 */
	public void set(Frame window)
	{
		window.setBounds(m_bounds);
		if (m_bCanMaximize && m_bMaximized)
		{
			window.setExtendedState(Frame.MAXIMIZED_BOTH);
		}
	}

	/*----- Accessors -----*/

	public boolean isMaximized()
	{
		return m_bMaximized;
	}

	public Rectangle getBounds()
	{
		return m_bounds;
	}

	/*----- Nested class allowing storing/loading settings to a properties file -----*/



}
