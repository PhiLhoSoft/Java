/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.00.000 -- 2008/09/20 (PL) -- Creation
 */
/*
Author: Philippe Lhoste <PhiLho(a)GMX.net> http://Phi.Lho.free.fr
Copyright notice: For details, see the following file:
http://Phi.Lho.free.fr/softwares/PhiLhoSoft/PhiLhoSoftLicence.txt
This program is distributed under the zlib/libpng license.
Copyright (c) 2008 Philippe Lhoste / PhiLhoSoft
*/
package org.philhosoft.tests.image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class TestFadeToGray
{
	private FadeToGray m_ftg;
	private BufferedImage[] m_images;
	private JPanel m_panel;
	private JLabel m_icon;

	TestFadeToGray(File image) throws IOException
	{
		m_ftg = new FadeToGray(image);
	}

	private JScrollPane GetContent(BufferedImage[] images)
   {
		m_images = images;
		m_panel = new JPanel(new GridLayout(1, 0));
		m_panel.add(Wrap(m_images[0]));
		m_panel.add(Wrap(m_images[1]));
		m_icon = new JLabel(m_ftg, SwingConstants.CENTER);
		m_panel.add(m_icon);
		return new JScrollPane(m_panel);
	}

	public void UpdateImage(float amount)
	{
		m_ftg.SetOpacity(amount);
		System.out.println(amount);
		m_icon.repaint();
	}

	private JLabel Wrap(BufferedImage image)
	{
		ImageIcon icon = new ImageIcon(image);
		return new JLabel(icon, SwingConstants.CENTER);
	}

	public static void main(String[] args) throws IOException
	{
		BufferedImage[] images = new BufferedImage[2];
		File fi = new File("E:/Documents/images/map.png");
		images[0] = ImageIO.read(fi);
		images[1] = new BufferedImage(images[0].getWidth(), images[0].getHeight(),
				 BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g2 = (Graphics2D) images[1].getGraphics();
		g2.drawImage(images[1], 0, 0, null);
		TestFadeToGray tg = new TestFadeToGray(fi);
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(tg.GetContent(images));
		f.setSize(400, 400);
		f.setLocation(200, 200);
		f.setVisible(true);

		int STEPS = 10;
		int TRANSITION_TIME = 3;	// In seconds
		for (int i = STEPS; i >= 0; i--)
		{
			tg.UpdateImage((float) i / STEPS);
			try { Thread.sleep(1000 * TRANSITION_TIME / STEPS); } catch (Exception e) {}
		}
	}
}
