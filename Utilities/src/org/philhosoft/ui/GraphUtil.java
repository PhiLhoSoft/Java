/*
 * org.philhosoft.*: A collection of utility classes for Java.
 */
/* File history:
 *  1.00.000 -- 2012/10/29 (PL) -- Creation.
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.ui;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;


/**
 * Various utility classes to manipulate the Graphics2D context.
 *
 * @author PhiLho
 */
public class GraphUtil
{
	private GraphUtil() {} // Only static functions

	/**
	 * Reduces the font size of the given graphics context to ensure the given text can be displayed within the given width.
	 *
	 * @return the final string width we get with the (possibly adjusted) current font size
	 */
	public static int adjustFontSizeToTextWidth(Graphics2D g2d, String textToDisplay, int maxWidth)
	{
		assert g2d != null && textToDisplay != null && maxWidth > 0 : "Invalid input";

		// Get current value about font and size
		Font currentFont = g2d.getFont();
		int fontSize = currentFont.getSize();
		int stringWidth;
		// Find out the right font size for the wanted width
		do
		{
			stringWidth = getStringWidthInPixel(g2d, textToDisplay);
			if (stringWidth <= maxWidth) break;

			fontSize--;
			g2d.setFont(currentFont.deriveFont((float) fontSize));
			stringWidth = GraphUtil.getStringWidthInPixel(g2d, textToDisplay);
		} while (stringWidth > maxWidth && fontSize > 3);

		return stringWidth;
	}

	/**
	 * @return the width in pixels of the input string in the given graphics context
	 */
	public static int getStringWidthInPixel(Graphics2D g2D, String textToDisplay)
	{
		Rectangle2D bounds = getBounds(g2D, textToDisplay);
		return (int) bounds.getWidth();
	}

	/**
	 * @return the height in pixels of the input string in the given graphics context
	 */
	public static int getStringHeightInPixel(Graphics2D g2D, String textToDisplay)
	{
		Rectangle2D bounds = getBounds(g2D, textToDisplay);
		return (int) bounds.getHeight();
	}

	/**
	 * Returns the bounds of the given string if drawn in the given graphics context (including the current font).
	 *
	 * @param g2D the graphics context
	 * @param textToDisplay the text to analyze
	 * @return the dimensions of the text
	 */
	public static Rectangle2D getBounds(Graphics2D g2D, String textToDisplay)
	{
		if (textToDisplay == null || textToDisplay.length() == 0)
		{
			return new Rectangle(0, 0);
		}
		FontRenderContext fontContext = g2D.getFontRenderContext();
		Font font = g2D.getFont();
		TextLayout layout = new TextLayout(textToDisplay, font, fontContext);

		Rectangle2D bounds = layout.getBounds();
		return bounds;
	}

	/**
	 * Draws a possibly multi-line message in red in the center of the given graphics context.
	 *
	 * @param g2d the graphics context
	 * @param messages the lines of message
	 * @param imageWidth the image width
	 * @param imageHeight the image height
	 */
	public static void drawErrorMessage(Graphics2D g2d, String[] messages, int imageWidth, int imageHeight)
	{
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g2d.setPaint(Color.WHITE);
		g2d.fillRect(0, 0, imageWidth, imageHeight);

		Font font = new Font("Arial", Font.BOLD, 30);
		g2d.setFont(font);
		// Find the widest line
		int maxIdx = 0;
		for (int i = 0, maxWidth = 0; i < messages.length; i++)
		{
			int width = GraphUtil.getStringWidthInPixel(g2d, messages[i]);
			if (width > maxWidth)
			{
				maxWidth = width;
				maxIdx = i;
			}
		}
		int maxMsgWidth = 3 * imageWidth / 4;
		int msgWidth = GraphUtil.adjustFontSizeToTextWidth(g2d, messages[maxIdx], maxMsgWidth);
		int fontHeight = GraphUtil.getStringHeightInPixel(g2d, messages[maxIdx]);

		// we center the Strings
		int xPos = (imageWidth - msgWidth) / 2;
		int yPos = (imageHeight - messages.length * fontHeight) / 2 + fontHeight;

		// Border around the text
		g2d.setColor(new Color(255, 192, 128));
		int inset = 10;
		Stroke stk = g2d.getStroke();
		g2d.setStroke(new BasicStroke(3));
		g2d.drawRect(xPos - inset, yPos - fontHeight - inset,
				msgWidth + 2 * inset, fontHeight * messages.length + 2 * inset);
		g2d.setStroke(stk);

		// the message will be written in red
		g2d.setColor(new Color(192, 0, 0));
		for (int i = 0; i < messages.length; i++)
		{
			g2d.drawString(messages[i], xPos, yPos + fontHeight * i);
		}
	}

	/**
	 * Draws the given string using the given font and returns the text position.
	 */
	public static int drawString(Graphics2D g2d, Font f, String textToDisplay, int x, int y)
	{
		assert g2d != null && f != null && textToDisplay != null : "Invalid parameter, g2D: " + g2d + ", f: " + f + ", s: " + textToDisplay;
		g2d.setFont(f);
		g2d.drawString(textToDisplay, x, y);
		return x + getStringWidthInPixel(g2d, textToDisplay);
	}
}
