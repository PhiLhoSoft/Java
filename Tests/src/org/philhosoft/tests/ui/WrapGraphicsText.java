/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2008/10/27 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.ui;


import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.swing.*;


import org.philhosoft.ui.*;


@SuppressWarnings("serial")
public class WrapGraphicsText extends JFrame
{
	class TextContainer extends JPanel
	{
		private int m_width;
		private int m_height;
		private String m_text;
		private AttributedCharacterIterator m_iterator;
		private int m_start;
		private int m_end;

		public TextContainer(String text, int width, int height)
		{
			m_text = text;
			m_width = width;
			m_height = height;

			AttributedString styledText = new AttributedString(text);
			m_iterator = styledText.getIterator();
			m_start = m_iterator.getBeginIndex();
			m_end = m_iterator.getEndIndex();
		}

		public String getText()
		{
			return m_text;
		}

		public Dimension getPreferredSize()
		{
		    return new Dimension(m_width, m_height);
		}

		public void paint(Graphics g)
		{
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			FontRenderContext frc = g2.getFontRenderContext();

			LineBreakMeasurer measurer = new LineBreakMeasurer(m_iterator, frc);
			measurer.setPosition(m_start);

			float x = 0, y = 0;
			while (measurer.getPosition() < m_end)
			{
				TextLayout layout = measurer.nextLayout(m_width);

				y += layout.getAscent();
				float dx = layout.isLeftToRight() ?
						0 : m_width - layout.getAdvance();

				layout.draw(g2, x + dx, y);
				y += layout.getDescent() + layout.getLeading();
			}
		}
	}

	class RoundTextContainer extends JPanel
	{
		private int m_width;
		private int m_height;
		private String m_text;
		private AttributedCharacterIterator m_iterator;
		private int m_start;
		private int m_end;

		public RoundTextContainer(String text, int width, int height)
		{
			m_text = text;
			m_width = width;
			m_height = height;

			AttributedString styledText = new AttributedString(text);
			m_iterator = styledText.getIterator();
			m_start = m_iterator.getBeginIndex();
			m_end = m_iterator.getEndIndex();
		}

		public String getText()
		{
			return m_text;
		}

		public Dimension getPreferredSize()
		{
		    return new Dimension(m_width, m_height);
		}

		public void paint(Graphics g)
		{
			super.paintComponent(g);

			Graphics2D g2 = (Graphics2D) g;
			FontRenderContext frc = g2.getFontRenderContext();

			LineBreakMeasurer measurer = new LineBreakMeasurer(m_iterator, frc);
			measurer.setPosition(m_start);

			float y = 0;
			while (measurer.getPosition() < m_end)
			{
				double ix = Math.sqrt((m_width / 2 - y) * y);
				float x = m_width / 2.0F - (float) ix;
				int width = (int) ix * 2;

				TextLayout layout = measurer.nextLayout(width);

				y += layout.getAscent();
				float dx = layout.isLeftToRight() ?
						0 : width - layout.getAdvance();

				layout.draw(g2, x + dx, y);
				y += layout.getDescent() + layout.getLeading();
			}
		}
	}

	public WrapGraphicsText()
	{
		Container cont = getContentPane();
		cont.setLayout(new FlowLayout());
		String text = "This is a quite long string to be wrapped automatically." +
				"It can run on several lines, and it will be wrapped " +
				"nicely by Java.\n Nifty, eh?\n " +
				"But newlines are ignored, apparently.";

		TextContainer label = new TextContainer(text, 200, 200);
		cont.add(label);
		RoundTextContainer roundLabel = new RoundTextContainer(text +
				"This is ano ther quite long string to be wrap ped au to ma ti cal ly." +
				"It can run on se ve ral li nes, and it will be wrap ped " +
				"ni ce ly by Ja va. Nif ty, eh? " +
				"But new li nes are ig no red, ap pa ren tly.",
				400, 400);
		cont.add(roundLabel);
	}

	/**
	 * Start the demo.
	 *
	 * @param args   the command line arguments
	 */
	public static void main(String[] args)
	{
		// turn bold fonts off in metal
		UIManager.put("swing.boldMetal", Boolean.FALSE);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame demoFrame = new WrapGraphicsText();
				demoFrame.setTitle("Test GUI");
				demoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				demoFrame.setSize(500, 300);
				demoFrame.setVisible(true);
			}
		});
	}
}
