/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2005/12/15 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2005-2011 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Show a full screen frame without title,
 * with buttons to act on frame
 * like system buttons on title bar.
 *
 * Code from http://www-128.ibm.com/developerworks/java/library/j-mer0717/
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/15
 */
public class TitlelessFrameTest
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new TestTitlelessFrame();
			}
		});
	}
}

class TestTitlelessFrame
{
	private final JFrame m_frame = new JFrame();
	private Point m_origin = new Point();
	private boolean m_bMaximized;

	TestTitlelessFrame()
	{
		// Remove title bar
		m_frame.setUndecorated(true);
		m_frame.setSize(300, 300);
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//~ 			// Add a mouse listener to allow dragging of the m_frame
//~ 			m_frame.addMouseListener(new MouseAdapter()
//~ 			{
//~ 				public void mousePressed(MouseEvent e)
//~ 				{
//~ 					m_origin.x = e.getX();
//~ 					m_origin.y = e.getY();
//~ 				}
//~ 			});
//~ 			m_frame.addMouseMotionListener(new MouseMotionAdapter() {
//~ 				public void mouseDragged(MouseEvent e)
//~ 				{
//~ 					Point p = m_frame.getLocation();
//~ 					m_frame.setLocation(
//~ 							p.x + e.getX() - m_origin.x,
//~ 							p.y + e.getY() - m_origin.y);
//~ 				}
//~ 			});

		Label noTitle = new Label("See ma', no title!", Label.CENTER);
		noTitle.setBackground(Color.ORANGE);
		// Add a mouse listener to allow dragging of the m_frame using the label
		noTitle.addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				m_origin.x = e.getX();
				m_origin.y = e.getY();
			}
		});
		noTitle.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				// In Windows at least, a maximized window cannot be moved
				if (!m_bMaximized)
				{
					Point p = m_frame.getLocation();
					m_frame.setLocation(
							p.x + e.getX() - m_origin.x,
							p.y + e.getY() - m_origin.y);
				}
			}
		});

		// Add buttons
		Button max = new Button("Maximize");
		max.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_bMaximized = true;
				m_frame.setExtendedState(Frame.MAXIMIZED_BOTH);
			}
		});

		Button min = new Button("Minimize (Iconify)");
		min.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				// Preserve maximized
				m_frame.setExtendedState(
						Frame.ICONIFIED |
						m_frame.getExtendedState());
			}
		});

		Button rest = new Button("Restore (Normal)");
		rest.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_bMaximized = false;
				m_frame.setExtendedState(Frame.NORMAL);
			}
		});

		Button close = new Button("Close");
		close.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});

		Container frameCP = m_frame.getContentPane();
		frameCP.setLayout(new GridLayout(5, 1));
		frameCP.add(max);
		frameCP.add(min);
		frameCP.add(noTitle);
		frameCP.add(rest);
		frameCP.add(close);
		m_frame.setVisible(true);
	}
}
