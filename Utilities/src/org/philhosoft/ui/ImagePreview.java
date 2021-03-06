/*
 * org.philhosoft.*: A collection of utility classes for Java.
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
package org.philhosoft.ui;

//import java.io.File;
import java.awt.*;
import javax.swing.*;

import java.beans.*;
import java.io.*;

/**
 * A component to provide an image preview in JFileChooser.
 *
 * Code from http://java.sun.com/developer/JDCTechTips/2004/tt0316.html
 *
 * @author John Zukowski & Philippe Lhoste (mostly reformatting)
 * @version 1.00.000
 * @date 2005/12/13
 */
public class ImagePreview
		extends JLabel
		implements PropertyChangeListener
{
	private static final long serialVersionUID = 1L;

	protected static int PREFERRED_WIDTH = 125;
	protected static int PREFERRED_HEIGHT = 100;

	public ImagePreview(JFileChooser chooser)
	{
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		chooser.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent changeEvent)
	{
		String changeName = changeEvent.getPropertyName();
		if (changeName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY))
		{
			File file = (File) changeEvent.getNewValue();
			if (file != null)
			{
				ImageIcon icon = new ImageIcon(file.getPath());
				if (icon.getIconWidth() > PREFERRED_WIDTH)
				{
					icon = new ImageIcon(
							icon.getImage().getScaledInstance(
									PREFERRED_WIDTH, -1,
									Image.SCALE_DEFAULT));
				}
				if (icon.getIconHeight() > PREFERRED_HEIGHT)
				{
					icon = new ImageIcon(
							icon.getImage().getScaledInstance(
									-1, PREFERRED_HEIGHT,
									Image.SCALE_DEFAULT));
				}
				setIcon(icon);
			}
		}
	}
}
