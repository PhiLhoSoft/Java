import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.*;

// Code found at http://www.ibm.com/developerworks/java/library/j-mer0918/

@SuppressWarnings("serial")
public class CompositeIt extends JFrame
{
	private JSlider sourcePercentage = new JSlider();
	private JSlider destinationPercentage = new JSlider();
	private JComboBox<String> alphaComposites = new JComboBox<>();
	private DrawingPanel drawingPanel = new DrawingPanel();

	private static final String rules[] =
	{
		"CLEAR",
		"DST",
		"DST_ATOP",
		"DST_IN",
		"DST_OUT",
		"DST_OVER",
		"SRC",
		"SRC_ATOP",
		"SRC_IN",
		"SRC_OUT",
		"SRC_OVER",
		"XOR"
	};

	public CompositeIt()
	{
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout(10, 10));

		Dictionary<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		labels.put(Integer.valueOf(0),   new JLabel("0.00"));
		labels.put(Integer.valueOf(25),  new JLabel("0.25"));
		labels.put(Integer.valueOf(33),  new JLabel("0.33"));
		labels.put(Integer.valueOf(50),  new JLabel("0.50"));
		labels.put(Integer.valueOf(67),  new JLabel("0.67"));
		labels.put(Integer.valueOf(75),  new JLabel("0.75"));
		labels.put(Integer.valueOf(100), new JLabel("1.00"));

		sourcePercentage.setOrientation(SwingConstants.VERTICAL);
		sourcePercentage.setLabelTable(labels);
		sourcePercentage.setPaintTicks(true);
		sourcePercentage.setPaintLabels(true);
		sourcePercentage.setValue(100);
		sourcePercentage.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				int sourceValue = sourcePercentage.getValue();
				drawingPanel.setSourcePercentage(sourceValue / 100.0f);
			}
		});

		destinationPercentage.setOrientation(SwingConstants.VERTICAL);
		destinationPercentage.setLabelTable(labels);
		destinationPercentage.setPaintTicks(true);
		destinationPercentage.setPaintLabels(true);
		destinationPercentage.setValue(100);
		destinationPercentage.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent e)
			{
				int destinationValue = destinationPercentage.getValue();
				drawingPanel.setDestinationPercentage(destinationValue / 100.0f);
			}
		});

		ComboBoxModel<String> model = new DefaultComboBoxModel<>(rules);
		alphaComposites.setModel(model);
		alphaComposites.setSelectedItem("XOR");
		alphaComposites.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String alphaValue = alphaComposites.getSelectedItem().toString();
				Class<AlphaComposite> alphaClass = AlphaComposite.class;
				try
				{
					Field field = alphaClass.getDeclaredField(alphaValue);
					int rule = ((Integer) field.get(AlphaComposite.Clear)).intValue();
					drawingPanel.setCompositeRule(rule);
				}
				catch (Exception exception)
				{
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

	private static class DrawingPanel extends JPanel
	{
		private static final int SIZE = 200;

		private GeneralPath sourcePath, destPath;
		private BufferedImage source, dest;
		private float sourcePercentage = 1, destinationPercentage = 1;
		private int compositeRule = AlphaComposite.XOR;
		private Dimension dimension = new Dimension(SIZE, SIZE);

		public DrawingPanel()
		{
			sourcePath = new GeneralPath();
			sourcePath.moveTo(0, 0);
			sourcePath.lineTo(SIZE * 0.75, 0);
			sourcePath.lineTo(0, SIZE);
			sourcePath.closePath();

			source = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);

			destPath = new GeneralPath();
			destPath.moveTo(SIZE, 0);
			destPath.lineTo(SIZE * 0.25, 0);
			destPath.lineTo(SIZE, SIZE);
			destPath.closePath();

			dest = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
		}

		public void setSourcePercentage(float value)
		{
			sourcePercentage = value;
			repaint();
		}

		public void setDestinationPercentage(float value)
		{
			destinationPercentage = value;
			repaint();
		}

		public void setCompositeRule(int value)
		{
			compositeRule = value;
			repaint();
		}

		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			Dimension d = getSize();
			int posX = (d.width - SIZE) / 2;
			int posY = (d.height - SIZE) / 2;

			Graphics2D g2d = (Graphics2D) g;
			g2d.translate(posX, posY);
			g2d.setColor(Color.GRAY);
			g2d.drawRect(0, 0, SIZE, SIZE);
			for (int i = 0; i < SIZE / 10; i++)
			{
				g2d.drawLine(i * 10, 0, 0, i * 10);
				g2d.drawLine(i * 10, SIZE, SIZE, i * 10);
			}

			Graphics2D sourceG = source.createGraphics();
			Graphics2D destG = dest.createGraphics();

			destG.setComposite(AlphaComposite.Clear);
			destG.fillRect(0, 0, SIZE, SIZE);
			destG.setComposite(AlphaComposite.getInstance(AlphaComposite.XOR, destinationPercentage));
			destG.setPaint(Color.magenta);
			destG.fill(destPath);

			sourceG.setComposite(AlphaComposite.Clear);
			sourceG.fillRect(0, 0, SIZE, SIZE);
			sourceG.setComposite(AlphaComposite.getInstance(AlphaComposite.XOR, sourcePercentage));
			sourceG.setPaint(Color.green);
			sourceG.fill(sourcePath);

			destG.setComposite(AlphaComposite.getInstance(compositeRule));
			destG.drawImage(source, 0, 0, null);

			g2d.drawImage(dest, 0, 0, this);
		}

		@Override
		public Dimension getPreferredSize()
		{
			return dimension;
		}
	}

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame compositeFrame = new CompositeIt();
				compositeFrame.setTitle("Porter-Duff Composite Operations");
				compositeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				compositeFrame.setSize(400, 400);
				compositeFrame.setVisible(true);
			}
		});
	}
}
