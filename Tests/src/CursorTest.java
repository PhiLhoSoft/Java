/*
 * Tests: A collection of little test programs to explore Java language.
 *
 * Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
 *
 * Copyright notice: See the PhiLhoSoftLicence.txt file for details.
 * This file is distributed under the zlib/libpng license.
 * Copyright (c) 2005-2006 Philippe Lhoste / PhiLhoSoft
 */
/* File history:
 *  1.00.000 -- 2008/10/31 (PL) -- Creation
 */

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
public class CursorTest extends JFrame
{
	private CursorTest()
	{
	}

	private void ShowDialog()
	{
        JLabel label = new JLabel("Move mouse here for hand cursor");
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JOptionPane pane = new JOptionPane(label);
        pane.setOptions(new Object[] { "OK" } );

        JDialog dialog = pane.createDialog(this, "Test Dialog");
        dialog.setVisible(true);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				CursorTest testFrame = new CursorTest();
				testFrame.setTitle("Test GUI");
				testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				testFrame.setSize(500, 300);
				testFrame.setVisible(true);
				testFrame.ShowDialog();
			}
		});
	}
}
