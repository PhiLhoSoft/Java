import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

public class GradientPaintDemo extends JFrame
{
  public GradientPaintDemo()
  {
    Container cont = getContentPane();
    cont.setLayout(new BorderLayout());
    cont.add(new PaintedPane(), BorderLayout.CENTER);
  }

  class PaintedPane extends JPanel
  {
    void init()
    {
      setBackground(Color.white);
    }

    public void paint(Graphics g)
    {
      Graphics2D g2 = (Graphics2D) g;

      // GradientPaint coordinates are relative to the Graphics' coordinates,
      // not to the coordinates of the shape to paint!

      // Gradient fitting to the panel:
      // much bigger than rectangle, so still grayish at the bottom right
      g2.setPaint(new GradientPaint(
          0, 0, Color.lightGray,
          500, 500, Color.blue));
      Rectangle r1 = new Rectangle(10, 10, 310, 310);
      g2.fill(r1);

      // Slightly bigger than rectangle
      g2.setPaint(new GradientPaint(
          30, 30, Color.lightGray,
          350, 350, Color.blue));
      Rectangle r2 = new Rectangle(30, 30, 270, 270);
      g2.fill(r2);

      // Fitting exactly
      g2.setPaint(new GradientPaint(
          50, 50, Color.lightGray,
          230, 230, Color.blue));
      Rectangle r3 = new Rectangle(50, 50, 230, 230);
      g2.fill(r3);
    }
  }

  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
     public void run()
     {
      JFrame demoFrame = new GradientPaintDemo();
      demoFrame.setTitle("Test Gradient Paint");
      demoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      demoFrame.setSize(500, 500);
      demoFrame.setVisible(true);
     }
    });
  }
}
