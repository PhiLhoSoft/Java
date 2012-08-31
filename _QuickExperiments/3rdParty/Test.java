import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Test extends JPanel implements ActionListener {

    private final int DELAY = 2000;

    private Timer timer;
    private boolean bShowOval;

    /*
     * constructor
     */
    public Test() {

        setFocusable(true);
        initGame();
    }

    /*
     * initialize board
     */
    public void initGame() {

        timer = new Timer(DELAY, this);
        timer.start();

    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (!bShowOval) return;

        g.setColor(Color.gray);
        // draw an oval starting at 20,20 with a width and height of 100 and
        // fill it
        g.drawOval(20, 20, 100, 100);
        g.fillOval(20, 20, 100, 100);

//~         g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bShowOval = !bShowOval;
        repaint();
    }

  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
     public void run()
     {
      JFrame demoFrame = new JFrame("Test");
      demoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      demoFrame.setSize(500, 500);

      demoFrame.add(new Test());

//~       demoFrame.pack();
      demoFrame.setVisible(true);
     }
    });
  }
}
