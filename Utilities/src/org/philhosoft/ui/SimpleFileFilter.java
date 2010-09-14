/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2005/12/11 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.ui;

import javax.swing.filechooser.*;

import java.io.File;
import java.util.Iterator;
import java.util.HashMap;

import org.philhosoft.io.*;

/**
 * Simple file filter to use with JFileChooser.
 *
 * Based on Sun's demo ExampleFileFilter (j2sdk1.4.2_10\demo\jfc\FileChooserDemo),
 * using HashMap instead of Hashtable and Iterator instead of Enumeration.
 *
 * This is a convenience implementation of FileFilter that filters out
 * all files except for those type extensions that it knows about.
 *
 * Extensions are of the type ".foo", which is typically found on
 * Windows and Unix boxes, but rarely on Macinthosh. Case is ignored.
 *
 * Example - create a new filter that filters out all files
 * but gif and jpg image files:
 *
 *	 JFileChooser chooser = new JFileChooser();
 *	 SimpleFileFilter filter = new SimpleFileFilter("jpg", "JPEG & GIF Images")
 *   filter.addExtension("gif");
 *	 chooser.addChoosableFileFilter(filter);
 *	 chooser.showOpenDialog(this);
 *
 * @author Jeff Dinkins (core) & Philippe Lhoste (added bugs...)
 * @version 1.00.000 (based on ExampleFileFilter 1.14 01/23/03)
 * @date 2005/12/11
 */
public class SimpleFileFilter extends FileFilter
{
	/// List of accepted extensions
	private HashMap<String, String> m_filters;
	/// Description of the set of extensions
	private String m_description;
	/// Full description (may include the list of accepted extensions)
	private String m_fullDescription;
	/// Indicates whether the extensions must be in the full description
	private boolean m_bShowExtensionsInDescription = true;


	/*===== Constructors: choice for flexibility... =====*/

	/**
	 * Create a file filter.
	 * If no filters are added, then all files are accepted.
	 */
	public SimpleFileFilter()
	{
		m_filters = new HashMap<String, String>();
	}

	/**
	 * Create a file filter that accepts the given file type,
	 * and displays the given description.
	 * Example: new SimpleFileFilter("jpg", "JPeg Images");
	 *
	 * Note that the "." before the extension is not needed.
	 * If provided, it will be ignored.
	 *
	 * @see #addExtension
	 * @see #setDescription
	 */
	public SimpleFileFilter(String extension, String description)
	{
		this();
		if (extension != null)
		{
			addExtension(extension);
		}
		if (description != null)
		{
			setDescription(description);
		}
	}

	/**
	 * Create a file filter that accepts files with the given extension.
	 * Example: new SimpleFileFilter("jpg");
	 */
	public SimpleFileFilter(String extension)
	{
		this(extension, null);
	}

	/**
	 * Create a file filter that accepts the given file types,
	 * and displays the given description.
	 * Example: new SimpleFileFilter(new String[] { "jpg", "Gif" }, "JPeg & Gif Images");
	 *
	 * @see #addExtension
	 * @see #setDescription
	 */
	public SimpleFileFilter(String[] aExtensions, String description)
	{
		this();
		for (int i = 0; i < aExtensions.length; i++)
		{
			addExtension(aExtensions[i]);
		}
		if (description != null)
		{
			setDescription(description);
		}
	}

	/**
	 * Create a file filter that accepts the given file types.
	 * Example: new SimpleFileFilter(new String[] { "jpg", "Gif" });
	 *
	 * @see #addExtension
	 * @see #setDescription
	 */
	public SimpleFileFilter(String[] aExtensions)
	{
		this(aExtensions, null);
	}


	/*===== Mandatory methods, defined in the abstract class FileFilter =====*/

	/**
	 * Return true if this file should be shown in the chooser,
	 * false if it shouldn't.
	 *
	 * Files that begin with "." are ignored.
	 *
	 * @see #getFileExtension
	 * @see FileFilter#accepts
	 */
	@Override
	public boolean accept(File f)
	{
		if (f != null)
		{
			if (f.isDirectory())
			{
				// OK to display, ignore extensions on directory names...
				return true;
			}
			String extension = FileX.getFileExtension(f);
			if (extension != null && m_filters.containsKey(extension.toLowerCase()))
			{
				// Extension found in the pool
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the human readable description of this filter.
	 * For example: "JPeg and Gif Image Files (*.jpg, *.gif)"
	 * or: "All Image Files" (no extension list in description)
	 * or: "*.jpg, *.gif" (no description provided)
	 *
	 * @see setDescription
	 * @see showExtensionsInDescription
	 * @see areExtensionsInDescription
	 * @see FileFilter#getDescription
	 */
	@Override
	public String getDescription()
	{
		if (m_fullDescription == null)
		{
			if (m_description == null || m_bShowExtensionsInDescription)
			{
				// Build the description from the extension list
				if (m_filters.size() > 0)
				{
					m_fullDescription = m_description == null ? "" : m_description + " (";
					Iterator<String> it = m_filters.keySet().iterator();
					m_fullDescription += "*." + it.next();
					while (it.hasNext())
					{
						m_fullDescription += ", *." + it.next();
					}
					if (m_description != null)
					{
						m_fullDescription += ")";
					}
				}
			}
			else
			{
				m_fullDescription = m_description;
			}
		}
		return m_fullDescription;
	}


	/*===== Other public methods =====*/

	/**
	 * Add a filetype "dot" extension to filter against.
	 *
	 * For example: the following code will create a filter that filters
	 * out all files except those that end in ".jpg" and ".tif":
	 *
	 *   SimpleFileFilter filter = new SimpleFileFilter();
	 *   filter.addExtension("jpg");
	 *   filter.addExtension("tif");
	 *
	 * Note that the "." before the extension is not needed and will be ignored.
	 */
	public void addExtension(String extension)
	{
		int dotPos = extension.indexOf('.');
		if (dotPos == 0)
		{
			// Skip initial dot
			extension = extension.substring(1);
			dotPos = extension.indexOf('.');
		}
		if (dotPos > -1)
		{
			// Ignore invalid extension with dot inside
			return;
		}
		m_filters.put(extension.toLowerCase(), extension);
		// Reset full description as it may change
		m_fullDescription = null;
	}

	/**
	 * Set the human readable description of this filter.
	 * For example: filter.setDescription("Gif and JPeg Images");
	 *
	 * @see showExtensionsInDescription
	 * @see areExtensionsInDescription
	 */
	public void setDescription(String description)
   {
		m_description = description;
		// Reset full description as it may change
		m_fullDescription = null;
	}

	/**
	 * Determines whether the extension list (.jpg, .gif, etc) should
	 * show up in the human readable description.
	 *
	 * Only relevant if a description was provided in the constructor
	 * or using setDescription();
	 *
	 * @see getDescription
	 * @see setDescription
	 * @see areExtensionsInDescription
	 */
	public void showExtensionsInDescription(boolean b)
	{
		m_bShowExtensionsInDescription = b;
		m_fullDescription = null;
	}

	/**
	 * Return whether the extension list (*.jpg, *.gif, etc.) should
	 * show up in the human readable description.
	 *
	 * Only relevant if a description was provided in the constructor
	 * or using setDescription().
	 *
	 * @see getDescription
	 * @see setDescription
	 * @see showExtensionsInDescription
	 */
	public boolean areExtensionsInDescription()
	{
		return m_bShowExtensionsInDescription;
	}
}
