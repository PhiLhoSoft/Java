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

import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.philhosoft.util.ResourceUtil;

public class TestFadeToGray
{
	private FadeToGray m_ftg;
	private BufferedImage[] m_images;
	private JPanel m_panel;
	private JLabel m_icon;

	TestFadeToGray()
	{
	}

	private JScrollPane getContent(BufferedImage[] images)
	{
		m_images = images;
		m_panel = new JPanel(new GridLayout(1, 0));
		m_panel.add(wrap(m_images[0]));
		m_panel.add(wrap(m_images[1]));
		m_icon = new JLabel("_._", m_ftg, SwingConstants.CENTER);
		m_panel.add(m_icon);
		return new JScrollPane(m_panel);
	}

	public void updateImage(float amount)
	{
		m_ftg.setOpacity(amount);
		m_icon.repaint();

		m_icon.setText(Float.toString(amount));
	}

	private JLabel wrap(BufferedImage image)
	{
		ImageIcon icon = new ImageIcon(image);
		return new JLabel(icon, SwingConstants.CENTER);
	}

	private void setFile(File file) throws IOException
	{
		m_ftg = new FadeToGray(file);
	}

	public static void main(String[] args) throws IOException
	{
		final TestFadeToGray tg = new TestFadeToGray();
		String imagePath = ResourceUtil.getClassPath(tg);
		System.out.println(imagePath);

		File file = new File("resources/" + imagePath, "map.png");
		tg.setFile(file);

		final BufferedImage[] images = new BufferedImage[2];
		images[0] = ImageIO.read(file);
		images[1] = new BufferedImage(images[0].getWidth(), images[0].getHeight(),
				 BufferedImage.TYPE_BYTE_GRAY);

		Graphics2D g2 = (Graphics2D) images[1].getGraphics();
		g2.drawImage(images[0], 0, 0, null);

		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(tg.getContent(images));
		f.setSize(400, 400);
		f.setLocation(200, 200);
		f.setVisible(true);

		int STEPS = 10;
		int TRANSITION_TIME = 3;	// In seconds
		for (int i = STEPS; i >= 0; i--)
		{
			tg.updateImage((float) i / STEPS);
			try { Thread.sleep(1000 * TRANSITION_TIME / STEPS); } catch (InterruptedException e) {}
		}
	}
}
