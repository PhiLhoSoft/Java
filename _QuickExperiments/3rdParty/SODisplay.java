import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class SODisplay extends JFrame {
    final int FRAME_WIDTH = 820;
    final int FRAME_HEIGHT = 700;
    final int X_OFFSET = 40;
    final int Y_OFFSET = 40;

    JButton submit;
    JTextField top;
    JTextField bottom;
    JTextField numPoint;
    JPanel bpanel;
    JPanel points;
    GridPanel grid;

    int maxPoints;

    public SODisplay() {
        init();
    }

    public void init() {
        setBackground(Color.WHITE);
        setLocation(X_OFFSET, Y_OFFSET);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle("Geometric Transformations");
        getContentPane().setLayout(null);
        setDefaultLookAndFeelDecorated(true);

        grid = new GridPanel();
        grid.setBounds(0,0,530,FRAME_HEIGHT);
        this.add(grid);

        top = new JTextField();    // parameter is size of input characters
        top.setText("1 2 3");
        top.setBounds(590, 150, 120, 25);

        bottom = new JTextField();    // parameter is size of input characters
        bottom.setText("5 6 7");
        bottom.setBounds(590, 200, 120, 25);

        numPoint = new JTextField();
        numPoint.setText("Number of Points?");
        numPoint.setBounds(550,200,200,25);
        this.add(numPoint);

        SubmitButton submit = new SubmitButton("Submit");
        submit.setBounds(570, 250, 170, 25);

        bpanel = new JPanel(new GridLayout(2,3));
        bpanel.add(top);
        bpanel.add(bottom);
        bpanel.add(submit);

        points = new JPanel(new GridLayout(2,2));
        points.setBounds(540,250,265,60);
        this.add(points);

        bpanel.setBounds(550,100,200,70);
        this.add(bpanel, BorderLayout.LINE_START);

        Component[] a = points.getComponents();
        System.out.println(a.length);
        repaint();
    }

    class SubmitButton extends JButton implements ActionListener {

        public SubmitButton(String title){
            super(title);
            addActionListener(this);
            this.setVisible(true);
        }
        public void actionPerformed(ActionEvent e) {
            maxPoints = Integer.parseInt(numPoint.getText()) * 2;

            points.removeAll();

            for (int i=0; i<maxPoints; i++) {
                JTextField textField = new JTextField();
                points.add(textField);
            }
            points.validate();        // necessary when adding components to a JPanel
            points.repaint();
            // http://stackoverflow.com/questions/369823/java-gui-repaint-problem-solved
            // What to Check:
            // Things between commas are either spaces (which will be stripped later)
            // or numbers!

            // Pairs must match up!
        }
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                SODisplay display = new SODisplay();
                display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                display.setVisible(true);
            }
        });
    }

    class GridPanel extends JPanel {
        final int GRAPH_OFFSETX = 35;
        final int GRAPH_OFFSETY = 60;
        final int GRAPH_WIDTH = 500;
        final int GRAPH_HEIGHT = 500;
        final int GRAPH_INTERVAL = 20;

        public GridPanel() {
        }

        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(Color.WHITE);
            g.fillRect(100, 100, 20, 30);
            g.setColor(Color.BLACK);
            genGraph(g, GRAPH_OFFSETX, GRAPH_OFFSETY, GRAPH_WIDTH, GRAPH_HEIGHT, GRAPH_INTERVAL);
        }

        public void genGraph (Graphics g, int x, int y, int width, int height, int interval) {
            // draw background
            int border = 5;
            g.setColor(Color.BLACK);
            width = width - (width % interval);
            height = height - (height % interval);
            for (int col=x; col <= x+width; col+=interval) {
                g.drawLine(col, y, col, y+height);
            }
            for (int row=y; row <= y+height; row+=interval) {
                g.drawLine(x, row, x+width, row);
            }
        }
    }
}
