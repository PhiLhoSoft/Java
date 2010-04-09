import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GradientPaintDemo extends JPanel {
  public void init() {
    setBackground(Color.white);
  }

  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setPaint(new GradientPaint(0, 0, Color.lightGray, 200,
        200, Color.blue, false));
    Rectangle r = new Rectangle(5, 5, 200, 200);
    g2.fill(r);
  }

  public static void main(String s[]) {
    JFrame f = new JFrame();
    f.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    GradientPaintDemo p = new GradientPaintDemo();
    f.getContentPane().add("Center", p);
    p.init();
    f.pack();
    f.setSize(new Dimension(250, 250));
    f.show();
  }
}
