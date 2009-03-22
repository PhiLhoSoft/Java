/*
 * Tests: A collection of little test programs to explore Java language.
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
package org.philhosoft.tests.ui;


import org.philhosoft.ui.*;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class TwoPartsComboBoxDemo extends JFrame
{
	private TwoPartsComboBox m_combo;

	public TwoPartsComboBoxDemo()
	{
		Container cont = getContentPane();
		cont.setLayout(new FlowLayout());

		cont.add(new JLabel("Data: ")) ;

		String[] itemsRecent = new String[] { "ichi", "ni", "san" };
		String[] itemsOther = new String[] { "one", "two", "three" };
		m_combo = new TwoPartsComboBox(itemsRecent, itemsOther);

		m_combo.setSelectedIndex(-1);
		cont.add(m_combo);
		m_combo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				String si = (String) m_combo.getSelectedItem();
				System.out.println(si == null ? "No item selected" : si.toString());
			}
		});
	}

	/**
	 * Start the demo.
	 *
	 * @param args   the command line arguments
	 */
	public static void main(String[] args)
	{
		// turn bold fonts off in metal
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame demoFrame = new TwoPartsComboBoxDemo();
				demoFrame.setTitle("Test GUI");
				demoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				demoFrame.setSize(400, 100);
				demoFrame.setVisible(true);
			}
		});
	}
}
