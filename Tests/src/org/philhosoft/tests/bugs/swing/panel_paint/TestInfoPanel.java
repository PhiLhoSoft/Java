/*
 * Tests: A collection of little test programs to explore Java language.
 * Tries to recreate a Swing paint bug.
 */
/* File history:
 *  1.00.000 -- 2012/10/29 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.bugs.swing.panel_paint;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class TestInfoPanel
{
	private JPanel m_panel;

	TestInfoPanel()
	{
	}

	private JScrollPane getContent()
	{
		m_panel = new JPanel(new GridLayout(0, 1));
		m_panel.add(new TitlePanel("A Title jqpgy_@,;!§", null, Color.RED, Color.LIGHT_GRAY, Color.GRAY, false));
		m_panel.add(new SubTitlePanel("A Sub-Title jqpgy_@,;!§", null, Color.BLUE, Color.WHITE, Color.YELLOW, false));
		m_panel.add(new JPanel());
		return new JScrollPane(m_panel);
	}

	public static void main(String[] args) throws IOException
	{
		final TestInfoPanel tm = new TestInfoPanel();
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame f = new JFrame();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.setTitle(tm.getClass().getName());
				f.setContentPane(tm.getContent());
				f.setSize(800, 400);
				f.setLocation(200, 200);
				f.setVisible(true);
			}
		});
	}
}
