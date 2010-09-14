/*
 * Tests: A collection of little test programs to explore Java language.
 * Test of the dial control.
 */
/* File history:
 *  1.01.000 -- 2010/09/14 (PL) -- Putting the control in its own file.
 *  1.00.000 -- 2010/09/13 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2010 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.ui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;

import org.philhosoft.ui.*;


public class DialControlTest
{
	public DialControlTest()
	{
	}

	public JScrollPane GetGUI()
	{
		JPanel panel = new JPanel(new GridLayout(2, 2));

		DialControl dc1 = new DialControl(200);
		DialControl dc2 = new DialControl(100,
				Color.BLACK,
				new Color(180, 255, 220), false);
		DialControl dc3 = new DialControl(100,
				Color.WHITE,
				new Color(180, 255, 220), true);
		DialControl dc4 = new DialControl(300,
				new Color(200, 55, 44),
				new Color(250, 255, 220), true);
		panel.add(dc1);
		panel.add(dc2);
		panel.add(dc3);
		panel.add(dc4);

		return new JScrollPane(panel);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame f = new JFrame();
				f.setTitle("Simple dial control");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				DialControlTest cg = new DialControlTest();
				f.setContentPane(cg.GetGUI());
				f.setSize(500, 500);
				f.setLocation(200, 200);
				f.pack();
				f.setVisible(true);
			}
		});
	}
}
