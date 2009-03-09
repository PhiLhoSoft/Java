/*
 * org.philhosoft.*: A collection of utility classes for Java.
 *
 * Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 *
 * Copyright notice: See the PhiLhoSoftLicence.txt file for details.
 * This file is distributed under the zlib/libpng license.
 * Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
 */
/* File history:
 *  1.00.000 -- 2008/09/20 (PL) -- Creation
 */
package org.philhosoft.ui;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;


@SuppressWarnings("serial")
class BottomThickLineBorder extends AbstractBorder
{
	private int m_thickness;
	private Color m_color;

	BottomThickLineBorder()
	{
		this(1, Color.BLACK);
	}

	BottomThickLineBorder(int thickness, Color color)
	{
		m_thickness = thickness;
		m_color = color;
	}

	public void paintBorder(Component c, Graphics g,
			int x, int y, int width, int height)
	{
		Graphics copy = g.create();
		if (copy != null)
		{
			try
			{
				copy.translate(x, y);
				copy.setColor(m_color);
				copy.fillRect(0, height - m_thickness, width - 1, height - 1);
			}
			finally
			{
				copy.dispose();
			}
		}
	}

	public boolean isBorderOpaque()
	{
		return true;
	}
	public Insets getBorderInsets(Component c)
	{
		return new Insets(0, 0, m_thickness, 0);
	}
	public Insets getBorderInsets(Component c, Insets i)
	{
		i.left = i.top = i.right = 0;
		i.bottom = m_thickness;
		return i;
	}
}
