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
package org.philhosoft.tests;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class TestMask
{
	private BufferedImage[] m_images;
	private JPanel m_panel;

	TestMask() throws IOException
	{
		String imagePath = "C:/Personnel/Images";
		m_images = new BufferedImage[9];
		int i = 0;
		// Image in color
		m_images[i++] = ImageIO.read(new File(imagePath, "map.png"));
		// Mask with transparency: opaque black, transparent white
		m_images[i++] = ImageIO.read(new File(imagePath, "mapMask1.png"));
		// B&W image
		m_images[i++] = ImageIO.read(new File(imagePath,  "mapMask3.png"));
		// Color image with transparency around
		m_images[i++] = ImageIO.read(new File(imagePath, "people.png"));

		m_images[i++] = GetComposite(m_images[0], m_images[1],
				AlphaComposite.DST_IN, 1.0F);
 		m_images[i++] = GetComposite(m_images[0], m_images[1],
 				AlphaComposite.SRC_OVER, 0.5F);

 		m_images[i++] = GetComposite(m_images[0], m_images[3],
 				AlphaComposite.SRC_OVER, 1.0F);
		m_images[i++] = GetComposite(m_images[3], m_images[0],
				AlphaComposite.SRC_IN, 1.0F);

		Image transpImg = TransformGrayToTransparency(m_images[2]);
		m_images[i++] = ApplyTransparency(m_images[0], transpImg);
	}

	private JScrollPane GetContent()
	{
		m_panel = new JPanel(new GridLayout(1, 0));
		for (int i = 0; i < m_images.length; i++)
		{
			m_panel.add(Wrap(m_images[i]));
		}
		return new JScrollPane(m_panel);
	}

	private JLabel Wrap(BufferedImage image)
	{
		ImageIcon icon = new ImageIcon(image);
		return new JLabel(icon, SwingConstants.CENTER);
	}

	private BufferedImage GetComposite(
			BufferedImage source, BufferedImage destination,
			int mode, float alpha)
	{
		BufferedImage result = new BufferedImage(
				source.getWidth(), source.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = result.createGraphics();
		g2.drawImage(source, 0, 0, null);
		AlphaComposite ac = AlphaComposite.getInstance(mode, alpha);
		g2.setComposite(ac);
		g2.drawImage(destination, 0, 0, null);
		g2.dispose();
		return result;
	}

	private Image TransformGrayToTransparency(BufferedImage image)
	{
		ImageFilter filter = new RGBImageFilter()
		{
			public final int filterRGB(int x, int y, int rgb)
			{
				return (rgb << 8) & 0xFF000000;
			}
		};

		ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
	    return Toolkit.getDefaultToolkit().createImage(ip);
	}

	private BufferedImage ApplyTransparency(BufferedImage image, Image mask)
	{
		BufferedImage dest = new BufferedImage(
				image.getWidth(), image.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(image, 0, 0, null);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0F);
		g2.setComposite(ac);
		g2.drawImage(mask, 0, 0, null);
		g2.dispose();
		return dest;
	}

	public static void main(String[] args) throws IOException
	{
		TestMask tm = new TestMask();
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle(tm.getClass().getName());
		f.setContentPane(tm.GetContent());
		f.setSize(800, 400);
		f.setLocation(200, 200);
		f.setVisible(true);
	}
}
