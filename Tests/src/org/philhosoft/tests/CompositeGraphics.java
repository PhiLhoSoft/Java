/*
 * Tests: A collection of little test programs to explore Java language.
 */
/* File history:
 *  1.01.000 -- 2010/09/13 (PL) -- Updated to a more modern Swing
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
import java.awt.image.WritableRaster;
import java.awt.image.DataBuffer;
import java.awt.image.ComponentColorModel;
import java.awt.color.ColorSpace;

import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;


public class CompositeGraphics
{
	private BufferedImage[] m_images;

	public CompositeGraphics(BufferedImage[] images)
	{
		assert images.length > 1;
		m_images = images;
	}

	public JScrollPane GetGUI()
	{
		JPanel panel = new JPanel(new GridLayout(5, 3));

		panel.add(WrapImageInComponent(m_images[0]));
		panel.add(WrapImageInComponent(m_images[1]));
		panel.add(WrapImageInComponent(GetCompositeImage(m_images, 0.2f)));

		panel.add(WrapImageInComponent(m_images[0]));
		panel.add(WrapImageInComponent(m_images[1]));
		panel.add(WrapImageInComponent(GetCompositeImage(m_images, 0.8f)));

		BufferedImage[] bwImages = new BufferedImage[m_images.length];
		for (int i = 0; i < m_images.length; i++)
		{
			bwImages[i] = CopyOpaqueImageToType(m_images[i], BufferedImage.TYPE_BYTE_GRAY);
		}
		panel.add(WrapImageInComponent(bwImages[0]));
		panel.add(WrapImageInComponent(bwImages[1]));
		panel.add(WrapImageInComponent(GetCompositeImage(bwImages, 0.5f)));

		BufferedImage[] bwaImages = new BufferedImage[m_images.length];
		for (int i = 0; i < m_images.length; i++)
		{
			bwaImages[i] = CopyTranslucentImageToGray(m_images[i]);
		}
		panel.add(WrapImageInComponent(bwaImages[0]));
		panel.add(WrapImageInComponent(bwaImages[1]));
		panel.add(WrapImageInComponent(GetCompositeImage(bwaImages, 0.3f)));

		panel.add(WrapImageInComponent(bwaImages[0]));
		panel.add(WrapImageInComponent(bwaImages[1]));
		panel.add(WrapImageInComponent(GetCompositeImage(bwaImages, 0.7f)));

		return new JScrollPane(panel);
	}

	private BufferedImage GetCompositeImage(BufferedImage[] images, float alpha)
	{
		int w = Math.max(images[0].getWidth(), images[1].getWidth());
		int h = Math.max(images[0].getHeight(), images[1].getHeight());
		int type = BufferedImage.TYPE_INT_ARGB;
		BufferedImage dest = new BufferedImage(w, h, type);
		Graphics2D g2 = dest.createGraphics();
		int x = (w - images[0].getWidth()) / 2;
		int y = (h - images[0].getHeight()) / 2;
		g2.drawImage(images[0], x, y, null);
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, alpha);
		g2.setComposite(ac);
		x = (w - images[1].getWidth()) / 2;
		y = (h - images[1].getHeight()) / 2;
		g2.drawImage(images[1], x, y, null);
		g2.dispose();
		return dest;
	}

	private BufferedImage CopyOpaqueImageToType(BufferedImage src, int type)
	{
		int w = src.getWidth();
		int h = src.getHeight();
		BufferedImage copy = new BufferedImage(w, h, type);
		Graphics2D g2 = copy.createGraphics();
		// If image has transparency, it will be replaced by the given color (white here)
		g2.drawImage(src, 0, 0, new Color(1f, 1f, 1f), null);
		g2.dispose();
//~ 		System.out.println(copy);
		return copy;
	}

	private BufferedImage CopyTranslucentImageToGray(BufferedImage src)
	{
		int w = src.getWidth();
		int h = src.getHeight();

		ColorSpace gsColorSpace = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ComponentColorModel ccm = new ComponentColorModel(gsColorSpace,
				true,  // hasAlpha
				false, // isAlphaPremultiplied
				Transparency.TRANSLUCENT,
				DataBuffer.TYPE_BYTE);
		WritableRaster raster = ccm.createCompatibleWritableRaster(w, h);
		BufferedImage copy = new BufferedImage(ccm, raster, ccm.isAlphaPremultiplied(), null);

		Graphics2D g2 = copy.createGraphics();
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
//~ 		System.out.println(copy);
		return copy;
	}

	private JLabel WrapImageInComponent(BufferedImage image)
	{
		ImageIcon icon = new ImageIcon(image);
		return new JLabel(icon, SwingConstants.CENTER);
	}

	public static void main(String[] args) throws IOException
	{
		final String IMAGE_PATH = "C:/Personnel/Images";
		String[] imageNames =
		{
			"Color.png", // 48x48 flat green square
			"people-AA.png" // 48x48 icon with stylized two persons
		};
		final BufferedImage[] images = new BufferedImage[imageNames.length];
		for (int i = 0; i < images.length; i++)
		{
			images[i] = ImageIO.read(new File(IMAGE_PATH, imageNames[i]));
		}
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				JFrame f = new JFrame();
				f.setTitle("Exploring Composite Graphics");
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				CompositeGraphics cg = new CompositeGraphics(images);
				f.setContentPane(cg.GetGUI());
				f.setSize(400, 400);
				f.setLocation(200, 200);
				f.setVisible(true);
			}
		});
	}
}
