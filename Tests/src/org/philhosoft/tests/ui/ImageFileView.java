/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2005/12/13 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.ui;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.util.HashMap;

import org.philhosoft.io.*;

/**
 * A FileView customizer for JFileChooser.
 *
 * Code from http://java.sun.com/developer/JDCTechTips/2004/tt0316.html
 * and http://java.sun.com/docs/books/tutorial/uiswing/components/filechooser.html
 *
 * @author John Zukowski & Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/13
 */
public class ImageFileView extends FileView
{
	/// List of accepted extensions
	private HashMap<String, ImageType> m_imageTypes;
	private ImageType m_gifType, m_jpegType, m_pngType, m_tiffType;
//~ 	ImageType m_bmpType;

	public ImageFileView()
	{
		m_imageTypes = new HashMap<String, ImageType>();
		m_gifType  = new ImageType("Gif",
				"Graphics Interchange Format",
				"image/gif", "images/gifIcon_s.png");
		m_imageTypes.put("gif", m_gifType);
		m_jpegType  = new ImageType("JPeg",
				"Joint Photographic Experts Group",
				"image/jpeg", "images/jpegIcon_s.png");
		m_imageTypes.put("jpg", m_jpegType);
		m_imageTypes.put("jpe", m_jpegType);
		m_imageTypes.put("jpeg", m_jpegType);
		m_pngType  = new ImageType("PNG",
				"Portable Network Graphics",
				"image/png", "images/pngIcon_s.png");
		m_imageTypes.put("png", m_pngType);
		m_tiffType = new ImageType("Tiff",
				"Tagged Image File Format",
				"image/tiff", "images/tiffIcon_s.png");
		m_imageTypes.put("tif", m_tiffType);
		m_imageTypes.put("tiff", m_tiffType);
//~ 		m_bmpType  = new ImageType("BMP",
//~ 					"Bitmap",
//~ 					"", "images/bmpIcon_s.png");
	}

	/**
	 * Customize the displayed name:
	 * add length to name.
	 */
	@Override
	public String getName(File f)
	{
//~ 		return null; // Let the L&F FileView figure this out
		String extension = FileX.getFileExtension(f);
		String name = null;

		ImageType it = m_imageTypes.get(extension);
		if (it != null)
		{
			name = f.getName() + " (" +
					Math.ceil(f.length() / 102.4) / 10 +
					"KiB)";
		}
		return name;
	}

	@Override
	public String getDescription(File f)
	{
//		return null; // Let the L&F FileView figure this out
		String extension = FileX.getFileExtension(f);
		String desc = null;

		ImageType it = m_imageTypes.get(extension);
		if (it != null)
		{
			desc = it.getLongName();
		}
		return desc;
	}

	@Override
	public Boolean isTraversable(File f)
	{
		return null; // Let the L&F FileView figure this out
	}

	@Override
	public String getTypeDescription(File f)
	{
		String extension = FileX.getFileExtension(f);
		String type = null;

		ImageType it = m_imageTypes.get(extension);
		if (it != null)
		{
			type = it.getShortName();
		}
		return type;
	}

	@Override
	public Icon getIcon(File f)
	{
		String extension = FileX.getFileExtension(f);
		Icon icon = null;

		ImageType it = m_imageTypes.get(extension);
		if (it != null)
		{
			icon = it.getIcon();
		}
		return icon;
	}
}
