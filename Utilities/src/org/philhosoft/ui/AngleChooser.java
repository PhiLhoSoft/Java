/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2010/09/14 (PL) -- Creation (split from DialControlTest.java)
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2010 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

/**
 * A dial control, allowing to select an angle/direction with the mouse.
 *
 * @author Philippe Lhoste (PhiLho)
 * @version 1.00.000
 * @date 2010/09/14
 */
@SuppressWarnings("serial")
public class DialControl extends JComponent implements MouseListener, MouseMotionListener
{
	/** The angle of the dial hand. */
	private double m_angle;

	/** The size of the dial face. */
	private int m_size;
	/** The color of the hand. */
	private Color m_foreColor;
	/** The (base) color of the face. */
	private Color m_backColor;
	/** If true, the face uses a gradient. */
	private boolean m_bGradient;
	/** The gradient used to paint the face, if gradient is requested. */
	private Paint m_gpBackground;
	/** The polygon representing the hand, oriented to East. */
	private Polygon m_arrow;

	// To regenerate a gradient only if really needed
	private int m_prevWidth;
   // Center of the control
	private int m_centerX;
	private int m_centerY;

   public DialControl()
   {
      this( 64 );
   }

	/**
	 * Simple constructor where only the size is defined.
	 * Use a light gray color with gradient and black arrow.
	 *
	 * @param size  the pixel size (diameter) of the dial face. Must be above or equal to 32.
	 */
	public DialControl(int size)
	{
      this( size, Color.BLACK, Color.LIGHT_GRAY, true );
	}

	/**
	 * Constructor allowing to specify the size of the dial,
	 * the color of the arrow, the color of the back, and if we use a pure color or a gradient.
	 *
	 * @param size       the pixel size (diameter) of the dial face
	 * @param foreColor  the color of the arrow
	 * @param backColor  the color of the back
	 * @param bGradient  if false, use a pure back color, otherwise, make a gradient out of it
	 */
	public DialControl(int size, Color foreColor, Color backColor, boolean bGradient)
	{
		super();

		m_foreColor = foreColor;
		m_backColor = backColor;
		m_bGradient = bGradient;

		InitListeners();
		Resize(size);
	}

	private void InitListeners()
	{
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void Resize(int size)
	{
      if ( size == m_size)
         return;
		if (size < 32)
		{
			size = 32; // Impose a minimal size
		}
		m_size = size;
		int lenArrow = (int) (m_size * 0.4) - 4;
		int lenTip = lenArrow / 8;
		if (lenTip < 8)
		{
			lenTip = 8;
		}

		m_arrow = new Polygon();
		m_arrow.addPoint(0, 0);
		m_arrow.addPoint(lenArrow - lenTip, lenTip / 8);
		m_arrow.addPoint(lenArrow - lenTip, lenTip / 4);
		m_arrow.addPoint(lenArrow, 0);
		m_arrow.addPoint(lenArrow - lenTip, -lenTip / 4);
		m_arrow.addPoint(lenArrow - lenTip, -lenTip / 8);
		m_arrow.addPoint(0, 0);

		repaint();
	}

	/**
	 * Returns the chosen trigonometric angle, in radians.
	 * @return the angle, in radians.
	 */
   // We have to convert the trigonometric angle to Java2D one (y axis toward South).
	public double GetAngle()
	{
		return 2 * Math.PI - m_angle;
	}
	/**
	 * Sets the trigonometric angle (counter-clockwise, 0 toward East), in radians.
	 * @param angle  the angle to set, in radians.
	 */
	public void SetAngle( double angle )
	{
		m_angle = 2 * Math.PI - angle;
		UpdateTooltip();
		repaint();
	}
	public void SetBackColor(Color c)
	{
		m_backColor = c;
		repaint();
	}
	public void SetForeColor(Color c)
	{
		m_foreColor = c;
		repaint();
	}

	private void UpdateBackColor()
	{
		if (m_prevWidth == getWidth())
			return;	// No need to generate a new gradient
		m_prevWidth = getWidth();
		int margin = (m_prevWidth - m_size) / 2;
//~ 		System.out.println("GP W " + m_prevWidth + " M " + margin);
		m_gpBackground = new GradientPaint(
				margin, margin, m_backColor.darker(),
				margin + m_size, margin + m_size, m_backColor.brighter());
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		if (isOpaque())
		{
			// Paint background
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}

		// Copy the graphics context, to avoid changing its state
		Graphics2D g2D = (Graphics2D) g.create();
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//~ 		g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//~ 		g2D.setFont(m_font);

		if (isEnabled())
		{
			if (m_bGradient)
			{
				UpdateBackColor();
				g2D.setPaint(m_gpBackground);
			}
			else
			{
				g2D.setColor(m_backColor);
			}
		}
		else
		{
			g2D.setColor(Color.LIGHT_GRAY);
		}

		int margin = ( getWidth() - m_size ) / 2;
		int size = m_size - 4;
		m_centerX = getWidth() / 2;
		m_centerY = getHeight() / 2;
//~ 		System.out.println("W " + getWidth() + " M " + margin + " S " + size);

		// Background of the dial face
		g2D.fillOval( margin, margin, size, size );
		// Draw the border, 4 circles of two colors to give a light 3D look
		g2D.setColor( isEnabled() ? m_backColor.brighter() : Color.GRAY );
		g2D.drawOval( margin,     margin,     size, size );
		g2D.drawOval( margin + 1, margin + 1, size, size );
		g2D.setColor( isEnabled() ? m_backColor.darker().darker() : Color.GRAY );
		g2D.drawOval( margin + 2, margin + 2, size, size );
		g2D.drawOval( margin + 3, margin + 3, size, size );

		// Draw the dial hand
		g2D.translate( m_centerX, m_centerY );
		g2D.rotate( m_angle );
		g2D.setColor( isEnabled() ? m_foreColor : Color.DARK_GRAY );
		g2D.fillPolygon( m_arrow );

		g2D.dispose();
	}

	private Dimension GetDimension()
	{
		Insets insets = getInsets();
		return new Dimension(
				m_size + insets.left + insets.right,
				m_size + insets.top + insets.bottom
		);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return GetDimension();
	}

	@Override
	public Dimension getMinimumSize()
	{
		return GetDimension();
	}

   private void UpdateTooltip()
   {
      setToolTipText( "Angle: " + Math.ceil( 180 * GetAngle() * 100 / Math.PI ) / 100 );
   }

	private void UpdateAngle(MouseEvent e, boolean bPressed)
	{
		if (!isEnabled())
			return;
		boolean bConstraint = e.isControlDown();
		int x = e.getX();
		int y = e.getY();
		float deltaX = (float) (x - m_centerX);
		float deltaY = (float) (y - m_centerY);
		m_angle = Math.atan2(deltaY, deltaX);
		if (m_angle < 0)
		{
			m_angle += 2 * Math.PI;
		}
//~ 		System.out.println("Drag " + 180 * m_angle / Math.PI);
		if (bConstraint)
		{
			// angle between 0 and 16
			m_angle = Math.ceil(8 * m_angle / Math.PI);
			// Back to 0 - 2*PI
			m_angle *= Math.PI / 8;
		}
		UpdateTooltip();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		UpdateAngle(e, true);
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		UpdateAngle(e, false);
		repaint();
	}

	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseMoved(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
	@Override public void mouseClicked(MouseEvent e) {}
}
