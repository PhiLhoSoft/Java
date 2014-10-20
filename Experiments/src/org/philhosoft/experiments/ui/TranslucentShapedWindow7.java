/*
 * Tests: A collection of little test programs to explore Java language.
 * Test of translucent, shaped windows.
*/
/* File history:
 *  1.00.000 -- 2012/11/08 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.experiments.ui;

import static java.awt.GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT;
import static java.awt.GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT;
import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;


/**
 * Creates a translucent (semi-transparent) and shaped window,
 * using the Java 7 API.
 *
 * @author PhiLho
 */
public class TranslucentShapedWindow7
{
	private JFrame m_frame;
	private JPanel m_panel;
	private JLabel m_dragHandle;

    private Point initialClick;

	public TranslucentShapedWindow7()
	{
		m_frame = new JFrame();
		m_frame.setTitle("Translucent Shaped Window");
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setContentPane(getGUI());
		m_frame.setSize(500, 500);
		m_frame.setLocation(200, 200);

		// The secret ingredient!
		m_frame.removeNotify();
		m_frame.setUndecorated(true);
		m_frame.addNotify();
		m_frame.setBackground(new Color(0, 0, 0, 0));
		m_frame.setOpacity(1); // Global opacity of the frame, we set it opaque for using per-pixel opactiy

		m_frame.setVisible(true);
	}

	@SuppressWarnings("serial")
	public JPanel getGUI()
	{
		m_panel = new JPanel()
		{
			@Override
			protected void paintComponent(Graphics g)
			{
				Paint p = new GradientPaint(
						0.0f, 0.0f, new Color(128, 128, 255, 0), // Totally transparent
						0.0f, getHeight(), new Color(55, 55, 128, 255), // Totally opaque
						true);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setPaint(p);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		m_panel.setLayout(new BorderLayout());

		m_dragHandle = new JLabel("Drag handle");
		m_dragHandle.setOpaque(true);
//		m_dragHandle.setSize(200, 50);
		m_dragHandle.setBackground(new Color(222, 222, 255));
		m_panel.add(m_dragHandle, BorderLayout.SOUTH);

		m_dragHandle.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				initialClick = e.getPoint();
			}
		});
		m_dragHandle.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				// Get current location of window
				int fx = m_frame.getX();
				int fy = m_frame.getY();

				// Determine how much the mouse moved since the initial click
				int xMove = e.getX() - initialClick.x;
				int yMove = e.getY() - initialClick.y;

				// Move window to this position
				m_frame.setLocation(fx + xMove, fy + yMove);
			}
		});

		return m_panel;
	}

	public void shapeWindow()
	{
		int width = m_frame.getWidth();
		int height= m_frame.getHeight();

		// The shape
		GeneralPath path = new GeneralPath();

		// Draw a circle
		Ellipse2D.Float circle = new Ellipse2D.Float(0, 0, width, height);
		// And add it to the shape
		path.append(circle, true);

		// Draw a triangle, hole in the circle
		Polygon polygon = new Polygon();
		polygon.addPoint(width, height / 2);
		polygon.addPoint(0, height / 2);
		polygon.addPoint(width / 2, height);
		// And add it
		path.append(polygon, true);

		m_frame.setShape(path);
	}

	private static final KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
	private static final String dispatchWindowClosingActionMapKey = "org.philhosoft.ui:WINDOW_CLOSING";
	// http://stackoverflow.com/questions/642925/swing-how-do-i-close-a-dialog-when-the-esc-key-is-pressed
	public void installEscapeCloseOperation()
	{
		@SuppressWarnings("serial")
		Action dispatchClosing = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				m_frame.dispatchEvent(new WindowEvent(m_frame, WindowEvent.WINDOW_CLOSING));
			}
		};
		JRootPane root = m_frame.getRootPane();
		root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKey, dispatchWindowClosingActionMapKey);
		root.getActionMap().put(dispatchWindowClosingActionMapKey, dispatchClosing);
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				// Determine what the default GraphicsDevice can support
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice gd = ge.getDefaultScreenDevice();

				boolean isUniformTranslucencySupported = gd.isWindowTranslucencySupported(TRANSLUCENT);
				boolean isPerPixelTranslucencySupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT);
				boolean isShapedWindowSupported = gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT);
				System.out.println(
						"\nisUniformTranslucencySupported: " + isUniformTranslucencySupported +
						"\nisPerPixelTranslucencySupported: " + isPerPixelTranslucencySupported +
						"\nisShapedWindowSupported: " + isShapedWindowSupported
				);

				TranslucentShapedWindow7 dc = new TranslucentShapedWindow7();
				dc.installEscapeCloseOperation();
				dc.shapeWindow();
			}
		});
	}
}
