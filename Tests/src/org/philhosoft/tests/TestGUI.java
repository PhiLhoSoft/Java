/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2008/10/31 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * Template class for test applications.
 *
 * @author Philippe Lhoste
 * @version 1.01.000
 * @date 2008/10/31
 */
@SuppressWarnings("serial")
public class TestGUI extends JFrame
{
	private TestGUI()
	{
		Container cont = getContentPane();
		cont.setLayout(new FlowLayout());
		String text = "Test text.";
		JPanel panel = new JPanel();
		cont.add(panel);
		JLabel label1 = new JLabel(text);
		JLabel label2 = new JLabel(text);
		label1.setForeground(Color.BLUE);
		label2.setBackground(Color.GREEN);
		label2.setOpaque(true);
		cont.add(label1);
		panel.add(label2);
		panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// turn bold fonts off in metal
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame testFrame = new TestGUI();
				testFrame.setTitle("Test GUI");
				testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				testFrame.setSize(500, 300);
				testFrame.setVisible(true);
			}
		});
	}
}
