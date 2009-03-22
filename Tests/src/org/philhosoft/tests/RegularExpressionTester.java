package org.philhosoft.tests;

import java.awt.*;
import java.awt.event.*;

import java.util.regex.*;

import javax.swing.*;

public class RegularExpressionTester {

    public static void main (String [] args) {

        final JTextField patternField = new JTextField (12);

        final JTextField testField = new JTextField (12);

        patternField.addActionListener (new ActionListener () {
            public void actionPerformed (ActionEvent event) {
                String pattern = patternField.getText ();

                try    {
                    Pattern.compile (pattern);
                    patternField.setBackground (Color.GREEN);
                }
                catch (PatternSyntaxException exception) {
                    patternField.setBackground (Color.RED);
                }
            }
        });

        testField.addActionListener (new ActionListener () {
            public void actionPerformed (ActionEvent event) {
                String pattern = patternField.getText ();
                String string = testField.getText ();

                //Check if input matches regex String
                if (string.matches (pattern)) {
                    testField.setBackground (Color.GREEN);
                }
                else {
                    testField.setBackground (Color.RED);
                }
            }
        });

        final JFrame frame = new JFrame ("Regular Expression Tester");
        frame.addWindowListener (new WindowAdapter () {
            public void windowClosing (WindowEvent event) {
                System.exit (1);
            }
        });

        Container contentPane = frame.getContentPane ();

        contentPane.setLayout (new GridLayout (2, 2, 0, 0));
        contentPane.add (new JLabel ("Pattern "));
        contentPane.add (patternField);
        contentPane.add (new JLabel ("Test String "));
        contentPane.add (testField);
        frame.pack ();
        frame.setVisible(true);
    }
}
