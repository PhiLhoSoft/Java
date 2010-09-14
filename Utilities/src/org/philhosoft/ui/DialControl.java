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
	private double m_angle;

	private int m_size;
	private int m_prevWidth;
	private Color m_backColor;
	private boolean m_bGradient;
	private Paint m_gpBackground;
	private Polygon m_arrow;

	private int m_centerX;
	private int m_centerY;

	public DialControl(int size)
	{
//~ 		m_backColor = getBackground();
		m_backColor = Color.LIGHT_GRAY;
		m_bGradient = true;

		InitListeners();
		Resize(size);
	}

	public DialControl(int size, Color backColor, boolean bGradient)
	{
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

	public double GetAngle()
	{
		return m_angle;
	}
	public void SetBackColor(Color c)
	{
		m_backColor = c;
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

		if (m_bGradient)
		{
			UpdateBackColor();
			g2D.setPaint(m_gpBackground);
		}
		else
		{
			g2D.setColor(m_backColor);
		}
		int margin = (getWidth() - m_size) / 2;
		int size = m_size - 4;
		m_centerX = getWidth() / 2 + 4;
		m_centerY = getHeight() / 2 + 4;
//~ 		System.out.println("W " + getWidth() + " M " + margin + " S " + size);

		g2D.fillOval(margin, margin, size, size);
		g2D.setColor(m_backColor.brighter());
		g2D.drawOval(margin,     margin,     size, size);
		g2D.drawOval(margin + 1, margin + 1, size, size);
		g2D.setColor(m_backColor.darker().darker());
		g2D.drawOval(margin + 2, margin + 2, size, size);
		g2D.drawOval(margin + 3, margin + 3, size, size);

		g2D.translate(m_centerX, m_centerY);
		g2D.rotate(m_angle);
		g2D.setColor(Color.BLACK);
//~ 		g2D.rotate(m_angle - Math.PI / 2);
//~ 		g2D.drawLine(0, 0, 0, (int) (m_size * 0.4) - 4);
		g2D.drawPolygon(m_arrow);

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

	private void UpdateAngle(MouseEvent e, boolean bPressed)
	{
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
