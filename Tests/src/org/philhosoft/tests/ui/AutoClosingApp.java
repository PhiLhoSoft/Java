/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2008/11/03 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.ui;

import java.awt.event.*;

import javax.swing.*;

/**
 *
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2008/11/03
 */
@SuppressWarnings("serial")
public class AutoClosingApp extends JFrame implements WindowListener
{
	Timer m_clock;

	public AutoClosingApp()
	{
		super("Some Frame");
		setSize(400, 400);
		// If the user closes the window, exit immediately
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Enable Window Events on this Component
		addWindowListener(this);
		setVisible(true);

		// Start a timer
		WindowCloser task = new WindowCloser(10);
		m_clock = new Timer(500, task);	// Fire every 500ms
		m_clock.setInitialDelay(2000);	// First delay 2 seconds
		m_clock.setRepeats(true);
		m_clock.start();
	}

	public void windowOpened(WindowEvent e)
	{
		System.out.println("windowOpened: " + e);
	}
	// Called when user closes the window (X button for example)
	public void windowClosing(WindowEvent e)
	{
		System.out.println("windowClosing: " + e);
	}
	// Called when dispose() is called on frame
	public void windowClosed(WindowEvent e)
	{
		System.out.println("windowClosed: " + e);
		m_clock.stop();
//		System.exit(0);
	}

	public void windowIconified(WindowEvent e)
	{
		System.out.println("windowIconified: " + e);
	}
	public void windowDeiconified(WindowEvent e)
	{
		System.out.println("windowDeiconified: " + e);
	}
	public void windowActivated(WindowEvent e)
	{
		System.out.println("windowActivated: " + e);
	}
	public void windowDeactivated(WindowEvent e)
	{
		System.out.println("windowDeactivated: " + e);
	}

	//a simple timer
	class WindowCloser implements ActionListener
	{
		int m_delay = 10;
		public WindowCloser(int delay)
		{
			m_delay = delay;
		}
		public WindowCloser()
		{
		}

		@Override
		public void actionPerformed(ActionEvent ae)
		{
			if (m_delay-- > 0)
			{
				System.out.println("Still Waiting: " + m_delay);
				return;
			}
			System.out.println("About to close");
			// Close the frame
			AutoClosingApp.this.dispose();
		}
	}

	public static void main(String args[])
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new AutoClosingApp();
			}
		});
	}
}
