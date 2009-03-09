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
 *  1.00.000 -- 2005/12/29 (PL) -- Creation
 */
package org.philhosoft.tests.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
//import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
//import javax.swing.JLabel;

import org.philhosoft.ui.FrameState;


/**
 * Test of method to store and retreive the bounds and state
 * of a Swing application.
 * Can also be used as boilerplate code for Swing app.
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/29
 */
public class MemorizeWindowState
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				new MainFrame();
			}
		});
	}
}

class MainFrame extends JFrame implements ComponentListener
{
	private static final long serialVersionUID = 1L;
	private FrameState m_frameState;
	private GUI m_gui;			// Presentation part
//~ 	private DataAccess m_da;	// Model part


	public MainFrame()
	{
		super("Sample Swing Application");

		// Create the presentation layer
		createGUI();
		// Create the model layer
//		createData();
		// Connect model to presentation
//		m_gui.connect(m_da);
		// Build the GUI, using the data
		m_gui.buildGUI();

		// Manage closing of application to cleanly get rid of data
		// and store preferences.
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				closeWindow();
			}
		});
		addComponentListener(this);

		// Retreive the user preferences for window position/state, if any
		m_frameState = new FrameState();

		// Display the window
		pack();
		m_frameState.set(this);
//~ 		setSize(750, 500);
		setVisible(true);
	}

	/**
	 * Create the GUI and show it.
	 * For thread safety, this method should be invoked from the
	 * event-dispatching thread.
	 */
	private void createGUI()
	{
		// For default (Java) look and feel. Commented out to get system LaF.
//		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane
		m_gui = new GUI(this);
		// Content panes must be opaque. Default on most but not all window systems.
		m_gui.setOpaque(true);
		setContentPane(m_gui);
	}

//~ 	/**
//~ 	 * Create an access to data.
//~ 	 */
//~ 	private void createData()
//~ 	{
//~ 		try
//~ 		{
//~ 			m_da = new DataAccess(ms_aArgs);
//~ 		}
//~ 		catch (NoDataException ex)
//~ 		{
//~ 			ex.printStackTrace();
//~ 			System.err.println(ex.getMessage());
//~ 			System.exit(1);
//~ 		}
//~ 	}

	public void closeWindow()
	{
//~ 		// Close data source
//~ 		m_da.close();
		// Store window state
		m_frameState.store(this);
		// Kill the window
		setVisible(false);
		dispose();
		// Say bye bye!
		System.exit(0);
	}


	// Component listener methods

	public void componentMoved(ComponentEvent e)
	{
		m_frameState.get(this);
//~ 		Point newLoc = getLocation();
//~ 		Rectangle bounds = getBounds();
//~ 		System.out.println("Main frame moved to " + bounds.x + ", " + bounds.y);
	}
	public void componentResized(ComponentEvent e)
	{
		m_frameState.get(this);
//~ 		Dimension newSize = getSize();
//~ 		Rectangle bounds = getBounds();
//~ 		System.out.println("Main frame resized to " + bounds.width + ", " + bounds.height);
	}
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
}

/**
 * The GUI layer.
 * On a real application, I would move it to its own file...
 *
 * @author Philippe Lhoste
 * @version 1.00.000
 * @date 2005/12/29
 */
class GUI extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;
	
	// Access to data
//~ 	private DataAccess m_da;
	// Access to parent frame
	private MainFrame m_mainFrame;

	private JTextArea m_jEdit;
	private JButton m_jOK;
	private JButton m_jCancel;

	/**
	 * Default constructor
	 */
	public GUI(MainFrame parent)
	{
		super(new BorderLayout());
		m_mainFrame = parent;
//		buildGUI();	// Defered, so can access to data before building GUI showing it...
	}

//~ 	/**
//~ 	 * Model:
//~ 	 * Connect to server, to allow access to data.
//~ 	 */
//~ 	public void connect(DataAccess da)
//~ 	{
//~ 		m_da = da;
//~ 	}

	/**
	 * Build the GUI...
	 *
	 * @return void
	 */
	public void buildGUI()
	{
		// Lazy, ugly layout...
		m_jEdit = new JTextArea();
		add(m_jEdit, BorderLayout.CENTER);

		m_jOK = new JButton("OK");
		add(m_jOK, BorderLayout.NORTH);
		m_jOK.setActionCommand("ok");
		m_jOK.addActionListener(this);

		m_jCancel = new JButton("Cancel");
		add(m_jCancel, BorderLayout.SOUTH);
		m_jCancel.setActionCommand("cancel");
		m_jCancel.addActionListener(this);
	}

	/**

	 * Manage the buttons.
	 */
	public void actionPerformed(ActionEvent ae)
	{
		String buttonPressed = ae.getActionCommand();
		if (buttonPressed.equals("ok"))
		{
			m_jEdit.setText("Arbitrary string!");
		}
		else if (buttonPressed.equals("cancel"))
		{
			m_mainFrame.closeWindow();
		}
	}
}
