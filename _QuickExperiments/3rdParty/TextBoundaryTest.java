/**
   @version 1.11 2001-08-27
   @author Cay Horstmann
*/

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

/**
   This program demonstrates breaking text under
   various locales.
*/
public class TextBoundaryTest
{
   public static void main(String[] args)
   {
      JFrame frame = new TextBoundaryFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.show();
   }
}

/**
   This frame contains radio buttons to select where to break
   the text, a combo box to pick a locale, a text field area
   to enter text, and a text area to display the broken text.
*/
class TextBoundaryFrame extends JFrame
{
   public TextBoundaryFrame()
   {
      setSize(WIDTH, HEIGHT);
      setTitle("TextBoundaryTest");

      ActionListener listener = new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               updateDisplay();
            }
         };

      JPanel p = new JPanel();
      addRadioButton(p, characterRadioButton, rbGroup, listener);
      addRadioButton(p, wordRadioButton, rbGroup, listener);
      addRadioButton(p, lineRadioButton, rbGroup, listener);
      addRadioButton(p, sentenceRadioButton, rbGroup, listener);

      getContentPane().setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = GridBagConstraints.NONE;
      gbc.anchor = GridBagConstraints.EAST;
      add(new JLabel("Locale"), gbc, 0, 0, 1, 1);
      gbc.anchor = GridBagConstraints.WEST;
      add(localeCombo, gbc, 1, 0, 1, 1);
      add(p, gbc, 0, 1, 2, 1);
      gbc.fill = GridBagConstraints.BOTH;
      gbc.weighty = 100;
      add(new JScrollPane(inputText), gbc, 0, 2, 2, 1);
      add(new JScrollPane(outputText), gbc, 0, 3, 2, 1);

      locales = BreakIterator.getAvailableLocales();
      for (int i = 0; i < locales.length; i++)
         localeCombo.addItem(locales[i].getDisplayName());
      localeCombo.setSelectedItem(
         Locale.getDefault().getDisplayName());

      localeCombo.addActionListener(listener);

      inputText.setText("The quick, brown fox jump-ed\n"
        + "over the lazy \"dog.\" And then... what happened?");
      updateDisplay();
   }

   /**
      Adds a radio button to a container.
      @param p the container into which to place the button
      @param b the button
      @param g the button group
      @param listener the button listener
   */
   public void addRadioButton(Container p, JRadioButton b,
      ButtonGroup g, ActionListener listener)
   {
      b.setSelected(g.getButtonCount() == 0);
      b.addActionListener(listener);
      g.add(b);
      p.add(b);
   }

   /**
      A convenience method to add a component to given grid bag
      layout locations.
      @param c the component to add
      @param constraints the grid bag constraints to use
      @param x the x grid position
      @param y the y grid position
      @param w the grid width
      @param h the grid height
   */
   public void add(Component c, GridBagConstraints gbc,
      int x, int y, int w, int h)
   {
      gbc.gridx = x;
      gbc.gridy = y;
      gbc.gridwidth = w;
      gbc.gridheight = h;
      getContentPane().add(c, gbc);
   }

   /**
      Updates the display and breaks the text according
      to the user settings.
   */
   public void updateDisplay()
   {
      Locale currentLocale = locales[
         localeCombo.getSelectedIndex()];
      BreakIterator currentBreakIterator = null;
      if (characterRadioButton.isSelected())
         currentBreakIterator
            = BreakIterator.getCharacterInstance(currentLocale);
      else if (wordRadioButton.isSelected())
         currentBreakIterator
            = BreakIterator.getWordInstance(currentLocale);
      else if (lineRadioButton.isSelected())
         currentBreakIterator
            = BreakIterator.getLineInstance(currentLocale);
      else if (sentenceRadioButton.isSelected())
         currentBreakIterator
            = BreakIterator.getSentenceInstance(currentLocale);

      String text = inputText.getText();
      currentBreakIterator.setText(text);
      outputText.setText("");

      int from = currentBreakIterator.first();
      int to;
      while ((to = currentBreakIterator.next()) !=
         BreakIterator.DONE)
      {
         outputText.append(text.substring(from, to) + "|");
         from = to;
      }
      outputText.append(text.substring(from));
   }

   private Locale[] locales;
   private BreakIterator currentBreakIterator;

   private JComboBox localeCombo = new JComboBox();
   private JTextArea inputText = new JTextArea(6, 40);
   private JTextArea outputText = new JTextArea(6, 40);
   private ButtonGroup rbGroup = new ButtonGroup();
   private JRadioButton characterRadioButton
      = new JRadioButton("Character");
   private JRadioButton wordRadioButton
      = new JRadioButton("Word");
   private JRadioButton lineRadioButton
      = new JRadioButton("Line");
   private JRadioButton sentenceRadioButton
      = new JRadioButton("Sentence");
   private static final int WIDTH = 400;
   private static final int HEIGHT = 400;
}










