/*
 * Tests: A collection of little test programs to explore Java language.
 * Test of the angle chooser control.

javac -d ..\bin -cp ../../Utilities/bin org/philhosoft/tests/ui/AngleChooserTest.java
java -cp ../bin;../../Utilities/bin org.philhosoft.tests.ui.AngleChooserTest
*/
/* File history:
 *  1.02.000 -- 2011/01/17 (PL) -- Normalize case of methods
 *  1.01.000 -- 2010/09/14 (PL) -- Putting the control in its own file.
 *  1.00.000 -- 2010/09/13 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2010-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.ui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;

import org.philhosoft.ui.*;


public class AngleChooserTest
{
	public AngleChooserTest()
	{
	}

	public JScrollPane getGUI()
	{
		JPanel panel = new JPanel(new GridLayout(2, 3));

		AngleChooser ac0 = new AngleChooser();
		AngleChooser ac1 = new AngleChooser(200);
		AngleChooser ac2 = new AngleChooser(100,
				Color.BLACK,
				new Color(180, 255, 220), false);
		AngleChooser ac3 = new AngleChooser(100,
				Color.WHITE,
				new Color(180, 255, 220), true);
		ac3.setAngle(5 * Math.PI / 4);
		AngleChooser ac4 = new AngleChooser(300,
				new Color(200, 55, 44),
				new Color(250, 255, 220), true);
		AngleChooser ac5 = new AngleChooser(100,
				new Color(66, 55, 44),
				new Color(250, 155, 120), true);
		ac5.setAngle(-Math.PI / 4);
		ac5.setEnabled(false);
		panel.add(ac0);
		panel.add(ac1);
		panel.add(ac2);
		panel.add(ac3);
		panel.add(ac4);
		panel.add(ac5);

		return new JScrollPane(panel);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame f = new JFrame();
				f.setTitle("Test of angle chooser control");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				AngleChooserTest dc = new AngleChooserTest();
				f.setContentPane(dc.getGUI());
//				f.setSize(500, 500);
				f.setLocation(200, 200);
				f.pack();
				f.setVisible(true);
			}
		});
	}
}
