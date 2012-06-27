/*
 * Tests: A collection of little test programs to explore Java language.
 *
 * Since Java 7, long strings in JTextPane are no longer wrapped automatically.
 * Now, we have to explicitly tell the pane to wrap on any character.
 *
 * Based on http://www.experts-exchange.com/Programming/Languages/Java/Q_20393892.html
 * See also http://java-sl.com/src/wrap_src.html
 * See also http://stackoverflow.com/questions/8666727/wrap-long-words-in-jtextpane-java-7
 * which links to http://bugs.sun.com/view_bug.do?bug_id=6539700
 */
/* File history:
 *  1.00.000 -- 2012/06/26 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
 */
package org.philhosoft.tests.ui;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.BreakIterator;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.*;

public class WrapTextPane
{
	static final String WRAP_MODE_ATTRIBUTE_NAME = "WrappingMode";
	static final String ATTRIBUTE_CHAR = "Char";
	static final String ATTRIBUTE_WORD = "Word";

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame demoFrame = new WrapTextPaneDemo();
				demoFrame.setTitle("Demo of wrapping a text pane");
				demoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				demoFrame.setSize(400, 300);
				demoFrame.setVisible(true);
			}
		});
	}
}

@SuppressWarnings("serial")
class WrapTextPaneDemo extends JFrame
{
	Debug debug;
	
	WrapTextPaneDemo()
	{
		debug = new Debug(this);
		addComponentListener(debug);
		
		Container cp = getContentPane();
		JTextPane tp = new JTextPane();

		// Character wrap
		SimpleAttributeSet cw = new SimpleAttributeSet();
		SimpleAttributeSet cws = new SimpleAttributeSet();
		// Word wrap
		SimpleAttributeSet ww = new SimpleAttributeSet();

		tp.setEditorKit(new CustomEditorKit());

		cws.addAttribute(WrapTextPane.WRAP_MODE_ATTRIBUTE_NAME, WrapTextPane.ATTRIBUTE_CHAR);
		
		StyleConstants.setFontFamily(cw, "monospaced");
		StyleConstants.setFontSize(cw, 18);
		StyleConstants.setAlignment(cw, StyleConstants.ALIGN_LEFT);
		cw.addAttribute(WrapTextPane.WRAP_MODE_ATTRIBUTE_NAME, WrapTextPane.ATTRIBUTE_CHAR);

		StyleConstants.setFontFamily(ww, "sansserif");
		StyleConstants.setFontSize(ww, 18);
		StyleConstants.setAlignment(ww, StyleConstants.ALIGN_LEFT);
		// Actually keep the default behavior
		ww.addAttribute(WrapTextPane.WRAP_MODE_ATTRIBUTE_NAME, WrapTextPane.ATTRIBUTE_WORD);

		Document doc = tp.getDocument();
		try
		{
			doc.insertString(doc.getLength(), "This text must be a character wrapped text aligned with the left margin. " +
					"It can have VeryLongRunningWordsWithoutNaturalBreak.\n", cws);

			doc.insertString(doc.getLength(), "This text must be a word wrapped text aligned with the left margin.\nIt can have VeryLongRunningWordsWithoutNaturalBreak too.\n", ww);
			doc.insertString(doc.getLength(), "Java version: " + System.getProperty("java.version") +
					"\nJava home: " + System.getProperty("java.home") +
					"\nJava vendor: " + System.getProperty("java.vendor") +
					"\n", null);
		}
		catch (BadLocationException ble) {}

		cp.add(tp);
	}
	
	private static class Debug extends ComponentAdapter
	{
		JFrame watchee;
		
		Debug(JFrame w)
		{
			watchee = w;
		}
		
		@Override
		public void componentResized(ComponentEvent e)
		{
//			Dimension newSize = getSize();
			Rectangle bounds = watchee.getBounds();
			System.out.println("Resize to " + bounds.width + ", " + bounds.height);
		}
	}
}

@SuppressWarnings("serial")
class CustomEditorKit extends StyledEditorKit
{
	private static final ViewFactory custom = new CustomViewFactory();

	@Override
	public ViewFactory getViewFactory()
	{
		return custom;
	}

	private static final class CustomViewFactory implements ViewFactory
	{
		@Override
		public View create(Element e)
		{
			String kind = e.getName();

System.out.println(kind);
			if (kind == null)
				return new LabelView(e); // Default text display
			if (kind.equals(AbstractDocument.ContentElementName))
			{
				String attr = (String) e.getAttributes().getAttribute(WrapTextPane.WRAP_MODE_ATTRIBUTE_NAME);
				if (attr == null)
					return new LabelView(e);
				return attr.equals(WrapTextPane.ATTRIBUTE_CHAR) ? new CharWrappingView(e) : new LabelView(e);
			}

			if (kind.equals(AbstractDocument.ParagraphElementName))
				return new ParagraphView(e);
			if (kind.equals(StyleConstants.ComponentElementName))
				return new ComponentView(e);
			if (kind.equals(AbstractDocument.SectionElementName))
				return new BoxView(e, View.Y_AXIS);
			if (kind.equals(StyleConstants.IconElementName))
				return new IconView(e);
			return new LabelView(e);
		}

		/**
		 * A LabelView forcing wrapping at character bounds.
		 */
		private static final class CharWrappingView extends LabelView
		{
			CharWrappingView(Element e) { super(e); }
/*
			// Probably not necessary, default is enough
			@Override
			public int getBreakWeight(int axis, float pos, float len)
			{
				if (axis == View.X_AXIS)
				{
					checkPainter();
					int p0 = getStartOffset();
					int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
					if (p1 == p0)
					{
						// can't even fit a single character
						return View.BadBreakWeight;
					}
System.out.println("getBreakWeight " + p0 + " " + p1 + " for " + pos + " " + len);
					return View.ForcedBreakWeight;
				}
				return super.getBreakWeight(axis, pos, len);
			}
//*/

			@Override
			public View breakView(int axis, int p0, float pos, float len)
			{
				if (axis == View.X_AXIS)
				{
System.out.println("breakView " + pos + " " + len);
					int p1 = getGlyphPainter().getBoundedPosition(this, p0, pos, len);
					CharWrappingView v = (CharWrappingView) createFragment(p0, p1);
System.out.println(p0 + " " + p1);
					return v;
				}

				return super.breakView(axis, p0, pos, len);
			}
			
			// It doesn't solves completely the issue: when the window size goes below the
			// length of the longest word, it no longer wraps at char bounds.
			// Apparently, there is a minimum len value, it won't query breakView below this value.
			// I think we should rewrite getMinimumSpan to call a custom getBreakSpot to call a custom
			// getBreaker to provide a custom BreakIterator. All this because these methods are private
			// and there is no way to inject a custom BreakIterator.
			
			// Actually, for simple cases, there is a simple workaround: loop over each character of the
			// string to set, inserting them in the document, setting alternate arbitrary styles.
			// This creates segments of char size, breakable.
		}
	}
}
