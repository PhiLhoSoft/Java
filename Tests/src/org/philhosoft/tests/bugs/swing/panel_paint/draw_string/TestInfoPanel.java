/*
 * Tests: A collection of little test programs to explore Java language.
 * Tries to recreate a Swing paint bug.
 */
/* File history:
 *  1.00.000 -- 2012/10/29 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicense.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2012 Philippe Lhoste / PhiLhoSoft
*/
import java.awt.*;

import javax.swing.*;

public class TestInfoPanel
{
  private JPanel m_panel;

  TestInfoPanel()
  {
  }

  private JScrollPane getContent()
  {
    m_panel = new JPanel(new GridLayout(0, 1));
    m_panel.add(new InfoPanel("A Title jqpgy_@,;!§",
        new Font("Arial", Font.PLAIN, 24),
        Color.RED, Color.LIGHT_GRAY));
    m_panel.add(new InfoPanel("A Sub-Title jqpgy_@,;!§",
        new Font("Tahoma", Font.PLAIN, 18),
        Color.BLUE, Color.YELLOW));
    m_panel.add(new JPanel());
    return new JScrollPane(m_panel);
  }

  public static void main(String[] args)
  {
    final TestInfoPanel tm = new TestInfoPanel();
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle(tm.getClass().getName());
        f.setContentPane(tm.getContent());
        f.setSize(800, 400);
        f.setLocation(200, 200);
        f.setVisible(true);
      }
    });
  }
}
