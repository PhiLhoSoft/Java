import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


public class TableTester extends JPanel {
    public TableTester() {
        super(new GridLayout(1,0));

        final String[] columnNames = {"First Name",
                                      "Last Name",
                                      "Sport",
                                      "# of Years",
                                      "Vegetarian"};

        final Object[][] data = {
            {"Tom",   "Roberts","Athletic", new Integer(5),  new Boolean(false)},
            {"Sarah", "Watt",   "Football", new Integer(3),  new Boolean(true)},
            {"Laura", "Brown",  "Swimming", new Integer(2),  new Boolean(false)},
            {"Simon", "Smith",  "Tennis",   new Integer(20), new Boolean(true)},
            {"Paul",  "Jones",  "Rugby",    new Integer(10), new Boolean(false)},
            {"Paul 1",  "Jones",  "Rugby",    new Integer(10), new Boolean(false)},
            {"Paul 2",  "Jones",  "Rugby",    new Integer(10), new Boolean(false)},
            {"Paul 3",  "Jones",  "Rugby",    new Integer(10), new Boolean(false)},
            {"Paul 4",  "Jones",  "Rugby",    new Integer(10), new Boolean(false)}
       };

        JTable table = new SpecialTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 100));
/*
        table.addMouseListener(new MouseListener(){
            public void mouseEntered(MouseEvent me){}
            public void mouseExited(MouseEvent me){}
            public void mouseReleased(MouseEvent me){}
            public void mouseClicked(MouseEvent me){}
            public void mousePressed(MouseEvent me){
                if (me.isControlDown()){
                    System.out.println("This is working ");
                }
            }
        });
*/
        InputMap inputMap = table.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK);
        inputMap.put(keyStroke, "none");

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }

    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("TableTester");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        TableTester newContentPane = new TableTester();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

class SpecialTable extends JTable
{
    SpecialTable(Object[][] data, String[] columnNames)
    {
        super(data, columnNames);
// That's already the default        
//        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }
    
    /**
     * Called by javax.swing.plaf.basic.BasicTableUI.Handler.adjustSelection(MouseEvent)
     * like: table.changeSelection(pressedRow, pressedCol, e.isControlDown(), e.isShiftDown());
     */
    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend)
    {
        if (toggle && !isRowSelected(rowIndex))
            return; // Don't do the selection
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
    }
}
