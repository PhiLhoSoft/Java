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
import javax.swing.*;


@SuppressWarnings("serial")
public class TwoPartsComboBox extends JComboBox
{
	private ListCellRenderer m_renderer;
	private int m_lastRecentIndex;

	public TwoPartsComboBox(String[] itemsRecent, String[] itemsOthers)
	{
		super(itemsRecent);
		m_lastRecentIndex = itemsRecent.length - 1;
		for (int i = 0; i < itemsOthers.length; i++)
		{
			insertItemAt(itemsOthers[i], i);
		}

		m_renderer = new JLRenderer();
		setRenderer(m_renderer);
	}

	protected class JLRenderer extends JLabel implements ListCellRenderer
	{
		JLabel m_lastRecent;

		public JLRenderer()
		{
			m_lastRecent = new JLabel();
			m_lastRecent.setBorder(new BottomLineBorder());
//			m_lastRecent.setBorder(new BottomThickLineBorder(10, Color.BLUE));
		}

		public Component getListCellRendererComponent(
				JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus)
		{
			if (value == null)
			{
				value = "Select an option";
			}
			if (index == m_lastRecentIndex)
			{
				m_lastRecent.setText(value.toString());
				return m_lastRecent;
			}
			setText(value.toString());

			return this;
		}
	}
}
