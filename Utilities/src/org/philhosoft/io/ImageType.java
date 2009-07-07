/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2005/12/07 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.io;

import javax.swing.ImageIcon;

/**
 * Image type information class.
 *
 * Store information about a type of image:
 * abbreviation, long name, Mime type and an associated icon.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/07
 */
public class ImageType
{
	private String m_shortName;
	private String m_longName;
	private String m_mimeType;
	private ImageIcon m_icon;


	/**
	 * Constructor.
	 */
	public ImageType()
	{
	}

	/**
	 * Constructor.
	 */
	public ImageType(String shortName, String longName, String mimeType, String iconPath)
	{
		m_shortName = shortName;
		m_longName = longName;
		m_mimeType = mimeType;
		m_icon = createImageIcon(iconPath);
	}


	/**
	 * @return icon.
	 */
	public ImageIcon getIcon()
	{
		return m_icon;
	}

	/**
	 * @param icon - icon to set.
	 */
	public void setIcon(ImageIcon icon)
	{
		m_icon = icon;
	}

	/**
	 * @param iconPath - path of icon to set.
	 */
	public void setIcon(String iconPath)
	{
		m_icon = createImageIcon(iconPath);
	}

	/**
	 * @return longName.
	 */
	public String getLongName()
	{
		return m_longName;
	}

	/**
	 * @param longName - longName to set.
	 */
	public void setLongName(String longName)
	{
		m_longName = longName;
	}

	/**
	 * @return mimeType.
	 */
	public String getMimeType()
	{
		return m_mimeType;
	}

	/**
	 * @param mimeType - mimeType to set.
	 */
	public void setMimeType(String mimeType)
	{
		m_mimeType = mimeType;
	}

	/**
	 * @return shortName.
	 */
	public String getShortName()
	{
		return m_shortName;
	}

	/**
	 * @param shortName - shortName to set.
	 */
	public void setShortName(String shortName)
	{
		m_shortName = shortName;
	}


	/** Returns an ImageIcon, or null if the path was invalid. */
	private static ImageIcon createImageIcon(String path)
	{
		java.net.URL imgURL = ImageType.class.getResource(path);
		if (imgURL != null)
		{
			return new ImageIcon(imgURL);
		}
		else
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

}
