import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class ComboBoxTest extends JFrame
{
  private static final long serialVersionUID = 1L;
  private JComboBox comboBox;
//~     private ImageIcon infoIcon = (ImageIcon) UIManager.getIcon("OptionPane.informationIcon");
//~     private ImageIcon warnIcon = (ImageIcon) UIManager.getIcon("OptionPane.warningIcon");

  ComboBoxTest()
  {
    Label lab = new Label("Choose one item");
    String[] items = { "Abba", "Antibe", "Manix", "Munroe", "Mycom", "Zulu" };
    comboBox = new JComboBox(items);
    comboBox.setFont(new Font("Dialog", Font.PLAIN, 10));
    comboBox.addItemListener(new ItemListener()
    {
      public void itemStateChanged(ItemEvent e)
      {
        if (e.getStateChange() == ItemEvent.SELECTED)
        {
          System.out.println("Item selected: " + e.getItem());
        }
      }
    });
    comboBox.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        JComboBox cb = (JComboBox) e.getSource();
        System.out.println("Action: " + e.getActionCommand() + " -> " + cb.getSelectedItem());
      }
    });
    comboBox.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        System.out.print("Key pressed: ");
        JComboBox cb = (JComboBox) e.getSource();
        char ch = e.getKeyChar();
        // If not a printable character, return
        if (ch != KeyEvent.CHAR_UNDEFINED)
        {
          if (ch == '\n') ch = '§';
          System.out.println(ch + " " + cb.getSelectedItem());
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN)
        {
          System.out.println("Up or Down");
        }
        else
        {
          System.out.println(e);
        }
      }
    });
    for (int i = 0; i < comboBox.getComponentCount(); i++)
    {
      Component component = comboBox.getComponent(i);
          System.out.println(component);
      if (component instanceof CellRendererPane || component instanceof javax.swing.plaf.basic.ComboPopup)
      {
        System.out.println("Adding listener to " + component);
        component.addMouseListener(new MouseAdapter()
        {
          @Override
          public void mousePressed(MouseEvent e)
          {
            JComboBox cb = (JComboBox) e.getSource();
            System.out.println("Cell click: " + e + " " + cb.getSelectedItem());
          }
        });
      }
    }
    comboBox.getEditor().getEditorComponent().addMouseListener(
        new MouseAdapter()
        {
          @Override
          public void mousePressed(MouseEvent e)
          {
            JComboBox cb = (JComboBox) e.getSource();
            System.out.println("Editor click: " + e + " " + cb.getSelectedItem());
          }
        }
    );
    comboBox.addPopupMenuListener(new PopupMenuListener()
    {
      @Override public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
      {
        JComboBox cb = (JComboBox) e.getSource();
        System.out.println("Popup hides: " + e + " " + cb.getSelectedItem());
      }
      @Override public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
      @Override public void popupMenuCanceled(PopupMenuEvent e) {}
    });


    Container c = getContentPane();
    c.setLayout(new FlowLayout());
    c.add(lab);
    c.add(comboBox);
//~         comboBox.setUI(new MyUI());
  }

  public static void main(String[] args)
  {
    try
    {
      // Set cross-platform Java L&F (also called "Metal")
      UIManager.setLookAndFeel(
          UIManager.getSystemLookAndFeelClassName());
//~           UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch (UnsupportedLookAndFeelException e)
    {
      System.err.println(e.getMessage());
    }
    catch (ClassNotFoundException e)
    {
      System.err.println(e.getMessage());
    }
    catch (InstantiationException e)
    {
      System.err.println(e.getMessage());
    }
    catch (IllegalAccessException e)
    {
      System.err.println(e.getMessage());
    }


    ComboBoxTest frame = new ComboBoxTest();
    frame.setTitle("ComboBox Test");
    frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    frame.setDefaultLookAndFeelDecorated(true);
    frame.setLocationRelativeTo(null); // Center on screen
    frame.pack();
    frame.setVisible(true);
  }

//~     class MyUI extends javax.swing.plaf.basic.BasicComboBoxUI {

//~         @Override
//~         protected JButton createArrowButton() {
//~             JButton btn = new JButton();
//~             btn.setIcon(infoIcon);
//~             btn.setRolloverIcon(warnIcon);
//~             return btn;
//~         }
//~     }
}
