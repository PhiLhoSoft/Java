/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.01.000 -- 2012/01/04 (PL) -- Update aiming to address the issues mentioned in a comment
 *  1.00.000 -- 2008/09/20 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008-2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.ui;


import java.awt.*;

import javax.swing.*;


/**
 * A combo box showing two parts, with a separator between them.
 * Can be used, for example, to list "recent choices", then regular choices.
 * Written to answer a question on <a href="http://stackoverflow.com/questions/138793/how-do-i-add-a-separator-to-a-jcombobox-in-java">StackOverflow</a>.
 */
@SuppressWarnings("serial")
public class TwoPartsComboBox extends JComboBox<String>
{
	private int m_lastFirstPartIndex;

	public TwoPartsComboBox(String[] itemsFirstPart, String[] itemsSecondPart)
	{
		super(itemsFirstPart);
		m_lastFirstPartIndex = itemsFirstPart.length - 1;
		for (int i = 0; i < itemsSecondPart.length; i++)
		{
			insertItemAt(itemsSecondPart[i], i);
		}

		setRenderer(new JLRenderer());
	}

	protected class JLRenderer extends JLabel implements ListCellRenderer<String>
	{
		private JLabel m_lastFirstPart;

		public JLRenderer()
		{
			m_lastFirstPart = new JLabel();
			m_lastFirstPart.setBorder(new BottomLineBorder());
//			m_lastFirstPart.setBorder(new BottomLineBorder(10, Color.BLUE));
		}

		@Override
		public Component getListCellRendererComponent(
				JList<? extends String> list,
				String value,
				int index,
				boolean isSelected,
				boolean cellHasFocus)
		{
			if (value == null)
			{
				value = "Select an option";
			}
			JLabel label = this;
			if (index == m_lastFirstPartIndex)
			{
				label = m_lastFirstPart;
			}
			label.setText(value.toString());
			label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
			label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
			label.setOpaque(true);

			return label;
		}
	}
}
