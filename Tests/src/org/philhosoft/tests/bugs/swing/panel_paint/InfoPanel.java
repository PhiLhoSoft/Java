package org.philhosoft.tests.bugs.swing.panel_paint;


import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.*;

import org.philhosoft.ui.GraphUtil;


/**
 * Panel showing information.
 */
@SuppressWarnings("serial")
abstract class InfoPanel extends JComponent
{
	/** Information to display */
	private String m_information;
	/** The font to use to display information */
	private Font m_font;
	/** The color of the text */
	private Color m_colorForeground;
	/** The first color of the background (if gradient) */
	private Color m_colorBackground1;
	/** The second color of the background (if gradient) */
	private Color m_colorBackground2;
	/** The gradient, if any */
	private GradientPaint m_gradientBackground;
	/** Should we draw a border? */
	private boolean m_bDisplayBorder;

	// Parameters that are not officially available to user, yet
	/** Color of the border, default to BLACK */
	protected Color m_colorBorder = Color.BLACK;
	/** Size of the border */
	protected int m_borderSize;
	/** Vertical margin around text, proportional to font */
	protected float m_heightFactor;
	/**
	 * Alignment of text. Must be SwingConstants.LEFT, CENTER or RIGHT Default to CENTER.
	 */
	protected int m_alignment = SwingConstants.CENTER;

	private int m_width;
	private int m_height;

	InfoPanel(String info, Color colorFore,
			Color colorBackground1, Color colorBackground2, boolean bDisplayBorder)
	{
		setInformation(info);
		m_colorForeground = colorFore;
		m_bDisplayBorder = bDisplayBorder;

		// Default values
		if (colorFore == null)
		{
			m_colorForeground = Color.BLACK;
		}
		// Colors can be null (not set by user)
		if (colorBackground1 == null)
		{
			if (colorBackground2 == null)
			{
				// Use default color
				m_colorBackground1 = Color.GRAY;
			}
			else
			{
				// User have set only the second color
				m_colorBackground1 = colorBackground2;
				m_colorBackground2 = null;
			}
		}
		else
		{
			m_colorBackground1 = colorBackground1;
			if (colorBackground2 != null)
			{
				m_colorBackground2 = colorBackground2;
				// We can make a gradient
				// It is hard-coded as diagonal of fixed size (using second bg color beyond)
				m_gradientBackground = new GradientPaint(
						0, 0, m_colorBackground1,
						500, 50, m_colorBackground2);
			}
		}
	}

	void setInfoFont(Font font)
	{
		m_font = font;
		FontMetrics metrics = getFontMetrics(m_font);
		m_height = (int) (metrics.getHeight() * m_heightFactor);

		setPreferredSize(new Dimension(20, m_height));
	}

	/**
	 * Set information to display.
	 *
	 * @param strInformation a string holding the information to display.
	 */
	void setInformation(String strInformation)
	{
		if (strInformation == null)
		{
			strInformation = "";
		}
		m_information = strInformation;
	}

	/**
	 * Allows to change the size of the border.
	 *
	 * @param nBorderSize border size, in pixels.
	 */
	void setBorderSize(int nBorderSize)
	{
		m_borderSize = nBorderSize;
	}

	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2D = (Graphics2D) g;
		g2D.setFont(m_font);

		if (m_gradientBackground != null)
		{
			g2D.setPaint(m_gradientBackground);
		}
		else
		{
			g2D.setColor(m_colorBackground1);
		}
		Dimension d = getSize();
		g2D.fillRect(0, 0, d.width, d.height);
		g2D.setColor(m_colorForeground);

		// Get the text bounds
		FontRenderContext fontContext = g2D.getFontRenderContext();
		TextLayout layout = new TextLayout(m_information, m_font, fontContext);
		layout.draw(g2D, 0, 0);
		Rectangle2D bounds = layout.getBounds();

		int posX = 10;
		m_width = (int) bounds.getWidth();
		m_height = (int) bounds.getHeight();
		if (m_alignment == SwingConstants.CENTER)
		{
			posX = (d.width - m_width) / 2;
		}
		else if (m_alignment == SwingConstants.RIGHT)
		{
			posX = d.width - m_width - posX;
		}
		// Center vertically
		FontMetrics fm = g2D.getFontMetrics();
		int posY = d.height / 2 + m_height / 2 - fm.getDescent();

		g2D.drawString(m_information, posX, posY);

		// finally we draw a border all around the panel
		if (m_bDisplayBorder)
		{
			g2D.setColor(m_colorBorder);
			for (int i = 0; i < m_borderSize; i++)
			{
				g2D.drawRect(i, i, d.width - 1 - 2 * i, d.height - 1 - 2 * i);
			}
		}
	}
}


/**
 * Shows the title.
 */
@SuppressWarnings("serial")
final class TitlePanel extends InfoPanel
{
	/** The default font (if not changed) specific to this class */
	private static Font s_fontDefault = new Font("Arial", Font.PLAIN, 24);

	// Class init
	{
		m_heightFactor = 1.7F;
		m_borderSize = 3;
	}

	TitlePanel(String title, Font font, Color colorFore,
			Color colorBackground1, Color colorBackground2, boolean bDisplayBorder)
	{
		super(title, colorFore, colorBackground1, colorBackground2, bDisplayBorder);
		if (font == null)
		{
			font = s_fontDefault;
		}
		setInfoFont(font);
	}
}


/**
 * Shows the sub-title.
 */
@SuppressWarnings("serial")
final class SubTitlePanel extends InfoPanel
{
	/** The default font (if not changed) specific to this class */
	private static Font s_fontDefault = new Font("Tahoma", Font.PLAIN, 18);

	// Class init
	{
		m_heightFactor = 1.5F;
		m_borderSize = 2;
		m_colorBorder = Color.DARK_GRAY;
	}

	SubTitlePanel(String title, Font font, Color colorFore,
			Color colorBackground1, Color colorBackground2, boolean bDisplayBorder)
	{
		super(title, colorFore, colorBackground1, colorBackground2, bDisplayBorder);
		if (font == null)
		{
			font = s_fontDefault;
		}
		setInfoFont(font);
	}
}
