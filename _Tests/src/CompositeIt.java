import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

public class CompositeIt extends JFrame {
  JSlider sourcePercentage = new JSlider();
  JSlider destinationPercentage = new JSlider();
  JComboBox alphaComposites = new JComboBox();
  DrawingPanel drawingPanel = new DrawingPanel();

  public CompositeIt() {
    super("Porter-Duff");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel contentPane = (JPanel) this.getContentPane();
    Dictionary labels = new Hashtable();
    labels.put(new Integer(0),   new JLabel("0.0"));
    labels.put(new Integer(25),  new JLabel("0.25"));
    labels.put(new Integer(33),  new JLabel("0.33"));
    labels.put(new Integer(50),  new JLabel("0.50"));
    labels.put(new Integer(67),  new JLabel("0.67"));
    labels.put(new Integer(75),  new JLabel("0.75"));
    labels.put(new Integer(100), new JLabel("1.00"));
    sourcePercentage.setOrientation(JSlider.VERTICAL);
    sourcePercentage.setLabelTable(labels);
    sourcePercentage.setPaintTicks(true);
    sourcePercentage.setPaintLabels(true);
    sourcePercentage.setValue(100);
    sourcePercentage.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        int sourceValue = sourcePercentage.getValue();
        drawingPanel.setSourcePercentage(sourceValue/100.0f);
      }
    });
    destinationPercentage.setOrientation(JSlider.VERTICAL);
    destinationPercentage.setLabelTable(labels);
    destinationPercentage.setPaintTicks(true);
    destinationPercentage.setPaintLabels(true);
    destinationPercentage.setValue(100);
    destinationPercentage.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        int destinationValue = destinationPercentage.getValue();
        drawingPanel.setDestinationPercentage(destinationValue/100.0f);
      }
    });
    String rules[] = {
      "CLEAR",    "DST",
      "DST_ATOP", "DST_IN",
      "DST_OUT",  "DST_OVER",
      "SRC",      "SRC_ATOP",
      "SRC_IN",   "SRC_OUT",
      "SRC_OVER", "XOR"};
    ComboBoxModel model = new DefaultComboBoxModel(rules);
    alphaComposites.setModel(model);
    alphaComposites.setSelectedItem("XOR");
    alphaComposites.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String alphaValue = alphaComposites.getSelectedItem().toString();
        Class alphaClass = AlphaComposite.class;
        try {
          Field field = alphaClass.getDeclaredField(alphaValue);
          int rule = ((Integer)field.get(AlphaComposite.Clear)).intValue();
          drawingPanel.setCompositeRule(rule);
        } catch (Exception exception) {
          System.err.println("Unable to find field");
        }
      }
    });
    contentPane.add(sourcePercentage, BorderLayout.WEST);
    contentPane.add(destinationPercentage, BorderLayout.EAST);
    contentPane.add(alphaComposites, BorderLayout.SOUTH);
    contentPane.add(drawingPanel, BorderLayout.CENTER);
    pack();
  }

  public static void main(String args[]) {
    new CompositeIt().show();
  }

  class DrawingPanel extends JPanel {
    GeneralPath sourcePath, destPath;
    BufferedImage source, dest;
    float sourcePercentage = 1, destinationPercentage = 1;
    int compositeRule = AlphaComposite.XOR;
    Dimension dimension = new Dimension(200, 200);

    public DrawingPanel() {
      sourcePath = new GeneralPath();
      sourcePath.moveTo(0,   0);   sourcePath.lineTo(150, 0);
      sourcePath.lineTo(0, 200);   sourcePath.closePath();
      source = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
      destPath = new GeneralPath();
      destPath.moveTo(200,  0);    destPath.lineTo(50, 0);
      destPath.lineTo(200, 200);    destPath.closePath();
      dest = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
    }

    public void setSourcePercentage(float value) {
      sourcePercentage = value;
       repaint();
    }

    public void setDestinationPercentage(float value) {
      destinationPercentage = value;
       repaint();
    }

    public void setCompositeRule(int value) {
      compositeRule = value;
       repaint();
    }

    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      Graphics2D sourceG = source.createGraphics();
      Graphics2D destG = dest.createGraphics();

      destG.setComposite(AlphaComposite.Clear);
      destG.fillRect(0, 0, 200, 200);
      destG.setComposite(AlphaComposite.getInstance(
          AlphaComposite.XOR, destinationPercentage));
      destG.setPaint(Color.magenta);
      destG.fill(destPath);

      sourceG.setComposite(AlphaComposite.Clear);
      sourceG.fillRect(0, 0, 200, 200);
      sourceG.setComposite(AlphaComposite.getInstance(
        AlphaComposite.XOR, sourcePercentage));
      sourceG.setPaint(Color.green);
      sourceG.fill(sourcePath);
      destG.setComposite(AlphaComposite.getInstance(compositeRule));
      destG.drawImage(source, 0, 0, null);
      g2d.drawImage(dest, 0, 0, this);
    }

    public Dimension getPreferredSize() {
      return dimension;
    }
  }
}

