import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

//import net.miginfocom.layout.*;
import net.miginfocom.swing.MigLayout;

// Based on code found a thttp://wiki.java.net/bin/view/Javadesktop/MigLayout
// See also http://www.miglayout.com/ and http://www.migcalendar.com/miglayout/javadoc/index.html

// javac -cp C:\Java\libraries\miglayout-3.7.2-swing.jar TestingMiGLayout.java
// java -cp .;C:\Java\libraries\miglayout-3.7.2-swing.jar TestingMiGLayout

public class TestingMiGLayout extends JFrame
{
	public TestingMiGLayout()
	{
		super("Testing MiG Layout");

		String[] population =
		{
			"Bunny, Bugs",
			"Cat, Sylvester",
			"Coyote, Wile E.",
			"Devil, Tasmanian",
			"Duck, Daffy",
			"Fudd, Elmer",
			"Le Pew, Pepé",
			"Martian, Marvin",
			"Mouse, Mickey",
			"Canary, Twitty",
			"--- Showing scrollbars",
			"Bunny, Bugs",
			"Cat, Sylvester",
			"Coyote, Wile E.",
			"Devil, Tasmanian",
			"Duck, Daffy",
			"Fudd, Elmer",
			"Le Pew, Pepé",
			"Martian, Marvin",
			"Mouse, Mickey",
			"Canary, Twitty",
		};

		// First column (list) grows and its components grow as well.
		// Second column (labels) is left-aligned
		// Third column (text fields) grows and the fields grow as well
		// Last column (labels & text fields) grows, and let the fields to choose their behavior
		JPanel p = new JPanel(new MigLayout("", "[grow,fill] 15 [right][grow,fill] 15 [grow]"));

		JScrollPane listScrollPane = new JScrollPane(new JList(population));

		// Fills up vertically the whole layout.
		// Grows (vertically and horizontally) with layout.
		// Minimum width 150 pixels.
		p.add(listScrollPane,            "spany, grow, wmin 150");
		p.add(new JLabel("Last name"));
		p.add(new JTextField());
		p.add(new JLabel("First name"),  "split");         // split here puts the next components in the same cell
		p.add(new JTextField(),          "growx, wrap");   // grow horizontally, tell next component to go on new row
		p.add(new JLabel("Phone"));
		p.add(new JTextField());
		p.add(new JLabel("E-mail"),      "split");         // same as First name
		p.add(new JTextField(),          "growx, wrap");
		p.add(new JLabel("Address 1"));
		p.add(new JTextField(),          "span");          // span merges cells up to the end of the row (so marks end of row)
		p.add(new JLabel("Address 2"));
		p.add(new JTextField(),          "span");
		p.add(new JLabel("City"));
		p.add(new JTextField(),          "wrap");          // wrap continues on next line
		p.add(new JLabel("State"));
		p.add(new JTextField());
		p.add(new JLabel("Postal code"), "split");         // same as First name
		p.add(new JTextField(),          "growx, wrap");
		p.add(new JLabel("Country"));
		p.add(new JTextField(),          "wrap 30");       // end of row, skip 30 pixels vertically

		// Merges cells to the end of row; re-split to each component (so no alignment with above); keep next to the list
		p.add(new JButton("New"),        "span, split, align left");
		p.add(new JButton("Delete"));
		p.add(new JButton("Edit"));
		p.add(new JButton("Save"));
		p.add(new JButton("Cancel"),     "wrap push");     // end of row, make remaining space to grow

		Container contentPane = getContentPane();
		contentPane.add(p, BorderLayout.CENTER);
	}

	public static void main(String[] args)
	{
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame display = new TestingMiGLayout();
				display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				display.pack();
				display.setVisible(true);
			}
		});
	}
}
