/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2005/12/31 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.ui;

import java.io.*;

import java.util.Properties;
//import java.util.prefs.Preferences;

import java.awt.*;
//import java.awt.event.*;

import org.philhosoft.string.StringConverter;


/**
 * Tracks a frame's size, position and state (maximized).
 * Allow to store and retrieve these values to/from properties files
 * and to set these settings when recreating the frame (on startup).
 */
public class FrameState
{
	private static String s_propertiesPath = System.getProperty("user.name") + ".properties";
	/** Name of the frame. */
	private String m_frameName;
	/** True if system can do a real maximize. */
	private boolean m_bCanMaximize;
	/** Dimension & position of the window when not maximized. */
	private Rectangle m_bounds;
	/** True if window was maximized or must be maximized
	 * (real maximizing or approximated by getting near screen size)...
	 */
	private boolean m_bMaximized;

	private boolean m_bIgnoreChanges;

	private static final String PROP_WIDTH  = ".width";
	private static final String PROP_HEIGHT = ".height";
	private static final String PROP_XPOS   = ".xPos";
	private static final String PROP_YPOS   = ".yPos";
	private static final String PROP_MAX    = ".isMaximized";

	/**
	 * Reads the user properties file
	 * and tries to get his preferred window size/position/state.
	 */
	public FrameState(String frameName)
	{
		assert m_frameName != null;

		m_frameName = frameName;
		init();
	}

	/**
	 * If no name is given, defaults to "window".
	 */
	public FrameState()
	{
		this("window");
	}

	/**
	 * Gets from given window current bounds (size & position) and maximized state if relevant.
	 */
	public void update(Frame window)
	{
		if (m_bIgnoreChanges)
			return;

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
	 * Sets to given window bounds (size & position) and maximized state if relevant.
	 */
	public void apply(Frame window)
	{
		// Don't call update() through listeners
		m_bIgnoreChanges = true;
		window.setBounds(m_bounds);
		if (m_bCanMaximize && m_bMaximized)
		{
			window.setExtendedState(Frame.MAXIMIZED_BOTH);
		}
		// Now, business as usual
		m_bIgnoreChanges = false;
	}

	@Override
	public String toString()
	{
		return m_bounds + " (max? " + m_bMaximized + ")";
	}

	/*----- Accessors -----*/

	public static void setPropertyPath(String path)
	{
		s_propertiesPath = path;
	}

	public static String getPropertyPath()
	{
		return s_propertiesPath;
	}

	public boolean isMaximized()
	{
		return m_bMaximized;
	}

	public Rectangle getBounds()
	{
		return m_bounds;
	}

	/*----- Loading and saving from/to properties -----*/

	/**
	 * Loads from a properties file bounds (size & position) and maximized state if relevant.
	 */
	public boolean loadFromProperties()
	{
		assert s_propertiesPath != null;
		assert m_frameName != null;

		int x = m_bounds.x;
		int y = m_bounds.y;
		int w = m_bounds.width;
		int h = m_bounds.height;
		boolean bMax = m_bMaximized;

		// Read properties file
		boolean bOK = false;
		Properties properties = new Properties();
		try
		{
			properties.load(new FileInputStream(s_propertiesPath));
			String prop = properties.getProperty(m_frameName + PROP_WIDTH);
			w = StringConverter.toInt(prop, w);
			prop = properties.getProperty(m_frameName + PROP_HEIGHT);
			h = StringConverter.toInt(prop, h);
			prop = properties.getProperty(m_frameName + PROP_XPOS);
			x = StringConverter.toInt(prop, x);
			prop = properties.getProperty(m_frameName + PROP_YPOS);
			y = StringConverter.toInt(prop, y);
			prop = properties.getProperty(m_frameName + PROP_MAX);
			bMax = StringConverter.toBoolean(prop, bMax);
			bOK = true;
		}
		catch (IOException e)
		{
			// Do nothing, use default values
		}
		m_bounds = new Rectangle(x, y, w, h);
		m_bMaximized = bMax;

		return bOK;
	}

	/**
	 * Saves to a properties file bounds (size & position) and maximized state if relevant.
	 */
	public void saveToProperties()
			throws IOException
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
		// Write properties file - throws IOException
		properties.store(new FileOutputStream(s_propertiesPath), null);
	}

	/*----- Private area -----*/

	private void init()
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		m_bCanMaximize = toolkit.isFrameStateSupported(Frame.MAXIMIZED_BOTH);

		// Default settings
		m_bMaximized = false;

		int x, y, w, h;

		// Cannot read the properties, take default values
		Dimension screenSize = toolkit.getScreenSize();
		// Default size is two third of screen, centered
		w = screenSize.width * 2 / 3;
		h = screenSize.height * 2 / 3;
		x = (screenSize.width - w) / 2;
		y = (screenSize.height - h) / 2;
		m_bounds = new Rectangle(x, y, w, h);
	}
}
