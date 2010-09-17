import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

// Inspired by http://www.java2s.com/Code/Java/Swing-JFC/TableDemoAppletTableCellEditor.htm
public class TableEditorTest
{
	private static final String[] HEADERS = new String[] { "Country", "Company" };
	private static final Object[][] DATA = new String[][]
	{
		{ "USA", "Microsoft" },
		{ "France", "Ubisoft" },
		{ "Japan", "Nintendo" },
		{ "France", "Ankama" },
		{ "USA", "EA" },
		{ "Japan", "Atari" },
	};

	private JLabel currentValue = new JLabel("-");
	private JTable table;

	public TableEditorTest()
	{
	}

	private void CreateGUI(Container contentPane)
	{
		table = new JTable(DATA, HEADERS);
		CountryCellEditor cce = new CountryCellEditor();
		table.getColumnModel().getColumn(0).setCellEditor(cce);
		TableCellEditor tce = new TextFieldCellEditor();
		table.getColumnModel().getColumn(1).setCellEditor(tce);

		ListSelectionModel listSelectionModel = table.getSelectionModel();
		listSelectionModel.addListSelectionListener(new SelectionHandler(this));

		table.setPreferredScrollableViewportSize(new Dimension(400, 100));
		JScrollPane scrollPane = new JScrollPane(table);

		contentPane.setLayout(new java.awt.FlowLayout());
		contentPane.add(scrollPane);
		contentPane.add(currentValue);
	}

	public void ShowSelection()
	{
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();
		Object value = table.getValueAt(row, col);
		System.out.println(value);
	}

	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				TableEditorTest tet = new TableEditorTest();
				tet.CreateGUI(frame.getContentPane());
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}

// http://www.exampledepot.com/egs/javax.swing.table/CustEdit.html
class TextFieldCellEditor extends AbstractCellEditor implements TableCellEditor
{
	// Component that will handle the editing of the cell value
	JTextField component = new JTextField();
	// Called when a cell value is edited by the user
	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int rowIndex, int colIndex)
	{
		// 'value' is value contained in the cell located at (rowIndex, colIndex)
		if (isSelected)
		{
			// cell (and perhaps other cells) are selected
		}
		// Configure the component with the specified value
		component.setText((String) value);
		// Return the configured component
		return component;
	}

	// This method is called when editing is completed.
	// It must return the new value to be stored in the cell.
	public Object getCellEditorValue()
	{
		return component.getText();
	}
}

class CountryCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private static final String[] COUNTRIES = new String[]
	{
		"France", "USA", "Japan", "Spain"
	};
	JComboBox countryComboBox;

	public CountryCellEditor()
	{
		countryComboBox = new JComboBox(COUNTRIES);
		countryComboBox.setEditable(true);
	}

	public Component getTableCellEditorComponent(JTable table,
			Object value, boolean isSelected, int rowIndex, int colIndex)
	{
		countryComboBox.setSelectedItem(value);
		return countryComboBox;
	}

	public Object getCellEditorValue()
	{
		return countryComboBox.getSelectedItem();
	}
}

class SelectionHandler implements ListSelectionListener
{
	TableEditorTest mainTet;

	public SelectionHandler(TableEditorTest tet)
	{
		mainTet = tet;
	}

	public void valueChanged(ListSelectionEvent e)
	{
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();

		int firstIndex = e.getFirstIndex();
		int lastIndex = e.getLastIndex();
		boolean isAdjusting = e.getValueIsAdjusting();
		System.out.print("Event for indexes " + firstIndex + " - " + lastIndex +
			"; isAdjusting is " + isAdjusting + "; selected indexes:");

		if (lsm.isSelectionEmpty())
		{
			System.out.print(" <none>");
		}
		else
		{
			// Find out which indexes are selected.
			int minIndex = lsm.getMinSelectionIndex();
			int maxIndex = lsm.getMaxSelectionIndex();
			for (int i = minIndex; i <= maxIndex; i++)
			{
				if (lsm.isSelectedIndex(i))
				{
					System.out.print(" " + i);
				}
			}
		}
		System.out.println();
		mainTet.ShowSelection();
	}
}
